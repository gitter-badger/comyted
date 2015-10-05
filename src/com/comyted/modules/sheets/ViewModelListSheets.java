package com.comyted.modules.sheets;

import com.comyted.models.SheetSumary;
import com.comyted.repository.ISheetsRepository;
import com.enterlib.mvvm.DataViewModel;
import com.enterlib.mvvm.IDataView;

public class ViewModelListSheets extends DataViewModel{
	
	private int orderId;		
	private ISheetsRepository repository;	
	SheetSumary[] sheets;
	private int userId;

	public ViewModelListSheets(IDataView view, ISheetsRepository repository, int orderId, int userId) {
		super(view);
		
		this.orderId = orderId;
		this.userId = userId;
		this.repository = repository;
	}
		
	
	public int getOrderId() {
		return orderId;
	}

	
	public SheetSumary[] getSheets() {
		return sheets;
	}
	
//	
//	public void goToAddActivity(){
//		Intent i =new Intent(activity.getApplicationContext(), SheetEditActivity.class);
//		i.putExtra("orderID", orderId);	
//		activity.startActivityForResult(i, OperationCodes.ADD_SHEET);
//	}
//	
//	
//	public class SelectedItemListener implements OnItemClickListener{
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			int position = arg2;		
//			if(position < 0 || sheets == null) return;
//			
//			SheetSumary s = (SheetSumary) arg0.getItemAtPosition(position);				
//			if(s != null)
//			{				
//				activity.startActivity(SheetDetailViewModel.getActivityIntent(activity.getApplicationContext(), s, orderId));
//			}
//			
//		}
//		
//	}

	@Override
	protected boolean loadAsync() throws Exception {
		if(orderId > 0)
			sheets = repository.getOrderSheets(orderId);
		else if(userId > 0)
			sheets = repository.getSheets(userId);
		else 
			sheets  = new SheetSumary[0];
		return true;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
}
