package ahimsa.common.json;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.Date;

public class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

    // Datetime format according to RFC 3339 - "Date and Time on the Internet: Timestamps"
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern(DATETIME_FORMAT).withOffsetParsed();

    private DateTimeZone preferredTimeZone;

    public DateTimeTypeConverter(final DateTimeZone preferredTimeZone) {
        this.preferredTimeZone = preferredTimeZone;
    }

    @Override
    public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context) {
        DateTime zonedDateTime = this.preferredTimeZone != null ? src.toDateTime(this.preferredTimeZone) : src;
        return new JsonPrimitive(DATETIME_FORMATTER.print(zonedDateTime));
    }

    @Override
    public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        try {
            return DATETIME_FORMATTER.parseDateTime(json.getAsString());
        } catch (IllegalArgumentException e) {
            // Maybe it came in formatted as a java.util.Date, so try that
            Date date = context.deserialize(json, Date.class);
            return new DateTime(date);
        }
    }

}
