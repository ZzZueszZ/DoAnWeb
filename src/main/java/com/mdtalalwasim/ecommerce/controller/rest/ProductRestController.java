package com.mdtalalwasim.ecommerce.controller.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mdtalalwasim.ecommerce.entity.Product;
import com.mdtalalwasim.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage = productService.findPaginated(page, size, "id", "desc");
        return ResponseEntity.ok(productPage.getContent());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Product>> getAllActiveProducts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage;
        if (category != null && !category.isEmpty()) {
            productPage = productService.findByCategory(category, page, size);
        } else {
            productPage = productService.findPaginated(page, size, "id", "desc");
        }
        return ResponseEntity.ok(productPage.getContent());
    }
    
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage = productService.findByCategory(categoryName, page, size);
        return ResponseEntity.ok(productPage.getContent());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
} 