package com.comyted.repository;

import com.comyted.conectivity.GetUsersClient;
import com.comyted.models.AppUser;
import com.enterlib.exceptions.InvalidOperationException;

public class UserRepository implements IUserRepository {

	GetUsersClient client;
	
	public UserRepository() {
		this(new GetUsersClient());
	}
	
	public UserRepository(GetUsersClient client) {
		this.client = client;
	}

	@Override
	public AppUser[] getUsers() throws InvalidOperationException {
		return client.ObtenerUsuarios();
	}

	@Override
	public AppUser[] getTecnicos() throws InvalidOperationException {
		return client.ObtenerTecnicos();
	}

	@Override
	public String getUserEmail(int userId) throws InvalidOperationException {
		return client.ObtenerEmailUsuarioById(userId);
	}

	@Override
	public String getUserEmail(String login) throws InvalidOperationException {
		return client.ObtenerEmailUsuario(login);
	}

}
