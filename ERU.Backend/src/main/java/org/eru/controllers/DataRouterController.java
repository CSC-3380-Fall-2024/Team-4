package org.eru.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataRouterController {
    @PostMapping("/datarouter/api/v1/public/data")
    public ResponseEntity<String> data() {
        return ResponseEntity.ok().build();
    }
}
