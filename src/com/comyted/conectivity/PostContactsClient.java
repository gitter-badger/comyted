package com.comyted.conectivity;

import com.comyted.models.ContactEdit;
import com.enterlib.exceptions.InvalidOperationException;

public class PostContactsClient extends AppServiceClient {

	public PostContactsClient( ) {
		super("PostContactos.svc");		
	}

	public boolean ActualizarContacto(ContactEdit value) throws InvalidOperationException{
		 return post(boolean.class, "ActualizarContacto", value);
	}
	
	public boolean GrabarContacto(ContactEdit value) throws InvalidOperationException{
		 return post(boolean.class, "GrabarContacto", value);
	}
}
