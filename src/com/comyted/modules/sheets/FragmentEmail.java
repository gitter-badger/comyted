package com.comyted.modules.sheets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.Utils;
import com.comyted.models.SheetEmailRequest;
import com.enterlib.DialogUtil;
import com.enterlib.converters.Converters;
import com.enterlib.fields.Form;
import com.enterlib.fields.TextViewField;
import com.enterlib.app.DefaultEditView;
import com.enterlib.validations.ErrorInfo;
import com.enterlib.validations.validators.RegExValueValidator;

public class FragmentEmail  extends Fragment								
{
	private View rootView;       
    ViewModelEmail vm;     
    Form form;
	ActivitySheet activity;
	EditView view;
	private boolean fromSavedInstance;
	
    class EditView extends DefaultEditView<ActivitySheet>{

		public EditView(ActivitySheet activity) {
			super(activity);			
		}
		
		@Override
		public String getSavingString() {
			return getString(R.string.enviando);
		}

		@Override
		public void onDataLoaded() {
			SheetEmailRequest r = vm.getSheetEmailRequest();
			if(r==null){
				 Utils.showMessage(getActivity(), getActivity().getString(R.string.no_se_pudieron_cargar_los_datos));
				 return;
			 }
			
			form.setValue(R.id.subject, r.asunto);
			form.setValue(R.id.fecha, r.fechahoja);
			form.setValue(R.id.email_cliente, r.emailcliente);
			form.setValue(R.id.email_tecnico, r.emailtecnico);			
		}
		
		@Override
		public void onSaved(ErrorInfo ei) {
			super.onSaved(ei);
			if(ei != null && ei.containsErrors())
			{
				DialogUtil.showErrorDialog(getActivity(), ei, null);
			}
			else
				Utils.showMessage(getActivity(), getString(R.string.email_enviado_correctamente));
		}    	
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {			
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);			
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 rootView = inflater.inflate(R.layout.fragment_email, container, false);				 		 
		 return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
						
		activity = (ActivitySheet)getActivity();
		int sheetId = activity.getIntent().getIntExtra(Constants.SHEET_ID, 0);
		view = new EditView(activity);
	    vm = new ViewModelEmail(sheetId, MainApp.getCurrentUser().id, view);			
			
		if(savedInstanceState!=null){
			String emailcliente= savedInstanceState.getString("emailcliente");
			String emailtecnico = savedInstanceState.getString("emailtecnico");
			Utils.SetEditViewText(rootView,R.id.email_cliente, emailcliente);
			Utils.SetEditViewText(rootView,R.id.email_tecnico, emailtecnico);
		}
		
		createForm();
		if(savedInstanceState!=null){
			form.loadValues(savedInstanceState);
			fromSavedInstance = true;
		}
	}
	
	@Override
	public void onResume() {		
		super.onResume();
		
		view.setIsValid(true);
		
		if(!fromSavedInstance)
			vm.load();
	}
	
	@Override
	public void onStop() {
		view.setIsValid(false);
		super.onStop();
	}
	
	private void createForm() {
		 form = new Form();
		 RegExValueValidator emailValidator =new RegExValueValidator("(\\w+)(\\.(\\w+))*@(\\w+)(\\.(\\w+))*", activity.getString(R.string.email_no_valido)); 
		 
		 form.addValidator(new TextViewField((TextView)rootView.findViewById(R.id.subject), true));
		 
	     form.addValidator(new TextViewField((TextView)rootView.findViewById(R.id.fecha), true)
	     						.setValueConverter(Converters.DateToStringConverter));	    	
	     
	     form.addValidator(new TextViewField((TextView)rootView.findViewById(R.id.email_cliente), true)
	     						.addValueValidator(emailValidator));
	     
	     form.addValidator(new TextViewField((TextView)rootView.findViewById(R.id.email_tecnico), true)
	     						.addValueValidator(emailValidator));
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {			
		menu.add(getActivity().getString(R.string.enviar))
			.setIcon(R.drawable.ic_launcher_email_generic)						
			.setOnMenuItemClickListener(new OnMenuItemClickListener() {				
				@Override
				public boolean onMenuItemClick(MenuItem item) {					
					SendEmail();					
					return true;
				}			
			}).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(getActivity().getString(R.string.refresh))
		.setIcon(R.drawable.ic_refresh_inverse)						
		.setOnMenuItemClickListener(new OnMenuItemClickListener() {				
			@Override
			public boolean onMenuItemClick(MenuItem item) {					
				vm.load();
				return true;
			}			
		}).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}	

	private void SendEmail() {			  	    
	    if(form.validate()){
	    	SheetEmailRequest entry = vm.getSheetEmailRequest();
	    	
	    	if(entry == null){
		    	Utils.showMessage(getActivity(), getActivity().getString(R.string.error));
		    	return;
		    }
	    	
	    	entry.emailcliente = (String) form.getValue(R.id.email_cliente);
	    	entry.emailtecnico = (String) form.getValue(R.id.email_tecnico);
	    	vm.save();
	    }	    	    	    				
	}
			
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		
		form.saveValues(outState);
	}
}
