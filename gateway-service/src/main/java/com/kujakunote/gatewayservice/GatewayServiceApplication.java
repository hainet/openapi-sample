package com.kujakunote.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/resources")
public class GatewayServiceApplication {

    private final RestOperations restOperations;

    public GatewayServiceApplication(final RestTemplateBuilder builder) {
        this.restOperations = builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @GetMapping("")
    public ResponseEntity<List<Map<String, Object>>> findResources() {
        return restOperations.exchange(
                "http://localhost:8081/resources",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }
        );
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createResource(@RequestBody final Map<String, Object> resource) {
        return restOperations.exchange(
                URI.create("http://localhost:8081/resources"),
                HttpMethod.POST,
                new HttpEntity<>(resource),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findResource(@PathVariable final String id) {
        return restOperations.exchange(
                "http://localhost:8081/resources" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateResource(
            @PathVariable final String id,
            @RequestBody final Map<String, Object> resource) {
        return restOperations.exchange(
                "http://localhost:8081/resources" + id,
                HttpMethod.PUT,
                new HttpEntity<>(resource),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteResource(@PathVariable final String id) {
        return restOperations.exchange(
                "http://localhost:8081/resources" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
