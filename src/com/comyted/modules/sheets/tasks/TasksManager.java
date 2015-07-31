package com.comyted.modules.sheets.tasks;

import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.comyted.Messages;
import com.comyted.conectivity.GetHojasClient;
import com.comyted.conectivity.GetTareasClient;
import com.comyted.conectivity.PostTareasClient;
import com.comyted.models.HorasLibres;
import com.comyted.models.SheetDetails;
import com.comyted.models.Task;
import com.comyted.models.TaskDetails;
import com.comyted.models.TaskEdit;
import com.comyted.modules.admin.UsersManager;
import com.comyted.persistence.LocalDatabase;
import com.comyted.repository.ISheetTaskRepository;
import com.comyted.repository.ISheetsRepository;
import com.comyted.repository.SheetTaskRepository;
import com.comyted.repository.SheetsRepository;
import com.enterlib.DateUtils;
import com.enterlib.TimeValue;
import com.enterlib.converters.Converters;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.exceptions.ValidationException;
import com.enterlib.validations.ErrorInfo;


public class TasksManager {
		
	ISheetTaskRepository taskRespository;		
	UsersManager userManager;
	ISheetsRepository sheetsRepository;
	
	public TasksManager(
			ISheetTaskRepository taskRespository,
			UsersManager userManager,
			ISheetsRepository sheetsRepository) {
		
		this.taskRespository = taskRespository;				
		this.userManager = userManager;
		this.sheetsRepository = sheetsRepository; 
	}
	
	public TasksManager(Context context){
		this.taskRespository = new SheetTaskRepository( new LocalDatabase(context), new GetTareasClient(), new PostTareasClient());		
		this.userManager = new UsersManager(null);
		this.sheetsRepository = new SheetsRepository(new GetHojasClient(), null, null);
	}
		
	public Task[] getTasksBySheet(int cod_hoja)
			throws InvalidOperationException {
		return taskRespository.getTasksBySheet(cod_hoja);
	}

	public TaskDetails getTask(int cod_tarea) throws InvalidOperationException {
		return taskRespository.getTask(cod_tarea);
	}

	public TaskDetails[] getTaskDetailsBySheet(int cod_hoja)
			throws InvalidOperationException {
		return taskRespository.getTaskDetailsBySheet(cod_hoja);
	}

	public TaskEdit getTaskEdit(int cod_tarea) throws InvalidOperationException {
		return taskRespository.getTaskEdit(cod_tarea);
	}
	
	public boolean createTask(TaskEdit task, Date sheetDate) 
			throws InvalidOperationException, ValidationException{
		
		return createOrUpdateTask(task, sheetDate);	
	}
	

	public boolean updateSheet(TaskEdit task, Date sheetDate) 
			throws InvalidOperationException, ValidationException{
		
		return createOrUpdateTask(task, sheetDate);	
	}

	public boolean createOrUpdateTask(TaskEdit task, Date sheetDate)
			throws InvalidOperationException, ValidationException {
		if(sheetDate == null){
			if(sheetsRepository == null)
				throw new InvalidOperationException("No Repositorio de Hojas");
			
			SheetDetails sheet = sheetsRepository.getSheetDetails(task.codtarea);
			if(sheet == null)
				throw new InvalidOperationException("No Hoja");
			
			sheetDate = sheet.fechahoja;
			if(sheetDate == null)
				throw new InvalidOperationException("No Fecha de Hoja");
		}
		
		ErrorInfo error = new  ErrorInfo();
		validate(task, sheetDate ,error);
		
		if(error.containsErrors())
			throw new ValidationException(error);
				
		return task.codtarea > 0 ? taskRespository.updateTask(task): 
								   taskRespository.createTask(task);
	}	

	private void validate(TaskEdit task, Date sheetDate, ErrorInfo error) {
						
		if(task.fecha==null)
			error.add("fecha", Messages.getString("TasksManager.RequiredS"));  //$NON-NLS-1$ //$NON-NLS-2$
		if(task.dhFecha == null)
			error.add("dhFecha", Messages.getString("TasksManager.RequiredS")); //$NON-NLS-1$
		if(task.ddFecha == null)
			error.add("ddFecha", Messages.getString("TasksManager.RequiredS")); //$NON-NLS-1$ //$NON-NLS-2$
		if(task.thFecha == null)
			error.add("thFecha", Messages.getString("TasksManager.RequiredS")); //$NON-NLS-1$ //$NON-NLS-2$
		if(task.tdFecha == null)
			error.add("tdFecha", Messages.getString("TasksManager.RequiredS"));				 //$NON-NLS-1$ //$NON-NLS-2$
		
		if(!error.containsErrors())
		{
			if(task.fecha.compareTo(sheetDate) < 0)
			{
				error.add("fecha", String.format(Locale.US,Messages.getString("TasksManager.Posterior_Fecha_Hoja"),  //$NON-NLS-1$ //$NON-NLS-2$
						Converters.DateToStringConverter.convert(sheetDate)));
			}
			
			
			//validar que la fecha de la tarea sea posterior a la fecha del movil
			Date currentDate = DateUtils.getCurrentDate();
			if(task.fecha.compareTo(currentDate) > 0)
				error.add("fecha", Messages.getString("TasksManager.Anterior_Actual")); //$NON-NLS-1$ //$NON-NLS-2$
						
			if(task.thFecha.compareTo(task.tdFecha) > 0){
				error.add("thFecha", Messages.getString("TasksManager.inicio_tarea_no_mayor_fin")); //$NON-NLS-1$ //$NON-NLS-2$
			}			
			 
			if( task.dhFecha.compareTo(task.ddFecha) > 0){
				error.add("dhFecha", Messages.getString("TasksManager.inicio_descanso_no_mayor_fin_descanso"));  //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			if(task.ddFecha.compareTo(task.tdFecha) > 0){
				error.add("dhFecha", Messages.getString("TasksManager.inicio_descanso_no_mayor_fin"));			 //$NON-NLS-1$ //$NON-NLS-2$
			}			
			
			if(task.dhFecha.compareTo(task.thFecha) > 0){
				error.add("dhFecha", Messages.getString("TasksManager.Fin_descanso_no_mayor_fin")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
		}
						
		HorasLibres chekHoras;
		try {
			chekHoras = userManager.checkHorasUsuario(task.idtecnico, sheetDate);
		} catch (InvalidOperationException e) {
			error.add("fecha", Messages.getString("TasksManager.error_validacion_horas_libres")); //$NON-NLS-1$ //$NON-NLS-2$
			error.add(e.getMessage());
			return;
		}
		if(chekHoras!=null){
		    TimeValue timeStar =DateUtils.getTimeValue(task.tdFecha);
		    TimeValue timeEnd =  DateUtils.getTimeValue(task.thFecha);
		    
			int diffHours = timeEnd.Hours = timeStar.Hours;		
			if(diffHours > chekHoras.horas)
			{	
				//no se pude poner una tarea con mas horas que el numero de horas validas				
				error.add("tdFecha",String.format(Messages.getString("TasksManager.numero_horas_no_mayor"), chekHoras.horas));	 //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

//	public void getTasks(final int sheetId, OpenCloseResult<TaskDetails> tasksEdits)
//			throws Exception {
//					
//		TaskDetails[] tasks = taskRespository.getTaskDetailsBySheet(sheetId);	
//		if(tasks == null)
//		{ 
//			tasksEdits.Closed = new TaskDetails[0];
//			tasksEdits.Opens = new TaskDetails[0];
//			
//			return;			
//		}
//	
//		ArrayList<TaskDetails>openList = new ArrayList<TaskDetails>();
//		ArrayList<TaskDetails>closeList = new ArrayList<TaskDetails>();
//		
//		//ArrayList<Task> sheetTasksOpenList = new ArrayList<Task>();
//		//ArrayList<Task> sheetTaskClosedList = new ArrayList<Task>();
//			
//		for (TaskDetails task : tasks) {
//				if(task != null){
//					if(task.estado == 1)
//					{
//						openList.add(task);					
//					}
//					else
//					{
//						closeList.add(task);					
//					}
//				}
//		}
//		
//		tasksEdits.Opens = new TaskDetails[openList.size()];
//		tasksEdits.Closed = new TaskDetails[closeList.size()];
//		openList.toArray(tasksEdits.Opens);
//		closeList.toArray(tasksEdits.Closed);			
//	}
	
}
