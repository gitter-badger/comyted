package com.comyted.conectivity;

import com.comyted.models.AppUser;
import com.comyted.models.HorasLibres;
import com.enterlib.exceptions.InvalidOperationException;

public class GetUsersClient extends AppServiceClient {
	public GetUsersClient(){
		super("GetUsers.svc");
	}
	
	public AppUser[] ObtenerUsuarios() 
			throws InvalidOperationException{		
		 return get(AppUser[].class, "ObtenerUsuarios");
	 }
	 

	public AppUser logIn(String login, String password) 
			throws InvalidOperationException{		
		 return get(AppUser.class, "ValidarUsuarios",
				 new RequestParameter("login", login),
				 new RequestParameter("password", password));
    }
   
   
	 public  boolean ValidarUsuariosService(String login) 
			 throws InvalidOperationException{		 
		 return get(boolean.class, "ValidarUsuariosService",
				 new RequestParameter("login", login));
    }
   
	 public AppUser[] ObtenerTecnicos()
		   throws InvalidOperationException{  
	   
		 return get(AppUser[].class, "ObtenerTecnicos");
    }
    
    public String ObtenerEmailUsuario(String login)
    		 throws InvalidOperationException{    
		 return get(String.class, "ObtenerEmailUsuario",
				 new RequestParameter("login", login));
    }
    
    public String ObtenerEmailUsuarioById(int id) 
    		throws InvalidOperationException{
		 return get(String.class, "ObtenerEmailUsuarioById",
				 new RequestParameter("id", id));
   }
    
    public HorasLibres checkHorasUsuario(int cod_usuario, String fechahoja) 
    		throws InvalidOperationException{    	
    	
		 return get(HorasLibres.class, "CheckHorasUsuario",
				 new RequestParameter("cod_usuario", cod_usuario),
				 new RequestParameter("fechahoja", fechahoja));
    }

	public boolean logOut() {
		return true;
	}	
}
