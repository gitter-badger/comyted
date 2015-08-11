package com.comyted.repository;

import com.comyted.models.Task;
import com.comyted.models.TaskDetails;
import com.comyted.models.TaskEdit;
import com.enterlib.exceptions.InvalidOperationException;

public interface ISheetTaskRepository {
	
	Task[] getTasksBySheet(int cod_hoja)
			throws InvalidOperationException;

	TaskDetails getTask(int cod_tarea)
			throws InvalidOperationException;

	TaskDetails[] getTaskDetailsBySheet(int cod_hoja)
			throws InvalidOperationException;
	
	TaskEdit getTaskEdit(int cod_tarea)
			throws InvalidOperationException;
	
	boolean createTask(TaskEdit tarea) 
			throws InvalidOperationException;

	boolean updateTask(TaskEdit tarea)
			throws InvalidOperationException;
}
