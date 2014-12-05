package ahimsa.mongo;

/**
 * Interface toward a Mongo collection, heavily inspired by Jongo's org.jongo.MongoCollection
 */
public interface MongoCollection {

    MongoFindOne findOneById(String id);

    MongoFindOne findOneByObjectId(String objectId);

    MongoFindOne findOne(String query);

    MongoFindOne findOne(String query, Object... parameters);

    MongoFindOne findOne(MongoQuery query);

    MongoFind findAll();

    MongoFind find(String query);

    MongoFind find(String query, Object... parameters);

    MongoFind find(MongoQuery query);

    boolean existsAny();

    boolean existsById(String id);

    boolean existsByObjectId(String objectId);

    boolean exists(String query);

    boolean exists(String query, Object... parameters);

    boolean exists(MongoQuery query);

    long countAll();

    long count(String query);

    long count(String query, Object... parameters);

    long count(MongoQuery query);

    MongoFindAndModify findAndModifyOneById(String id);

    MongoFindAndModify findAndModifyOneByObjectId(String objectId);

    MongoFindAndModify findAndModify(String query);

    MongoFindAndModify findAndModify(String query, Object... parameters);

    MongoFindAndModify findAndModify(MongoQuery query);

    MongoUpdate updateById(String id);

    MongoUpdate updateByObjectId(String objectId);

    MongoUpdate update(String query, Object... parameters);

    MongoUpdate update(MongoQuery query);

    MongoWriteResult updateEntity(Object entity);

    MongoUpdate upsertById(String id);

    MongoUpdate upsertByObjectId(String objectId);

    MongoUpdate upsert(String query, Object... parameters);

    MongoUpdate upsert(MongoQuery query);

    MongoWriteResult insert(String query, Object... parameters);

    MongoWriteResult insert(MongoQuery query);

    MongoWriteResult insertEntity(Object... entities);

    MongoWriteResult removeAll();

    MongoWriteResult removeById(String id);

    MongoWriteResult removeByObjectId(String objectId);

    MongoWriteResult remove(String query, Object... parameters);

    MongoWriteResult remove(MongoQuery query);

    MongoDistinct distinct(String key);

    MongoAggregate aggregate(String pipelineOperator, Object... parameters);

    void ensureIndex(String query);

    boolean isHealthy();

}
