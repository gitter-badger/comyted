package com.comyted.conectivity;

import com.comyted.models.ClientContactSummary;
import com.enterlib.conetivity.WCFServiceClient.RequestParameter;
import com.enterlib.exceptions.InvalidOperationException;

public class GetContactos extends AppServiceClient {

	public GetContactos() {
		super("GetContactos.svc");
	
	}
	
	public ClientContactSummary[] ObtenerContactosCliente(int cod_cliente)
			throws InvalidOperationException{	
		
		 return get(ClientContactSummary[].class, "ObtenerContactosCliente",
				 new RequestParameter("cod_cliente", cod_cliente));
	 }


}
