package com.comyted.repository;

import com.comyted.models.SheetSignatureState;
import com.enterlib.exceptions.InvalidOperationException;

import android.graphics.Bitmap;

public interface ISheetSignatureRepository {

	public enum IssuedBy
	{
		CLIENT,
		TECNITIAN
	}
	
	boolean setSheetSignature(int cod_hoja, Bitmap bitmap , IssuedBy by)
			throws InvalidOperationException;
	
	SheetSignatureState getSheetSignatureState(int cod_hoja)
			throws InvalidOperationException;

}