package com.comyted.modules.sheets.tasks;

import java.util.ArrayList;

import com.comyted.Messages;
import com.comyted.R;
import com.comyted.conectivity.PostTareasClient;
import com.comyted.models.AppUser;
import com.comyted.models.HorasLibres;
import com.comyted.models.IdNameValue;
import com.comyted.models.SheetDetails;
import com.comyted.models.TaskEdit;
import com.comyted.modules.admin.IUserManager;
import com.comyted.modules.admin.UsersManager;
import com.comyted.persistence.LocalDatabase;
import com.comyted.repository.ISheetTaskRepository;
import com.comyted.repository.IStateRepository;
import com.comyted.repository.SheetTaskRepository;
import com.comyted.repository.StateRepository;
import com.enterlib.app.EditViewModel;
import com.enterlib.DateUtils;
import com.enterlib.TimeValue;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.exceptions.ValidationException;
import com.enterlib.app.IEditView;
import com.enterlib.validations.ErrorInfo;

public class ViewModelEditTask extends EditViewModel {
		
	private IdNameValue[]tecnicos;
	private IdNameValue[] estados;	
	private TaskEdit task;
	private SheetDetails sheet;
	private IUserManager userManager;
	private ISheetTaskRepository taskRepository;
	private IStateRepository stateRepository;			
	private AppUser user;
	private ArrayList<String> notifications = new ArrayList<String>();
	
	public ViewModelEditTask(TaskEdit task,
			SheetDetails sheet,
			AppUser user,
			IUserManager userManager,  
			ISheetTaskRepository taskRepository, 
			IStateRepository stateRepository,
			IEditView view) {
		super(view);
		
		this.task = task;
		this.sheet = sheet;	
		this.user = user;
		this.userManager = userManager;
		this.taskRepository = taskRepository;
		this.stateRepository = stateRepository;
		
		if(this.task == null){
			this.task = new TaskEdit();	
			this.task.init();
			this.task.idhoja = sheet.id;
		}
	}
	
	public ViewModelEditTask(TaskEdit task,SheetDetails sheet, AppUser user, 
			LocalDatabase localDb, IEditView view){
		
		this(task,sheet,user,
				new UsersManager(null),
				new SheetTaskRepository(localDb, null, new PostTareasClient()),
				new StateRepository(),
				view);
	}
	
	public IdNameValue[] getTecnicos() {
		return tecnicos;
	}

	public IdNameValue[] getEstados() {
		return estados;
	}

	public TaskEdit getTask() {
		return task;
	}	

	
	@Override
	protected boolean saveAsync() throws Exception {
		
		ErrorInfo error = null;
		task.setTimes();
		
		//Verificar si las horas no estan bloqueadas para el usuario
		HorasLibres chekHoras = userManager. checkHorasUsuario(task.idtecnico, sheet.fechahoja);
		
		if(chekHoras!=null){
			
			TimeValue timeStart = DateUtils.getTimeValue(task.tdFecha) ;
		    TimeValue timeEnd   = DateUtils.getTimeValue(task.thFecha);
		    
		    //tomar la diferencia en horas
			int diffHours = timeEnd.Hours = timeStart.Hours;
			
			if(diffHours > chekHoras.horas)
			{
				error = new ErrorInfo();
				//no se pude poner una tarea con mas horas que el numero de horas validas
				error.add("thFecha",String.format(Messages.getString("EditTaskViewModel.error_tareas_horas"), chekHoras.horas)); //$NON-NLS-1$ //$NON-NLS-2$
				error.add("tdFecha",String.format(Messages.getString("EditTaskViewModel.error_tareas_horas"), chekHoras.horas));			 //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		
		//Validacion fallo.
		if(error!=null)
			throw new ValidationException(error);
		
		boolean result;
		// salvar tarea	
		if(task.codtarea > 0)
			result = taskRepository.updateTask(task);
		else
			result = taskRepository.createTask(task);
		
		 if(!result){
			 throw new InvalidOperationException(getView().getString(R.string.no_se_pudo_salvar));
		 }
			
		return true;
	}
	

	@Override
	protected boolean loadAsync() throws Exception {
		estados = stateRepository.getStates();		
		AppUser[] usersTecnicos = userManager.ObtenerTecnicos();
		
		if(!user.jefeDepto)
		{
			if(!user.tecnico){
				tecnicos = new IdNameValue[0];
				throw new InvalidOperationException(getView().getString(R.string.usuario_no_valido));						
			}
			else {						
				usersTecnicos = new AppUser[]{ user };
			}
		}
		tecnicos = new  IdNameValue[usersTecnicos.length];
		for (int i = 0; i < usersTecnicos.length; i++) {
			tecnicos[i] = new IdNameValue(usersTecnicos[i].id, usersTecnicos[i].nombre);
			
			//asociar el tecnico de la hoja a la tarea.
			if(task.idtecnico == 0 && tecnicos[i].name.equals(sheet.tecnico) ){
				task.idtecnico = tecnicos[i].id;
			}
		}
		return true;
	}

	public ArrayList<String> getNotifications() {
		return notifications;
	}
	
//	private void validate(final TaskEdit task) 
//	throws ValidationException, InvalidOperationException {
//
//ErrorInfo error = new ErrorInfo();
//		
//if(task.fecha==null)
//	error.add("fecha", activity.getString(R.string.fecha_requerida)); //$NON-NLS-1$
//if(task.dhFecha == null)
//	error.add("dhFecha", activity.getString(R.string.tiempo_final_descanso_requerido)); //$NON-NLS-1$
//if(task.ddFecha == null)
//	error.add("ddFecha", activity.getString(R.string.tiempo_inicial_de_descanso_requerido));		 //$NON-NLS-1$
//if(task.thFecha == null)
//	error.add("thFecha", activity.getString(R.string.tiempo_final_de_la_tarea_requerido)); //$NON-NLS-1$
//if(task.tdFecha == null)
//	error.add("tdFecha",activity.getString(R.string.tiempo_inicial_de_la_tarea_requerido)); //$NON-NLS-1$
//
//if(!error.containsErrors())
//{
//	//validar que la fecha de la tarea no sea posterior a la fecha del movil
//	Date currentDate = Utils.getCurrentDate();
//	if(task.fecha.compareTo(currentDate) > 0)
//		error.add("fecha", activity.getString(R.string.la_fecha_no_valida)); //$NON-NLS-1$
//	
//	if(task.fecha.compareTo(sheet.fechahoja) < 0){
//		error.add("fecha", activity.getString(R.string.fecha_de_tarea_mayor_fecha_de_hoja_) +Utils.formatDateOnly(sheet.fechahoja) ); //$NON-NLS-1$
//	}
//	
//	//La hora de inicio de la tarea debe ser menor que la hora de fin
//	if(task.tdFecha.compareTo(task.thFecha) > 0){
//		error.add("tdFecha", activity.getString(R.string.la_hora_de_inicio_de_la_tarea_debe_ser_menor_que_la_hora_de_fin));			 //$NON-NLS-1$
//	}
//	//La hora final de la tarea no debe ser menor que la hora de inicio 
//	if(task.thFecha.compareTo(task.tdFecha) < 0 ){
//		error.add("thFecha", "La hora final de la tarea debe ser mayor que la hora de inicio"); //$NON-NLS-1$ //$NON-NLS-2$
//	}
//		
//	
//	//La hora de inicio de descanso debe ser menor que la hora de final de descanso
//	if(task.ddFecha.compareTo( task.dhFecha) > 0){
//		error.add("ddFecha", activity.getString(R.string.la_hora_inicial_de_descanso_debe_ser_menor_que_la_hora_final_de_descanso));						 //$NON-NLS-1$
//	}
//	if(task.dhFecha.compareTo(task.ddFecha) < 0){
//		error.add("dhFecha", activity.getString(R.string.la_hora_final_de_descanso_debe_ser_mayor_que_la_hora_inicial_de_descanso));						 //$NON-NLS-1$
//	}
//	
//	//La hora final de descanso debe ser menor que la hora final de la tarea
//	if(task.dhFecha.compareTo(task.thFecha) > 0){
//		error.add("dhFecha",activity.getString(R.string.la_hora_final_de_descanso_debe_ser_menor_que_la_hora_final_de_la_tarea));			 //$NON-NLS-1$
//	}
//	
//	//La hora de inicio de descanso debe ser mayor que la hora de inicio de la tarea
//	if(task.ddFecha.compareTo(task.tdFecha) < 0){
//		error.add("ddFecha", activity.getString(R.string.la_hora_inicial_de_descanso_debe_ser_mayor_que_la_hora_inicial_de_la_tarea) );			 //$NON-NLS-1$
//	}						
//}						
//
//HorasLibres chekHoras = userManager. checkHorasUsuario(task.idtecnico, sheet.fechahoja);
//
//if(chekHoras!=null){
//	TimeValue timeStart =Utils.getTime(task.tdFecha) ;
//    TimeValue timeEnd   =Utils.getTime(task.thFecha);
//    
//	int diffHours = timeEnd.Hours = timeStart.Hours;
//	//int diffMin = timeEnd.Minutes - timeStart.Minutes;
//	if(diffHours > chekHoras.horas)
//	{	
//		//no se pude poner una tarea con mas horas que el numero de horas validas
//		error.add("thFecha", activity.getString(R.string.la_cantidad_de_horas_no_puede_ser_mayor_que_)+String.valueOf(chekHoras.horas)); //$NON-NLS-1$
//		error.add("tdFecha", activity.getString(R.string.la_cantidad_de_horas_no_puede_ser_mayor_que_)+String.valueOf(chekHoras.horas));			 //$NON-NLS-1$
//	}
//}
//
//if(error.containsErrors())
//	throw new ValidationException(error);		
//}
	
}
