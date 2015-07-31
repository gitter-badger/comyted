package com.comyted.conectivity;

import com.comyted.models.SheetEmailRequest;
import com.comyted.models.IdNameValue;
import com.comyted.models.PlantEngine;
import com.comyted.models.SheetDetails;
import com.comyted.models.SheetEdit;
import com.comyted.models.SheetSignatureState;
import com.comyted.models.SheetSumary;
import com.enterlib.exceptions.InvalidOperationException;

public class GetHojasClient extends AppServiceClient {

	public GetHojasClient() {
		super("GetHojas.svc");
		
	}

	
	public SheetSumary[] ObtenerHojas(int cod_tecnico) 
			throws InvalidOperationException{		
		 return get(SheetSumary[].class, "ObtenerHojas", 
				 new RequestParameter("cod_tecnico", cod_tecnico));
	}
	
	
	public SheetSumary[] ObtenerHojasOrden(int cod_orden) throws InvalidOperationException {		
		 return get(SheetSumary[].class, "ObtenerHojasOrden", 
				 new RequestParameter("cod_orden", cod_orden));
	}
	
	
	public SheetDetails  ObtenerHoja(int cod_hoja) throws InvalidOperationException {	   		 
		 return get(SheetDetails.class, "ObtenerHoja", 
				 new RequestParameter("cod_hoja", cod_hoja));
   }
   
	
	public SheetEdit ObtenerHojaDetalles(int cod_hoja) throws InvalidOperationException{	    
		 return get(SheetEdit.class, "ObtenerHojaDetalles", 
				 new RequestParameter("cod_hoja", cod_hoja));
   }
   
	
	public IdNameValue[] ObtenerListaPlantas() throws InvalidOperationException{	   
		 return get(IdNameValue[].class, "ObtenerListaPlantas");
   }
   
	
	public IdNameValue[] ObtenerTipoTrabajos() 
			throws InvalidOperationException{	     
		 return get(IdNameValue[].class, "ObtenerTipoTrabajos");
   }
   
	
	public IdNameValue[] ObtenerCargoRegion()
			throws InvalidOperationException{	  
		 return get(IdNameValue[].class, "ObtenerCargoRegion");	   
   }
   
	
	public IdNameValue[] ObtenerTipoHojas() 
			throws InvalidOperationException{	  
		 return get(IdNameValue[].class, "ObtenerTipoHojas");
   }
   
	
	public PlantEngine[] ObtenerMotorPlanta(int cod_planta) 
			throws InvalidOperationException{				 
		 return get(PlantEngine[].class, "ObtenerMotorPlanta", 
				 new RequestParameter("cod_planta", cod_planta));
   }
   
	
	public IdNameValue[] ObtenerPeticionarios(int cod_planta)
			throws InvalidOperationException{	    
		 return get(IdNameValue[].class, "ObtenerPeticionarios", 
				 new RequestParameter("cod_planta", cod_planta));
   }
   
	
	public SheetSignatureState CheckHoja(int cod_hoja)
			throws InvalidOperationException{	   
		 return get(SheetSignatureState.class, "CheckHoja", 
				 new RequestParameter("cod_hoja", cod_hoja));
   }

	
	public String ObtenerEmailPeticionario(int cod_peticionario)
			throws InvalidOperationException{				
		 return get(String.class, "ObtenerEmailPeticionario", 
				 new RequestParameter("cod_peticionario", cod_peticionario));
	}
	
	public SheetEmailRequest ObtenerEmailHoja(int cod_hoja)
			throws InvalidOperationException{				 
		 return get(SheetEmailRequest.class, "ObtenerEmailHoja", 
				 new RequestParameter("cod_hoja", cod_hoja));
	}
	
}
