package ahimsa.mongo;

/**
 * Interface for db.collection.findAndModify(...) queries
 */
public interface MongoFindAndModify {

    MongoFindAndModify with(String modifier, Object... parameters);

    MongoFindAndModify with(MongoQuery query);

    MongoFindAndModify upsert();

    MongoFindAndModify returnPreviousVersion();

    MongoFindAndModify projection(String query);

    MongoFindAndModify projection(String query, Object... parameters);

    <T> T as(Class<T> clazz);

    <T> T remove(Class<T> clazz);
}
