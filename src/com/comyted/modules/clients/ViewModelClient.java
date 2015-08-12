package com.comyted.modules.clients;

import com.comyted.conectivity.GetClientesClient;
import com.comyted.models.AppUser;
import com.comyted.models.Client;
import com.comyted.repository.ClientRepository;
import com.comyted.repository.IClientRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

public class ViewModelClient extends DataViewModel<IDataView> {
	
	private int clientId;
	private Client client;
	private IClientRepository repository;

	/**Constructor for Tests. Allows the injection a Stub Repository*/
	public ViewModelClient(int clientId, IDataView view, IClientRepository repository) {
		super(view);
		
		this.clientId = clientId;
		this.repository = repository;
	}
	
	/**Constructor for Production*/
	public ViewModelClient(int clientId, IDataView view){
		this(clientId, view, new ClientRepository(new GetClientesClient(), null));
	}

	@Override
	protected boolean loadAsync() throws Exception {
		client = repository.getClient(clientId);
		return true;
	}

	public Client getClient() {		
		return client;
	}

	public boolean canUserEdit(AppUser currentUser) {
		// TODO Auto-generated method stub
		return true;
	}

}
