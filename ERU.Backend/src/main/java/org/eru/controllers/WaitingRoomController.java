package org.eru.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaitingRoomController {
    @GetMapping("/waitingroom/api/waitingroom")
    public ResponseEntity waitingroom() {
        return ResponseEntity.status(204).build();
    }
}
