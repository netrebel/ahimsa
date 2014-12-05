package ahimsa.mongo;

import ahimsa.common.reflection.Reflection;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.jongo.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jongo version of MongoCollection
 */
public class JongoMongoCollection implements MongoCollection {

    private static final String QUERY_ID = "{ _id:# }";

    private final org.jongo.MongoCollection jongoCollection;
    private final MongoConnection.HealthChecker connectionHealthChecker;
    private final ResultMapperStore resultMappers;

    public JongoMongoCollection(org.jongo.MongoCollection jongoCollection,
                                MongoConnection.HealthChecker connectionHealthChecker,
                                Map<Class<?>, List<MongoConnection.ResultMapper<?>>> resultMappers) {
        this.jongoCollection = jongoCollection;
        this.connectionHealthChecker = connectionHealthChecker;
        if (resultMappers == null || resultMappers.size() == 0) {
            this.resultMappers = new EmptyResultMapperStore();
        } else {
            this.resultMappers = new ResultMapperStore(resultMappers);
        }
    }

    @Override
    public JongoFindOne findOneById(String id) {
        return new JongoFindOne(jongoCollection.findOne(QUERY_ID, id), resultMappers);
    }

    @Override
    public JongoFindOne findOneByObjectId(String objectId) {
        return new JongoFindOne(jongoCollection.findOne(new ObjectId(objectId)), resultMappers);
    }

    @Override
    public JongoFindOne findOne(String query) {
        return new JongoFindOne(jongoCollection.findOne(query), resultMappers);
    }

    @Override
    public JongoFindOne findOne(String query, Object... parameters) {
        return new JongoFindOne(jongoCollection.findOne(query, parameters), resultMappers);
    }

    @Override
    public JongoFindOne findOne(MongoQuery query) {
        return findOne(query.getQueryString(), query.getQueryParameters());
    }

    @Override
    public JongoFind findAll() {
        return new JongoFind(jongoCollection.find(), resultMappers);
    }

    @Override
    public JongoFind find(String query) {
        return new JongoFind(jongoCollection.find(query), resultMappers);
    }

    @Override
    public JongoFind find(String query, Object... parameters) {
        return new JongoFind(jongoCollection.find(query, parameters), resultMappers);
    }

    @Override
    public JongoFind find(MongoQuery query) {
        return find(query.getQueryString(), query.getQueryParameters());
    }

    @Override
    public boolean existsAny() {
        return countAll() > 0;
    }

    @Override
    public boolean existsById(String id) {
        return findOneById(id).exists();
    }

    @Override
    public boolean existsByObjectId(String objectId) {
        return findOneByObjectId(objectId).exists();
    }

    @Override
    public boolean exists(String query) {
        return find(query).exists();
    }

    @Override
    public boolean exists(String query, Object... parameters) {
        return find(query, parameters).exists();
    }

    @Override
    public boolean exists(MongoQuery query) {
        return exists(query.getQueryString(), query.getQueryParameters());
    }

    @Override
    public long countAll() {
        return jongoCollection.count();
    }

    @Override
    public long count(String query) {
        return jongoCollection.count(query);
    }

    @Override
    public long count(String query, Object... parameters) {
        return jongoCollection.count(query, parameters);
    }

    @Override
    public long count(MongoQuery query) {
        return count(query.getQueryString(), query.getQueryParameters());
    }

    @Override
    public JongoFindAndModify findAndModifyOneById(String id) {
        return new JongoFindAndModify(jongoCollection.findAndModify(QUERY_ID, id), resultMappers);
    }

    @Override
    public JongoFindAndModify findAndModifyOneByObjectId(String objectId) {
        return new JongoFindAndModify(jongoCollection.findAndModify(QUERY_ID, new ObjectId(objectId)), resultMappers);
    }

    @Override
    public JongoFindAndModify findAndModify(String query) {
        return new JongoFindAndModify(jongoCollection.findAndModify(query), resultMappers);
    }

    @Override
    public JongoFindAndModify findAndModify(String query, Object... parameters) {
        return new JongoFindAndModify(jongoCollection.findAndModify(query, parameters), resultMappers);
    }

    @Override
    public JongoFindAndModify findAndModify(MongoQuery query) {
        return findAndModify(query.getQueryString(), query.getQueryParameters());
    }

    @Override
    public JongoUpdate updateById(String id) {
        return new JongoUpdate(jongoCollection.update(QUERY_ID, id));
    }

    @Override
    public JongoUpdate updateByObjectId(String objectId) {
        return new JongoUpdate(jongoCollection.update(new ObjectId(objectId)));
    }

    @Override
    public JongoUpdate update(String query, Object... parameters) {
        return new JongoUpdate(jongoCollection.update(query, parameters));
    }

    @Override
    public JongoUpdate update(MongoQuery query) {
        return update(query.getQueryString(), query.getQueryParameters());
    }

    @Override
    public MongoWriteResult updateEntity(Object entity) {
        // Extract id from entity
        String id = Reflection.getOrNull(entity, "_id");
        if (id == null) {
            throw new IllegalArgumentException("Cannot update entity without `_id` field");
        }
        return updateById(id).with("#", entity);
    }

    @Override
    public JongoUpdate upsertById(String id) {
        return updateById(id).upsert();
    }

    @Override
    public JongoUpdate upsertByObjectId(String objectId) {
        return updateByObjectId(objectId).upsert();
    }

    @Override
    public JongoUpdate upsert(String query, Object... parameters) {
        return update(query, parameters).upsert();
    }

    @Override
    public JongoUpdate upsert(MongoQuery query) {
        return update(query).upsert();
    }

    @Override
    public MongoWriteResult insert(String query, Object... parameters) {
        return createMongoWriteResult(jongoCollection.insert(query, parameters));
    }

    @Override
    public MongoWriteResult insert(MongoQuery query) {
        return insert(query.getQueryString(), query.getQueryParameters());
    }

    @Override
    public MongoWriteResult insertEntity(Object... entities) {
        return createMongoWriteResult(jongoCollection.insert(entities));
    }

    @Override
    public MongoWriteResult removeAll() {
        return createMongoWriteResult(jongoCollection.remove());
    }

    @Override
    public MongoWriteResult removeById(String id) {
        return createMongoWriteResult(jongoCollection.remove(QUERY_ID, id));
    }

    @Override
    public MongoWriteResult removeByObjectId(String objectId) {
        return createMongoWriteResult(jongoCollection.remove(new ObjectId(objectId)));
    }

    @Override
    public MongoWriteResult remove(String query, Object... parameters) {
        return createMongoWriteResult(jongoCollection.remove(query, parameters));
    }

    @Override
    public MongoWriteResult remove(MongoQuery query) {
        return remove(query.getQueryString(), query.getQueryParameters());
    }

    @Override
    public MongoDistinct distinct(String key) {
        return new JongoDistinct(jongoCollection.distinct(key));
    }

    @Override
    public MongoAggregate aggregate(String pipelineOperator, Object... parameters) {
        return new JongoAggregate(jongoCollection.aggregate(pipelineOperator, parameters));
    }

    @Override
    public void ensureIndex(String query) {
        jongoCollection.ensureIndex(query);
    }

    @Override
    public boolean isHealthy() {
        return connectionHealthChecker.isHealthy();
    }

    private MongoWriteResult createMongoWriteResult(WriteResult writeResult) {
        return new MongoWriteResult(writeResult.getN(), writeResult.isUpdateOfExisting());
    }

    /**
     * Jongo based implementation of MongoFindOne
     */
    public static class JongoFindOne implements MongoFindOne {

        private final FindOne jongoFindOne;
        private final ResultMapperStore resultMappers;

        private JongoFindOne(FindOne jongoFindOne, ResultMapperStore resultMappers) {
            this.jongoFindOne = jongoFindOne;
            this.resultMappers = resultMappers;
        }

        @Override
        public JongoFindOne projection(String query) {
            jongoFindOne.projection(query);
            return this;
        }

        @Override
        public JongoFindOne projection(String query, Object... parameters) {
            jongoFindOne.projection(query, parameters);
            return this;
        }

        @Override
        public <T> T as(final Class<T> clazz) {
            return resultMappers.map(clazz, jongoFindOne.as(clazz));
        }

        @Override
        public String getString(final String fieldName) {
            return jongoFindOne.projection("{" + fieldName + ":1}").map(new ResultHandler<String>() {
                @Override
                public String map(DBObject result) {
                    return (String) result.get(fieldName);
                }
            });
        }

        @Override
        public String getId() {
            return getString("_id");
        }

        /**
         * Package internal method used by the different existsXxx methods in JongoMongoCollection
         */
        boolean exists() {
            Object value = jongoFindOne.projection("{ _id:1 }").map(new ResultHandler<Object>() {
                @Override
                public Object map(DBObject result) {
                    return result;
                }
            });
            return value != null;
        }
    }

    /**
     * Jongo based implementation of MongoFind
     */
    public static class JongoFind implements MongoFind {

        private final Find jongoFind;
        private final ResultMapperStore resultMappers;

        private JongoFind(Find jongoFind, ResultMapperStore resultMappers) {
            this.jongoFind = jongoFind;
            this.resultMappers = resultMappers;
        }

        @Override
        public JongoFind sort(String sort) {
            jongoFind.sort(sort);
            return this;
        }

        @Override
        public JongoFind limit(int limit) {
            jongoFind.limit(limit);
            return this;
        }

        @Override
        public JongoFind skip(int skip) {
            jongoFind.skip(skip);
            return this;
        }

        @Override
        public JongoFind projection(String query) {
            jongoFind.projection(query);
            return this;
        }

        @Override
        public JongoFind projection(String query, Object... parameters) {
            jongoFind.projection(query, parameters);
            return this;
        }

        @Override
        public <T> List<T> as(Class<T> clazz) {
            return Lists.newArrayList(asIterator(clazz));
        }

        @Override
        public <T> Iterator<T> asIterator(final Class<T> clazz) {
            final MongoCursor<T> cursor = jongoFind.as(clazz);
            return new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return cursor.hasNext();
                }

                @Override
                public T next() {
                    return resultMappers.map(clazz, cursor.next());
                }

                @Override
                public void remove() {
                    cursor.remove();
                }
            };
        }

        @Override
        public List<String> getStrings(final String fieldName) {
            return Lists.newArrayList((Iterator<String>) jongoFind.projection("{" + fieldName + ":1}").map(new ResultHandler<String>() {
                @Override
                public String map(DBObject result) {
                    return (String) result.get(fieldName);
                }
            }));
        }

        @Override
        public List<String> getIds() {
            return getStrings("_id");
        }

        /**
         * Package internal method used by the different existsXxx methods in JongoMongoCollection
         */
        public boolean exists() {
            MongoCursor<Object> cursor = jongoFind.limit(1).projection("{ _id:1 }").map(new ResultHandler<Object>() {
                @Override
                public Object map(DBObject result) {
                    return result;
                }
            });
            return cursor.hasNext();
        }
    }

    /**
     * Jongo based implementation of MongoFindAndModify
     */
    public static class JongoFindAndModify implements MongoFindAndModify {

        private final FindAndModify jongoFindAndModify;
        private final ResultMapperStore resultMappers;
        private boolean returnNew;

        private JongoFindAndModify(FindAndModify jongoFindAndModify, ResultMapperStore resultMappers) {
            this.jongoFindAndModify = jongoFindAndModify;
            this.resultMappers = resultMappers;
            // Default to returning new
            this.returnNew = true;
        }

        @Override
        public JongoFindAndModify with(String modifier, Object... parameters) {
            jongoFindAndModify.with(modifier, parameters);
            return this;
        }

        @Override
        public JongoFindAndModify with(MongoQuery query) {
            return with(query.getQueryString(), query.getQueryParameters());
        }

        @Override
        public JongoFindAndModify upsert() {
            jongoFindAndModify.upsert();
            return this;
        }

        @Override
        public JongoFindAndModify returnPreviousVersion() {
            returnNew = false;
            return this;
        }

        @Override
        public JongoFindAndModify projection(String query) {
            jongoFindAndModify.projection(query);
            return this;
        }

        @Override
        public JongoFindAndModify projection(String query, Object... parameters) {
            jongoFindAndModify.projection(query, parameters);
            return this;
        }

        @Override
        public <T> T as(final Class<T> clazz) {
            if (returnNew) {
                jongoFindAndModify.returnNew();
            }
            return resultMappers.map(clazz, jongoFindAndModify.as(clazz));
        }

        @Override
        public <T> T remove(final Class<T> clazz) {
            jongoFindAndModify.remove();

            // Force execution by invoking `as` (ignore return new to always return previous version for remove)
            return resultMappers.map(clazz, jongoFindAndModify.as(clazz));
        }

    }

    /**
     * Jongo based version of MongoUpdate
     */
    public static class JongoUpdate implements MongoUpdate {

        private Update jongoUpdate;

        private JongoUpdate(Update jongoUpdate) {
            this.jongoUpdate = jongoUpdate;
        }

        @Override
        public JongoUpdate upsert() {
            this.jongoUpdate.upsert();
            return this;
        }

        @Override
        public JongoUpdate multi() {
            this.jongoUpdate.multi();
            return this;
        }

        @Override
        public MongoWriteResult with(MongoQuery query) {
            return with(query.getQueryString(), query.getQueryParameters());
        }

        @Override
        public MongoWriteResult with(String modifier, Object... parameters) {
            WriteResult writeResult = jongoUpdate.with(modifier, parameters);
            return new MongoWriteResult(writeResult.getN(), writeResult.isUpdateOfExisting());
        }

        @Override
        public MongoWriteResult set(MongoQuery query) {
            return set(query.getQueryString(), query.getQueryParameters());
        }

        @Override
        public MongoWriteResult set(String modifier, Object... parameters) {
            return with("{ $set: " + modifier + " }", parameters);
        }

    }

    /**
     * Jongo version of MongoDistinct
     */
    public static class JongoDistinct implements MongoDistinct {

        private final Distinct distinct;

        private JongoDistinct(Distinct distinct) {
            this.distinct = distinct;
        }

        @Override
        public MongoDistinct query(String query) {
            distinct.query(query);
            return this;
        }

        @Override
        public MongoDistinct query(String query, Object... parameters) {
            distinct.query(query, parameters);
            return this;
        }

        @Override
        public <T> List<T> as(Class<T> clazz) {
            return distinct.as(clazz);
        }

    }

    /**
     * Jongo version of MongoAggregate
     */
    public static class JongoAggregate implements MongoAggregate {

        private final Aggregate aggregate;

        private JongoAggregate(Aggregate aggregate) {
            this.aggregate = aggregate;
        }

        @Override
        public MongoAggregate and(String pipelineOperator) {
            aggregate.and(pipelineOperator);
            return this;
        }

        @Override
        public MongoAggregate and(String pipelineOperator, Object... parameters) {
            aggregate.and(pipelineOperator, parameters);
            return this;
        }

        @Override
        public <T> List<T> as(Class<T> clazz) {
            return aggregate.as(clazz);
        }

    }

    /**
     * Internal helper to map results
     */
    private static class ResultMapperStore {

        private Map<Class<?>, List<MongoConnection.ResultMapper<?>>> resultMappers;

        private ResultMapperStore(Map<Class<?>, List<MongoConnection.ResultMapper<?>>> resultMappers) {
            this.resultMappers = resultMappers;
        }

        @SuppressWarnings("unchecked")
        public <T> T map(Class<T> type, T value) {
            // Return directly for null values
            if (value == null) {
                return null;
            }

            // Check result mappers for the specific type
            if (resultMappers.containsKey(type)) {
                List<MongoConnection.ResultMapper<?>> mappers = resultMappers.get(type);
                if (mappers != null) {
                    for (MongoConnection.ResultMapper<?> mapper : mappers) {
                        if (mapper != null) {
                            value = ((MongoConnection.ResultMapper<T>) mapper).map(value);
                        }
                    }
                }
            }

            // Return (possibly updated) value
            return value;
        }

    }

    public static class EmptyResultMapperStore extends ResultMapperStore {

        private EmptyResultMapperStore() {
            super(Maps.<Class<?>, List<MongoConnection.ResultMapper<?>>>newHashMap());
        }

        @Override
        public <T> T map(Class<T> type, T value) {
            return value;
        }

    }

}
