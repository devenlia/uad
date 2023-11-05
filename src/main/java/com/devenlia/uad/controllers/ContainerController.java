package com.devenlia.uad.controllers;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.Container;
import com.devenlia.uad.services.ContainerService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/container")
public class ContainerController {

    @Resource
    ContainerService containerService;

    @GetMapping("/get")
    public ResponseEntity<?> getContainer(@RequestParam String id) {
        Container container = containerService.get(id);
        if (container == null) {
            return new ResponseEntity<>("Container Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(container, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addContainer(@RequestBody Container container) {
        Container savedContainer;

        try {
            savedContainer = containerService.add(container);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (savedContainer == null) return new ResponseEntity<>("Container cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(savedContainer, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteContainer(@RequestParam String id) {
        try {
            containerService.delete(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
