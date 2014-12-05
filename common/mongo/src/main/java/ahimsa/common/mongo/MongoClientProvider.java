package ahimsa.common.mongo;

import ahimsa.common.configuration.Configuration;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.net.UnknownHostException;
import java.util.List;

@Singleton
public class MongoClientProvider implements Provider<MongoClient> {

    private static Logger LOG = LoggerFactory.getLogger(MongoClientProvider.class);

    private static final String CONFIGURATION_KEY = "mongodb.connection-string";

    private MongoClient mongoClient;

    @PostConstruct
    void setupMongoClient() {
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

    @Override
    public MongoClient get() {
        if (mongoClient == null) {
            LOG.error("Failed to create MongoClient, not connected to server");
            throw new RuntimeException("Failed to create MongoClient, not connected to server");
        }

        return mongoClient;
    }

}
