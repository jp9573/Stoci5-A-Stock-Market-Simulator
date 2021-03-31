package com.csci5308.stocki5.user.password;

import com.csci5308.stocki5.user.User;
import com.csci5308.stocki5.user.IUserDb;
import org.springframework.stereotype.Service;

@Service
public class UserChangePassword implements IUserChangePassword
{
	private String passwordValidityMessage;

	@Override
	public String getPasswordValidityMessage() {
		return passwordValidityMessage;
	}

	public void setPasswordValidityMessage(String passwordValidityMessage) {
		this.passwordValidityMessage = passwordValidityMessage;
	}

	@Override
	public boolean validateCurrentPassword(User user, String currentPassword)
	{
		if (currentPassword.equals(user.getPassword()))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean changePassword(User user, String newPassword, String confirmNewPassword, IUserDb userDb)
	{
		user.setPassword(newPassword);
		user.setConfirmPassword(confirmNewPassword);
		boolean isValid = user.validatePassword();
		setPasswordValidityMessage(user.getValidityMessage());
		if(isValid){
			boolean isChanged = userDb.updateUserPassword(user);
			return  isChanged;
		}
		return isValid;
	}
}
