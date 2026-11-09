
package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.ServiceCategory;
import com.example.demo.service.ServiceCategoryService;

@RestController
@RequestMapping("/api")
public class ServiceCategoryController {

    private final ServiceCategoryService categoryService;

    public ServiceCategoryController(
            ServiceCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<ServiceCategory> addCategory(
            @RequestBody ServiceCategory category) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryService.addCategory(category));
    }

    @GetMapping("/categories")
    public List<ServiceCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{id}")
    public ServiceCategory getCategoryById(
            @PathVariable Integer id) {

        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Integer id) {

        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
