# Working Queues

A service mesh for "*competing consumers pattern*" tests using *RabbitMQ* via *Spring AMQP*.

**WORK IN PROGRESS!**

## Some links for introduction

- concerning *Competing Consumers Pattern*, see https://www.enterpriseintegrationpatterns.com/CompetingConsumers.html
- concerning *RabbitMQ*, see https://www.rabbitmq.com/tutorials/tutorial-two-java.html
- concerning *Spring AMQP*, see https://docs.spring.io/spring-amqp/reference/html/

### Docker setup

```bash
docker pull rabbitmq:management
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
docker exec rabbitmq rabbitmqctl --list_commands
docker exec rabbitmq rabbitmqctl list_queues
```

With the setup the RabbitMQ admin UI is accessible via http://localhost:15672 and *guest/guest* credentials.


## TODO / Open Issues

- [ ] General
  - [ ] *at-least-once* vs. *exactly-once* setup - see https://groups.google.com/g/rabbitmq-discuss/c/eniPTe1aKvk?pli=1
- [ ] Spring AMQP
  - [ ] AsyncRabbitTemplate vs. RabbitStreamTemplate vs. RabbitTemplate 
  - [ ] Which ConnectionFactory (CachingConnectionFactory, LocalizedQueueConnectionFactory, PooledChannelConnectionFactory, SimpleRoutingConnectionFactory, ThreadChannelConnectionFactory) to be used for Spring RabbitMQ? See https://docs.spring.io/spring-amqp/reference/html/#choosing-factory
- Coding to be done   
  - [ ] JMeter based load generator
  - [ ] Duplicate message and lost message detection

