package com.comyted.conectivity;

import com.comyted.models.SheetEmailRequest;
import com.comyted.models.SheetEdit;
import com.enterlib.exceptions.InvalidOperationException;

public class PostHojasClient extends AppServiceClient {

	public PostHojasClient() {
		super("PostHojas.svc");
		
	}
	
	
	public boolean GrabarHoja(SheetEdit hoja) 
			throws InvalidOperationException {		
		 return post(boolean.class, "GrabarHoja", hoja);
	}
	
	
	public boolean ActualizarHoja(SheetEdit hoja)
			throws InvalidOperationException{		 
		 return post(boolean.class, "ActualizarHoja", hoja);
	}
	
	
	public boolean EnviarEmail(SheetEmailRequest email) throws InvalidOperationException{		
		 return post(boolean.class, "EnviarEmail", email);
	}
	   
   public boolean ActualizarEmail(SheetEmailRequest email) throws InvalidOperationException{	    
		 return post(boolean.class, "ActualizarEmail", email);
   }

}
