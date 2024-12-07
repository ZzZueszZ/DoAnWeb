package com.mdtalalwasim.ecommerce.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.mdtalalwasim.ecommerce.entity.Category;
import com.mdtalalwasim.ecommerce.service.CategoryService;

@RestController
@RequestMapping("/api")
public class AdminRestController {
	
	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@ModelAttribute Category category,
                                        @RequestParam("file") MultipartFile file) {
		try {
			Category savedCategory = categoryService.saveCategory(category, file);
			return ResponseEntity.ok(savedCategory);
		} catch (Exception e) {
			return ResponseEntity.badRequest()
                               .body("Error saving category: " + e.getMessage());
		}
	}

	@PostMapping("/update-category")
	public ResponseEntity<?> updateCategory(@ModelAttribute Category category,
                                          @RequestParam("file") MultipartFile file) {
		try {
			Category updatedCategory = categoryService.updateCategory(category, file);
			return ResponseEntity.ok(updatedCategory);
		} catch (Exception e) {
			return ResponseEntity.badRequest()
                               .body("Error updating category: " + e.getMessage());
		}
	}
}
