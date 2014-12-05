package ahimsa.mongo;

import java.util.Iterator;
import java.util.List;

/**
 * Interface for db.collection.find(...) queries
 */
public interface MongoFind {

    MongoFind sort(String sort);

    MongoFind limit(int limit);

    MongoFind skip(int skip);

    MongoFind projection(String query);

    MongoFind projection(String query, Object... parameters);

    <T> List<T> as(Class<T> clazz);

    <T> Iterator<T> asIterator(Class<T> clazz);

    List<String> getStrings(String fieldName);

    List<String> getIds();

}
