package org.eru.controllers;

import org.eru.models.eru.friends.SocialBan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class FriendController {
    @GetMapping("/friends/api/v1/{accountId}/blocklist")
    public ResponseEntity<String[]> blocklist(@PathVariable String accountId) {
        return ResponseEntity.ok(new String[0]);
    }

    @GetMapping(value = "/friends/api/v1/{accountId}/settings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> summary(@PathVariable String accountId) {
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @GetMapping("/socialban/api/public/v1/{accountId}")
    public ResponseEntity<SocialBan> socialBan(@PathVariable String accountId) {
        return ResponseEntity.ok(new SocialBan(new ArrayList<>(), new ArrayList<>()));
    }
}
