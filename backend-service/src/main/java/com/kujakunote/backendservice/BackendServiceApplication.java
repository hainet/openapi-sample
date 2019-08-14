package com.kujakunote.backendservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@RequestMapping("/resources")
public class BackendServiceApplication {

    private final JdbcTemplate jdbc;

    public BackendServiceApplication(final JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendServiceApplication.class, args);
    }

    @GetMapping("")
    public List<Map<String, Object>> findResources() {
        return jdbc.queryForList("SELECT * FROM resource")
                .stream()
                .map(it -> it.keySet()
                        .stream()
                        .collect(Collectors.toMap(String::toLowerCase, it::get)))
                .collect(Collectors.toList());
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createResource(@RequestBody final Map<String, Object> resource) {
        jdbc.update("INSERT INTO resource (name) VALUES ?", resource.get("name"));

        Map<String, Object> newResource = jdbc.queryForMap("SELECT * FROM resource ORDER BY id DESC LIMIT 1");

        return ResponseEntity
                .created(URI.create("http://localhost:8081/resources/" + newResource.get("id")))
                .body(newResource.keySet()
                        .stream()
                        .collect(Collectors.toMap(String::toLowerCase, newResource::get))
                );
    }

    @GetMapping("/{id}")
    public Map<String, Object> findResource(@PathVariable final String id) {
        Map<String, Object> resource = jdbc.queryForMap("SELECT * FROM resource WHERE id = ?", id);

        return resource.keySet()
                .stream()
                .collect(Collectors.toMap(String::toLowerCase, resource::get));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateResource(
            @PathVariable final String id,
            @RequestBody final Map<String, Object> resource) {
        try {
            jdbc.queryForMap("SELECT * FROM resource WHERE id = ?", id);

            jdbc.update("UPDATE resource SET name = ? WHERE id = ?", resource.get("name"), id);

            return ResponseEntity
                    .noContent()
                    .build();
        } catch (final EmptyResultDataAccessException e) {
            jdbc.update("INSERT INTO resource (id, name) VALUES (?, ?)", id, resource.get("name"));

            Map<String, Object> newResource = jdbc.queryForMap("SELECT * FROM resource ORDER BY id DESC LIMIT 1");

            return ResponseEntity
                    .created(null)
                    .body(newResource.keySet()
                            .stream()
                            .collect(Collectors.toMap(String::toLowerCase, newResource::get))
                    );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteResource(@PathVariable final String id) {
        jdbc.update("DELETE FROM resource WHERE id = ?", id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
