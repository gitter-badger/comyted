package com.comyted.modules.sheets;

import java.security.InvalidParameterException;

import com.comyted.models.SheetSignatureState;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.mvvm.DataViewModel;
import com.enterlib.mvvm.IDataView;
import com.enterlib.threading.AsyncManager;
import com.enterlib.threading.IWorkPost;

import android.graphics.Bitmap;

public class ViewModelSignature extends DataViewModel {
		
	public static final int TECNITIAN = 1;
	public static final int CLIENT = 2;
	
	SheetSignatureState state;
	SignatureManager signatureManager;
	int cod_hoja;
	
	ISavingNotifyCallback savingCallback;
	ISavedNotifyCallback savedCallback;
	
	public static interface ISavingNotifyCallback{
		void onNotify(int type);
	}
	
	public static interface ISavedNotifyCallback{
		void onNotify(int type, Exception e);
	}
	
	public ViewModelSignature(int cod_hoja, IDataView view){
		this(cod_hoja, new SignatureManager(), view);
	}
	
	public ViewModelSignature(int cod_hoja, SignatureManager signatureManager,IDataView view){		
		super(view);
				
		this.signatureManager = signatureManager;
		this.cod_hoja = cod_hoja;
			
	}	
	
	public ISavingNotifyCallback getSavingCallback() {
		return savingCallback;
	}

	public void setSavingCallback(ISavingNotifyCallback savingCallback) {
		this.savingCallback = savingCallback;
	}

	public ISavedNotifyCallback getSavedCallback() {
		return savedCallback;
	}

	public void setSavedCallback(ISavedNotifyCallback savedCallback) {
		this.savedCallback = savedCallback;
	}

	public SheetSignatureState getState() {
		return state;
	}
	
	public void setState(SheetSignatureState state){
		this.state = state;
	}

	public int getCod_hoja() {
		return cod_hoja;
	}
		
	@Override
	protected boolean loadAsync() throws Exception {
		state = signatureManager.getSheetSignatureState(cod_hoja);
		return true;
	}
	
	public void save(final int type, final Bitmap bitmap){
		onSaving(type);			
		
		AsyncManager.postAsync(new IWorkPost() {
			
			@Override
			public boolean runWork() throws Exception {				
					return saveAsync(type, bitmap);				
			}				

			@Override
			public void onWorkFinish(Exception workException) {
				IDataView view = getView();
				if(view !=null && view.isValid()){														
					if(workException!=null)
						view.onFailure(workException);					
				}
				onSaved(type ,workException);	
			}		
		});
		
	}
	
	private boolean saveAsync(int type, Bitmap bitmap) throws InvalidParameterException, InvalidOperationException {
		switch (type) {
		case TECNITIAN:		
			signatureManager.addTechnicianSignature(cod_hoja, bitmap);
			break;
			
		case CLIENT:
			signatureManager.addClientSignature(cod_hoja, bitmap);
			break;
		}
		return true;
	}
	
	private void onSaving(int type) {
		if(savingCallback!=null){
			savingCallback.onNotify(type);
		}
	}
	
	private void onSaved(int type, Exception workException) {
		if(savedCallback!=null){
			savedCallback.onNotify(type, workException);
		}
		
	}
}
