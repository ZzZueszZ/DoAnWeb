package com.mdtalalwasim.ecommerce.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mdtalalwasim.ecommerce.entity.User;
import com.mdtalalwasim.ecommerce.repository.UserRepository;
import com.mdtalalwasim.ecommerce.service.UserService;
import com.mdtalalwasim.ecommerce.utils.AppConstant;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public User saveUser(User user) {
		System.out.println("user obje :"+user.toString());
		user.setRole("ROLE_USER");
		user.setIsEnable(true);
		user.setAccountStatusNonLocked(true);
		user.setAccountFailedAttemptCount(0);
		user.setAccountLockTime(null);
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		try {
			User saveUser = userRepository.save(user);

			return saveUser;
		} catch (Exception e) {
			throw new RuntimeException("Failed to create user", e);
		}
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> getAllUsersByRole(String role) {
		// TODO Auto-generated method stub
		return userRepository.findByRole(role);
	}

	@Override
	public Boolean updateUserStatus(Boolean status, Long id) {
		Optional<User> userById = userRepository.findById(id);
		if (userById.isPresent()) {
			User user = userById.get();
			user.setIsEnable(status);
			userRepository.save(user);

			return true;
		} else {
			return false;
		}

	}

	@Override
	public void userFailedAttemptIncrease(User user) {
		int userAttempt= user.getAccountFailedAttemptCount()+1;
		user.setAccountFailedAttemptCount(userAttempt);
		userRepository.save(user);

	}

	@Override
	public void userAccountLock(User user) {
		user.setAccountStatusNonLocked(false);
		user.setAccountLockTime(new Date());
		userRepository.save(user);
	}

	@Override
	public boolean isUnlockAccountTimeExpired(User user) {
		long accountLockTime = user.getAccountLockTime().getTime();
		System.out.println("Account LockTime: "+accountLockTime);
		long  accountUnlockTime = accountLockTime+ AppConstant.UNLOCK_DURATION_TIME;
		System.out.println("Account Unlock Time :"+accountUnlockTime);
		
		long currentTime = System.currentTimeMillis();
		System.out.println("currentTime :"+currentTime);
		
		if(accountUnlockTime < currentTime) {
			user.setAccountStatusNonLocked(true);
			user.setAccountFailedAttemptCount(0);
			user.setAccountLockTime(null);
			userRepository.save(user);
			return true;
		}
		
		return false;
	}

	@Override
	public void userFailedAttempt(int userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUserResetTokenForSendingEmail(String email, String resetToken) {
		User user = userRepository.findByEmail(email);
		user.setResetTokens(resetToken);
		userRepository.save(user);
		
	}

	@Override
	public User getUserByresetTokens(String token) {
		// TODO Auto-generated method stub
		return userRepository.findByResetTokens(token);
	}

	@Override
	public User updateUserWhileResetingPassword(User userByToken) {
		// TODO Auto-generated method stub
		return userRepository.save(userByToken);
	}

	@Override
	public void changePassword(String email, String oldPassword, String newPassword, String confirmPassword) {

		User user = getUserByEmail(email);

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new RuntimeException("Old password is incorrect.");
		}

		if (!newPassword.equals(confirmPassword)) {
			throw new RuntimeException("New password and confirm password do not match.");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}


	public void updateUserProfile(User user) {
			if (user.getId() == null) { throw new IllegalArgumentException("User ID must not be null"); }
				User existingUser = userRepository.findById(user.getId())
						.orElseThrow(() -> new RuntimeException("User not found"));

				// Cập nhật các trường thông tin
				existingUser.setName(user.getName());
				existingUser.setEmail(user.getEmail());
				existingUser.setMobile(user.getMobile());
				existingUser.setAddress(user.getAddress());
				existingUser.setCity(user.getCity());
				existingUser.setState(user.getState());
				existingUser.setPinCode(user.getPinCode());

				// Mã hóa mật khẩu nếu thay đổi và mật khẩu mới không bị null
				if (user.getPassword() != null && !user.getPassword().equals(existingUser.getPassword())) {
					existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
				}

				userRepository.save(existingUser);
			}

		public void changeProfilePicture(String email, MultipartFile file) throws IOException {
					User user = getUserByEmail(email);

					// Đường dẫn tới thư mục lưu trữ ảnh
					Path uploadPath = Paths.get("uploads/profile/");
					if (!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
					}

					// Lưu file ảnh vào thư mục lưu trữ
					Path filePath = uploadPath.resolve(file.getOriginalFilename());
					Files.write(filePath, file.getBytes());

					// Cập nhật đường dẫn ảnh trong cơ sở dữ liệu
					user.setProfileImage("/uploads/profile/" + file.getOriginalFilename());
					userRepository.save(user);
				}

		}














