package com.comyted.modules.sheets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.Utils;
import com.comyted.models.SheetDetails;
import com.enterlib.mvvm.DefaultDataView;

public class FragmentSheet extends Fragment {
				
	private ViewGroup rootView;	
	private ViewModelSheet vm;
	private DataView view;	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{					
		 rootView = (ViewGroup)inflater.inflate(R.layout.fragment_sheet, container, false);		 
		 return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
	
		ActivitySheet activity = (ActivitySheet) getActivity();
		view = new DataView(getActivity());
		vm = activity.getViewModel();
		vm.setView(view);
		
		if(vm.getSheet()!=null){
			onInvoke(vm, vm.getSheet());
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//activate the view and associate it with the activity view model
		//this view delegates the loading to the activity view model
		view.setIsValid(true);
		vm.setView(view);
	}
	
	@Override
	public void onStop() {
		//invalidates and release the view from the view model
		view.setIsValid(false);	
		vm.setView(null);
		super.onStop();
	}
		
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		inflater.inflate(R.menu.fragment_sheet, menu);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
		case R.id.edit:			
			 startEditActivity();
			return true;
		case R.id.refresh:			
			  vm.load();
			 return true;
		default:
			return false;
		}
	}
	
	
	private void startEditActivity() {
		if(!vm.canUserEdit(MainApp.getCurrentUser())){
			Utils.showAlertDialog(getActivity(), 
					getString(R.string.aviso), 
					getString(R.string.no_posible_editar_hoja), null);
		}else{
			SheetDetails sheet = vm.getSheet();
			int orderId = getActivity().getIntent().getIntExtra(Constants.ORDER_ID, 0);
			Intent intent = new Intent(getActivity(), ActivityEditSheet.class)
					 			.putExtra(Constants.SHEET_ID, sheet.id)			 
					 			.putExtra(Constants.ORDER_ID, orderId);
			
			startActivityForResult(intent, Constants.EDIT_SHEET);
		}
		
	}	
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(resultCode == Constants.SHEET_OK){
			vm.load();
		}
	}
	
	public void onInvoke(Object sender, SheetDetails sheet) {
		if(sheet == null){
			Utils.showErrorDialogAndFinish(getActivity(), getActivity().getString(R.string.no_hoja));						
		}
		else{
			getActivity().getActionBar().setTitle(sheet.titulo);
		
			Utils.setTextViewText(rootView, R.id.title, sheet.titulo);
			
			TextView txtTotalHoras = (TextView)rootView.findViewById(R.id.txttiempototal_orden);
			txtTotalHoras.setText(sheet.horastotales + "h " + sheet.minutostotales + "m");
			
			TextView txtTiempoReal = (TextView)rootView.findViewById(R.id.txtduracionreal_orden);
			txtTiempoReal.setText((sheet.horastotales - sheet.horaspausa) + "h " + (sheet.minutostotales - sheet.minutospausa) + "m");
			
			TextView txtTiempoPausa = (TextView)rootView.findViewById(R.id.txttiempopausa_orden);
			txtTiempoPausa.setText(sheet.horaspausa + "h " + sheet.minutospausa + "m");
			
			// Datos de la planta
	        TextView txt_planta = (TextView)rootView.findViewById(R.id.txtplanta_orden);
	        txt_planta.setText(sheet.planta);
	        
	        Utils.setTextViewText(rootView, R.id.txtpeticionario, sheet.peticionario);
	        
	        // txt_planta.setText(args.getCharArray("planta").toString()); Asi recogeriamos el valor en caso de venir como argumento dentro del Fragment.
	        TextView txt_direccion = (TextView)rootView.findViewById(R.id.txtdireccion_orden);
	        txt_direccion.setText(sheet.direccion);
	        
	        TextView txt_expediente = (TextView)rootView.findViewById(R.id.txtexpdte_orden);
	        txt_expediente.setText(sheet.expediente);
	        
	        TextView txt_tipotrabajo = (TextView)rootView.findViewById(R.id.txttipotrabajo_orden);
	        txt_tipotrabajo.setText(sheet.tipotrabajo);
	        
	        TextView txt_motor = (TextView)rootView.findViewById(R.id.txtmotor_orden);
	        txt_motor.setText(sheet.motor);
	        
	        TextView txt_numserie= (TextView)rootView.findViewById(R.id.txtnumserie_orden);
	        txt_numserie.setText(sheet.numserie);
	        
	        TextView txt_estado= (TextView)rootView.findViewById(R.id.txtestado_orden);
	        txt_estado.setText(sheet.estado);
	        
	        TextView txt_fecha = (TextView)rootView.findViewById(R.id.txtfecha);
	        txt_fecha.setText(Utils.formatDateOnly(sheet.fechahoja));
	        
	        if(SheetsManager.IsFinish(sheet.estado)){
	        	txt_estado.setBackgroundResource(R.color.taskTitle);
	        }
	        else{
	        	txt_estado.setBackgroundResource(R.color.task_abierta);
	        }
	        
	        Utils.setTextViewText(rootView,R.id.txtcargoregion, sheet.cargoregion);
	        Utils.setTextViewText(rootView,R.id.tvTecnico, sheet.tecnico);
		}
		
	}

	class DataView extends DefaultDataView<Activity>{

		public DataView(Activity activity) {
			super(activity);		
		}

		@Override
		public void onDataLoaded() {
			onInvoke(vm, vm.getSheet());			
		}
		
	}

	
}
