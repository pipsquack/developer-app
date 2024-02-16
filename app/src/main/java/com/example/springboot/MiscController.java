package com.example.springboot;

import java.io.IOException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class MiscController {

	Logger logger = LoggerFactory.getLogger("developer-app");

	@GetMapping("/singularity")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void singularity() {
		System.out.println(5 / 0);
	}

	@GetMapping(value = "/environment", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void environment(final HttpServletResponse response) {
		ServletOutputStream out;
		try {
			out = response.getOutputStream();
			for (Entry<String, String> entry : System.getenv().entrySet()) {
				out.println(entry.getKey() + ": " + entry.getValue());
			}
			out.println("=============================");
			for (Entry<Object, Object> property : System.getProperties().entrySet()) {
				out.println(property.getKey() + ": " + property.getValue());
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
