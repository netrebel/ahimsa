package ahimsa.common.configuration;

import java.util.Collection;

public class StringUtils {

	private StringUtils() {
	}

	public static boolean isEmpty(String value) {
		if (value == null) {
			return true;
		} else {
			return value.trim().isEmpty();
		}
	}

	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

    public static boolean isPresent(String value) {
        return isNotEmpty(value);
    }

	public static String join(String separator, Collection<String> parts) {
		return join(separator, parts.toArray(new String[parts.size()]));
	}

	public static String join(String separator, String... parts) {
		if (parts == null || parts.length == 0) {
			return "";
		} else if (parts.length == 1) {
			return parts[0];
		} else {
			StringBuilder result = new StringBuilder();
			for (int i=0; i<parts.length-1; i++) {
				result.append(parts[i]).append(separator);
			}
			result.append(parts[parts.length - 1]);
			return result.toString();
		}
	}

}
