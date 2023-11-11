package com.example.springboot;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
public class KafkaController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String bootstrapServers;
	private String consumerGroup;
	private String topic;
	private Properties producerProperties = new Properties();
	private Properties consumerProperties = new Properties();

	private Gson gson = new Gson();

	private long pollDuration = 15;

	public KafkaController() {
		bootstrapServers = "kafka:9092";
		consumerGroup = "developer-app";
		topic = "developer-app-topic";

		producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class.getName());
		consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class.getName());
		consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
		consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		try {
			pollDuration = Long.valueOf(System.getenv().getOrDefault("APP_KAFKA_POLL_DURATION", "15"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@PutMapping(path = "/kafka/produce", consumes = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void produceText(@RequestBody String payload) {

		try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties)) {
			logger.info("Sending payload {}", payload);
			ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, payload);
			producer.send(producerRecord);
		}

	}

	@PutMapping(path = "/kafka/produce", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void produceJSON(@RequestBody Map<String, Object> payload) {

		try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties)) {
			logger.info("Sending payload {}", payload);
			ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, gson.toJson(payload));
			producer.send(producerRecord);
		}

	}

	@GetMapping("/kafka/consume")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void consume() {
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties)) {
			consumer.subscribe(Arrays.asList(topic));
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(pollDuration));
			for (ConsumerRecord<String, String> record : records) {
				JsonObject o = new JsonObject();
				o.addProperty("key", record.key());
				o.addProperty("partition", record.partition());
				o.addProperty("offset", record.offset());
				o.addProperty("payload", record.value());
				JsonArray headers = new JsonArray();
				for (Header header : record.headers()) {
					JsonObject h = new JsonObject();
					h.addProperty("key", header.key());
					h.addProperty("value", header.value().toString());
					headers.add(h);
				}
				o.add("headers", headers);
				logger.info(gson.toJson(o));
			}
		}
	}
}
