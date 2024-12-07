package com.mdtalalwasim.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.mdtalalwasim.ecommerce.entity.Product;

public interface ProductService {

	Product saveProduct(Product product, MultipartFile file);

	Product updateProductById(Product product, MultipartFile file);

	Product getProductById(long id);

	Boolean deleteProduct(long id);

	Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	Page<Product> findByCategory(String category, int pageNo, int pageSize);

	Page<Product> searchProducts(String search, int pageNo, int pageSize);
}
