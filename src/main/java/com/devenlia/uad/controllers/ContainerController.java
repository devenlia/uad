package com.devenlia.uad.controllers;

import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.requests.ReqContainer;
import com.devenlia.uad.services.ContainerService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/container")
public class ContainerController {

    @Resource
    ContainerService containerService;

    @PostMapping("/addContainer")
    public ResponseEntity<?> addContainer(@RequestBody ReqContainer reqContainer) {
        Container savedContainer;

        try {
            savedContainer = containerService.add(reqContainer.getParent(), reqContainer.getContainer());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (savedContainer == null) return new ResponseEntity<>("Container cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(savedContainer, HttpStatus.OK);
    }
}
