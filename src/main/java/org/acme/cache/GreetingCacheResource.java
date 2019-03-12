package org.acme.cache;

import com.oracle.svm.core.annotate.Substitute;
import io.quarkus.runtime.StartupEvent;
import org.acme.cache.config.CacheListener;
import org.acme.cache.config.DefaultCache;
import org.infinispan.Cache;
import sun.reflect.Reflection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cache")
@ApplicationScoped
public class GreetingCacheResource {

    @Inject
    @DefaultCache
    public Cache<String, String> cache;

    @Inject
    public CacheListener cacheListener;

    public void onStart(@Observes StartupEvent ev) {
        cache.addListener(cacheListener);
    }

    @PUT
    @Path("{key}/{value}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addvalueToCache(@PathParam("key") String key, @PathParam("value") String value) {
        cache.put(key, value);
        return Response.ok("Key/Pair [" + key + ":" + value +"] added to cache").build();
    }

    @GET
    @Path("{key}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getValueFromCache(@PathParam("key") String key) {
        String value =  cache.get(key);
        String response;
        if (null == value) {
            response = "Key " + key + " not found on cache.";
        } else {
            response = "Value for " + key + " is " + value;
        }
        return Response.ok(response).build();
    }

    @DELETE
    @Path("{key}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteValueFromCache(@PathParam("key") String key) {
        String value =  cache.get(key);
        if (null == value) {
            return Response.ok("Key " + key + " not found on cache.").build();
        } else {
            cache.remove(key);
            return Response.ok("Key " + key + " remove from cache.").build();
        }
    }

    @POST
    @Path("update/{key}/{newValue}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateValueFromCache(@PathParam("key") String key, @PathParam("newValue") String newValue) {
        String value =  cache.get(key);
        if (null == value) {
            return Response.ok("Key " + key + " not found on cache.").build();
        } else {
            cache.replace(key, newValue);
            return Response.ok("Key " + key + " update from " + value + " to " + newValue).build();
        }
    }


}