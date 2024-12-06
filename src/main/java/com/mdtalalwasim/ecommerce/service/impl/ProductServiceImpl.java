package com.mdtalalwasim.ecommerce.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
    public Product saveProduct(Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
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

    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public Product updateProductById(Product product, MultipartFile file) {
        Product dbProductById = getProductById(product.getId());

        String imageName = file.isEmpty() ? dbProductById.getProductImage() : file.getOriginalFilename();
        dbProductById.setProductImage(imageName);
        dbProductById.setProductTitle(product.getProductTitle());
        dbProductById.setProductDescription(product.getProductDescription());
        dbProductById.setCategory(product.getCategory());
        dbProductById.setProductPrice(product.getProductPrice());
        dbProductById.setProductStock(product.getProductStock());
        dbProductById.setCreatedAt(product.getCreatedAt());
        dbProductById.setIsActive(product.getIsActive());
        
        dbProductById.setDiscount(product.getDiscount());
        Double discount = product.getProductPrice() * (product.getDiscount()/100.0);
        Double discountPrice = product.getProductPrice() - discount;
        dbProductById.setDiscountPrice(discountPrice);
        
        dbProductById.setDiscountStartDate(product.getDiscountStartDate());
        dbProductById.setDiscountEndDate(product.getDiscountEndDate());
        dbProductById.setIsDiscountActive(product.getIsDiscountActive());

        Product updatedProduct = productRepository.save(dbProductById);

        if(!ObjectUtils.isEmpty(updatedProduct) && !file.isEmpty()) {
            try {
                File savefile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + "product_image" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return updatedProduct;
    }

    @Override
    public List<Product> findAllActiveProducts(String categoryName) {
        if(ObjectUtils.isEmpty(categoryName)) {
            return productRepository.findByIsActiveTrue();
        }
        
        Category category = categoryRepository.findByCategoryName(categoryName)
            .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName)
            .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
