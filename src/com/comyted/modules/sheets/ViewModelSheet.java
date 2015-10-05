package com.comyted.modules.sheets;

import com.comyted.conectivity.GetHojasClient;
import com.comyted.models.AppUser;
import com.comyted.models.SheetDetails;
import com.comyted.repository.ISheetsRepository;
import com.comyted.repository.Messages;
import com.comyted.repository.SheetsRepository;
import com.enterlib.generics.SimpleEvent;
import com.enterlib.generics.ObservableValue;
import com.enterlib.mvvm.DataViewModel;
import com.enterlib.mvvm.IDataView;

public class ViewModelSheet extends DataViewModel {
	
	public static final int TASKS = 1;
	public static final int SHEET = 2;
	public static final int SHEET_STATUS = 3;
		
	private int sheetId;			
	private SheetDetails sheetDetails;
	private ISheetsRepository sheetsRepository;
	
	public final SimpleEvent<SheetDetails> SheetLoaded = new SimpleEvent<SheetDetails>();
	public final SimpleEvent<Void> SheetLoading = new SimpleEvent<Void>();
	
	public final ObservableValue<String>Error = new ObservableValue<String>();
	
	
	public ViewModelSheet(int sheetId, IDataView view, ISheetsRepository sheetsRepository){
		super(view);
		
		//this.orderId = activity.getIntent().getIntExtra("orderID", 0);
		//this.sheetId = activity.getIntent().getIntExtra("sheetID", 0);
		this.sheetId = sheetId;		
		this.sheetsRepository = sheetsRepository;
	}
	
	public ViewModelSheet(int sheetId, IDataView view){
		this(sheetId, view, new SheetsRepository(new GetHojasClient(),null, null));
	}
	

	public SheetDetails getSheet() {
		return sheetDetails;
	}		
		
	
	public int getSheetId() {
		return sheetId;
	}
	
	@Override
	protected boolean loadAsync() throws Exception {
		sheetDetails = sheetsRepository.getSheetDetails(sheetId);
		return true;
	}
	
	@Override
	protected void onLoading() {	
		super.onLoading();
		
		SheetLoading.invoke(this, null);
	}
	
	@Override
	protected void onLoaded(Exception workException) {		
		super.onLoaded(workException);
		
		if(workException != null)
			Error.setValue(workException.getMessage());
		
		SheetLoaded.invoke(this, sheetDetails);
	}
	
	public boolean canUserEdit(AppUser user){
		String abierta = Messages.getString("StateRepository.Abierta");
		boolean isopen = sheetDetails.estado.equalsIgnoreCase(abierta);
		return user.jefeDepto || isopen;
	}
//	
//	public boolean editSheet(){
//		AppUser user =MainApp.getCurrentUser();
//		if(SheetsManager.IsFinish(sheetObservable.estado) && !user.jefeDepto){
//			Utils.showAlertDialog(activity, activity.getString(R.string.aviso), activity.getString(R.string.no_posible_editar_hoja), null);
//			return false;
//		}
//		activity.startActivityForResult(EditSheetViewModel.getIntent(activity, sheetId, orderId), 
//				OperationCodes.EDIT_SHEET);
//		return true;
//	}
	
}
