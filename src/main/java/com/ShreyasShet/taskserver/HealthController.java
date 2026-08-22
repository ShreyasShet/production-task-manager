package com.ShreyasShet.taskserver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {
    
    @GetMapping("/")
    public ResponseEntity<String> root(){
        return ResponseEntity.ok("Task manager API is running");
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health(){
        return ResponseEntity.ok(Map.of("status", "up"));
    }
}
