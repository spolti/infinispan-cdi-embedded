# Quarkus infinispan-cdi-embedded

This quickstart demonstrates how to use Infinspan CDI Embedded cache with Quarkus.

Here you'll find an example about how to:

    - Create your own Qualifier and Producer and get it injected to your class.
    - Create your custon Cache Listener to perform custon actions when, for example, something is added, modified or deleted from Cache.

## Start the application

The application can be started using: 

```bash
mvn compile quarkus:dev
```

Then perform actions using curl or any tool of your choice:


    - Add value on cache: $ curl -X PUT http://localhost:8080/cache/key/value1
    - Get value from a given Key from cache: $ curl -X GET http://localhost:8080/cache/key
    - Update a value for the given Key con cache: $ curl -X POST http://localhost:8080/cache/update/key/newValue03
    - Delete a cache entry: $ curl -X DELETE http://localhost:8080/cache/key
