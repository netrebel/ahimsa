package ahimsa.mongo;

import ahimsa.common.configuration.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * An injectable MongoConnection that creates Jongo-backed MongoCollections.
 * <p/>
 * The entry class for using the dazoo.mongo package.
 */
@Singleton
public class MongoConnection {

    private static Logger LOG = LoggerFactory.getLogger(MongoConnection.class);

    private static final String CONFIGURATION_KEY = "mongodb.connection-string";

    private final HealthChecker healthChecker = new HealthChecker();
    private MongoClient mongoClient;

    public interface ResultMapper<T> {
        T map(T value);
    }

    public boolean isHealthy() {
        return healthChecker.isHealthy();
    }

    public MongoDatabase getDatabase(String databaseName) {
        return getDatabaseBuilder(databaseName).build();
    }

    /**
     * Return a MongoDatabase builder and allow for configuration of Gson (e.g. to add deserializers)
     */
    public DatabaseBuilder getDatabaseBuilder(String databaseName) {
        return new DatabaseBuilder(databaseName);
    }

    @PostConstruct
    void setupMongoClient() {
        if (mongoClient != null) {
            throw new IllegalStateException("MongoConnection should not be instantiated multiple times, please @Inject a reference to make sure it is a singleton.");
        }

        // Load configuration
        Configuration.loadConfigurationWithApplicationAndEnvironmentOverrides("mongo");

        // Load connection string configuration as array to handle ,-separated hosts
        List<String> connectionStrings = Configuration.getStringList(CONFIGURATION_KEY);
        if (connectionStrings == null || connectionStrings.isEmpty()) {
            LOG.error("Could not load MongoDB connection string configuration");
            throw new IllegalArgumentException("Could not load MongoDB connection string configuration");
        }

        // Merge any ,-separated hosts
        String connectionString = StringUtils.join(connectionStrings, ",");
        LOG.info("Using MongoDB connection string: {}", connectionString);

        // Create MongoClient instance
        MongoClientURI uri = new MongoClientURI(connectionString);
        try {
            mongoClient = new MongoClient(uri);
        } catch (UnknownHostException e) {
            LOG.error("Invalid MongoDB connection string: " + uri, e);
            throw new IllegalArgumentException("Invalid MongoDB connection string: " + uri, e);
        }
    }

    @PreDestroy
    void shutdownMongoClient() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public final class DatabaseBuilder {

        private String databaseName;
        private Map<Class<?>, List<Object>> jsonTypeAdapters;
        private Map<Class<?>, List<ResultMapper<?>>> resultMappers;

        private DatabaseBuilder(String databaseName) {
            this.databaseName = databaseName;
            this.jsonTypeAdapters = Maps.newHashMap();
            this.resultMappers = Maps.newHashMap();
        }

        public <T> DatabaseBuilder addJsonTypeAdapter(Class<T> type, Object adapter) {
            if (!jsonTypeAdapters.containsKey(type)) {
                jsonTypeAdapters.put(type, Lists.newArrayList());
            }
            jsonTypeAdapters.get(type).add(adapter);
            return this;
        }

        public <T> DatabaseBuilder addResultMapper(Class<T> type, ResultMapper<? super T> resultMapper) {
            if (!resultMappers.containsKey(type)) {
                resultMappers.put(type, Lists.<ResultMapper<?>>newArrayList());
            }
            resultMappers.get(type).add(resultMapper);
            return this;
        }

        public MongoDatabase build() {
            if (mongoClient == null) {
                LOG.error("Failed to create MongoDatabase, not connected to server. Make sure you @Inject:ed the instance of MongoConnection.");
                throw new RuntimeException("Failed to create MongoDatabase, not connected to server. Make sure you @Inject:ed the instance of MongoConnection.");
            }

            // Default to Jongo implementation
            return new JongoMongoDatabase(mongoClient.getDB(databaseName), healthChecker, jsonTypeAdapters, resultMappers);
        }
    }

    class HealthChecker {

        public boolean isHealthy() {
            if (mongoClient == null) {
                return false;
            } else {
                // Simply try connecting to the "local" database to see if we are connected
                try {
                    mongoClient.getDB("local");
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }

    }
}
