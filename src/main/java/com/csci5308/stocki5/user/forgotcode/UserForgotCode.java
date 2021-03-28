package com.csci5308.stocki5.user.forgotcode;

import com.csci5308.stocki5.user.User;
import com.csci5308.stocki5.user.IUserDb;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserForgotCode implements IUserForgotCode
{

	public String getUserCode(String email, String dob, IUserDb userDb)
	{
		String result = null;
		User user = userDb.getUserByEmail(email);
		if (user != null)
		{
			try
			{
				Date dobDate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
				if (user.getEmailId().equals(email) && user.getDateOfBirth().compareTo(dobDate) == 0)
				{
					result = user.getUserCode();
				}
			} catch (ParseException e)
			{
				e.printStackTrace();
				return result;
			}
		}
		return result;
	}
}