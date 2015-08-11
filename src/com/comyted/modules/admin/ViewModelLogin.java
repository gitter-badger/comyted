package com.comyted.modules.admin;

import android.content.Context;
import android.util.Log;

import com.comyted.MainApp;
import com.comyted.models.AppUser;
import com.comyted.models.UserLogin;
import com.comyted.persistence.LocalDatabase;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;
import com.enterlib.serialization.JSonSerializer;

public class ViewModelLogin extends DataViewModel<IDataView> {

	UsersManager userManager;	
	AppUser user;
	String username;	
	String password;
	
	public ViewModelLogin(Context context, IDataView view){
		this(new UsersManager(new LocalDatabase(context)), view);
	}
	
	
	public ViewModelLogin(UsersManager userManager, IDataView view) {
		super(view);
		this.userManager = userManager;	
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AppUser getUser() {
		return user;
	}
	
	public UserLogin getLastLoggedUser() {
		return userManager.getLastLoggedUser();
	}


	@Override
	protected boolean loadAsync() throws Exception {
		Log.d("LoginViewModel", "login "+username +":"+ password);
		user = userManager.logIn(username, password);
		
		if(user!=null){
			JSonSerializer serializer = new JSonSerializer();
			Log.d("LoginViewModel", "result="+serializer.serialize(user));
		}
		MainApp.setCurrentUser(user);
		return true;
	}

}
