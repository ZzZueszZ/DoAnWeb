package com.mdtalalwasim.ecommerce.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mdtalalwasim.ecommerce.entity.User;
import com.mdtalalwasim.ecommerce.repository.UserRepository;
import com.mdtalalwasim.ecommerce.service.UserService;
import com.mdtalalwasim.ecommerce.utils.AppConstant;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public User saveUser(User user) {
		try {
			user.setRole("ROLE_USER");
			user.setIsEnable(true);
			user.setAccountStatusNonLocked(true);
			user.setAccountFailedAttemptCount(0);
			user.setAccountLockTime(null);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			return userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create user", e);
		}
	}

	@Override
	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found with email: " + email);
		}
		return user;
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}


	@Override
	public List<User> getAllUsersByRole(String role) {
		return userRepository.findByRole(role);
	}

	@Override
	public Boolean updateUserStatus(Boolean status, Long id) {
		try {
			User user = getUserById(id);
			if ("ROLE_ADMIN".equals(user.getRole())) {
				throw new RuntimeException("Cannot modify admin account status");
			}
			user.setIsEnable(status);
			userRepository.save(user);
			return true;
		} catch (Exception e) {
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

	@Override
	@Transactional
	public void updateUserProfile(User user) {
		try {
			// Validate user
			if(user == null || user.getEmail() == null) {
				throw new RuntimeException("Invalid user data");
			}

			// Lấy user hiện tại t DB
			User existingUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new RuntimeException("User not found"));

			// Cập nhật thông tin
			existingUser.setName(user.getName());
			existingUser.setMobile(user.getMobile());
			existingUser.setAddress(user.getAddress());
			existingUser.setCity(user.getCity());
			existingUser.setState(user.getState());
			existingUser.setPinCode(user.getPinCode());

			// Lưu user
			userRepository.save(existingUser);
			
		} catch (Exception e) {
			throw new RuntimeException("Error updating user profile: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void changeProfilePicture(String email, MultipartFile file) throws IOException {
		User user = getUserByEmail(email);
		
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path uploadPath = Paths.get("uploads/profile/");
		
		if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = file.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				
				if (user.getProfileImage() != null) {
						Path oldFilePath = uploadPath.resolve(user.getProfileImage().substring("/uploads/profile/".length()));
						Files.deleteIfExists(oldFilePath);
				}
				
				user.setProfileImage("/uploads/profile/" + fileName);
				userRepository.save(user);
		}
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void updateUser(User user) {
		// Validate user
		if (user == null || user.getId() == null) {
			throw new RuntimeException("Invalid user data");
		}

		User existingUser = getUserById(user.getId());
		
		user.setEmail(existingUser.getEmail());
		user.setPassword(existingUser.getPassword());
		
		user.setAccountStatusNonLocked(existingUser.getAccountStatusNonLocked());
		user.setAccountFailedAttemptCount(existingUser.getAccountFailedAttemptCount());
		user.setAccountLockTime(existingUser.getAccountLockTime());
		user.setResetTokens(existingUser.getResetTokens());

		existingUser.setName(user.getName());
		existingUser.setMobile(user.getMobile());
		existingUser.setAddress(user.getAddress());
		existingUser.setCity(user.getCity());
		existingUser.setState(user.getState());
		existingUser.setPinCode(user.getPinCode());
		existingUser.setRole(user.getRole());
		
		if (user.getProfileImage() != null && !user.getProfileImage().equals(existingUser.getProfileImage())) {
			existingUser.setProfileImage(user.getProfileImage());
		}

		userRepository.save(existingUser);
	}

	@Override
	public void deleteUser(Long id) {
		User user = getUserById(id);
		
		if ("ROLE_ADMIN".equals(user.getRole())) {
			throw new RuntimeException("Cannot delete admin user");
		}
		
		if (user.getProfileImage() != null) {
			try {
				Path imagePath = Paths.get("uploads/profile/")
					.resolve(user.getProfileImage().substring("/uploads/profile/".length()));
				Files.deleteIfExists(imagePath);
			} catch (IOException e) {
				System.err.println("Error deleting profile image: " + e.getMessage());
			}
		}
		
		userRepository.deleteById(id);
	}

}














