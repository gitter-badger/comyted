package com.comyted.repository;

import com.comyted.conectivity.GetContactosClient;
import com.comyted.models.ClientContactSummary;
import com.comyted.models.Contact;
import com.comyted.models.ContactSummary;
import com.enterlib.exceptions.InvalidOperationException;

public class ContactsRepository implements IContactsRepository {
	
	GetContactosClient getContacts;
	
	public ContactsRepository(){
		this(new GetContactosClient());
	}
	
	public ContactsRepository(GetContactosClient getContacts) {
		this.getContacts = getContacts;
	}
	

	@Override
	public ClientContactSummary[] getClientContacts(int clientId)
			throws InvalidOperationException {
		return getContacts.ObtenerContactosCliente(clientId);
	}
	

	@Override
	public Contact getItem(int id) throws InvalidOperationException {
		return getContacts.ObtenerContacto(id);
	}
	

	@Override
	public boolean updateItem(Contact item) {
			// TODO Auto-generated method stub
			return false;
	}

	@Override
	public boolean createItem(Contact item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ContactSummary[] getItems() throws InvalidOperationException {
		return getContacts.ObtenerContactos();
	}
}
