package com.comyted.modules.orders;

import android.app.Activity;
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
import com.comyted.Utils;
import com.comyted.models.Order;
import com.enterlib.app.DefaultDataView;

public class FragmentOrder extends Fragment {
	
	ViewGroup rootView;			
	ViewModelOrder vm;
	DefaultDataView<Activity> view;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);						
	}
	
	public static FragmentOrder newInstance(int orderId){
		FragmentOrder fr = new FragmentOrder();
		Bundle b = new Bundle();
		b.putInt(Constants.ORDER_ID, orderId);
		fr.setArguments(b);
		return fr;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){       		
		
		rootView = (ViewGroup)inflater.inflate(R.layout.fragment_order, container, false);                            
		return rootView;
    }	
	
	//Called once the parent Activity and the Fragment’s UI have 
	// been created.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	
		int orderId = getArguments().getInt(Constants.ORDER_ID);
		vm =new ViewModelOrder(view = new DefaultDataView<Activity>(getActivity()) {
			@Override
			public void onDataLoaded() {
				loadUI(vm.getOrder());				
			}
		}, orderId);			            	       
		
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
		inflater.inflate(R.menu.fragment_order, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			vm.load();
			return true;
		default:
			return false;
		}
	}	
	
	public void loadUI(Order order) {		 
		if(order == null){
			Utils.showErrorDialogAndFinish(getActivity(), getString(R.string.respuesta_invalida));
			return;
		}
		
		Utils.setTextViewText(rootView, R.id.title, order.titulo);
		
		TextView txtTotalHoras = (TextView)rootView.findViewById(R.id.txttiempototal_orden);
		txtTotalHoras.setText(order.horastotales + "h " + order.minutostotales + "m");
		
		TextView txtTiempoReal = (TextView)rootView.findViewById(R.id.txtduracionreal_orden);
		txtTiempoReal.setText(order.horasreales + "h " + order.minutosreales + "m");
		
		TextView txtTiempoPausa = (TextView)rootView.findViewById(R.id.txttiempopausa_orden);
		txtTiempoPausa.setText(order.horasdescanso + "h " + order.minutosdescanso + "m");
		
		// Datos de la planta
        TextView txt_planta = (TextView)rootView.findViewById(R.id.txtplanta_orden);
        txt_planta.setText(order.planta);
        
        Utils.setTextViewText(rootView, R.id.txtpeticionario, order.peticionario);
        
        // txt_planta.setText(args.getCharArray("planta").toString()); Asi recogeriamos el valor en caso de venir como argumento dentro del Fragment.
        TextView txt_direccion = (TextView)rootView.findViewById(R.id.txtdireccion_orden);
        txt_direccion.setText(order.direccion);
        
        TextView txt_expediente = (TextView)rootView.findViewById(R.id.txtexpdte_orden);
        txt_expediente.setText(order.expediente);
        
        TextView txt_tipotrabajo = (TextView)rootView.findViewById(R.id.txttipotrabajo_orden);
        txt_tipotrabajo.setText(order.tipotrabajo);
        
        TextView txt_motor = (TextView)rootView.findViewById(R.id.txtmotor_orden);
        txt_motor.setText(order.motor);
        
        TextView txt_numserie= (TextView)rootView.findViewById(R.id.txtnumserie_orden);
        txt_numserie.setText(order.numserie);
        
        TextView txt_estado= (TextView)rootView.findViewById(R.id.txtestado_orden);
        txt_estado.setText(order.estado);
		
        TextView txt_fecha = (TextView)rootView.findViewById(R.id.txtfecha);
        txt_fecha.setText(Utils.formatDateOnly(order.fechainicio));        
        
        rootView.refreshDrawableState();
	}
		
}
