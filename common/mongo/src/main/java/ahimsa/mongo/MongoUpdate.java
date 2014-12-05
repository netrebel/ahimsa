package ahimsa.mongo;

/**
 * Interface for db.collection.update(...) queries
 */
public interface MongoUpdate {

    MongoUpdate upsert();

    MongoUpdate multi();

    MongoWriteResult with(MongoQuery query);

    MongoWriteResult with(String modifier, Object... parameters);

    MongoWriteResult set(MongoQuery query);

    MongoWriteResult set(String modifier, Object... parameters);

}
