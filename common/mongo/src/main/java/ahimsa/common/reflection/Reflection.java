package ahimsa.common.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static ahimsa.common.configuration.StringUtils.isEmpty;


public class Reflection {

    /**
     * Private util constructor
     */
    private Reflection() {
    }

    public static <T> T getOrNull(Object object, String path) {
        try {
            return get(object, path);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object object, String path) throws Exception {
        if (object == null) {
            return null;
        }

        if (isEmpty(path)) {
            return (T)object;
        }

        // Split by .
        String[] parts = path.split("\\.");
        int lastIndex = parts.length - 1;

        // Get all except last part
        for (int i=0; i < lastIndex; i++) {
            object = getChild(object, parts[i]);
            if (object == null) {
                return null;
            }
        }

        // Return last part
        return (T) getChild(object, parts[lastIndex]);
    }

    private static Object getChild(Object object, String field) throws IllegalAccessException, InvocationTargetException {
        Class<?> objectClass = object.getClass();

        // Try getting property on object
        try {
            Field objectField = objectClass.getField(field);

            // Assert accessible
            if (!objectField.isAccessible()) {
                objectField.setAccessible(true);
            }

            // Return value
            return objectField.get(object);
        } catch (NoSuchFieldException ignored) {
        }

        // Check for no-args method
        try {
            Method method = objectClass.getMethod(field);

            // Assert accessible
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            // Return value
            return method.invoke(object);
        } catch (NoSuchMethodException ignored) {
        }

        throw new IllegalArgumentException("Cannot find " + field + " inside class " + objectClass);
    }

}
