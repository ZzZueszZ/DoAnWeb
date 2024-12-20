# E-commerce Course Platform

Một nền tảng thương mại điện tử chuyên về bán khóa học trực tuyến, được xây dựng bằng Spring Boot.

## Tính năng chính

### Người dùng
- Đăng ký/Đăng nhập tài khoản
- Xem danh sách khóa học theo danh mục
- Tìm kiếm khóa học
- Thêm khóa học vào giỏ hàng
- Thanh toán đơn hàng
- Chat với tư vấn viên
- Đánh giá khóa học

### Admin
- Quản lý danh mục (CRUD)
- Quản lý khóa học (CRUD)
- Quản lý đơn hàng
- Quản lý người dùng
- Xem thống kê doanh thu
- Chat với khách hàng

## Công nghệ sử dụng

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- MySQL Database
- Thymeleaf Template Engine
- Maven

### Frontend
- HTML5
- CSS3 
- JavaScript
- Bootstrap 5
- Font Awesome Icons

## Cấu trúc Database

### Các bảng chính:
- user: Quản lý thông tin người dùng
- category: Danh mục khóa học
- product: Thông tin khóa học
- cart: Giỏ hàng
- orders: Đơn hàng
- order_item: Chi tiết đơn hàng
- chat: Tin nhắn tư vấn
- review: Đánh giá khóa học

## Cài đặt và Chạy

1. Clone repository:
2. Chạy docker-compose up
3. Config file yaml tới DATABASE 
4. Build Project

