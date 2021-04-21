// https://github.com/simplesteph/kafka-streams-course/blob/2.0.0/word-count/src/main/java/com/github/simplesteph/udemy/kafka/streams/WordCountApp.java
package org.example;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Properties;

public class StreamsStarterApp {
    public static void main(String[] args) {
        Properties properties = StreamsStarterApp.buildProperties();
        Topology topology = StreamsStarterApp.buildTopology();
        System.out.println(topology.describe());

        KafkaStreams streams = new KafkaStreams(topology, properties);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Kafka stream...");
            streams.close();
            System.out.println("Shutdown done");
        }));
    }

    private static Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> wordCountInput = builder.stream("word-count-input");
        KTable<String, Long> wordCounts = wordCountInput
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .selectKey((key, value) -> value)
                .groupByKey()
                .count(Materialized.as("Counts"));
        KStream<String, Long> wordCountsStream = wordCounts.toStream();
        wordCountsStream.to("word-count-output", Produced.with(Serdes.String(), Serdes.Long()));
        return builder.build();
    }

    private static Properties buildProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-counts");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return properties;
    }
}
