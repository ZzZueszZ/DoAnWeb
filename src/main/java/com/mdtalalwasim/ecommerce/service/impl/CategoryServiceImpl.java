package com.mdtalalwasim.ecommerce.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mdtalalwasim.ecommerce.entity.Category;
import com.mdtalalwasim.ecommerce.repository.CategoryRepository;
import com.mdtalalwasim.ecommerce.service.CategoryService;
@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category saveCategory(Category category, MultipartFile file) throws IOException {
		// Handle file upload
		if (file != null && !file.isEmpty()) {
			// Đường dẫn tới thư mục lưu trữ ảnh
			Path uploadPath = Paths.get("uploads/categorys/");
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Lưu file ảnh vào thư mục lưu trữ
			Path filePath = uploadPath.resolve(file.getOriginalFilename());
			Files.write(filePath, file.getBytes());

			// Cập nhật đường dẫn ảnh trong database
			category.setCategoryImage("/uploads/categorys/" + file.getOriginalFilename());
		} else {
			category.setCategoryImage("/uploads/categorys/default.png");
		}

		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategory(Category category, MultipartFile file) throws IOException {
		Category existingCategory = categoryRepository.findById(category.getId())
			.orElseThrow(() -> new RuntimeException("Category not found"));

		existingCategory.setCategoryName(category.getCategoryName());
		existingCategory.setIsActive(category.getIsActive());

		// Handle file upload if new file is provided
		if (file != null && !file.isEmpty()) {
			// Đường dẫn tới thư mục lưu trữ ảnh
			Path uploadPath = Paths.get("uploads/categorys/");
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// Lưu file ảnh vào thư mục lưu trữ
			Path filePath = uploadPath.resolve(file.getOriginalFilename());
			Files.write(filePath, file.getBytes());

			// Cập nhật đường dẫn ảnh trong database
			existingCategory.setCategoryImage("/uploads/categorys/" + file.getOriginalFilename());
		}

		return categoryRepository.save(existingCategory);
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public boolean existCategory(String categoryName) {
		// TODO Auto-generated method stub
		return categoryRepository.existsByCategoryName(categoryName);
	}

	@Override
	public Boolean deleteCategory(long id) {
		// TODO Auto-generated method stub

		Category categoryFound = categoryRepository.findById(id).orElse(null);

		if(!ObjectUtils.isEmpty(categoryFound)) {
			categoryRepository.delete(categoryFound);
			return true;
		}

		return false;
	}

	@Override
	public Optional<Category> findById(long id) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(id);
	}

	@Override
	public List<Category> findAllActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrue();
		return categories;
	}

}
