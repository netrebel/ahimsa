package ahimsa.common.configuration;

import com.google.common.collect.Lists;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.apache.commons.configuration.AbstractConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    private static final Values VALUES = new Values();

    /**
     * Private constructor for utility class (only static methods)
     */
    private Configuration() {
    }

    public static Values getValues() {
        return VALUES;
    }

    public static List<String> getKeys(String prefix) {
        return VALUES.getKeys(prefix);
    }

    public static String getString(String key) {
        return VALUES.getString(key);
    }

    public static List<String> getStringList(String key) {
        return VALUES.getStringList(key);
    }

    public static DynamicBooleanProperty getDynamicBoolean(String key, boolean defaultValue) {
        return VALUES.getDynamicBoolean(key, defaultValue);
    }

    /**
     * Load configuration files based on baseName and environment. Will load the following files when
     * invoked (if found):
     *
     *  ${baseName}.properties
     *  ${baseName}-${environment}.properties
     *
     * This means that the configuration values inside the environment based configuration overrides any existing
     * properties from the base configuration.
     *
     * @param baseName of the configuration files
     */
    public static void loadConfigurationWithEnvironmentOverrides(String baseName) {
        try {
            ConfigurationManager.loadCascadedPropertiesFromResources(baseName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration for " + baseName, e);
        }
    }

    /**
     * Load configuration files based on baseName, the current applicationId and environment. Will load the following
     * files when invoked (if found):
     *
     *  ${baseName}.properties
     *  ${baseName}-${environment}.properties
     *  ${applicationId}-${baseName}.properties
     *  ${applicationId}-${baseName}-${environment}.properties
     *
     * This means that the configuration values inside the application based configuration overrides any existing
     * properties from the base configuration and that environment specific versions of each configuration overrides
     * the generic ones.
     *
     * @param baseName of the configuration files
     */
    public static void loadConfigurationWithApplicationAndEnvironmentOverrides(String baseName) {
        // Extract application id
        String applicationId = Environment.getApplicationId();

        // Load application specific config first (to allow for overrides in application specific config)
        if (isNotBlank(applicationId)) {
            String applicationBaseName = applicationId + "-" + baseName;
            try {
                ConfigurationManager.loadCascadedPropertiesFromResources(applicationBaseName);
            } catch (IOException e) {
                // Ignore file not found or other IOExceptions and simply log
                LOG.info("Failed to load application specific configurations for \"{}\" ({}): {}",
                        baseName, applicationBaseName, e.getMessage());
            }
        }

        // Load base configurations
        loadConfigurationWithEnvironmentOverrides(baseName);
    }

    public static final class Values {

        private Values() {
        }

        public List<String> getKeys(String prefix) {
            if (isBlank(prefix)) {
                return Lists.newArrayList(getConfig().getKeys());
            } else {
                return Lists.newArrayList(getConfig().getKeys(prefix));
            }
        }

        public String getString(String key) {
            return getConfig().getString(key);
        }

        public String getString(String key, String defaultValue) {
            return getConfig().getString(key, defaultValue);
        }

        public double getDouble(String key) {
            return getConfig().getDouble(key);
        }

        public boolean hasKey(String key) {
            return getString(key) != null;
        }

        public List<Object> getList(String key) {
            return getConfig().getList(key);
        }

        public List<String> getStringList(String key) {
            List<String> result = Lists.newArrayList();
            for (Object item : getConfig().getList(key)) {
                result.add(item == null ? null : item.toString());
            }
            return result;
        }

        public String[] getStringArray(String key) {
            return getConfig().getStringArray(key);
        }

        public List<Integer> getIntegerList(String key) {
            List<Integer> result = Lists.newArrayList();
            for (Object item : getConfig().getList(key)) {
                result.add(item == null ? 0 : Integer.valueOf(item.toString(), 10));
            }
            return result;
        }

        public DynamicStringProperty getDynamicString(String key, String defaultValue) {
            return getDynamicConfig().getStringProperty(key, defaultValue);
        }

        public DynamicBooleanProperty getDynamicBoolean(String key, boolean defaultValue) {
            return getDynamicConfig().getBooleanProperty(key, defaultValue);
        }

        private DynamicPropertyFactory getDynamicConfig() {
            return DynamicPropertyFactory.getInstance();
        }

        private AbstractConfiguration getConfig() {
            return ConfigurationManager.getConfigInstance();
        }
    }

}
