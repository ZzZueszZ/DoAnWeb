package com.mdtalalwasim.ecommerce.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.mdtalalwasim.ecommerce.entity.Category;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {
	
	public Category saveCategory(Category category, MultipartFile file) throws IOException;
	
	boolean existCategory(String categoryName);
	
	public List<Category> getAllCategories();
	
	public Boolean deleteCategory(long id);
	
	public Optional<Category> findById(long id);
	
	List<Category> findAllActiveCategory();
	
	public Category updateCategory(Category category, MultipartFile file) throws IOException;
	

}
