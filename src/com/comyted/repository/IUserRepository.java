package com.comyted.repository;

import com.comyted.models.AppUser;
import com.enterlib.exceptions.InvalidOperationException;

public interface IUserRepository {
	AppUser[] getUsers() throws InvalidOperationException;
	
	AppUser[] getTecnicos() throws InvalidOperationException;
	
	String getUserEmail(int userId) throws InvalidOperationException;
	
	String getUserEmail(String login)throws InvalidOperationException;
}
