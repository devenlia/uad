package com.devenlia.uad.controllers;

import com.devenlia.uad.models.Container;
import com.devenlia.uad.models.Link;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.services.LinkService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Resource
    LinkService linkService;

    @GetMapping("/get")
    public ResponseEntity<?> getLink(@RequestParam String id) {
        Link link = linkService.get(id);
        if (link == null) {
            return new ResponseEntity<>("Link Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(link, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLink(@RequestBody Link link) {
        Link savedLink;

        try {
            savedLink = linkService.add(link);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (savedLink == null) return new ResponseEntity<>("Link cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(savedLink, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLink(@RequestBody Link link) {
        try {
            Link savedLink = linkService.update(link);
            if (savedLink == null) {
                return new ResponseEntity<>("Link cannot be updated", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(savedLink, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLink(@RequestParam String id) {
        try {
            linkService.delete(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
