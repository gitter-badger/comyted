package com.comyted.modules.sheets;

import java.security.InvalidParameterException;

import android.graphics.Bitmap;

import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.models.SheetSignatureState;
import com.comyted.repository.ISheetSignatureRepository;
import com.comyted.repository.SheetSignatureRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class SignatureManager  {
	
	public static final int SIGNATURE_WIDTH = 320;
	public static final int SIGNATURE_HEIGHT = 240;
	
	ISheetSignatureRepository signatureRep;
	

	public SignatureManager(ISheetSignatureRepository signatureRep) {
		this.signatureRep = signatureRep;
	}
	
	public SignatureManager(){
		this(new SheetSignatureRepository());
	}


	public void addClientSignature(int sheetId, Bitmap bitmap)
			throws InvalidParameterException, InvalidOperationException {
		
		boolean result = signatureRep.setSheetSignature(sheetId, bitmap, 
										ISheetSignatureRepository.IssuedBy.CLIENT);
		if(!result)
			throw new InvalidParameterException(MainApp.getInstance().getString(R.string.operacion_fallida));			
	}

	
	
	public void addTechnicianSignature(int sheetId, Bitmap bitmap)
			throws InvalidParameterException, InvalidOperationException {
		
		boolean result = signatureRep.setSheetSignature(sheetId, bitmap,
				ISheetSignatureRepository.IssuedBy.TECNITIAN);
		
		if(!result)
			throw new InvalidParameterException(MainApp.getInstance().getString(R.string.operacion_fallida));
		
		/*if(client.getResponseCode() >= 300)
			throw new OperationApplicationException("Response Code :" +client.getResponseCode() + 
					"\r\n message :"+ client.getErrorMessage());
		
		if(!CatContainer.getJSONParser().isOK(client.getResponse()))
			throw new InvalidParameterException(CatApplication.getInstance().getString(R.string.operacion_fallida));
		*/
	}

	public SheetSignatureState getSheetSignatureState(int cod_hoja)
			throws InvalidOperationException {
		return signatureRep.getSheetSignatureState(cod_hoja);
	}

}
