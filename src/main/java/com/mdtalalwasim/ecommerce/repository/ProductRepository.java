package com.mdtalalwasim.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mdtalalwasim.ecommerce.entity.Product;
import com.mdtalalwasim.ecommerce.entity.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findByIsActiveTrue(Pageable pageable);

	Page<Product> findByCategory(Category category, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
		   "(LOWER(p.productTitle) LIKE LOWER(CONCAT('%', :search, '%')) OR :search IS NULL)")
	Page<Product> findByProductTitleContainingAndIsActiveTrue(String search, Pageable pageable);

}
