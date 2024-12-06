package com.mdtalalwasim.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdtalalwasim.ecommerce.entity.Product;
import com.mdtalalwasim.ecommerce.entity.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByIsActiveTrue();

	List<Product> findByCategory(Category category);

	List<Product> findByCategoryId(Long categoryId);

	List<Product> findByCategoryCategoryName(String categoryName);

}
