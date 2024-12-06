package com.mdtalalwasim.ecommerce.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.mdtalalwasim.ecommerce.entity.Cart;
import com.mdtalalwasim.ecommerce.entity.Category;
import com.mdtalalwasim.ecommerce.entity.User;
import com.mdtalalwasim.ecommerce.service.CartService;
import com.mdtalalwasim.ecommerce.service.CategoryService;
import com.mdtalalwasim.ecommerce.service.UserService;
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
	
	
	@GetMapping("/orders")
	public String orderPage() {
		
		return "/user/order";
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
	public String viewProfile(Model model, Principal principal) {
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
		return "user-profile";
	}

	@GetMapping("/edit-profile")
	public String editProfile(Model model, Principal principal) {
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
		return "edit-profile";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
		userService.updateUserProfile(user);
		redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
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
}






