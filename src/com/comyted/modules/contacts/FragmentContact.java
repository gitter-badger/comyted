package com.comyted.modules.contacts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.MapView;
import com.comyted.MialDialogFragment;
import com.comyted.R;
import com.comyted.RefreshableFragment;
import com.comyted.activities.ActivityMap;
import com.comyted.models.Contact;
import com.comyted.models.MailMessage;
import com.comyted.repository.ContactsRepository;
import com.enterlib.StringUtils;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.PresentUtils;
import com.enterlib.app.RepositoryViewModel;
import com.enterlib.conetivity.RestClient;
import com.enterlib.threading.AsyncNotifyTask;
import com.enterlib.threading.IAsyncCallback;

public class FragmentContact extends RefreshableFragment 
							 implements OnClickListener {
	private ViewGroup rootView;
	private FrameLayout frame;
	private RepositoryViewModel<Contact> viewModel;
	protected Bitmap adressMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 rootView = (ViewGroup)inflater.inflate(R.layout.fragment_contact, container, false);
		 TextView lbPhone = (TextView) rootView.findViewById(R.id.lbContact_telefono);
		 lbPhone.setOnClickListener(this);
		 TextView lbEmail = (TextView) rootView.findViewById(R.id.contact_email);
		 
		 lbEmail.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Contact entity =viewModel.getEntity();
				if(entity == null){
					PresentUtils.showMessage(getActivity(), "Espere a que se cargen los datos");
					return;
				}
				
				MailMessage message=new MailMessage();
				message.Sender = MainApp.getCurrentUser().email;
				message.Receiver =entity.email;
				
				MialDialogFragment frag = MialDialogFragment.newInstance(message);
				frag.show(getFragmentManager(), "com.comyted.MailDialogFragment");
			}
		});
		 
//		 frame = (FrameLayout) rootView.findViewById(R.id.framelayout);
//		 ImageView mapView = (ImageView) frame.findViewById(R.id.googleMapImage);
//		 mapView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Contact entity = viewModel.getEntity();
//				if(entity == null){
//					PresentUtils.showMessage(getActivity(), "No hay ningun contacto cargado");
//					return;
//				}
//				if(StringUtils.isNullOrWhitespace(entity.direccion)){
//					PresentUtils.showMessage(getActivity(), "El Contacto no posee dirección");
//					return;
//				}
//				
//				Intent intent = new Intent(getActivity(), ActivityMap.class);
//				intent.putExtra(Constants.ADDRESS, entity.direccion);
//				startActivity(intent);				
//			}
//		});
		 return rootView;
	}
	
	@Override
	public void onDataLoaded() {
		Contact c = viewModel.getEntity();	
		if(c == null){
			PresentUtils.showMessage(getActivity(), "no hay cliente");
			return;
		}
			
		setText(R.id.contact_id, String.valueOf(c.id));
		setText(R.id.contact_nombrecontacto, c.nombrecontacto);
		setText(R.id.contact_nombrecliente, c.nombreempresa);
		setText(R.id.contact_puesto, c.puesto);
		setText(R.id.contact_departamento, c.dpto);
		setText(R.id.contact_direccion, c.direccion);
		setText(R.id.contact_ciudad, c.ciudad);
		setText(R.id.contact_provincia, c.provincia);
		setText(R.id.contact_codpos, c.codpos);
		setText(R.id.contact_pais, c.pais);
		setText(R.id.contact_telefono, c.telefono);
		setText(R.id.contact_fax, c.fax);
		setText(R.id.contact_email, c.email);
		setText(R.id.contact_observations, c.observaciones);
		
		MapView frame = (MapView) rootView.findViewById(R.id.framelayout);
		frame.loadMapAsync(c.direccion);
		
	}

	@Override
	protected DataViewModel createViewModel() {
		viewModel = new RepositoryViewModel<Contact>(this, 
				getActivity().getIntent().getIntExtra(Constants.ID, 0) ,
				new ContactsRepository());
		return viewModel;
	}


	@Override
	public void onClick(View v) {
		String phoneNumber= getViewText(R.id.contact_telefono);
		if(StringUtils.isNullOrWhitespace(phoneNumber)){
			PresentUtils.showMessage(getActivity(), "El cliente no tiene teléfono");
			return;
		}
		
		Intent i = new Intent(android.content.Intent.ACTION_DIAL, 
								Uri.parse("tel:+"+ phoneNumber)); 
		startActivity(i);
	}
	
	private String getViewText(int id){
		TextView tv = (TextView) rootView.findViewById(id);
		return tv.getText().toString();
	}
	private void setText(int id, String text){
		PresentUtils.setTextViewText(rootView, id, text);
	}

//	public void loadMapAsync(final Contact contact){		
//		if(!isValid())
//			return;
//				
//		BeginDownloadMap();
//		
//		ImageView mapView = (ImageView) frame.findViewById(R.id.googleMapImage);
//		int width = mapView.getWidth();
//		int height = mapView.getHeight();
//		String size = width+"x"+height;
//		final String url;
//		try {
//			url = "http://maps.googleapis.com/maps/api/staticmap?size="+size+"&markers=size:mid|color:red|" 
//					+ URLEncoder.encode(contact.direccion, "UTF-8") + "&zoom=15&sensor=false";
//			
//		} catch (UnsupportedEncodingException e) {			
//			e.printStackTrace();
//			onFailure(e);
//			return;
//		}
//		
//		new AsyncNotifyTask(this) {			
//			@Override
//			protected void doInBackground() throws Exception {												
//				adressMap = RestClient.downloadImage2(url);
//				
//			}
//		}.run();
//	}
//
//
//	@Override
//	public void operationCompleted(Exception e) {			
//		if(!isValid())
//			return;
//		
//		if(e!=null){
//			onFailure(e);
//			EndDownLoadMap(null);
//			return;
//		}
//	
//		EndDownLoadMap(adressMap);		
//	}
//	
//	
//	public void BeginDownloadMap() {
//		ProgressBar pbar = (ProgressBar) frame.findViewById(R.id.googleMapProgressBar);
//		pbar.setVisibility(View.VISIBLE);			
//		
//		ImageView mapView = (ImageView) frame.findViewById(R.id.googleMapImage);
//		mapView.setVisibility(View.INVISIBLE);
//		mapView.setEnabled(false);
//	}
//
//
//	public void EndDownLoadMap(Bitmap map) {
//		ProgressBar pbar = (ProgressBar) frame.findViewById(R.id.googleMapProgressBar);
//		pbar.setVisibility(View.INVISIBLE);			
//		
//		ImageView mapView = (ImageView) frame.findViewById(R.id.googleMapImage);
//		mapView.setImageBitmap(map);						
//		mapView.setVisibility(View.VISIBLE);			
//		mapView.setEnabled(map != null);
//		
//	}				

}
