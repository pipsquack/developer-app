package com.example.springboot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class DummyServiceNode {

    Logger logger = LoggerFactory.getLogger("developer-app");
    private List<ClassicHttpRequest> endpoints = new ArrayList<ClassicHttpRequest>();

    public DummyServiceNode() {
        String endpoints = System.getenv("APP_ENDPOINTS");
        if (endpoints != null) {
            for (String url : endpoints.split(";")) {
                this.endpoints.add(ClassicRequestBuilder.get(url).build());
            }
        }
    }

    @GetMapping("/downstream")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void downstream(HttpServletRequest request) {
        logger.info("Responding from {}", request.getServerName());
        ObjectMapper om = new ObjectMapper();
        ObjectNode headers = om.createObjectNode();
        for (String headerName : Collections.list(request.getHeaderNames())) {
            headers.put(headerName, request.getHeader(headerName));
        }
        JsonNode node = om.createObjectNode().set("http", om.createObjectNode().set("headers", headers));
        logger.info(node.toPrettyString());
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            for (ClassicHttpRequest endpoint : endpoints) {
                httpclient.execute(endpoint, response -> {
                    final HttpEntity entity = response.getEntity();
                    EntityUtils.consume(entity);
                    return null;
                });
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping(value = "/echo", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> echo(@RequestBody String content) {
        logger.info("Echoing {}", content);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

}
