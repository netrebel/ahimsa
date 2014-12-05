package ahimsa.common.configuration;

import com.netflix.config.ConfigurationBasedDeploymentContext;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;

/**
 * Provides access to environment variables
 */
public class Environment {

    private static final Values VALUES = new Values();

    /**
     * Private constructor for utility class (only static methods)
     */
    private Environment() {
    }

    public static Values getValues() {
        return VALUES;
    }

    /**
     *
     */
    public static String getApplicationId() {
        return VALUES.getApplicationId();
    }

    /**
     * Setup environment variables for running in development
     */
    public static void setupDevEnvironment() {
        setupEnvironment("dev");
    }

    /**
     * Setup environment variables for running in development
     */
    public static void setupEnvironment(String environmentName) {
        System.setProperty(ConfigurationBasedDeploymentContext.DEPLOYMENT_ENVIRONMENT_PROPERTY, environmentName);
    }

    public static final class Values {

        private Values() {
        }

        /**
         * Returns the current application's applicationId when running inside a common-server instance (or where the
         * deployment context has been set programmatically).
         *
         * @return applicationId as a String or null if not set.
         */
        public String getApplicationId() {
            DeploymentContext deploymentContext = ConfigurationManager.getDeploymentContext();
            return deploymentContext != null ? deploymentContext.getApplicationId() : null;
        }

    }

}
