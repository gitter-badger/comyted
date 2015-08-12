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

public class UsersManager implements IUserManager {
	
	GetUsersClient client;	
	LocalDatabase localDb;
		
	
	public UsersManager(LocalDatabase localDb){
		this(localDb, new GetUsersClient());
	}
	
	public UsersManager(LocalDatabase localDb, GetUsersClient client){
		this.client = client;
		this.localDb = localDb;
	}
	
	
	/* (non-Javadoc)
	 * @see com.comyted.modules.admin.IUserManager#ObtenerUsuarios()
	 */
	@Override
	public AppUser[] ObtenerUsuarios() throws InvalidOperationException {
		return client.ObtenerUsuarios();
	}

	
	/* (non-Javadoc)
	 * @see com.comyted.modules.admin.IUserManager#ObtenerTecnicos()
	 */
	@Override
	public AppUser[] ObtenerTecnicos() throws InvalidOperationException {
		return client.ObtenerTecnicos();
	}

	
	
	/* (non-Javadoc)
	 * @see com.comyted.modules.admin.IUserManager#logIn(java.lang.String, java.lang.String)
	 */
	@Override
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
	/* (non-Javadoc)
	 * @see com.comyted.modules.admin.IUserManager#logOut()
	 */
	@Override
	public void logOut() throws InvalidOperationException{
		if(!client.logOut()){
			Log.d(getClass().getName(), "Error Logout");
		}				
	}
	
	
	/* (non-Javadoc)
	 * @see com.comyted.modules.admin.IUserManager#logOut(com.enterlib.threading.IAsyncCallback)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see com.comyted.modules.admin.IUserManager#checkHorasUsuario(int, java.util.Date)
	 */
	@Override
	public HorasLibres checkHorasUsuario(int cod_usuario, Date fechaHoja)
			throws InvalidOperationException{
		HorasLibres result = client.checkHorasUsuario(cod_usuario, 
				Utils.format(fechaHoja));				
		return result;
	}
		

	/* (non-Javadoc)
	 * @see com.comyted.modules.admin.IUserManager#getLastLoggedUser()
	 */
	@Override
	public UserLogin getLastLoggedUser(){
		return localDb.getLogin();
	}
}
