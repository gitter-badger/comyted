package com.comyted.persistence;

import android.content.Context;

import com.comyted.conectivity.PostHojasClient;
import com.comyted.conectivity.PostTareasClient;
import com.comyted.models.LocalSheet;
import com.comyted.models.LocalTask;
import com.comyted.models.Syncronizable;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.generics.ObservableValue;
import com.enterlib.threading.AsyncManager;
import com.enterlib.threading.IAsyncInvocator;

public class SyncronizationService {
	
	LocalDatabase localDb;
	PostHojasClient postHojasClient;
	PostTareasClient postTareasClient;
	
	ObservableValue<Boolean> resultObserver;
	String error;
	
	public SyncronizationService(Context context) {
		this(new LocalDatabase(context));
	}
	
	public SyncronizationService(LocalDatabase localDb){
		this.localDb = localDb;
		resultObserver = new ObservableValue<Boolean>();
		postHojasClient = new PostHojasClient();
		postTareasClient = new PostTareasClient();
	}
	
	public ObservableValue<Boolean> getResultObserver() {
		return resultObserver;
	}

	public String getError() {
		return error;
	}
	
	public int syncronize() throws InvalidOperationException{
		int syncronizedObjects = 0;
		LocalSheet[] sheets = localDb.getSheets();
		for (int i = 0; i < sheets.length; i++) {
			LocalSheet s = sheets[i];
			boolean result = false;					
			switch (s.Action) {
				case Syncronizable.ADD:
					result = postHojasClient.GrabarHoja(s.Sheet);
					break;						
				case Syncronizable.UPDATE:
					result = postHojasClient.ActualizarHoja(s.Sheet);
					break;
			}	
			if(result){
				localDb.removeSheet(s.Id);
				syncronizedObjects++;
			}
		}
		
		LocalTask[] taks = localDb.getTasks();
		for (int i = 0; i < taks.length; i++) {
			LocalTask t = taks[i];
			boolean result = false;					
			switch (t.Action) {
				case Syncronizable.ADD:
					result = postTareasClient.GrabarTarea(t.Task);
					break;						
				case Syncronizable.UPDATE:
					result = postTareasClient.ActualizarTarea(t.Task);
					break;
			}											
			if(result){
				localDb.removeTask(t.Id);
				syncronizedObjects++;
			}
		}
		return syncronizedObjects;
	}

	public void syncronizeAsnc(){	
		error = null;
		
		AsyncManager.InvokeAsync(new IAsyncInvocator() {

			@Override
			public void DoAsyncOperation() throws Exception {
				syncronize();
			}

			@Override
			public void OnAsyncOperationComplete() {
				if(resultObserver!=null)
					resultObserver.setValue(true);				
			}

			@Override
			public void OnAsyncOperationException(Exception e) {
				if(resultObserver!=null)
					resultObserver.setValue(false);
				error = e.getMessage();
			}
		});
		
	}
}
