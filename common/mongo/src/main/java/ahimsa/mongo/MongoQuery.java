package ahimsa.mongo;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Class used to build queries for MongoCollections programmatically.
 *
 * A bit overly complex and should really only ever be used in very special
 * cases. By default, use the (String query, Object... parameters) versions
 * of the methods in MongoCollection instead.
 */
public class MongoQuery {

    private final List<String> queryFields;
    private final List<Object> parameters;

    public MongoQuery() {
        this(Lists.<String>newArrayList(), Lists.newArrayList());
    }

    protected MongoQuery(MongoQuery source) {
        this(source.queryFields, source.parameters);
    }

    protected MongoQuery(List<String> queryFields, List<Object> parameters) {
        this.queryFields = queryFields;
        this.parameters = parameters;
    }

    public MongoQuery addField(String fieldDefinition, Object... parameters) {
        this.queryFields.add(fieldDefinition);
        this.parameters.addAll(Arrays.asList(parameters));
        return this;
    }

    public MongoQuery addSubQuery(String field, MongoQuery query) {
        this.queryFields.add(field + ": " + query.getQueryString());
        this.parameters.addAll(query.parameters);
        return this;
    }

    public MongoQuery merge(MongoQuery other) {
        // Join both query fields and parameters lists
        return new MongoQuery(concat(this.queryFields, other.queryFields), concat(this.parameters, other.parameters));
    }

    @Override
    public String toString() {
        return "MongoQuery{" +
                "queryString=\"" + getQueryString() + "\"" +
                ", queryParameters=" + parameters +
                '}';
    }

    String getQueryString() {
        return "{ " + StringUtils.join(queryFields, ", ") + " }";
    }

    Object[] getQueryParameters() {
        return parameters.toArray();
    }

    private <T> List<T> concat(Collection<T> first, Collection<T> second) {
        List<T> result = Lists.newArrayListWithCapacity(first.size() + second.size());
        result.addAll(first);
        result.addAll(second);
        return result;
    }

    /**
     * Wraps a query inside { $set: ... }
     */
    public static class $set extends MongoQuery {

        public $set() {
        }

        public $set(MongoQuery childQuery) {
            super(childQuery);
        }

        public MongoQuery.$set merge(MongoQuery other) {
            // TODO: Is this really correct when merging to $set queries?
            return new $set(super.merge(other));
        }

        @Override
        String getQueryString() {
            return "{ $set: " + super.getQueryString() + " }";
        }

    }

}
