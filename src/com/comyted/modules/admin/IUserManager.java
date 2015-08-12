package com.comyted.modules.admin;

import java.security.InvalidParameterException;
import java.util.Date;

import com.comyted.models.AppUser;
import com.comyted.models.HorasLibres;
import com.comyted.models.UserLogin;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.threading.IAsyncCallback;

public interface IUserManager {

	AppUser[] ObtenerUsuarios() throws InvalidOperationException;

	AppUser[] ObtenerTecnicos() throws InvalidOperationException;

	AppUser logIn(String username, String password)
			throws InvalidParameterException, InvalidOperationException;

	/* (non-Javadoc)
	 * @see com.cat_movilidad.business.ICatUserManager#logOut()
	 */
	void logOut() throws InvalidOperationException;

	void logOut(IAsyncCallback callback);

	HorasLibres checkHorasUsuario(int cod_usuario, Date fechaHoja)
			throws InvalidOperationException;

	UserLogin getLastLoggedUser();

}