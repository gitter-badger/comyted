package com.comyted.repository;

import com.comyted.models.ClientContactSummary;
import com.comyted.models.Contact;
import com.comyted.models.ContactEdit;
import com.comyted.models.ContactSummary;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.data.IRepository;
import com.enterlib.exceptions.InvalidOperationException;

public interface IContactsRepository extends 
				IRepository<Contact> ,
				ICollectionRepository<ContactSummary>{

	ClientContactSummary[] getClientContacts(int clientId) throws InvalidOperationException;

}