package com.mdtalalwasim.ecommerce.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mdtalalwasim.ecommerce.entity.Product;
import com.mdtalalwasim.ecommerce.entity.Category;
import com.mdtalalwasim.ecommerce.repository.ProductRepository;
import com.mdtalalwasim.ecommerce.repository.CategoryRepository;
import com.mdtalalwasim.ecommerce.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product saveProduct(Product product, MultipartFile file) {
        try {
            // Validate category
            if (product.getCategory() != null && product.getCategory().getId() != null) {
                Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
                product.setCategory(category);
            }

            // Set default values
            product.setDiscount(0);
            product.setDiscountPrice(product.getProductPrice());
            product.setIsActive(true);
            product.setIsDiscountActive(false);

            // Handle file upload
            if (file != null && !file.isEmpty()) {
                // Đường dẫn tới thư mục lưu trữ ảnh
                Path uploadPath = Paths.get("uploads/products/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Lưu file ảnh vào thư mục lưu trữ
                Path filePath = uploadPath.resolve(file.getOriginalFilename());
                Files.write(filePath, file.getBytes());

                // Cập nhật đường dẫn ảnh trong database
                product.setProductImage("/uploads/products/" + file.getOriginalFilename());
            } else {
                product.setProductImage("/uploads/products/default.png");
            }

            return productRepository.save(product);
            
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    @Override
    public Boolean deleteProduct(long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            productRepository.deleteById(product.get().getId());
            return true;
        }
        return false;
    }

//    @Override
//    public Optional<Product> findById(long id) {
//        return productRepository.findById(id);
//    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public Product updateProductById(Product product, MultipartFile file) {
        try {
            Product existingProduct = getProductById(product.getId());

            // Update basic info
            existingProduct.setProductTitle(product.getProductTitle());
            existingProduct.setProductDescription(product.getProductDescription());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setProductPrice(product.getProductPrice());
            existingProduct.setProductStock(product.getProductStock());
            existingProduct.setIsActive(product.getIsActive());
            
            // Update discount info
            existingProduct.setDiscount(product.getDiscount());
            Double discount = product.getProductPrice() * (product.getDiscount()/100.0);
            Double discountPrice = product.getProductPrice() - discount;
            existingProduct.setDiscountPrice(discountPrice);
            existingProduct.setDiscountStartDate(product.getDiscountStartDate());
            existingProduct.setDiscountEndDate(product.getDiscountEndDate());
            existingProduct.setIsDiscountActive(product.getIsDiscountActive());

            // Handle file upload if new file is provided
            if (file != null && !file.isEmpty()) {
                // Đường dẫn tới thư mục lưu trữ ảnh
                Path uploadPath = Paths.get("uploads/products/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Lưu file ảnh vào thư mục lưu trữ
                Path filePath = uploadPath.resolve(file.getOriginalFilename());
                Files.write(filePath, file.getBytes());

                // Cập nhật đường dẫn ảnh trong database
                existingProduct.setProductImage("/uploads/products/" + file.getOriginalFilename());
            }

            return productRepository.save(existingProduct);
            
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    @Override
    public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) 
                   ? Sort.by(sortField).ascending()
                   : Sort.by(sortField).descending();
        
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return productRepository.findByIsActiveTrue(pageable);
    }
    
    @Override
    public Page<Product> findByCategory(String categoryName, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        if(ObjectUtils.isEmpty(categoryName)) {
            return productRepository.findByIsActiveTrue(pageable);
        }
        
        Category category = categoryRepository.findByCategoryName(categoryName)
            .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));
        return productRepository.findByCategory(category, pageable);
    }
    
    @Override
    public Page<Product> searchProducts(String search, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        if (search != null && !search.trim().isEmpty()) {
            return productRepository.findByProductTitleContainingAndIsActiveTrue(search.trim(), pageable);
        }
        return productRepository.findByIsActiveTrue(pageable);
    }
}
