package com.mdtalalwasim.ecommerce.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String mobile;

	private String email;

	private String address;

	private String city;

	private String state;

	private String pinCode;

	private String password;

	private String profileImage;

	@Column(columnDefinition = "ENUM('ROLE_USER', 'ROLE_VENDOR', 'ROLE_ADMIN') DEFAULT 'ROLE_USER'")
	private String role;

	@Column(columnDefinition = "TINYINT(1) DEFAULT 1")
	private Boolean isEnable;

	@Column(columnDefinition = "TINYINT(1) DEFAULT 1")
	private Boolean accountStatusNonLocked;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer accountFailedAttemptCount;

	private Date accountLockTime;

	private String resetTokens;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
