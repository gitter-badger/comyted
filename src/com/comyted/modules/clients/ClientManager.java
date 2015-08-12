package com.comyted.modules.clients;

import com.comyted.repository.IClientRepository;

public class ClientManager {
	
	IClientRepository repository;
	public ClientManager(IClientRepository repository){
		this.repository= repository;
	}
}
