package com.ahimsa;

import com.ahimsa.resources.CoffeeShopResource;
import com.google.inject.Guice;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;

/**
 * @author Miguel Reyes
 *         Date: 12/7/14
 *         Time: 8:06 PM
 */
public class MyApplication extends ResourceConfig {

    @Inject
    public MyApplication(ServiceLocator serviceLocator) {
        packages(CoffeeShopResource.class.getPackage().getName());

        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);

        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(Guice.createInjector(new Bootstrap()));
    }
}
