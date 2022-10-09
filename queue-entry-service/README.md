# Working Queues - Queue Entry Service

A REST based service to put working tasks to the queueing system for "*competing consumers pattern*" tests.

This solution is using the reactive stack:
- WebFlux REST services
- the AmqpTemplate methods are called via *Callable*

## Build and Run

### Build

```bash
mvn package
```

### Run

- Start RabbitMQ using `docker-compose up -d`.
- Admin UI of RabbitMQ is then available via `http://localhost:15672` and *guest/guest* credentials.
- Run either `mvn` or `java -jar target/queue-entry-service.jar`.

The server runs on port `9082`. Actuator health can be accessed with http://localhost:9082/actuator/health.

### Sample curl ingest

```bash
cd src/test/curl
export BASE_URL="http://localhost:9082"
./add-task.sh Fibonacci 5 $RANDOM
# More load
while true; do ./add-task.sh Fibonacci 35 $RANDOM; done
```
## AMQP Setup

- queue name: `QUEUE`
- exchange name: `entry`
- binding-key (from entry to QUEUE): `default`
- routing-key: `default`

### Release Notes

- 0.0.1 (05.10.2022)
  - Initial version
