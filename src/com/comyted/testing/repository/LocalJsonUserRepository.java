package com.comyted.testing.repository;

import java.util.ArrayList;

import android.content.Context;

import com.comyted.models.AppUser;
import com.comyted.repository.IUserRepository;
import com.comyted.testing.JSONFileStore;
import com.enterlib.exceptions.InvalidOperationException;

public class LocalJsonUserRepository extends JSONFileStore implements IUserRepository {

	static final String STORE = "usuarios.json";
	
	AppUser[]users;
	public LocalJsonUserRepository(Context context) {
		super(context);
	}

	@Override
	public AppUser[] getUsers() throws InvalidOperationException {
		users = load(AppUser[].class, STORE);
		return users;
	}
	
	

	@Override
	public AppUser[] getTecnicos() throws InvalidOperationException {
		loadUsers();

		ArrayList<AppUser>list = new ArrayList<AppUser>();
		for (int i = 0; i < users.length; i++) {
			AppUser user = users[i];
			if(user.tecnico){
				list.add(user);
			}
		}
		return (AppUser[]) list.toArray();
	}

	private void loadUsers() {
		if(users==null){
			users = load(AppUser[].class, STORE);	
		}
	}
	
	private AppUser findUser(int id){
		for (int i = 0; i < users.length; i++) {
			if(users[i].id == id)
				return users[i];
		}
		return null;
	}
	
	private AppUser findUser(String login){
		for (int i = 0; i < users.length; i++) {
			if(users[i].login.equalsIgnoreCase(login))
				return users[i];
		}
		return null;
	}

	@Override
	public String getUserEmail(int userId) throws InvalidOperationException {
		loadUsers();
		
		AppUser user = findUser(userId);
		return user!=null?user.email:null;			
	}

	@Override
	public String getUserEmail(String login) throws InvalidOperationException {
		AppUser user = findUser(login);
		return user!=null?user.email:null;	
	}

}
