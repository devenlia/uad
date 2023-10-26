package com.devenlia.uad.controllers;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.requests.ReqCategory;
import com.devenlia.uad.services.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody ReqCategory reqCategory) {
        Category savedCategory;

        try {
            savedCategory = categoryService.addCategory(reqCategory.getParent(), reqCategory.getCategory());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (savedCategory == null) return new ResponseEntity<>("Category cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }
}
