# Deserialization Error Handling Demo

This is a demo of how to handle errors caused by poison pills in a Kafka topic which cause deserialization exceptions. Note that there are other kinds of poison pills, e.g. those which can deserialized but cause exceptions when processed. We will not cover those in this demo.

In this demo, we set up two different producers. One producer produces normal messages, and the other produces poison pills. The consumer consumes both normal messages and poison pills. 

The consumer is configured to handle poison pills using a `ErrorHandlingDeserializer` so that the poison pills will affect the processing of the normal messages. 

In this demo, we also configure a recovery function which provides a default value to the handler when a poison pill is encountered. Without the recovery function, the consumer will simply log the error.

## Instructions

Build the JAR file with the following command:

```shell
mvn clean install
```

Build the Docker image with the following command:

```shell
docker build -t poison-pill-example_app .
```

Run everything with the following command:

```shell
docker-compose up -d
```

Show the logs with the following command:

```shell
docker-compose logs -f
```

Clean up everything with the following command:

```shell
docker-compose down --remove-orphans
```
