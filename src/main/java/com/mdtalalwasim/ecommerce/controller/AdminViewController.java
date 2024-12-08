package com.mdtalalwasim.ecommerce.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
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
import com.mdtalalwasim.ecommerce.service.OrderService;
import com.mdtalalwasim.ecommerce.entity.Order;

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
	
	@Autowired
	private OrderService orderService;
	
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
		try {
			// Get all users except current admin
			String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			List<User> allUsers = userService.getAllUsers().stream()
					.filter(user -> !user.getEmail().equals(currentUserEmail))
					.collect(Collectors.toList());
					
			model.addAttribute("allUsers", allUsers);
			return "/admin/users/user-home";
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Error loading users: " + e.getMessage());
			return "redirect:/admin/";
		}
	}
	

	@GetMapping("/edit-user-status")
	public String editUserStatus(@RequestParam("status") Boolean status, 
							   @RequestParam("id") Long id, 
							   HttpSession session) {
		try {
			// Kiểm tra không cho khóa chính mình
			String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			User userToUpdate = userService.getUserById(id);
			
			if(userToUpdate.getEmail().equals(currentUserEmail)) {
				throw new RuntimeException("Cannot change your own status");
			}
			
			// Không cho khóa admin khác
			if("ROLE_ADMIN".equals(userToUpdate.getRole())) {
				throw new RuntimeException("Cannot modify admin account status");
			}
			
			Boolean updated = userService.updateUserStatus(status, id);
			if(updated) {
				session.setAttribute("successMsg", "User status updated successfully!");
			} else {
				session.setAttribute("errorMsg", "Failed to update user status");
			}
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error updating status: " + e.getMessage());
		}
		return "redirect:/admin/get-all-users";
	}
	
	@GetMapping("/orders")
	public String viewOrders(Model model,
                        @RequestParam(required = false) String status,
                        @RequestParam(defaultValue = "desc") String sort,
                        @RequestParam(required = false) String orderId,
                        @RequestParam(required = false) String customer) {
    
        List<Order> orders = orderService.getAllOrders();

        // Filter by status
        if (status != null && !status.isEmpty()) {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status);
            orders = orders.stream()
                          .filter(order -> order.getStatus() == orderStatus)
                          .collect(Collectors.toList());
        }

        // Filter by Order ID
        if (orderId != null && !orderId.isEmpty()) {
            orders = orders.stream()
                          .filter(order -> order.getId().toString().contains(orderId))
                          .collect(Collectors.toList());
        }

        // Filter by Customer name or email
        if (customer != null && !customer.isEmpty()) {
            String searchTerm = customer.toLowerCase();
            orders = orders.stream()
                          .filter(order -> 
                              order.getUser().getName().toLowerCase().contains(searchTerm) ||
                              order.getUser().getEmail().toLowerCase().contains(searchTerm))
                          .collect(Collectors.toList());
        }

        // Sort orders
        orders.sort((a, b) -> {
            if ("asc".equals(sort)) {
                return a.getCreatedAt().compareTo(b.getCreatedAt());
            } else {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
        });

        model.addAttribute("orders", orders);
        return "admin/order/orders";
    }

	@PostMapping("/orders/{id}/approve")
	public String approveOrder(@PathVariable Long id, HttpSession session) {
		try {
			orderService.approveOrder(id);
			session.setAttribute("successMsg", "Order #" + id + " has been approved!");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error approving order: " + e.getMessage());
		}
		return "redirect:/admin/orders";
	}

	@PostMapping("/orders/{id}/cancel")
	public String cancelOrder(@PathVariable Long id, HttpSession session) {
		try {
			orderService.cancelOrder(id);
			session.setAttribute("successMsg", "Order #" + id + " has been cancelled!");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error cancelling order: " + e.getMessage());
		}
		return "redirect:/admin/orders";
	}

	@GetMapping("/revenue")
	public String viewRevenue(Model model,
                         @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) String endDate) {
    
        List<Order> confirmedOrders = orderService.getAllOrders().stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.CONFIRMED)
                .collect(Collectors.toList());

        // Filter by date range if provided
        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            
            confirmedOrders = confirmedOrders.stream()
                .filter(order -> {
                    LocalDate orderDate = order.getCreatedAt().toLocalDate();
                    return !orderDate.isBefore(start) && !orderDate.isAfter(end);
                })
                .collect(Collectors.toList());
        }

        // Calculate revenue metrics
        double totalRevenue = confirmedOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
                
        int totalOrders = confirmedOrders.size();
        
        double averageOrderValue = totalOrders > 0 ? 
                totalRevenue / totalOrders : 0;

        // Add to model
        model.addAttribute("orders", confirmedOrders);
        model.addAttribute("totalRevenue", String.format("%.2f", totalRevenue));
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("averageOrderValue", String.format("%.2f", averageOrderValue));

        return "admin/revenue/revenue";
    }

	@PostMapping("/update-user-role")
	public String updateUserRole(@RequestParam Long userId, 
                           @RequestParam String role,
                           HttpSession session) {
		try {
			User user = userService.getUserById(userId);
			user.setRole(role);
			userService.updateUserProfile(user);
			session.setAttribute("successMsg", "User role updated successfully.");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error updating user role: " + e.getMessage());
		}
		return "redirect:/admin/get-all-users";
	}

	@GetMapping("/edit-user/{id}")
	public String showEditUserForm(@PathVariable Long id, Model model) {
		try {
			User user = userService.getUserById(id);
			
			// Không cho edit admin khác
			if("ROLE_ADMIN".equals(user.getRole())) {
				String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
				if(!user.getEmail().equals(currentUserEmail)) {
					throw new RuntimeException("Cannot edit other admin accounts");
				}
			}
			
			model.addAttribute("user", user);
			return "admin/users/edit-user";
		} catch (Exception e) {
			model.addAttribute("errorMsg", e.getMessage());
			return "redirect:/admin/get-all-users";
		}
	}

	@PostMapping("/update-user")
	public String updateUser(@ModelAttribute User user, 
							@RequestParam(required = false) MultipartFile profileImage,
							HttpSession session) {
		try {
			// Kiểm tra quyền
			String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			User existingUser = userService.getUserById(user.getId());
			
			if("ROLE_ADMIN".equals(existingUser.getRole()) && 
			   !existingUser.getEmail().equals(currentUserEmail)) {
				throw new RuntimeException("Cannot modify other admin accounts");
			}

			// Validate role
			if(user.getRole() == null || user.getRole().isEmpty()) {
				throw new RuntimeException("Role cannot be empty");
			}

			// Xử lý upload ảnh mới
			if (profileImage != null && !profileImage.isEmpty()) {
				String uploadDir = "uploads/profile/";
				Path uploadPath = Paths.get(uploadDir);
				
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());
				Path filePath = uploadPath.resolve(fileName);
				
				try (InputStream inputStream = profileImage.getInputStream()) {
					Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
					user.setProfileImage("/uploads/profile/" + fileName);
				}
			}
			
			userService.updateUser(user);
			session.setAttribute("successMsg", "User updated successfully!");
			
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error updating user: " + e.getMessage());
		}
		return "redirect:/admin/get-all-users";
	}

	@GetMapping("/delete-user/{id}")
	public String deleteUser(@PathVariable Long id, HttpSession session) {
		try {
			// Kiểm tra không cho xóa chính mình
			String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			User userToDelete = userService.getUserById(id);
			
			if(userToDelete.getEmail().equals(currentUserEmail)) {
				throw new RuntimeException("Cannot delete your own account");
			}
			
			// UserService sẽ kiểm tra và không cho xóa ADMIN
			userService.deleteUser(id);
			session.setAttribute("successMsg", "User deleted successfully!");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error deleting user: " + e.getMessage());
		}
		return "redirect:/admin/get-all-users";
	}

}
