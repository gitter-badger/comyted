package com.comyted;

import junit.framework.Assert;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.enterlib.DialogUtil;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.EditViewModel;
import com.enterlib.app.IEditView;
import com.enterlib.app.PresentUtils;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.fields.Form;
import com.enterlib.validations.ErrorInfo;

public abstract class EditFragment extends RefreshableFragment
						  			implements IEditView{
	
	private Form form;
	private boolean fromSavedInstance;			
	
	public Form getForm(){
		return form;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
		
		Assert.assertTrue(getViewModel() instanceof EditViewModel);
		form = createForm();
		getActivity().setResult(Activity.RESULT_CANCELED);
		
		if(savedInstanceState!=null && form!=null){
			form.loadValues(savedInstanceState);
			fromSavedInstance = true;
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.edit, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		if(super.onOptionsItemSelected(item))
			return true;
				
		switch (item.getItemId()) {		
			case R.id.save:
				save();
				return true;		
		}
		return false;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {			
		super.onSaveInstanceState(outState);
		
		if(form!=null)
			form.saveValues(outState);
	}
	
	protected abstract Form createForm();

	public String getSavingString() {
		return getActivity().getString(R.string.salvando);
	}

	@Override
	public void showSaveProgressDialog() {
		Dialog saveDialog = DialogUtil.getProgressDialog(getActivity(), getSavingString()); 
		setDialog(saveDialog);		
        saveDialog.show();	
	}

	@Override
	public void dismisSaveProgressDialog() {
		Dialog saveDialog = getDialog();
		if(saveDialog!=null && saveDialog.isShowing())
			saveDialog.dismiss();
	}
	
	
	public int getEntityId(){
		Activity activity = getActivity();
		Assert.assertNotNull(activity);
		return activity.getIntent().getIntExtra(Constants.ID, 0);
	}
	
	protected abstract Object getData();
	
	public void save()
	{
		Object data = getData();
		if(data == null){
			PresentUtils.showMessage(getActivity(), "no hay datos");
			return;
		}
		
		DataViewModel viewModel = getViewModel() ;
		Assert.assertNotNull(viewModel);				
		EditViewModel editViewModel = (EditViewModel)viewModel;
		
		if(!form.validate()){
			form.showErrorDialog(getActivity(), null);	
			return;
		}
					
		try {
			form.updateSource(data);
		} catch (Exception e) {
			DialogUtil.showErrorDialog(getActivity(), e.getMessage());
			return;
		}
		
		editViewModel.save();		
	}
	
	@Override
	public void onSaved(ErrorInfo info) {
		if(info == null || !info.containsErrors()){
			PresentUtils.showMessage(getActivity(), getActivity().getString(R.string.salvado_));
			try {
				//esperar un segundo para mostrar el mensaje
				Thread.sleep(1000);
			} catch (InterruptedException e) {											
			}
			
			getActivity().setResult(Constants.MODIFIED);
			getActivity().finish();
			return;
		}
		else{
			Utils.showMessage(getActivity(), getActivity().getString(R.string.noti_errores_validacion));
			form.setFieldErrors(info);
			form.showErrorDialog(getActivity(),null);					
		}
	}
	
	@Override
	public void onDataLoaded() {
		Object entity  = getData();
		
		if(!fromSavedInstance && form!=null){
			try {
				form.updateTargets(entity);
			} catch (InvalidOperationException e) {
				DialogUtil.showErrorDialog(getActivity(), e.getMessage());
				return;
			}	
		}
		showNotifications();		
	}

}
