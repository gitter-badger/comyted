package com.comyted.conectivity;

import com.comyted.models.ContactVisit;
import com.enterlib.exceptions.InvalidOperationException;

public class GetVisitasClient extends AppServiceClient {

	public GetVisitasClient() {
		super("GetVisitas.svc");		
	}

	public ContactVisit[] ObtenerVisitasUsuarioCliente(int cod_cliente, int cod_usuario) throws InvalidOperationException{
		return get(ContactVisit[].class, "ObtenerVisitasUsuarioCliente", 
				 new RequestParameter("cod_cliente", cod_cliente),
				 new RequestParameter("cod_usuario", cod_usuario));
	}
	
	public ContactVisit[] ObtenerVisitasUsuarioContacto(int cod_contacto, int cod_usuario) throws InvalidOperationException
	{
		return get(ContactVisit[].class, "ObtenerVisitasUsuarioContacto", 
				 new RequestParameter("cod_contacto", cod_contacto),
				 new RequestParameter("cod_usuario", cod_usuario));
	}
	
	public ContactVisit[] ObtenerVisitas(int cod_usuario) throws InvalidOperationException{
		return get(ContactVisit[].class, "ObtenerVisitas", 				 
				 new RequestParameter("cod_usuario", cod_usuario));
	}
	
	public ContactVisit[] ObtenerVisitasOferta(int cod_oferta) throws InvalidOperationException{
		return get(ContactVisit[].class, "ObtenerVisitasOferta", 				 
				 new RequestParameter("cod_oferta", cod_oferta));
	}
		
}
