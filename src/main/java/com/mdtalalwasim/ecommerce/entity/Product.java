package com.mdtalalwasim.ecommerce.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "product_title", length = 500)
	private String productTitle;
	
	@Column(name = "product_description", columnDefinition = "TEXT")
	private String productDescription;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@Column(name = "product_price")
	private Double productPrice;
	
	@Column(name = "product_stock")
	private Integer productStock;
	
	@Column(name = "product_image", length = 255)
	private String productImage;
	
	private Integer discount;
	
	@Column(name = "discount_price")
	private Double discountPrice;
	
	@Column(name = "discount_start_date")
	private LocalDateTime discountStartDate;
	
	@Column(name = "discount_end_date")
	private LocalDateTime discountEndDate;
	
	@Column(name = "is_discount_active", columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean isDiscountActive;
	
	@Column(name = "is_active", columnDefinition = "TINYINT(1) DEFAULT 1")
	private Boolean isActive;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	
}
