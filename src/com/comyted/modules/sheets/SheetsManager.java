package com.comyted.modules.sheets;

import android.content.Context;

import com.comyted.Messages;
import com.comyted.conectivity.GetTareasClient;
import com.comyted.models.HorasLibres;
import com.comyted.models.SheetDetails;
import com.comyted.models.SheetEdit;
import com.comyted.models.SheetSumary;
import com.comyted.models.Task;
import com.comyted.modules.admin.IUserManager;
import com.comyted.modules.admin.UsersManager;
import com.comyted.persistence.LocalDatabase;
import com.comyted.repository.ISheetTaskRepository;
import com.comyted.repository.ISheetsRepository;
import com.comyted.repository.SheetTaskRepository;
import com.comyted.repository.SheetsRepository;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.exceptions.ValidationException;
import com.enterlib.validations.ErrorInfo;


public class SheetsManager 
{
	public static final String Required = Messages.getString("SheetsManager.Required"); //$NON-NLS-1$
	public static final String Invalid = Messages.getString("SheetsManager.Invalid"); //$NON-NLS-1$	

	ISheetsRepository sheetsRepository;
	ISheetTaskRepository taskRepository;
	IUserManager usersManager;	
	
	public SheetsManager(ISheetsRepository sheetsRepository, 
						ISheetTaskRepository taskRepository,
						IUserManager userManager) {	
		this.sheetsRepository = sheetsRepository;
		this.taskRepository = taskRepository;
		this.usersManager = userManager;
	}
	
	public SheetsManager(Context context){
		
		LocalDatabase localDb = new LocalDatabase(context);
		this.sheetsRepository =  new SheetsRepository(localDb);
		this.taskRepository = new SheetTaskRepository(localDb, new GetTareasClient(), null);
		this.usersManager = new UsersManager(localDb);
	}
	
	public SheetEdit getSheetEdit(int sheetId) throws InvalidOperationException{
		return sheetsRepository.getSheetEdit(sheetId);
	}
	
	public SheetSumary[] getSheets(int cod_tecnico)
			throws InvalidOperationException {
		return sheetsRepository.getSheets(cod_tecnico);
	}

	public SheetSumary[] getOrderSheets(int cod_orden)
			throws InvalidOperationException {
		return sheetsRepository.getOrderSheets(cod_orden);
	}

	public SheetDetails getSheetDetails(int cod_hoja)
			throws InvalidOperationException {
		return sheetsRepository.getSheetDetails(cod_hoja);
	}

	public boolean createSheet(SheetEdit sheet)
			throws InvalidOperationException ,ValidationException{
		
		ErrorInfo error = new ErrorInfo();				
		validateHours(sheet, error);
		
		if(error.containsErrors())
			throw new ValidationException(error);
		
		return sheetsRepository.createSheet(sheet);
	}

	public boolean updateSheet(SheetEdit sheet)
			throws InvalidOperationException ,ValidationException {
		
		ErrorInfo error = new ErrorInfo();				
		validateHours(sheet, error);
				
		if(sheet.codestado == 2){
			//si la hoja esta cerrada verificar que no halla ninguna tarea abierta						
			Task [] tasks = taskRepository.getTasksBySheet(sheet.id );
			for (int i = 0; i < tasks.length; i++) {
				if(!SheetsManager.IsFinish(tasks[i].estado)){
					error.add("codestado", Messages.getString("SheetsManager.error_tareas_abiertas")); //$NON-NLS-1$ //$NON-NLS-2$
					break;
				}
			}
		}
		if(error.containsErrors())
			throw new ValidationException(error);
		
		return sheetsRepository.updateSheet(sheet);
	}	
	
	public static boolean IsFinish(String estado){		
		String cerrada = com.comyted.repository.Messages.getString("StateRepository.Cerrada"); //$NON-NLS-1$
		return estado.equalsIgnoreCase(cerrada) || 
				estado.equalsIgnoreCase(com.comyted.repository.Messages.getString("StateRepository.Cerrado")); //$NON-NLS-1$
	}
		

	private void validateHours(final SheetEdit sheetEdit, ErrorInfo error)
			throws InvalidOperationException {
			
		HorasLibres checkResult = usersManager.checkHorasUsuario(sheetEdit.codtecnico, 
																sheetEdit.fechahoja);				
		if(checkResult!=null && !checkResult.isvalid)
			error.add("fechahoja", Messages.getString("SheetsManager.fecha_bloqueada")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
//	public void validate(final SheetEdit sheet, Map<String, String>validations){
//		if(sheet.titulo == null || sheet.titulo.isEmpty())
//			validations.put("title", Required);				 //$NON-NLS-1$
//		if(sheet.expediente == null || sheet.expediente.isEmpty())
//			validations.put("expediente", Required);				 //$NON-NLS-1$
//		if(sheet.refpedido == null || sheet.refpedido.isEmpty())
//			validations.put("refpedido", Required);				 //$NON-NLS-1$
//		if(sheet.numseriemotor == null || sheet.numseriemotor.isEmpty())
//			validations.put("numseriemotor", Required);				 //$NON-NLS-1$
//		if(sheet.fechahoja == null)
//			validations.put("fechahoja", Required);				 //$NON-NLS-1$
//		if(sheet.fechafinmontaje == null)
//			validations.put("fechafinmontaje", Required);				 //$NON-NLS-1$
//		if(sheet.fechahoja !=null && sheet.fechafinmontaje != null &&
//		   sheet.fechahoja.compareTo(sheet.fechafinmontaje) > 0)
//		{
//			validations.put("fechahoja", Invalid);	 //$NON-NLS-1$
//		}		
//				
//	}
		
}
