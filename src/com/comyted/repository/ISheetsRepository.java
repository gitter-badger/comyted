package com.comyted.repository;

import com.comyted.models.SheetEmailRequest;
import com.comyted.models.SheetDetails;
import com.comyted.models.SheetEdit;
import com.comyted.models.SheetSumary;
import com.enterlib.exceptions.InvalidOperationException;

public interface ISheetsRepository {

	SheetSumary[] getSheets(int cod_tecnico) throws InvalidOperationException;
	
	SheetSumary[] getOrderSheets(int cod_orden) throws InvalidOperationException;
	
	SheetDetails getSheetDetails(int cod_hoja) throws InvalidOperationException;
	
	SheetEdit getSheetEdit(int cod_hoja) throws InvalidOperationException;
	
	boolean createSheet(SheetEdit sheet) throws InvalidOperationException;
	
	boolean updateSheet(SheetEdit sheet)  throws InvalidOperationException;	
		
	SheetEmailRequest getSheetEmailRequest(int cod_hoja) throws InvalidOperationException;

	boolean createSheetEmailRequest(SheetEmailRequest email) throws InvalidOperationException;
}