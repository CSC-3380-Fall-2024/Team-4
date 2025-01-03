package org.eru.controllers;

import org.eru.models.eru.Lightswitch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class LightswitchController {
    @GetMapping("lightswitch/api/service/{serviceId}/status")
    public ResponseEntity<Lightswitch> status(@PathVariable("serviceId") String serviceId) {
        return ResponseEntity.ok(new Lightswitch(serviceId, new String[0]));
    }

    @GetMapping("lightswitch/api/service/bulk/status")
    public ResponseEntity<ArrayList<Lightswitch>> bulkStatus(@RequestParam("serviceId") String serviceId) {
        ArrayList<Lightswitch> ret = new ArrayList<>();
        ret.add(new Lightswitch(serviceId, new String[] { "BROWSE", "DOWNLOAD" }));

        return ResponseEntity.ok(ret);
    }
}
