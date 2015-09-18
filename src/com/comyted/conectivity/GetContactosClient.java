package com.comyted.conectivity;

import com.comyted.models.ClientContactSummary;
import com.comyted.models.Contact;
import com.comyted.models.ContactEdit;
import com.comyted.models.ContactSummary;
import com.comyted.models.IdNameValue;
import com.enterlib.exceptions.InvalidOperationException;

public class GetContactosClient extends AppServiceClient {

	public GetContactosClient() {
		super("GetContactos.svc");
	
	}
	
	public ClientContactSummary[] ObtenerContactosCliente(int cod_cliente)
			throws InvalidOperationException{	
		
		 return get(ClientContactSummary[].class, "ObtenerContactosCliente",
				 new RequestParameter("cod_cliente", cod_cliente));
	 }
	
	public Contact ObtenerContacto(int cod_contacto) 
			throws InvalidOperationException{	
		
		return get(Contact.class, "ObtenerContacto",
					 new RequestParameter("cod_contacto", cod_contacto));
	}
	
	public ContactSummary[] ObtenerContactos() 
			throws InvalidOperationException{	
		
		return get(ContactSummary[].class, "ObtenerContactos");
	}
	
	public ContactEdit ObtenerContactoEdit(int cod_contacto) throws InvalidOperationException{
		return get(ContactEdit.class, "ObtenerContactoEdit",
				 new RequestParameter("cod_contacto", cod_contacto));
	}
	
	 public IdNameValue[] ObtenerDepartamentos() throws InvalidOperationException{
		 return get(IdNameValue[].class, "ObtenerDepartamentos");
	 }

	 public IdNameValue[] ObtenerListaClientes() throws InvalidOperationException{
		 return get(IdNameValue[].class, "ObtenerListaClientes");
	 }
}
