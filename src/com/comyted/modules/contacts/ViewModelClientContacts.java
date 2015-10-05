package com.comyted.modules.contacts;

import com.comyted.models.ClientContactSummary;
import com.comyted.repository.IContactsRepository;
import com.enterlib.mvvm.DataViewModel;
import com.enterlib.mvvm.IDataView;

public class ViewModelClientContacts extends DataViewModel{

	private IContactsRepository contactRepository;
	private ClientContactSummary[] contacts;
    private int clientId;
    
	protected ViewModelClientContacts(IDataView view, IContactsRepository contactRepository, int clientId) {
		super(view);
		
		this.contactRepository = contactRepository;
		this.clientId = clientId;
	}

	@Override
	protected boolean loadAsync() throws Exception {		
		contacts = contactRepository.getClientContacts(clientId);
		return true;
	}

	public ClientContactSummary[] getContacts() {
		return contacts;
	}

}
