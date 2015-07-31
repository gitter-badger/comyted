package com.comyted.modules.sheets;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.Utils;
import com.comyted.models.BaseModelComparer;
import com.comyted.models.BaseModelConverter;
import com.comyted.models.IdNameValue;
import com.comyted.models.PlantEngine;
import com.comyted.models.SheetEdit;
import com.comyted.persistence.LocalDatabase;
import com.comyted.validations.AfterCurrentDateValidation;
import com.comyted.validations.BeforeDateValidation;
import com.enterlib.DateUtils;
import com.enterlib.DialogUtil;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.fields.DatePickerButtonField;
import com.enterlib.fields.FilterableSpinnerField;
import com.enterlib.fields.Form;
import com.enterlib.fields.SpinnerField;
import com.enterlib.fields.TextViewField;
import com.enterlib.app.DefaultEditView;
import com.enterlib.validations.ErrorInfo;
import com.enterlib.validations.validators.DoubleValidator;
import com.enterlib.validations.validators.IntegerValidator;
import com.enterlib.widgets.DateTimePickerButton;
import com.enterlib.widgets.FilterableSpinner;

public class FragmentEditSheet extends Fragment  
{
		private View rootView;	
		private ViewModelEditSheet vm;	
		private Form form;
		private EditView view;
		private boolean FromSavedInstance;				
		
		class EditView extends DefaultEditView<Activity> implements IViewSheetEdit{

			public EditView(Activity activity) {
				super(activity);				
			}

			@Override
			public void onDataLoaded() {
				SheetEdit s = vm.getSheetEdit();
				
				((FilterableSpinnerField)form.getFieldById(R.id.tecnicoBox)).setElements(vm.getTecnicos(), null);
				((FilterableSpinnerField)form.getFieldById(R.id.plantaBox)).setElements(vm.getPlantas(), null);
				
				((SpinnerField)form.getFieldById(R.id.peticionario)).setElements(vm.getPecticionarios());
				((SpinnerField)form.getFieldById(R.id.trabajo)).setElements(vm.getTrabajos());
				((SpinnerField)form.getFieldById(R.id.motor)).setElements(vm.getMotores());
				((SpinnerField)form.getFieldById(R.id.cargoRegion)).setElements(vm.getCargoregion());
				((SpinnerField)form.getFieldById(R.id.tipohoja)).setElements(vm.getTipoHojas());				
				((SpinnerField)form.getFieldById(R.id.estado)).setElements(vm.getEstados());
				
				if(!FromSavedInstance){
					try {
						form.updateTargets(s);
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
			public void onEnginesLoaded() {				
				ArrayList<String>notifications = vm.getNotifications();
				if(notifications.size() > 0){
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < notifications.size(); i++) {
						sb.append(notifications.get(i));
						sb.append('\n');
					}
					Utils.showAlertDialog(getActivity(), getString(R.string.aviso), sb.toString(),null);
				}												
				
				((SpinnerField)form.getFieldById(R.id.motor)).setElements(vm.getMotores());
				((SpinnerField)form.getFieldById(R.id.peticionario)).setElements(vm.getPecticionarios());								
				
			}
			
			@Override
			public void onSaved(ErrorInfo info) {				
				super.onSaved(info);
				
				if(info == null || !info.containsErrors()){
					Utils.showMessage(getActivity(), "Hoja salvada");
					try {
						//esperar un segundo para mostrar el mensaje
						Thread.sleep(1000);
					} catch (InterruptedException e) {											
					}
					
					getActivity().setResult(Constants.SHEET_OK);
					getActivity().finish();
					return;
				}
				else{
					Utils.showMessage(getActivity(), "Hay errores de validación.\nComplete correctamente los campos y salve nuevamente.");
					form.setFieldErrors(info);
					form.showErrorDialog(getActivity(), null);					
				}
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
						
			rootView =  inflater.inflate(R.layout.fragment_edit_sheet, container, false);			
											
			initializePlantasSpinner();
						
//			Utils.SetupDatePicker(getActivity(), (Button) rootView.findViewById(R.id.fecha), DateUtils.getCurrentDate());			
//			Utils.SetupDatePicker(getActivity(), (Button) rootView.findViewById(R.id.finmontaje), DateUtils.getCurrentDate());																	
			
			return rootView;
		}
		
		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
		 */
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			view  = new EditView(getActivity());			
			Activity activity = getActivity();
			int orderId = activity.getIntent().getIntExtra(Constants.ORDER_ID, -9999);
			int sheetId = activity.getIntent().getIntExtra(Constants.SHEET_ID, 0);
			
			vm = new ViewModelEditSheet(MainApp.getCurrentUser(), view, orderId, sheetId, 
					new LocalDatabase(activity));
			
			//******************* FORM INITIALIZATION ***************************************** 
			form = new Form();
			
			//TextView Fields
			form.addValidator(new TextViewField((TextView) findView(R.id.title), "titulo",getString(R.string.titulo),true));
			form.addValidator(new TextViewField((TextView) findView(R.id.expediente), "expediente", getString(R.string.numero_expediente), false));
			form.addValidator(new TextViewField((TextView) findView(R.id.num_serie), "numseriemotor",getString(R.string.numero_de_serie), true));
			form.addValidator(new TextViewField((TextView) findView(R.id.refpedido), "refpedido",getString(R.string.ref_pedido),false));
			form.addValidator(new TextViewField((TextView) findView(R.id.horasmotor), "horasmotor", getString(R.string.horas_motor),true)
									.addValueValidator(new DoubleValidator(0, Double.MAX_VALUE, "Número decimal positivo")));
			form.addValidator(new TextViewField((TextView) findView(R.id.direccion), "direccion"));
			form.addValidator(new TextViewField((TextView) findView(R.id.codpostal), "codpos"));
			form.addValidator(new TextViewField((TextView) findView(R.id.ciudad), "ciudad"));
			form.addValidator(new TextViewField((TextView) findView(R.id.provincia), "provincia"));
			form.addValidator(new TextViewField((TextView) findView(R.id.pais), "pais"));
			form.addValidator(new TextViewField((TextView) findView(R.id.telefono), "telefono")
									.addValueValidator(new IntegerValidator(0, Integer.MAX_VALUE, "Número entero positivo")));
			
						
			//Spinners
			
			//comparer
			BaseModelComparer modelComparer = new BaseModelComparer();
			//id extractor
			BaseModelConverter modelToIdConverter = new  BaseModelConverter();
			
			form.addValidator(new FilterableSpinnerField((FilterableSpinner)findView(R.id.tecnicoBox), "codtecnico", getString(R.string.tecnico),true)
									.setComparer(modelComparer)
									.setValueConverter(modelToIdConverter));
			form.addValidator(new FilterableSpinnerField((FilterableSpinner)findView(R.id.plantaBox), "codplanta",getString(R.string.planta), true)
									.setComparer(modelComparer)
									.setValueConverter(modelToIdConverter));
			form.addValidator(new SpinnerField((Spinner)findView(R.id.peticionario), "codpeticionario",getString(R.string.peticionario), true)
									.setComparer(modelComparer)
									.setValueConverter(modelToIdConverter));
			form.addValidator(new SpinnerField((Spinner)findView(R.id.trabajo), "tipotrabajo",getString(R.string.tipo_de_trabajo), true)
									.setComparer(modelComparer)
									.setValueConverter(modelToIdConverter));
			form.addValidator(new SpinnerField((Spinner)findView(R.id.motor), "codmotorplanta",getString(R.string.motor), true)
									.setComparer(modelComparer)
									.setValueConverter(modelToIdConverter));
			form.addValidator(new SpinnerField((Spinner)findView(R.id.cargoRegion), "codcargoregion",getString(R.string.cargo_region), true)
									.setComparer(modelComparer)
									.setValueConverter(modelToIdConverter));
			form.addValidator(new SpinnerField((Spinner)findView(R.id.tipohoja), "tipotrabajo",getString(R.string.tipo_de_hoja), true)
									.setComparer(modelComparer)
									.setValueConverter(modelToIdConverter));
			form.addValidator(new SpinnerField((Spinner)findView(R.id.estado), "codestado", getString(R.string.estado), true)
									.setComparer(modelComparer)
									.setValueConverter(modelToIdConverter));
			
			//DatePickers
			form.addValidator(new DatePickerButtonField((DateTimePickerButton)findView(R.id.fecha), "fechahoja", getString(R.string.fecha),true)
							.addValueValidator(new AfterCurrentDateValidation()));
			form.addValidator(new DatePickerButtonField((DateTimePickerButton)findView(R.id.finmontaje), "fechafinmontaje", getString(R.string.fin_de_montaje),true));
			
			form.setValue(R.id.fecha, DateUtils.getCurrentDate());
			form.setValue(R.id.finmontaje, DateUtils.getCurrentDate());
			
			//Date validators
			form.addValidator(new BeforeDateValidation((DatePickerButtonField)form.getFieldById(R.id.fecha),
													   (DatePickerButtonField)form.getFieldById(R.id.finmontaje)));
			
			if(savedInstanceState!=null){
				form.loadValues(savedInstanceState);
				FromSavedInstance = true;
			}
			
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
		
		
		private View findView(int id){
			return rootView.findViewById(id);
		}

		private void initializePlantasSpinner() {
			//Spinner spPlantas =(Spinner)rootView.findViewById(R.id.planta);
			FilterableSpinner spPlantas =(FilterableSpinner)rootView.findViewById(R.id.plantaBox);			
			
			spPlantas.setOnItemSelectedListener(new FilterableSpinner.OnItemSelectedListener() {				
				@Override
				public void onItemSelected(Object selectedItem) {									
					final IdNameValue planta = (IdNameValue)selectedItem;																				
					vm.loadEngines(planta.id, planta.name);														
				}
			});
			
			Spinner spMotores =(Spinner)rootView.findViewById(R.id.motor);
			spMotores.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {					
					
					Spinner sp = (Spinner)rootView.findViewById(R.id.motor);
					PlantEngine motorValue = (PlantEngine) sp.getSelectedItem();
					if(motorValue!=null && motorValue.serialno != null && !motorValue.serialno.isEmpty()){										
						Utils.SetEditViewText(rootView,R.id.num_serie, motorValue.serialno);																
					}					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {									
				}
			});
		}
						
		public void Save()
		{		
			if(!form.validate()){
				form.showErrorDialog(getActivity(), null);	
				return;
			}
			
			SheetEdit sheet = vm.getSheetEdit();
			try {
				form.updateSource(sheet);
			} catch (Exception e) {
				DialogUtil.showErrorDialog(getActivity(), e.getMessage());
				return;
			}						
			vm.save();
			
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
			
		@Override
		public void onSaveInstanceState(Bundle outState) {			
			super.onSaveInstanceState(outState);			
			
			form.saveValues(outState);
		}
}