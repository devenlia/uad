package com.devenlia.uad.controllers;

import com.devenlia.uad.models.Link;
import com.devenlia.uad.models.requests.ReqLink;
import com.devenlia.uad.services.LinkService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Resource
    LinkService linkService;

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
}
