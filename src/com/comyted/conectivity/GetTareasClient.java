package com.comyted.conectivity;

import com.comyted.models.Task;
import com.comyted.models.TaskDetails;
import com.enterlib.exceptions.InvalidOperationException;

public class GetTareasClient extends AppServiceClient {

	public GetTareasClient() {
		super("GetTareas.svc");
	
	}	
	
	public Task[] ObtenerTareasHoja(int cod_hoja) throws InvalidOperationException{				 
		 return get(Task[].class, "ObtenerTareasHoja",
				 new RequestParameter("cod_hoja", cod_hoja));
	}
	
	
	public TaskDetails ObtenerTarea(int cod_tarea) throws InvalidOperationException{		 		 
		 return get(TaskDetails.class, "ObtenerTarea",
				 new RequestParameter("cod_tarea", cod_tarea));
	}
	
	
	public TaskDetails[] ObtenerTareas(int cod_hoja) throws InvalidOperationException{		
		 return get(TaskDetails[].class, "ObtenerTareas",
				 new RequestParameter("cod_hoja", cod_hoja));
	}
}
