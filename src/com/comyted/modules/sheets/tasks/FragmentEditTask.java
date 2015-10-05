package com.comyted.modules.sheets.tasks;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.Utils;
import com.comyted.models.BaseModelComparer;
import com.comyted.models.BaseModelConverter;
import com.comyted.models.SheetDetails;
import com.comyted.models.TaskEdit;
import com.comyted.persistence.LocalDatabase;
import com.comyted.validations.AfterCurrentDateValidation;
import com.comyted.validations.BeforeDateValidation;
import com.enterlib.DateUtils;
import com.enterlib.DialogUtil;
import com.enterlib.converters.Converters;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.fields.DatePickerButtonField;
import com.enterlib.fields.FilterableSpinnerField;
import com.enterlib.fields.Form;
import com.enterlib.fields.SpinnerField;
import com.enterlib.fields.TextViewField;
import com.enterlib.mvvm.DefaultEditView;
import com.enterlib.validations.ErrorInfo;
import com.enterlib.validations.validators.DoubleValidator;
import com.enterlib.widgets.DateTimePickerButton;
import com.enterlib.widgets.FilterableSpinner;
import com.enterlib.widgets.TimePickerButton;

public class FragmentEditTask extends Fragment {
	
		private View rootView;		
		private ViewModelEditTask vm;		
		private EditView view;
		private boolean FromSavedInstance;	
		private Form form;
		
		public class EditView extends DefaultEditView<Activity> implements OnClickListener{

			public EditView(Activity activity) {
				super(activity);			
			}

			@Override
			public void onDataLoaded() {
				TaskEdit task = vm.getTask();
				
				((FilterableSpinnerField)form.getFieldById(R.id.tecnicoBox)).setElements(vm.getTecnicos(), null);
				((SpinnerField)form.getFieldById(R.id.estado)).setElements(vm.getEstados());
				
				if(!FromSavedInstance){
					try {
						form.updateTargets(task);
					} catch (InvalidOperationException e) {
						DialogUtil.showErrorDialog(getActivity(), e.getMessage());
						return;
					}	
				}
				
				ArrayList<String>notifications = vm.getNotifications();
				if(notifications.size() > 0){
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < notifications.size(); i++) {
						sb.append(notifications.get(i));
						sb.append('\n');
					}
					Utils.showAlertDialog(getActivity(), getString(R.string.aviso), sb.toString(),null);
				}						
			}
			
			@Override
			public void onSaved(ErrorInfo info) {				
				super.onSaved(info);
				
				if(info == null || !info.containsErrors()){
					Utils.showMessage(getActivity(), "Tarea salvada");
					try {
						//esperar un segundo para mostrar el mensaje
						Thread.sleep(1000);
					} catch (InterruptedException e) {											
					}
					
					getActivity().setResult(Constants.TASK_OK);
					getActivity().finish();
					return;
				}
				else{
					Utils.showMessage(getActivity(), "Hay errores de validación.\nComplete correctamente los campos y salve nuevamente.");
					form.setFieldErrors(info);
					form.showErrorDialog(getActivity(),null);					
				}
			}
			
			@Override
			public void onFailure(Exception workException) {
				DialogUtil.showErrorDialog(getActivity(), workException.getMessage(),this);
			}

			@Override
			public void onClick(DialogInterface dialog, int which) {
				getActivity().finish();				
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
			
			rootView = inflater.inflate(R.layout.fragment_edit_task, container, false);
														
			Utils.SetEditViewText(rootView, R.id.ehoras_viaje, "0.0");
			Utils.SetEditViewText(rootView, R.id.ekilometers, "0.0");
										
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			view = new EditView(getActivity());
			LocalDatabase localDb = new LocalDatabase(getActivity());
			TaskEdit task  = (TaskEdit) getActivity().getIntent().getSerializableExtra(Constants.TASK);
			SheetDetails sheet = (SheetDetails) getActivity().getIntent().getSerializableExtra(Constants.SHEET);
			vm = new ViewModelEditTask(task, sheet, MainApp.getCurrentUser(), localDb, view);
			
			
			
			//********************* BEGIN FORM ********************************
			form = new Form();
			{
				//*********** TEXT FIELDS **********************
				form.addField(new TextViewField((TextView) findView(R.id.title), "titulo", getString(R.string.titulo), true));
				
				form.addField(new TextViewField((TextView) findView(R.id.ehoras_viaje), "horasviaje", getString(R.string.horas_de_viaje), true)
						.addValueValidator(new DoubleValidator(0, Double.MAX_VALUE, getString(R.string.numero_decimal_positivo)))
						.setValueConverter(Converters.DoubleToStringConverter));
				
				form.addField(new TextViewField((TextView) findView(R.id.ekilometers), "kilometros", getString(R.string.kilometros_field),true)
						.addValueValidator(new DoubleValidator(0, Double.MAX_VALUE, getString(R.string.numero_decimal_positivo)))
						.setValueConverter(Converters.DoubleToStringConverter));
				
				//************ DATE FIELDS ****************************
				form.addField(new DatePickerButtonField((DateTimePickerButton)findView(R.id.fecha), "fecha",getString(R.string.fecha_task),true)
						.addValueValidator(new AfterCurrentDateValidation()));
				
				form.addField(new DatePickerButtonField((TimePickerButton)findView(R.id.deTime), "tdFecha", getString(R.string.inicio_de_tarea),true));
				form.addField(new DatePickerButtonField((TimePickerButton)findView(R.id.aTime), "thFecha", getString(R.string.fin_de_tarea),true));
				
				form.addField(new DatePickerButtonField((TimePickerButton)findView(R.id.ddTime), "ddFecha", getString(R.string.inicio_de_descanso),true));
				form.addField(new DatePickerButtonField((TimePickerButton)findView(R.id.daTime), "dhFecha", getString(R.string.fin_de_descanso),true));
				
				form.setValue(R.id.fecha, DateUtils.getCurrentDate());
				form.setValue(R.id.deTime, DateUtils.getCurrentDate());
				form.setValue(R.id.aTime, DateUtils.getCurrentDate());
				form.setValue(R.id.ddTime, DateUtils.getCurrentDate());
				form.setValue(R.id.daTime, DateUtils.getCurrentDate());
						
				//************* SELECTION FIELDS ************************					
				BaseModelComparer modelComparer = new BaseModelComparer();		
				BaseModelConverter modelToIdConverter = new  BaseModelConverter();
				
				form.addValidator(new FilterableSpinnerField((FilterableSpinner)findView(R.id.tecnicoBox), "idtecnico", getString(R.string.tecnico), true)
						.setComparer(modelComparer)
						.setValueConverter(modelToIdConverter));
				
				form.addValidator(new SpinnerField((Spinner)findView(R.id.estado), "estado", getString(R.string.estado),true)
						.setComparer(modelComparer)
						.setValueConverter(modelToIdConverter));
				
				//************* DATE VALIDATIONS ************************
				form.addValidator(new BeforeDateValidation((DatePickerButtonField)form.getFieldById(R.id.deTime),
													       (DatePickerButtonField)form.getFieldById(R.id.aTime)));
				
				form.addValidator(new BeforeDateValidation((DatePickerButtonField)form.getFieldById(R.id.ddTime),
					       									(DatePickerButtonField)form.getFieldById(R.id.daTime)));
				
				form.addValidator(new BeforeDateValidation((DatePickerButtonField)form.getFieldById(R.id.deTime),
															(DatePickerButtonField)form.getFieldById(R.id.ddTime)));
				
				form.addValidator(new BeforeDateValidation((DatePickerButtonField)form.getFieldById(R.id.daTime),
															(DatePickerButtonField)form.getFieldById(R.id.aTime)));
			}
			//*********** END FORM **********************************

			if(savedInstanceState!=null){
				form.loadValues(savedInstanceState);
				FromSavedInstance = true;
			}
		}
		
		private View findView(int id){
			return rootView.findViewById(id);
		}

	   @Override
	   public void onResume() {
			super.onResume();
			
			view.setIsValid(true);
			vm.load();
		}
		
	   @Override
		public void onStop() {
		    view.setIsValid(false);
		   
			super.onStop();
		}
	   
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.refresh, menu);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.refresh:
				FromSavedInstance = false;
				vm.load();	
				return true;
			default:
				return false;
			}
		}
		
		public void Save(){
			if(!form.validate()){	
				Utils.showMessage(getActivity(), getString(R.string.noti_errores_validacion));
				form.showErrorDialog(getActivity(), null);
				return;
			}
			
			TaskEdit task = vm.getTask();
			try {
				form.updateSource(task);
			} catch (Exception e) {
				DialogUtil.showErrorDialog(getActivity(), e.getMessage());
				return;
			}						
			vm.save();
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState) {			
			super.onSaveInstanceState(outState);						
			form.saveValues(outState);
		}

			
	
}
