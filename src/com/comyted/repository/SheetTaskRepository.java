package com.comyted.repository;

import com.comyted.conectivity.GetTareasClient;
import com.comyted.conectivity.PostTareasClient;
import com.comyted.models.Syncronizable;
import com.comyted.models.Task;
import com.comyted.models.TaskDetails;
import com.comyted.models.TaskEdit;
import com.comyted.persistence.LocalDatabase;
import com.enterlib.exceptions.InvalidOperationException;

public class SheetTaskRepository implements ISheetTaskRepository {

	LocalDatabase localDb;
	GetTareasClient get;
	PostTareasClient post;
	   

	public SheetTaskRepository(LocalDatabase localDb, GetTareasClient get,
			PostTareasClient post) {
		
		this.localDb = localDb;
		this.get = get;
		this.post = post;
	}

	@Override
	public Task[] getTasksBySheet(int cod_hoja)
			throws InvalidOperationException {
		return get.ObtenerTareasHoja(cod_hoja);
	}

	@Override
	public TaskDetails getTask(int cod_tarea) throws InvalidOperationException {
		return get.ObtenerTarea(cod_tarea);
	}

	@Override
	public TaskDetails[] getTaskDetailsBySheet(int cod_hoja)
			throws InvalidOperationException {
		return get.ObtenerTareas(cod_hoja);
	}

	@Override
	public boolean createTask(TaskEdit tarea) throws InvalidOperationException {
		try{
			return post.GrabarTarea(tarea);
		}
		catch(InvalidOperationException e){
			localDb.insertTask(tarea, Syncronizable.ADD);
			return false;
		}
	}

	@Override
	public boolean updateTask(TaskEdit tarea) throws InvalidOperationException {
		try{
			return post.ActualizarTarea(tarea);
		}
		catch(InvalidOperationException e){
			localDb.insertTask(tarea, Syncronizable.UPDATE);
			return false;
		}
	}

	@Override
	public TaskEdit getTaskEdit(int cod_tarea) throws InvalidOperationException {
		return get.ObtenerTarea(cod_tarea);
	}

}
