package com.comyted.modules.clients;

import com.comyted.conectivity.GetClientesClient;
import com.comyted.models.ClientSummary;
import com.comyted.repository.ClientRepository;
import com.comyted.repository.IClientRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

public class ViewModelListClients extends DataViewModel<IDataView> {
	

	ClientSummary[] clients;
	IClientRepository repo;
	
	public ClientSummary[] getClients() {
		return clients;
	}
	
	/**
	 * @param view
	 */
	public ViewModelListClients(IDataView view) {
		this(view, new ClientRepository(new GetClientesClient(), null));		
	}

	public ViewModelListClients(IDataView view, IClientRepository repo) {
		super(view);
		this.repo = repo;
	}

	@Override
	protected boolean loadAsync() throws Exception {
		clients =repo.getClients();
		return true;
	}	
	

}
