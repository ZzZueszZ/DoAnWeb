package com.mdtalalwasim.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "chat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String message;
    
    @Enumerated(EnumType.STRING)
    private MessageSender sentBy;
    
    private Date sentAt;
    
    private boolean isRead = false;

    public enum MessageSender {
        USER, CONSULTANT
    }
} 