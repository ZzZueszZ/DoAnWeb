package com.mdtalalwasim.ecommerce.service;

import com.mdtalalwasim.ecommerce.entity.Chat;
import com.mdtalalwasim.ecommerce.entity.User;

import java.util.List;

public interface ChatService {
    Chat saveMessage(Long userId, String message, Chat.MessageSender sender);
    List<Chat> getChatHistory(Long userId);
    List<User> getCustomersWithChat();
    void markAsRead(Long userId);
    long getUnreadCount(Long userId);
} 