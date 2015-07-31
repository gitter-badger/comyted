package com.comyted.conectivity;

import com.comyted.models.TaskEdit;
import com.enterlib.exceptions.InvalidOperationException;

public class PostTareasClient extends AppServiceClient {

	public PostTareasClient() {
		super("PostTareas.svc");
		
	}
	
	 
	public boolean GrabarTarea(TaskEdit tarea) throws InvalidOperationException{		
		 return post(boolean.class, "GrabarTarea", tarea);
	 }	 
	 
	
	public boolean ActualizarTarea(TaskEdit tarea) throws InvalidOperationException{		
		 return post(boolean.class, "ActualizarTarea", tarea);
	 }

}
