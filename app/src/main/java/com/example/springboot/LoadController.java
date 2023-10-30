package com.example.springboot;

import java.io.IOException;
import java.io.InputStreamReader;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class LoadController {

    Logger logger = LoggerFactory.getLogger("developer-app");
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
                    JsonObject json = (JsonObject) JsonParser.parseReader(new InputStreamReader(r.getEntity().getContent()));
                    JsonArray data = json.get("data").getAsJsonArray();
                    JsonObject top = null;
                    for (JsonElement element : data) {
                        if (element instanceof JsonObject) {
                            JsonObject node = (JsonObject) element;
                            if (top == null) {
                                top = node;
                            } else {
                                if (Integer.parseInt(node.get("value").getAsString()) > Integer
                                        .parseInt(top.get("value").getAsString())) {
                                    top = node;
                                }
                            }
                        }
                    }
                    logger.info("Highest value credit card is {}", top.get("cc").getAsString());
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
