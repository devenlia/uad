package com.devenlia.uad.controllers;

import com.devenlia.uad.models.Category;
import com.devenlia.uad.models.Page;
import com.devenlia.uad.models.requests.ReqCategory;
import com.devenlia.uad.services.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<?> getCategory(@RequestParam String id) {
        Category category = categoryService.get(id);
        if (category == null) {
            return new ResponseEntity<>("Category Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        Category savedCategory;

        try {
            savedCategory = categoryService.add(category);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (savedCategory == null) return new ResponseEntity<>("Category cannot be saved", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam String id) {
        try {
            categoryService.delete(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
