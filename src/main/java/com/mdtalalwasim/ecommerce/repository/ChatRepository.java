package com.mdtalalwasim.ecommerce.repository;

import com.mdtalalwasim.ecommerce.entity.Chat;
import com.mdtalalwasim.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserIdOrderBySentAtAsc(Long userId);
    
    @Query("SELECT DISTINCT c.user FROM Chat c")
    List<User> findDistinctUsers();
    
    long countByUserIdAndIsReadFalse(Long userId);
    
    @Modifying
    @Query("UPDATE Chat c SET c.isRead = true WHERE c.user.id = :userId AND c.isRead = false")
    void markMessagesAsRead(@Param("userId") Long userId);
} 