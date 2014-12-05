package ahimsa.common.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Json {

    private Json() {
    }

    public static Gson createGson() {
        return createGsonWithPreferredTimeZone(null);
    }

    public static Gson createGsonWithDefaultPreferredTimeZone() {
        return createGsonWithPreferredTimeZone(DateTimeZone.UTC);
    }

    public static Gson createGsonWithPreferredTimeZone(final DateTimeZone preferredTimeZone) {
        GsonBuilder builder = createGsonBuilderWithPreferredTimeZone(preferredTimeZone);
        return builder.create();
    }

    public static GsonBuilder createGsonBuilderWithPreferredTimeZone(DateTimeZone preferredTimeZone) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter(preferredTimeZone));
        builder.registerTypeAdapter(Class.class, new ClassTypeConverter());
        return builder;
    }

}
