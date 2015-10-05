package com.comyted.modules.clients;

import java.util.ArrayList;

import com.comyted.models.Client;
import com.enterlib.mvvm.EditViewModel;
import com.enterlib.mvvm.IEditView;

public class ViewModelEditClient extends EditViewModel {

	public Client getClient() {
		return client;
	}

	private Client client;
	private ClientManager clientManager;
	private ArrayList<String> notifications = new ArrayList<String>();
	
	public ViewModelEditClient(Client client, ClientManager clientManager, IEditView view) {
		super(view);
		
		this.client = client;
		this.clientManager = clientManager;		
	}
		

	public ArrayList<String> getNotifications() {
		return notifications;
	}


	@Override
	protected boolean saveAsync() throws Exception {
		return clientManager.save(client);
	}

	@Override
	protected boolean loadAsync() throws Exception {		
		return true;
	}

}
