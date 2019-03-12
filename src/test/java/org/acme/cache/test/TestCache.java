package org.acme.cache.test;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.cache.config.CacheListener;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TestCache {

    static Cache<String, String> cache;

    @Inject
    CacheListener cacheListener;

    @BeforeAll
    public static void startCache() {
        Configuration config = new ConfigurationBuilder()
                .indexing()
                .autoConfig(true)
                .memory()
                .size(1000)
                .build();

        GlobalConfiguration g = new GlobalConfigurationBuilder()
                .nonClusteredDefault()
                .defaultCacheName("default-cache")
                .globalJmxStatistics()
                .allowDuplicateDomains(false)
                .build();
        EmbeddedCacheManager embeddedCacheManager = new DefaultCacheManager(g, config);
        cache = embeddedCacheManager.getCache();

    }

    @Test
    public void testAdd() {
        cache.addListener(cacheListener);
        assert given()
                .when()
                .put("/cache/key1/value1")
                .getBody()
                .print()
                .equals("Key/Pair [key1:value1] added to cache");

        assert given()
                .when()
                .get("/cache/key1")
                .getBody()
                .print()
                .equals("Value for key1 is value1");

        assert given()
                .when()
                .post("/cache/update/key1/newValue02")
                .getBody()
                .print()
                .equals("Key key1 update from value1 to newValue02");

        assert given()
                .when()
                .delete("/cache/key1")
                .getBody()
                .print()
                .equals("Key key1 remove from cache.");
    }
}