package ahimsa.mongo;

import ahimsa.common.json.Json;
import com.google.gson.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jongo.*;
import org.jongo.bson.Bson;
import org.jongo.bson.BsonDocument;
import org.jongo.marshall.Marshaller;
import org.jongo.marshall.MarshallingException;
import org.jongo.marshall.Unmarshaller;
import org.jongo.query.BsonQueryFactory;
import org.jongo.query.QueryFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Implementation of MongoDatabase using Jongo.
 */
public class JongoMongoDatabase implements MongoDatabase {

    private final Gson mongoGson;
    private final Jongo jongoDatabase;
    private final Map<Class<?>, List<MongoConnection.ResultMapper<?>>> resultMappers;
    private final MongoConnection.HealthChecker connectionHealthChecker;

    public JongoMongoDatabase(DB database,
                              MongoConnection.HealthChecker connectionHealthChecker,
                              Map<Class<?>, List<Object>> jsonTypeAdapters,
                              Map<Class<?>, List<MongoConnection.ResultMapper<?>>> resultMappers) {
        this.mongoGson = createMongoGson(jsonTypeAdapters);
        this.jongoDatabase = new Jongo(database, new GsonMapper(mongoGson));
        this.connectionHealthChecker = connectionHealthChecker;
        this.resultMappers = resultMappers;
    }

    @Override
    public JongoMongoCollection getCollection(String collectionName) {
        return new JongoMongoCollection(this.jongoDatabase.getCollection(collectionName), connectionHealthChecker, resultMappers);
    }

    @Override
    public boolean isHealthy() {
        return connectionHealthChecker.isHealthy();
    }

    @Override
    public Gson getMongoGson() {
        return mongoGson;
    }

    static QueryFactory getQueryFactory() {
        return new GsonMapper(createMongoGson(null)).getQueryFactory();
    }

    private static Gson createMongoGson(Map<Class<?>, List<Object>> typeAdapters) {
        DateTimeZone preferredTimeZone = DateTimeZone.UTC;
        GsonBuilder builder = Json.createGsonBuilderWithPreferredTimeZone(preferredTimeZone);

        // Override DateTime converter from common-json with MongoDB specific one ({ "$date": "2014-09-10T10:00:00.000Z" }).
        builder.registerTypeAdapter(DateTime.class, new MongoDateTimeTypeConverter(preferredTimeZone));

        // Append provided type adapters
        if (typeAdapters != null) {
            for (Class<?> type : typeAdapters.keySet()) {
                List<Object> adapters = typeAdapters.get(type);
                if (adapters != null) {
                    for (Object adapter : adapters) {
                        builder.registerTypeAdapter(type, adapter);
                    }
                }
            }
        }

        return builder.create();
    }

    private static class GsonMapper implements Mapper {

        private final GsonEngine engine;
        private final ObjectIdUpdater objectIdUpdater;
        private final BsonQueryFactory queryFactory;

        private GsonMapper(Gson gson) {
            this.engine = new GsonEngine(gson);
            this.objectIdUpdater = new ReflectiveObjectIdUpdater(new DazooIdFieldSelector());
            this.queryFactory = new BsonQueryFactory(engine);
        }

        @Override
        public Marshaller getMarshaller() {
            return engine;
        }

        @Override
        public Unmarshaller getUnmarshaller() {
            return engine;
        }

        @Override
        public ObjectIdUpdater getObjectIdUpdater() {
            return objectIdUpdater;
        }

        @Override
        public QueryFactory getQueryFactory() {
            return queryFactory;
        }

    }

    private static class MongoDateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

        public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern(DATETIME_FORMAT).withZoneUTC();

        private DateTimeZone preferredTimeZone;

        public MongoDateTimeTypeConverter(final DateTimeZone preferredTimeZone) {
            this.preferredTimeZone = preferredTimeZone;
        }

        @Override
        public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context) {
            DateTime zonedDateTime = this.preferredTimeZone != null ? src.toDateTime(this.preferredTimeZone) : src;
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("$date", new JsonPrimitive(DATETIME_FORMATTER.print(zonedDateTime)));
            return jsonObject;
        }

        @Override
        public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement dateObject = jsonObject.get("$date");
            return DATETIME_FORMATTER.parseDateTime(dateObject.getAsString());
        }

    }

    private static class GsonEngine implements Marshaller, Unmarshaller {

        private Gson gson;

        private GsonEngine(Gson gson) {
            this.gson = gson;
        }

        @Override
        public BsonDocument marshall(Object pojo) throws MarshallingException {
            try {
                String jsonString = gson.toJson(pojo);
                Object dbObject = JSON.parse(jsonString);

                // Check special case where we serialize to a primitive (e.g. DateTime instance to java.util.Date primitive)
                if (dbObject instanceof DBObject) {
                    return Bson.createDocument((DBObject) dbObject);
                } else {
                    // Return empty object to trigger Jongo to retry with wrapper in org.jongo.query.BsonQueryFactory#marshallDocument
                    return Bson.createDocument(new BasicDBObject());
                }
            } catch (Exception e) {
                throw new MarshallingException("Unable to marshall " + pojo + " into bson", e);
            }
        }

        @Override
        public <T> T unmarshall(BsonDocument document, Class<T> clazz) throws MarshallingException {
            String jsonString = document.toString();
            try {
                return gson.fromJson(jsonString, clazz);
            } catch (Exception e) {
                String message = String.format("Unable to unmarshall result to %s from content %s", clazz, jsonString);
                throw new MarshallingException(message, e);
            }
        }

    }

    private static class DazooIdFieldSelector implements ReflectiveObjectIdUpdater.IdFieldSelector {
        @Override
        public boolean isId(Field field) {
            // Id values are stored under _id to prevent auto-generation by mongodb
            return field.getName().equals("_id");
        }

        @Override
        public boolean isObjectId(Field f) {
            // We only store ObjectId:s as strings
            return false;
        }
    }

}
