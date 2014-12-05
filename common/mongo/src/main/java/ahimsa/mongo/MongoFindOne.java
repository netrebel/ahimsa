package ahimsa.mongo;

/**
 * Interface for db.collection.findOne(...) queries
 */
public interface MongoFindOne {

    MongoFindOne projection(String query);

    MongoFindOne projection(String query, Object... parameters);

    <T> T as(Class<T> clazz);

    String getString(String fieldName);

    String getId();

}
