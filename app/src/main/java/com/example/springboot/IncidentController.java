package com.example.springboot;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Random;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class IncidentController {

	Logger logger = LoggerFactory.getLogger("developer-app");
	Random rand = new Random();

	@GetMapping(value = "/incident/record")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void incidentRecord(HttpServletRequest request,
			@RequestParam(name = "duration", required = false, defaultValue = "-1") int duration) {
		logger.info("Responding from {}", request.getServerName());

		Clock clock = Clock.systemUTC();
		Instant instant = clock.instant();
		long seconds = instant.getEpochSecond();
		long nano = instant.getNano();
		long finish = (long) (seconds * 1E9 + nano);
		duration = duration < 0 ? rand.nextInt(60) : duration;
		long start = (long) (finish - duration * 60 * 1E9);

		ObjectMapper om = new ObjectMapper();
		ObjectNode attributes = om.createObjectNode();
		attributes.set("services", om.createArrayNode().add(System.getenv("DD_SERVICE")));
		attributes.put("started_at", start);
		attributes.put("finished_at", finish);
		ObjectNode git = om.createObjectNode();
		git.put("commit_sha", System.getenv("DD_GIT_COMMIT_SHA"));
		git.put("repository_url", System.getenv("DD_GIT_REPOSITORY_URL"));
		attributes.set("git", git);
		attributes.put("name", "Issue with upstream Fake API");
		attributes.put("severity", "1");
		attributes.put("version", System.getenv("DD_VERSION"));
		ObjectNode data = om.createObjectNode();
		data.set("attributes", attributes);
		ObjectNode node = om.createObjectNode();
		node.set("data", data);
		logger.info(node.toPrettyString());

		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			ClassicHttpRequest doraRequest = ClassicRequestBuilder
					.post("https://api.datadoghq.com/api/v2/dora/incident")
					.setEntity(om.writeValueAsString(node))
					.addHeader("Accept", "application/json")
					.addHeader("Content-Type", "application/json")
					.addHeader("DD-API-KEY", System.getenv("DD_API_KEY"))
					.build();
			httpclient.execute(doraRequest, response -> {
				if (!HttpStatus.valueOf(response.getCode()).is2xxSuccessful()) {
					logger.error("Failed to record incident.");
					logger.error("Code: {} Message: {}", response.getCode(), response.getReasonPhrase());
				}
				final HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);
				return null;
			});
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
