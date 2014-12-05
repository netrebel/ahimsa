package ahimsa.mongo;

import com.google.gson.Gson;

/**
 * Interface backing a Mongo database (not a deployment host)
 */
public interface MongoDatabase {

    MongoCollection getCollection(String collectionName);

    boolean isHealthy();

    Gson getMongoGson();

}
