# Working Queues - Queue Worker Nodes

Worker node implementation for a queueing system performing the "*competing consumer pattern*".

Currently, there is only one worker payload implementation: "Fibonacci".

## Build and Run

### Build

```bash
mvn package
```

### Run

Either `mvn` or `java -jar target/queue-worker.jar`.

The server runs on port `9083`. Actuator health can be accessed with http://localhost:9083/actuator/health.

### Release Notes

- 0.0.1 (05.10.2022)
  - Initial version
