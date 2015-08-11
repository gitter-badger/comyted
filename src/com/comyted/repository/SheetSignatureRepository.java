package com.comyted.repository;

import android.graphics.Bitmap;

import com.comyted.conectivity.GetHojasClient;
import com.comyted.conectivity.PostFirmaClient;
import com.comyted.models.SheetSignatureState;
import com.enterlib.exceptions.InvalidOperationException;

public class SheetSignatureRepository implements ISheetSignatureRepository {

	GetHojasClient get;
	PostFirmaClient post;
	
	public SheetSignatureRepository(GetHojasClient get, PostFirmaClient post) {
		this.get = get;
		this.post = post;
	}
	
	
	
	public SheetSignatureRepository() {
		this(new GetHojasClient(), new PostFirmaClient());		
	}



	@Override
	public boolean setSheetSignature(int cod_hoja, Bitmap bitmap, IssuedBy by)
			throws InvalidOperationException {
		
		if(by == IssuedBy.CLIENT)
			return post.GrabarFirmaCliente(cod_hoja, bitmap);
		else if(by == IssuedBy.TECNITIAN)
			return post.GrabarFirmaTecnico(cod_hoja, bitmap);
		
		return false;			
	}

	

	@Override
	public SheetSignatureState getSheetSignatureState(int cod_hoja)
			throws InvalidOperationException {
		return get.CheckHoja(cod_hoja);
	}
	

}
