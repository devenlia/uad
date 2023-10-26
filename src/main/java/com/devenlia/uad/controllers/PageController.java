package com.devenlia.uad.controllers;

import com.devenlia.uad.models.Page;
import com.devenlia.uad.models.requests.ReqPage;
import com.devenlia.uad.services.PageService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/page")
public class PageController {

    @Resource
    private PageService pageService;

    @GetMapping("/search")
    public ResponseEntity<?> searchPage(@RequestParam String path) {
        Page page;
        try {
            page = pageService.searchPage(path);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/home")
    public ResponseEntity<?> getHomePage() {
        Page page = pageService.getPage("0");
        if (page == null) {
            page = pageService.createHomePage();
        }
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPage(@RequestParam String id) {
        Page page = pageService.getPage(id);
        if (page == null) {
            return new ResponseEntity<>("Page Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPage(@RequestBody ReqPage reqPage) {
        try {
            Page savedPage = pageService.addPage(reqPage.getPage());
            if (savedPage == null) {
                return new ResponseEntity<>("Page cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(savedPage, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
