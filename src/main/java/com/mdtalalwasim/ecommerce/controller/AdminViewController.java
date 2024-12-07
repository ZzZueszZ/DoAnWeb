package com.mdtalalwasim.ecommerce.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mdtalalwasim.ecommerce.entity.Category;
import com.mdtalalwasim.ecommerce.entity.Product;
import com.mdtalalwasim.ecommerce.entity.User;
import com.mdtalalwasim.ecommerce.service.CartService;
import com.mdtalalwasim.ecommerce.service.CategoryService;
import com.mdtalalwasim.ecommerce.service.ProductService;
import com.mdtalalwasim.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;

	@Autowired
	UserService userService;
	
	@Autowired
	CartService cartService;
	
	//to track which user is login right Now
	//by default call this method when any request come to this controller because of @ModelAttribut
	@ModelAttribute 
	public void getUserDetails(Principal principal, Model model) {
		if(principal != null) {
			String currenLoggedInUserEmail = principal.getName();
			User currentUserDetails = userService.getUserByEmail(currenLoggedInUserEmail);
			//System.out.println("Current Logged In User is :: ADMIN Controller :: "+currentUserDetails.toString());
			model.addAttribute("currentLoggedInUserDetails",currentUserDetails);
			
			//for showing user cart count
			Long countCartForUser = cartService.getCounterCart(currentUserDetails.getId());
			System.out.println("Admin Cart Count :"+countCartForUser);
			model.addAttribute("countCartForUser", countCartForUser);
			
		}
		List<Category> allActiveCategory = categoryService.findAllActiveCategory();
		model.addAttribute("allActiveCategory",allActiveCategory);
		
	}
	
	@GetMapping("/")
	public String adminIndex() {
		
		return "admin/admin-dashboard";
	}
	
	
	//CATEGORY-MODULE-START
	
	@GetMapping("/add-category")
	public String addCategory(Model model) {
		
		return "admin/category/category-add-form";
	}
	
	@PostMapping("/save-category")
	public String saveCategory(@ModelAttribute Category category, 
                             @RequestParam("file") MultipartFile file,
                             HttpSession session) {
		try {
			Category savedCategory = categoryService.saveCategory(category, file);
			if (savedCategory != null) {
				session.setAttribute("successMsg", "Category saved successfully");
			} else {
				session.setAttribute("errorMsg", "Error saving category");
			}
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error saving category: " + e.getMessage());
		}
		return "redirect:/admin/category";
	}

	@GetMapping("/category")
	public String category(Model model) {
		System.out.println("category:WWWWWWWWW");
		List<Category> allCategories = categoryService.getAllCategories();
		System.out.println("category: "+allCategories.toString());
		for (Category category : allCategories) {
			//category.getCreatedAt();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
			String format = formatter.format(category.getCreatedAt());
			model.addAttribute("formattedDateTimeCreatedAt",format);
			
		}
		
		model.addAttribute("allCategoryList",allCategories);
		
		return "/admin/category/category-home";
	}
	
	
	@GetMapping("/edit-category/{id}")
	public String editCategoryForm(@PathVariable("id") long id, Model model) {
		//System.out.println("ID :"+id);
		Optional<Category> categoryObj = categoryService.findById(id);
		if(categoryObj.isPresent()) {
			Category category = categoryObj.get();
			model.addAttribute("category", category);
		}else {
			System.out.println("ELSEEEEE");
		}
		return "/admin/category/category-edit-form";
	}
	
	
	@PostMapping("/update-category")
	public String updateCategory(@ModelAttribute Category category,
                               @RequestParam("file") MultipartFile file,
                               HttpSession session) {
		try {
			Category updatedCategory = categoryService.updateCategory(category, file);
			if (updatedCategory != null) {
				session.setAttribute("successMsg", "Category updated successfully");
			} else {
				session.setAttribute("errorMsg", "Error updating category");
			}
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error updating category: " + e.getMessage());
		}
		return "redirect:/admin/category";
	}
	
	@GetMapping("/delete-category/{id}")
	public String deleteCategory(@PathVariable("id") long id, HttpSession session) {
		Boolean deleteCategory = categoryService.deleteCategory(id);
		if(deleteCategory) {
			session.setAttribute("successMsg", "Category Deleted Successfully");
		}else {
			session.setAttribute("errorMsg", "Server Error");
		}
		
		return "redirect:/admin/category";
	}
	
	
	//PRODUCT-MODULE-START
	
	@GetMapping("/add-product")
	public String addProduct(Model model) {
		List<Category> allCategories = categoryService.getAllCategories();
		model.addAttribute("categories", allCategories);
		return "admin/product/add-product";
	}
	

	
	@PostMapping("/save-product")
	public String saveProduct(@ModelAttribute Product product, 
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         HttpSession session) {
		try {
			Product savedProduct = productService.saveProduct(product, file);
			if (savedProduct != null) {
				session.setAttribute("successMsg", "Product saved successfully");
			} else {
				session.setAttribute("errorMsg", "Error saving product");
			}
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error saving product: " + e.getMessage());
		}
		return "redirect:/admin/product-list";
	}
	
	@GetMapping("/product-list")
	public String productList(Model model) {
		// Lấy tất cả sản phẩm với phân trang
		Page<Product> products = productService.findPaginated(1, Integer.MAX_VALUE, "id", "desc");
		model.addAttribute("productList", products.getContent());
		return "/admin/product/product-list";
	}
	
	@GetMapping("/delete-product/{id}")
	public String deleteProduct(@PathVariable("id") long id, HttpSession session) {
		Boolean deleteProduct = productService.deleteProduct(id);
		
		if(deleteProduct) {
			session.setAttribute("successMsg", "Product Deleted Successfully.");
		}else {
			session.setAttribute("errorMsg", "Something Wrong on server while deleting Product");
		}
		return "redirect:/admin/product-list";
		
	}
	
	@GetMapping("/edit-product/{id}")
	public String editProduct(@PathVariable long id,Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product",product);
		model.addAttribute("allCategoryList",categoryService.getAllCategories());
		return "/admin/product/edit-product";
	}
	
	@PostMapping("/update-product")
	public String updateProduct(@ModelAttribute Product product, 
                          @RequestParam("file") MultipartFile file,
                          HttpSession session) {
		try {
			if (product.getDiscount() < 0 || product.getDiscount() > 100) {
				session.setAttribute("errorMsg", "Invalid discount value!");
				return "redirect:/admin/product-list";
			}

			Product updatedProduct = productService.updateProductById(product, file);
			if (updatedProduct != null) {
				session.setAttribute("successMsg", "Product updated successfully");
			} else {
				session.setAttribute("errorMsg", "Error updating product");
			}
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error updating product: " + e.getMessage());
		}
		return "redirect:/admin/product-list";
	}
	
	
	
	//USER-WORK
	//get all users
	@GetMapping("/get-all-users")
	public String getAllUser(Model model) {
		
		List<User> allUsers = userService.getAllUsersByRole("ROLE_USER");
		for (User user : allUsers) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String format = formatter.format(user.getCreatedAt());
			model.addAttribute("formattedDateTimeCreatedAt",format);
			
		}
		model.addAttribute("allUsers",allUsers);
		
		return "/admin/users/user-home";
		
	}
	

	@GetMapping("/edit-user-status")
	public String editUser(@RequestParam("status") Boolean status, @RequestParam("id") Long id, Model model, HttpSession session) {
		Boolean updateUserStatus = userService.updateUserStatus(status,id);
		if(updateUserStatus == true) {
			session.setAttribute("successMsg", "User Status Updated Successfully.");
		}
		else {
			session.setAttribute("errorMsg", "Something Wrong on server while Updating User status");
		}
		return "redirect:/admin/get-all-users";
		
	}
	


}
