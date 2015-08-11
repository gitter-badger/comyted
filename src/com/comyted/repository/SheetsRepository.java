package com.comyted.repository;

import com.comyted.conectivity.GetHojasClient;
import com.comyted.conectivity.PostHojasClient;
import com.comyted.models.SheetDetails;
import com.comyted.models.SheetEdit;
import com.comyted.models.SheetEmailRequest;
import com.comyted.models.SheetSumary;
import com.comyted.models.Syncronizable;
import com.comyted.persistence.LocalDatabase;
import com.enterlib.exceptions.InvalidOperationException;

public class SheetsRepository implements ISheetsRepository {

	GetHojasClient getClient;
	PostHojasClient postClient;
	LocalDatabase localDb;
	
	public SheetsRepository(){
		this(null);
	}
	
	public SheetsRepository(LocalDatabase localDb) {
		this(new GetHojasClient(),
			 new PostHojasClient(),
		     localDb);
	}
	
	public SheetsRepository(GetHojasClient getClient, PostHojasClient posClient, LocalDatabase localDb){
		this.getClient = getClient;
		this.postClient = posClient;
		this.localDb = localDb;
	}	
	
	@Override
	public SheetSumary[] getSheets(int cod_tecnico)
			throws InvalidOperationException {
		return getClient.ObtenerHojas(cod_tecnico);
	}

	@Override
	public SheetSumary[] getOrderSheets(int cod_orden)
			throws InvalidOperationException {
		return getClient.ObtenerHojasOrden(cod_orden);
	}

	@Override
	public SheetDetails getSheetDetails(int cod_hoja)
			throws InvalidOperationException {
		return getClient.ObtenerHoja(cod_hoja);
	}

	@Override
	public SheetEdit getSheetEdit(int cod_hoja)
			throws InvalidOperationException {
		return getClient.ObtenerHojaDetalles(cod_hoja);
	}

	@Override
	public boolean createSheet(SheetEdit sheet)
			throws InvalidOperationException {
		try{
			return postClient.GrabarHoja(sheet);
		}
		catch(InvalidOperationException e){
			localDb.insertSheet(sheet, Syncronizable.ADD);
			return false;
		}
	}

	@Override
	public boolean updateSheet(SheetEdit sheet)
			throws InvalidOperationException {
		try{
			return postClient.ActualizarHoja(sheet);
		}
		catch(InvalidOperationException e){
			localDb.insertSheet(sheet, Syncronizable.UPDATE);
			return false;
		}
	}

	@Override
	public SheetEmailRequest getSheetEmailRequest(int cod_hoja)
			throws InvalidOperationException {
			return getClient.ObtenerEmailHoja(cod_hoja);
	}
	
	@Override
	public boolean createSheetEmailRequest(SheetEmailRequest email) throws InvalidOperationException {
		return postClient.EnviarEmail(email);
	}

}
