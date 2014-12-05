package ahimsa.common.json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ClassTypeConverter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public JsonElement serialize(Class<?> src, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }

        @Override
        public Class<?> deserialize(JsonElement json, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                return Class.forName(json.getAsString());
            } catch (IllegalArgumentException | ClassNotFoundException e) {
                return null;
            }
        }
    }
