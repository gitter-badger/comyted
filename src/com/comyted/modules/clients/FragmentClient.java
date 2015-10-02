package com.comyted.modules.clients;

import java.util.List;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.MailDialogFragment;
import com.comyted.R;
import com.comyted.Utils;
import com.comyted.activities.ActivityMap;
import com.comyted.models.Client;
import com.comyted.models.Contact;
import com.comyted.models.MailMessage;
import com.enterlib.StringUtils;
import com.enterlib.app.DefaultDataView;
import com.enterlib.app.PresentUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentClient extends Fragment {

	private ViewGroup rootView;	
	private ViewModelClient vm;
	private DataView view;
	FrameLayout frame;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{					
		 rootView = (ViewGroup)inflater.inflate(R.layout.fragment_client, container, false);	
		 TextView lbPhone = (TextView) rootView.findViewById(R.id.lbContact_telefono);
		 lbPhone.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				callContact();
				
			}
		});
		 
		 TextView lbEmail = (TextView) rootView.findViewById(R.id.lb_contact_email);		 
		 lbEmail.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				sendMail();
			}
		});
		 
		 frame = (FrameLayout) rootView.findViewById(R.id.framelayout);
		 ImageView mapView = (ImageView) frame.findViewById(R.id.googleMapImage);
		 mapView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Client client = vm.getClient();
				if(client == null){
					PresentUtils.showMessage(getActivity(), getActivity().getString(
							R.string.no_se_ha_cargado_ningun_cliente));
					return;
				}
				if(StringUtils.isNullOrWhitespace(client.direccion)){
					PresentUtils.showMessage(getActivity(), getActivity().getString(
							R.string.el_cliente_no_posee_direcci_n));
					return;
				}
				
				Intent intent = new Intent(getActivity(), ActivityMap.class);
				intent.putExtra(Constants.ADDRESS, client.direccion);
				startActivity(intent);				
			}
		});
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
		if(vm.isLoaded()){
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
		inflater.inflate(R.menu.fragment_client, menu);		
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
		case R.id.call:
			callContact();
			return true;
		case R.id.send_mail:
			sendMail();
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
	
	private String getViewText(int id){
		TextView tv = (TextView) rootView.findViewById(id);
		return tv.getText().toString();
	}
	
	private void setText(int id, String text){
		PresentUtils.setTextViewText(rootView, id, text);
	}
	
	private void callContact(){
		//String phoneNumber= getViewText(R.id.client_telefono);
		Client client = vm.getClient();
		String phoneNumber = client.telefono;
		if(StringUtils.isNullOrWhitespace(phoneNumber)){
			PresentUtils.showMessage(getActivity(), "El cliente no tiene teléfono");
			return;
		}
		
		Intent i = new Intent(android.content.Intent.ACTION_DIAL, 
								Uri.parse("tel:+"+ phoneNumber)); 
		startActivity(i);
	}


	private void sendMail() {
		Client client = vm.getClient();
		if(client == null){
			PresentUtils.showMessage(getActivity(), getActivity().getString(
					R.string.espere_mientras_se_cargan_los_datos));
			return;
		}
		
		MailMessage message=new MailMessage();
		message.Sender = MainApp.getCurrentUser().email;
		message.Receiver =client.email;
		
		MailDialogFragment frag = MailDialogFragment.newInstance(message);
		frag.show(getFragmentManager(), "com.comyted.MailDialogFragment");
	}

	
	class DataView extends DefaultDataView<Activity> implements IClientView{

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
//			GoogleMap map = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.googleMap)).getMap();
//			if(map != null){
//				List<Address>adress = vm.getAdresses();			
//
//				if(adress.size() > 0){					
//					Address addr = adress.get(0);
//					
//					LatLng location = new LatLng(addr.getLatitude(), addr.getLongitude());
//					MarkerOptions marker = new MarkerOptions()
//					.position(location)
//					.title(c.direccion);			
//					
//					//adds a marker to the location
//					map.addMarker(marker);
//					
//					//move the camera to the location					
//					CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
//					map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));	
//				}
//				else{
//					map.clear();
//				}				
//			}
			
			vm.loadMapAsync();
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

		@Override
		public void BeginDownloadMap() {
			ProgressBar pbar = (ProgressBar) frame.findViewById(R.id.googleMapProgressBar);
			pbar.setVisibility(View.VISIBLE);			
			
			ImageView mapView = (ImageView) frame.findViewById(R.id.googleMapImage);
			mapView.setVisibility(View.INVISIBLE);
			mapView.setEnabled(false);
		}

		@Override
		public void EndDownLoadMap(Bitmap map) {
			ProgressBar pbar = (ProgressBar) frame.findViewById(R.id.googleMapProgressBar);
			pbar.setVisibility(View.INVISIBLE);			
			
			ImageView mapView = (ImageView) frame.findViewById(R.id.googleMapImage);
			mapView.setImageBitmap(map);						
			mapView.setVisibility(View.VISIBLE);			
			mapView.setEnabled(map != null);
			
		}				
		
	}
	
}
