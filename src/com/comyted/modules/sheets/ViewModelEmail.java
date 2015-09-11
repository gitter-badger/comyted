package com.comyted.modules.sheets;

import com.comyted.R;
import com.comyted.conectivity.GetHojasClient;
import com.comyted.conectivity.PostHojasClient;
import com.comyted.models.SheetEmailRequest;
import com.comyted.repository.PeticionariosRepository;
import com.comyted.repository.SheetsRepository;
import com.comyted.repository.UserRepository;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.app.EditViewModel;
import com.enterlib.app.IEditView;

public class ViewModelEmail extends EditViewModel {

	private int sheetId;
	private SheetEmailRequest sheetEmailRequest;   
	private MailManager mailingManager;  
    
	public ViewModelEmail(int sheetId, int userId, IEditView view){
		super(view);
		
		GetHojasClient client = new GetHojasClient();
		this.sheetId = sheetId;
		this.mailingManager = new MailManager(userId, 
				new UserRepository(), 
				new PeticionariosRepository(client), 
				new SheetsRepository(client, new PostHojasClient(), null));
	}
  
	public ViewModelEmail(int sheetId, 
		  MailManager mailingManager, 
		  IEditView view){
	  
	  super(view);	  	
	  this.mailingManager = mailingManager;
	  this.sheetId = sheetId;
	}
	
	public int getSheetId() {
		return sheetId;
	}

	public SheetEmailRequest getSheetEmailRequest() {
		return sheetEmailRequest;
	}


    
	@Override
	protected boolean loadAsync() throws Exception {
		 sheetEmailRequest = mailingManager.getSheetEmailRequest(sheetId);
		 return true;
	}


	@Override
	protected boolean saveAsync() throws InvalidOperationException {
		if(sheetEmailRequest == null) {
			  IEditView view = (IEditView) getView();
			  throw new InvalidOperationException(view!=null?
					  view.getString(R.string.no_data):"No hay datos");
		}
		
		return mailingManager.createSheetEmailRequest(sheetEmailRequest);				
	}
}
