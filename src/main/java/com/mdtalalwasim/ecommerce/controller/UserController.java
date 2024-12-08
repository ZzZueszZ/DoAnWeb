package com.mdtalalwasim.ecommerce.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import com.mdtalalwasim.ecommerce.entity.*;
import com.mdtalalwasim.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private ReviewService reviewService;
	
	//to track which user is login right Now
	//by default call this method when any request come to this controller because of @ModelAttribut
	@ModelAttribute 
	public void getUserDetails(Principal principal, Model model) {
		if(principal != null) {
			String currenLoggedInUserEmail = principal.getName();
			User currentUserDetails = userService.getUserByEmail(currenLoggedInUserEmail);
			//System.out.println("Current Logged In User is :: USER Controller :: "+currentUserDetails.toString());
			model.addAttribute("currentLoggedInUserDetails",currentUserDetails);
			
			//for showing user cart count
			Long countCartForUser = cartService.getCounterCart(currentUserDetails.getId());
			//System.out.println("User Cart Count :"+countCartForUser);
			model.addAttribute("countCartForUser", countCartForUser);
		}
		
		List<Category> allActiveCategory = categoryService.findAllActiveCategory();
		model.addAttribute("allActiveCategory",allActiveCategory);
		
	}
	
	
	@GetMapping("/")
	public String home(){
		return "user/user-home";
	}
	
	
	//ADD TO CART Module
	@GetMapping("/add-to-cart")
	String addToCart(@RequestParam Long productId, @RequestParam Long userId, HttpSession session) {
		System.out.println("INSIDE ITS");
		Cart saveCart = cartService.saveCart(productId, userId);
		
		//System.out.println("save Cart is :"+saveCart);
		if(ObjectUtils.isEmpty(saveCart)) {
			System.out.println("INSIDE Error");
			session.setAttribute("errorMsg", "Failed Product add to Cart");
		}else {
			session.setAttribute("successMsg", "Successfully, Product added to Cart");
		}
		System.out.println("pid"+productId+" uid:"+userId);
		return "redirect:/product/" + productId;
	}
	
	@GetMapping("/cart")
	String loadCartPage(Principal principal, Model model) {
		//when load cart, it is showing logged in user cart details:
		
		
		User user = getLoggedUserDetails(principal);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		model.addAttribute("carts", carts);
		if(carts.size() > 0) {
			Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice();
			model.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		
		
		return "/user/cart";
	}

	@GetMapping("/cart-quantity-update")
	public String updateCartQuantity(@RequestParam("symbol") String symbol , @RequestParam("cartId") Long cartId){
		System.out.println(symbol+ " " + cartId);
		Boolean f = cartService.updateCartQuantity(symbol, cartId);
		return "redirect:/user/cart";
	}

	private User getLoggedUserDetails(Principal principal) {
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		return user;
	}
	
	
	@GetMapping("/change-password")
	public String changePasswordPage() {
		return "change-password";
	}

	@PostMapping("/change-password")
	public String changePassword(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword,
			Principal principal,
			RedirectAttributes redirectAttributes) {
		try {

			String email = principal.getName();

			userService.changePassword(email, oldPassword, newPassword, confirmPassword);

			redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
			return "redirect:/signin";
		} catch (RuntimeException e) {

			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/user/change-password";
		}
	}


	@GetMapping("/profile")
	@PreAuthorize("hasAnyRole('USER', 'CONSULTANT')")
	public String viewProfile(Model model, Principal principal) {
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
		return "user/user-profile";
	}

	@GetMapping("/edit-profile")
	public String editProfile(Model model, Principal principal) {
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
		return "user/edit-profile";
	}

	@PostMapping("/update-profile")
	@PreAuthorize("hasAnyRole('USER', 'CONSULTANT')")
	public String updateProfile(@ModelAttribute User user, 
							  @RequestParam(required = false) MultipartFile profileImage,
							  HttpSession session) {
		try {
			// Lấy user hiện tại t DB
			User existingUser = userService.getUserByEmail(user.getEmail());
			
			// Cập nhật thông tin c bản
			existingUser.setName(user.getName());
			existingUser.setMobile(user.getMobile());
			existingUser.setAddress(user.getAddress());
			existingUser.setCity(user.getCity());
			existingUser.setState(user.getState());
			existingUser.setPinCode(user.getPinCode());
			
			// Lưu thông tin user
			userService.updateUserProfile(existingUser);
			
			// Xử lý upload ảnh nếu có
			if(profileImage != null && !profileImage.isEmpty()) {
				userService.changeProfilePicture(user.getEmail(), profileImage);
			}
			
			session.setAttribute("successMsg", "Profile Updated Successfully");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Error updating profile: " + e.getMessage());
		}
		return "redirect:/user/profile";
	}

	@PostMapping("/change-profile-picture")
	public String changeProfilePicture(@RequestParam("profileImage") MultipartFile file, Principal principal, RedirectAttributes redirectAttributes) {
		try {
			String email = principal.getName();
			userService.changeProfilePicture(email, file);
			redirectAttributes.addFlashAttribute("successMessage", "Profile picture updated successfully.");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile picture.");
		}
		return "redirect:/user/profile";
	}

	@PostMapping("/place-order")
	public String placeOrder(Principal principal, HttpSession session) {
		try {
			User user = userService.getUserByEmail(principal.getName());
			Order order = orderService.createOrder(user.getId());
			session.setAttribute("successMsg", "Order placed successfully! Waiting for admin approval.");
			return "redirect:/user/orders";
		} catch (Exception e) {
			session.setAttribute("errorMsg", e.getMessage());
			return "redirect:/user/cart";
		}
	}

	@GetMapping("/orders")
	public String viewOrders(Principal principal, Model model,
							@RequestParam(required = false) String status,
							@RequestParam(defaultValue = "desc") String sort,
							@RequestParam(required = false) String orderId) {
		User user = userService.getUserByEmail(principal.getName());
		List<Order> orders = orderService.getOrdersByUser(user.getId());
		
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

		// Sort orders
		orders.sort((a, b) -> {
			if ("asc".equals(sort)) {
				return a.getCreatedAt().compareTo(b.getCreatedAt());
			} else {
				return b.getCreatedAt().compareTo(a.getCreatedAt());
			}
		});

		model.addAttribute("orders", orders);
		model.addAttribute("currentUser", user);
		return "user/orders";
	}
	@PostMapping("/user/review/submit")
	public String submitReview(
			@RequestParam Long productId,
			@RequestParam Integer rating,
			@RequestParam(required = false) String comment,
			@AuthenticationPrincipal UserDetails userDetails,
			RedirectAttributes redirectAttributes,
			HttpSession session) {

		try {
			User user = userService.getUserByEmail(userDetails.getUsername());
			if (user == null) {
				throw new RuntimeException("User not found");
			}

			Review review = reviewService.addReview(
					user.getId(),
					productId,
					rating,
					comment
			);

			session.setAttribute("successMsg", "Review submitted successfully!");

		} catch (Exception e) {
			session.setAttribute("errorMsg", e.getMessage());
		}

		return "redirect:/user/orders";
	}
}






