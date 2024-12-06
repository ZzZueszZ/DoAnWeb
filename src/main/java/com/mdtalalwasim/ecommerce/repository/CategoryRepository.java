package com.mdtalalwasim.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdtalalwasim.ecommerce.entity.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryName(String categoryName);

	public List<Category> findByIsActiveTrue();

	Optional<Category> findByCategoryName(String categoryName);

}
