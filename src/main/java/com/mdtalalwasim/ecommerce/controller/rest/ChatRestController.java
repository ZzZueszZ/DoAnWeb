package com.mdtalalwasim.ecommerce.controller.rest;

import com.mdtalalwasim.ecommerce.dto.ChatMessage;
import com.mdtalalwasim.ecommerce.entity.Chat;
import com.mdtalalwasim.ecommerce.entity.User;
import com.mdtalalwasim.ecommerce.service.ChatService;
import com.mdtalalwasim.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public Chat sendMessage(ChatMessage message) {
        try {
            System.out.println("Received message: " + message.toString());
            
            // Lưu tin nhắn vào DB
            Chat savedMessage = chatService.saveMessage(
                message.getUserId(), 
                message.getMessage(),
                message.getSender()
            );
            
            System.out.println("Saved message: " + savedMessage.toString());
            
            // Gửi tin nhắn cho consultant
            messagingTemplate.convertAndSend("/topic/consultant", savedMessage);
            
            return savedMessage;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving message: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/chat/history/{userId}")
    public ResponseEntity<List<Chat>> getChatHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(chatService.getChatHistory(userId));
    }

    @GetMapping("/chat/customers")
    public ResponseEntity<List<User>> getCustomersWithChat() {
        return ResponseEntity.ok(chatService.getCustomersWithChat());
    }

    @PostMapping("/chat/consultant/status")
    public ResponseEntity<?> updateConsultantStatus(
            @RequestBody Map<String, Boolean> status,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        User consultant = userService.getUserByEmail(principal.getName());
        // Lưu trạng thái online của consultant
        // Có thể thêm field isOnline vào User entity
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chat/unread-count")
    public ResponseEntity<Map<String, Integer>> getUnreadCount(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        User consultant = userService.getUserByEmail(principal.getName());
        // Đếm số tin nhắn chưa đọc
        // Có thể thêm field isRead vào Chat entity
        
        return ResponseEntity.ok(Collections.singletonMap("count", 0));
    }
} 