package ahimsa.mongo;

import java.util.List;

/**
 * @author Miguel Reyes
 *         Date: 10/19/14
 *         Time: 8:03 PM
 */
public interface MongoAggregate {

    MongoAggregate and(String pipelineOperator);

    MongoAggregate and(String pipelineOperator, Object... parameters);

    <T> List<T> as(Class<T> clazz);

}
