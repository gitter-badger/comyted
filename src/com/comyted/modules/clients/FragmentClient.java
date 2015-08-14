package com.comyted.modules.clients;

import java.util.List;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.Utils;
import com.comyted.models.Client;
import com.enterlib.app.DefaultDataView;
import com.enterlib.app.PresentUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class FragmentClient extends Fragment {

	private ViewGroup rootView;	
	private ViewModelClient vm;
	private DataView view;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{					
		 rootView = (ViewGroup)inflater.inflate(R.layout.fragment_client, container, false);		 
		 return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
	
		ActivityClient activity = (ActivityClient)getActivity();
		view = new DataView(getActivity());
		
		vm = activity.getViewModel();
		vm.setView(view);
		
		/*
		 * Temporal Solucion
		 * La solucion definitiva sera
		 * Preguntar al ViewModel si los datos estan disponible. En caso positivo
		 * actualizar la vista sino se un EventHandler al Evento que se lanzara cuando
		 * los datos del ViewModel esten disponibles 
		 * */
		if(vm.getClient()!=null){
			//Si los datos estan disponibles se actualiza la vista
			view.onDataLoaded();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//activate the view and associate it with the activity view model
		//this view delegates the loading to the activity's view model
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
					"No se puede editar", null);
		}else{
			Client client = vm.getClient();			
			Intent intent = new Intent(getActivity(), ActivityEditClient.class)
					 			.putExtra(Constants.CLIENT, client);
			
			startActivityForResult(intent, Constants.EDIT_CLIENT);
		}
		
	}	
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(resultCode == Constants.CLIENT_MODIFIED){			
			//indica a la actividad que el cliente se modifico
			getActivity().setResult(resultCode);
			vm.load();
		}
	}
	
	class DataView extends DefaultDataView<Activity>{

		public DataView(Activity activity) {
			super(activity);		
		}

		@Override
		public void onDataLoaded() {
			Client c = vm.getClient();	
			if(c == null){
				PresentUtils.showMessage(getActivity(), "no hay cliente");
				return;
			}
				
			setText(R.id.client_nombreempresa, c.nombreempresa);
			setText(R.id.client_id, String.valueOf(c.id));
			setText(R.id.client_codigo, c.codigo);
			setText(R.id.client_cif, c.cif);
			setText(R.id.client_direccion, c.direccion);
			setText(R.id.client_ciudad, c.ciudad);
			setText(R.id.client_provincia, c.provincia);
			setText(R.id.client_codpos, c.codpos);
			setText(R.id.client_pais, c.pais);
			setText(R.id.client_telefono, c.telefono);
			setText(R.id.client_fax, c.fax);
			setText(R.id.client_email, c.email);
			
			//show map
			
			GoogleMap map = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			if(map != null){
				List<Address>adress = vm.getAdresses();			

				if(adress.size() > 0){
					Address addr = adress.get(0);
					MarkerOptions marker = new MarkerOptions()
					.position(new LatLng(addr.getLatitude(), addr.getLongitude()))
					.title(c.direccion);			

					map.addMarker(marker);
				}
				else{
					map.clear();
				}				
			}
			
			showNotifications();						
		}

		private void showNotifications() {
			List<String>notifications = vm.getNotifications();
			if(notifications.size() > 0){
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < notifications.size(); i++) {
					sb.append(notifications.get(i));
					sb.append('\n');
				}
				Utils.showAlertDialog(getActivity(), getString(R.string.aviso), sb.toString(),null);
			}
		}
		
		private void setText(int id, String text){
			PresentUtils.setTextViewText(rootView, id, text);
		}
	}
	
}
