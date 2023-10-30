package com.example.springboot;

import java.io.IOException;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LoadController {

    Logger logger = LoggerFactory.getLogger("developer-app");
    ObjectMapper mapper = new ObjectMapper();
    private ClassicHttpRequest httpGet;

    public LoadController() {
        httpGet = ClassicRequestBuilder.get("https://fakerapi.it/api/v1/custom")
                .addParameter("_quantity", "10")
                .addParameter("cc", "card_number")
                .addParameter("value", "buildingNumber")
                .build();
    }

    @GetMapping("/process")
    public ResponseEntity<String> process() {
        ResponseEntity<String> response = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            response = (ResponseEntity<String>) httpclient.execute(httpGet, r -> {
                if (HttpStatus.valueOf(r.getCode()).is2xxSuccessful()) {                    
                    JsonNode json = mapper.readTree(r.getEntity().getContent());
                    JsonNode data = json.get("data");
                    JsonNode top = null;
                    for (JsonNode jsonNode : data) {
                        if (top == null) {
                            top = jsonNode;
                        } else {
                            if (Integer.parseInt(jsonNode.get("value").textValue()) > Integer
                                    .parseInt(top.get("value").textValue())) {
                                top = jsonNode;
                            }
                        }
                    }
                    logger.info("Highest value credit card is {}", top.get("cc").textValue());
                    return new ResponseEntity<String>(json.toString(), HttpStatusCode.valueOf(r.getCode()));
                } else {
                    return new ResponseEntity<String>(r.getReasonPhrase(), HttpStatusCode.valueOf(r.getCode()));
                }
            });
        } catch (

        IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
