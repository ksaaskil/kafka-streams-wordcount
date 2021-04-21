# Kafka Streams Wordcount

Example application from [Kafka Streams for Data Processing](https://www.udemy.com/course/kafka-streams/) course.

## Usage

Launch Kafka and Zookeeper:

```bash
$ docker-compose up
```

Launch the Kafka Streams application from IntelliJ or from command-line (see below).

Produce messages to input topic `word-count-input` with [`kafkacat`](https://github.com/edenhill/kafkacat):

```bash
$ kafkacat -P -b localhost:9092 -t word-count-input
```

Consume messages from output topic `word-count-output`:

```bash
$ kafkacat -b localhost:9092 -s key=s -s value=q -f 'Key: %k, value: %s\n' -t word-count-output
```

The value is decoded as 64-bit signed integer by setting `-s value=q`. Use `kafkacat -h` to see all deserializer options.

## Packaging and running as stand-alone JAR

```bash
$ mvn clean package
```

Run application:

```bash
$ java -jar target/wordcount-1.0-SNAPSHOT-jar-with-dependencies.jar
```
