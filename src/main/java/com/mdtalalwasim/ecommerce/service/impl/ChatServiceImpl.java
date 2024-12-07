package com.mdtalalwasim.ecommerce.service.impl;

import com.mdtalalwasim.ecommerce.entity.Chat;
import com.mdtalalwasim.ecommerce.entity.User;
import com.mdtalalwasim.ecommerce.repository.ChatRepository;
import com.mdtalalwasim.ecommerce.repository.UserRepository;
import com.mdtalalwasim.ecommerce.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Chat saveMessage(Long userId, String message, Chat.MessageSender sender) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
                
            Chat chat = new Chat();
            chat.setUser(user);
            chat.setMessage(message);
            chat.setSentBy(sender);
            chat.setSentAt(new Date());
            chat.setRead(false);
            
            Chat savedChat = chatRepository.save(chat);
            System.out.println("Message saved successfully: " + savedChat.toString());
            return savedChat;
        } catch (Exception e) {
            System.err.println("Error in saveMessage: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Chat> getChatHistory(Long userId) {
        return chatRepository.findByUserIdOrderBySentAtAsc(userId);
    }

    @Override
    public List<User> getCustomersWithChat() {
        return chatRepository.findDistinctUsers();
    }

    @Override
    public void markAsRead(Long userId) {
        chatRepository.markMessagesAsRead(userId);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return chatRepository.countByUserIdAndIsReadFalse(userId);
    }
} 