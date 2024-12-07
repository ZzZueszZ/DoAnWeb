package com.mdtalalwasim.ecommerce.service;

import java.io.IOException;
import java.util.List;

import com.mdtalalwasim.ecommerce.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	
	public User saveUser(User user);
	
	public User getUserByEmail(String email);
	User getUserById(long id);
	
	public List<User> getAllUsersByRole(String role);

	public Boolean updateUserStatus(Boolean status, Long id);
	
	//lock user account for attempting wrong credentials
	public void userFailedAttemptIncrease(User user);
	
	public void userAccountLock(User user);
	
	public boolean isUnlockAccountTimeExpired(User user);
	
	public void userFailedAttempt(int userId);

	public void updateUserResetTokenForSendingEmail(String email, String resetToken);

	public User getUserByresetTokens(String token);

	public User updateUserWhileResetingPassword(User userByToken);

	public void changePassword(String email, String oldPassword, String newPassword, String confirmPassword);

	public void updateUserProfile(User user);

	public void changeProfilePicture(String email, MultipartFile file) throws IOException;

	public List<User> getAllUsers();

}
