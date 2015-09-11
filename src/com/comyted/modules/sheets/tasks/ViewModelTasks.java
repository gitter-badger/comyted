package com.comyted.modules.sheets.tasks;

import com.comyted.conectivity.GetTareasClient;
import com.comyted.models.SheetDetails;
import com.comyted.models.TaskDetails;
import com.comyted.repository.ISheetTaskRepository;
import com.comyted.repository.SheetTaskRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

public class ViewModelTasks extends DataViewModel {
	
	
	TasksManager taskManager;
	TaskDetails[] tasks;
	int orderId;
	int sheetId;	
	
	public ViewModelTasks(int sheetId,int orderId, IDataView view){
		this(sheetId, orderId, view, new SheetTaskRepository(null, new GetTareasClient(), null));
	}
	
	public ViewModelTasks(
			int sheetId,
			int orderId,
			IDataView view,
			ISheetTaskRepository taskRepository) {
		
		super(view);
		
		this.sheetId = sheetId;
		this.orderId = orderId;		
		this.taskManager = new TasksManager(taskRepository, null, null);
	}	

	public int getSheetId() {
		return sheetId;
	}	
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}		
			
	public TaskDetails[] getTasks() {
		return tasks;
	}
	
	public void setSheetId(int sheetId) {
		this.sheetId = sheetId;
	}

	public boolean canEditTask(TaskDetails task, SheetDetails sheet){
		return !sheet.isClosed();
		
		//if(sheet.isClosed()){
//			if(view!=null)
//				view.showMessage(view.getString(R.string.tarea_no_editar));
			
			//return ;
		//}						
//		 Intent i = new Intent(activity.getApplicationContext(), TaskEditActivity.class )
//		.putExtra("task", t)
//		.putExtra("sheet", sheet.id)
//		.putExtra("sheetId", sheet.id)
//		.putExtra("orderID", activity.getIntent().getIntExtra("orderID", 0));					
//											
//		activity.startActivityForResult(i, OperationCodes.EDIT_TASK);
	}

	@Override
	protected boolean loadAsync() throws Exception {
		tasks = taskManager.getTaskDetailsBySheet(sheetId);
		return true;
	}
}
