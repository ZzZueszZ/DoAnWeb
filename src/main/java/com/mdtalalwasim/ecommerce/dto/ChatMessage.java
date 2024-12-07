package com.mdtalalwasim.ecommerce.dto;

import com.mdtalalwasim.ecommerce.entity.Chat;
import lombok.Data;

@Data
public class ChatMessage {
    private Long userId;
    private Long receiverId;
    private String message;
    private Chat.MessageSender sender;
} 