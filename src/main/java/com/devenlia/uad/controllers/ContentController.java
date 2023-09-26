package com.devenlia.uad.controllers;

import com.devenlia.uad.models.*;
import com.devenlia.uad.models.requests.*;
import com.devenlia.uad.services.ContentService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Resource
    private ContentService contentService;

    @PostMapping("/addPage")
    public ResponseEntity<?> addPage(@RequestBody ReqPage reqPage) {
        try {
            Page savedPage = contentService.addPage(reqPage.getParent(), reqPage.getPage());
            if (savedPage == null) {
                return new ResponseEntity<>("Page cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(savedPage, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/searchPage")
    public ResponseEntity<?> searchPage(@RequestParam String path) {
        Page page;
        try {
            page = contentService.searchPage(path);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/getPage")
    public ResponseEntity<?> getHomePage() {
        Page page = contentService.getPage("0");
        if (page == null) {
            return new ResponseEntity<>("Page Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/getPage/{id}")
    public ResponseEntity<?> getPage(@PathVariable String id) {
        Page page = contentService.getPage(id);
        if (page == null) {
            return new ResponseEntity<>("Page Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/addContainer")
    public ResponseEntity<?> addContainer(@RequestBody ReqContainer reqContainer) {
        Container savedContainer;

        try {
            savedContainer = contentService.addContainer(reqContainer.getParent(), reqContainer.getContainer());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (savedContainer == null) return new ResponseEntity<>("Container cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(savedContainer, HttpStatus.OK);
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody ReqCategory reqCategory) {
        Category savedCategory;

        try {
            savedCategory = contentService.addCategory(reqCategory.getParent(), reqCategory.getCategory());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (savedCategory == null) return new ResponseEntity<>("Category cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    @PostMapping("/addLink")
    public ResponseEntity<?> addLink(@RequestBody ReqLink reqLink) {
        Link savedLink;

        try {
            savedLink = contentService.addLink(reqLink.getParent(), reqLink.getLink());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (savedLink == null) return new ResponseEntity<>("Link cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(savedLink, HttpStatus.OK);
    }
}
