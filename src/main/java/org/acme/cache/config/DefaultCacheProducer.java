package org.acme.cache.config;

import org.infinispan.Cache;
import org.infinispan.cdi.embedded.InfinispanExtensionEmbedded;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@ApplicationScoped
public class DefaultCacheProducer {

    public  Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Produces
    @DefaultCache
    public Cache<String, String> returnCache() {
        return defaultCacheContainer().getCache();
    }

    @Produces
    public Configuration defaultCacheProducer() {
        log.info("Configuring default-cache...");
        return new ConfigurationBuilder()
                .indexing()
                .autoConfig(true)
                .memory()
                .size(1000)
                .build();
    }

    @Produces
    public EmbeddedCacheManager defaultCacheContainer() {
        GlobalConfiguration g = new GlobalConfigurationBuilder()
                .nonClusteredDefault()
                .defaultCacheName("default-cache")
                .globalJmxStatistics()
                .allowDuplicateDomains(false)
                .build();
        return new DefaultCacheManager(g, defaultCacheProducer());
    }

    @Produces
    public InfinispanExtensionEmbedded defaultInfinispanExtensionEmbedded() {
        return new InfinispanExtensionEmbedded();
    }

}
