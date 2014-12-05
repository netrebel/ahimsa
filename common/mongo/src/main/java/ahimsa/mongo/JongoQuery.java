package ahimsa.mongo;

import com.mongodb.DBObject;
import org.jongo.query.Query;

/**
 * Jongo specific versions of MongoQuery and MongoQuery.$set mainly to be able to parse
 * existing string queries into a MongoQuery instance.
 */
public class JongoQuery extends MongoQuery {

    public JongoQuery(String query, Object... parameters) {
        super(parseJongoQuery(query, parameters));
    }

    public static class $set extends MongoQuery.$set {

        public $set(String query, Object... parameters) {
            super(parseJongoQuery(query, parameters));
        }

    }

    private static MongoQuery parseJongoQuery(String query, Object[] parameters) {
        // Parse using Jongo
        Query jongoQuery = JongoMongoDatabase.getQueryFactory().createQuery(query, parameters);

        if (jongoQuery != null && jongoQuery.toDBObject() != null) {
            return mongoQueryFromDBObject(jongoQuery.toDBObject());
        } else {
            // Return empty query
            return new MongoQuery();
        }
    }

    private static MongoQuery mongoQueryFromDBObject(DBObject dbObject) {
        MongoQuery result = new MongoQuery();
        for (String field : dbObject.keySet()) {
            Object value = dbObject.get(field);
            if (value instanceof DBObject) {
                result.addSubQuery(field, mongoQueryFromDBObject((DBObject) value));
            } else {
                result.addField(field + ":#", value);
            }
        }
        return result;
    }

}
