package com.mdtalalwasim.ecommerce.controller;

import com.mdtalalwasim.ecommerce.entity.User;
import com.mdtalalwasim.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class ChatViewController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addCommonAttributes(Principal principal, Model model) {
        if(principal != null) {
            String email = principal.getName();
            User user = userService.getUserByEmail(email);
            model.addAttribute("currentLoggedInUserDetails", user);
        }
    }

    @GetMapping("/user/chat")
    public String userChat(Principal principal) {
        if(principal == null) {
            return "redirect:/signin";
        }
        return "user/chat";
    }

    @GetMapping("/consultant/dashboard") 
    public String consultantDashboard(Principal principal) {
        if(principal == null) {
            return "redirect:/signin";  
        }
        return "consultant/consultant-dashboard";
    }
} 