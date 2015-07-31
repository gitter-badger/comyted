package com.comyted.modules.admin;

import java.security.InvalidParameterException;
import java.util.Date;

import android.util.Log;

import com.comyted.Utils;
import com.comyted.conectivity.GetUsersClient;
import com.comyted.models.AppUser;
import com.comyted.models.HorasLibres;
import com.comyted.models.UserLogin;
import com.comyted.persistence.LocalDatabase;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.threading.IAsyncCallback;

public class UsersManager {
	
	GetUsersClient client;	
	LocalDatabase localDb;
		
	
	public UsersManager(LocalDatabase localDb){
		this(localDb, new GetUsersClient());
	}
	
	public UsersManager(LocalDatabase localDb, GetUsersClient client){
		this.client = client;
		this.localDb = localDb;
	}
	
	
	public AppUser[] ObtenerUsuarios() throws InvalidOperationException {
		return client.ObtenerUsuarios();
	}

	
	public AppUser[] ObtenerTecnicos() throws InvalidOperationException {
		return client.ObtenerTecnicos();
	}

	
	
	public AppUser logIn(String username, String password)
			throws InvalidParameterException , InvalidOperationException{	
		
		AppUser user = client.logIn(username, password);							
		if(user!=null)
			localDb.setLogin(username, password);
		return user;			
	}
	
	/* (non-Javadoc)
	 * @see com.cat_movilidad.business.ICatUserManager#logOut()
	 */
	public void logOut() throws InvalidOperationException{
		if(!client.logOut()){
			Log.d(getClass().getName(), "Error Logout");
		}				
	}
	
	
	public void logOut(IAsyncCallback callback) {		
		try {
			logOut();			
		} catch (InvalidOperationException e) {		
			callback.operationCompleted(e);
			return;
		}	
		callback.operationCompleted(null);
	}
	
	
	/* (non-Javadoc)
	 * @see com.cat_movilidad.business.ICatUserManager#checkHorasUsuario(int, java.util.Date)
	 */
	
	public HorasLibres checkHorasUsuario(int cod_usuario, Date fechaHoja)
			throws InvalidOperationException{
		HorasLibres result = client.checkHorasUsuario(cod_usuario, 
				Utils.format(fechaHoja));				
		return result;
	}
		

	public UserLogin getLastLoggedUser(){
		return localDb.getLogin();
	}
}
