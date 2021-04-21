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

## View metadata

View topics and partitions:

```bash
$ kafkacat -L -b localhost:9092
```

Kafka creates internal topics per stream application ID for changelogs as well as repartitions. The former is used for storing aggregated data and the latter for managing transformations of keys.

Consume the `KTable` changelog:

```bash
$ kafkacat -b localhost:9092 -s key=s -s value=q '-f Key: %k, value: %s\n' -t word-counts-Counts-changelog
```

Consume the repartitions:

```bash
$ kafkacat -b localhost:9092 -s key=s -s value=s '-f Key: %k, value: %s\n' -t word-counts-Counts-repartition
```

## Packaging and running as stand-alone JAR

```bash
$ mvn clean package
```

Run application:

```bash
$ java -jar target/wordcount-1.0-SNAPSHOT-jar-with-dependencies.jar
```
