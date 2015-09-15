package com.comyted.modules.clients;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import com.comyted.conectivity.GetClientesClient;
import com.comyted.models.AppUser;
import com.comyted.models.Client;
import com.comyted.repository.ClientRepository;
import com.comyted.repository.IClientRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.conetivity.RestClient;
import com.enterlib.threading.AsyncNotifyTask;
import com.enterlib.threading.IAsyncCallback;

public class ViewModelClient extends DataViewModel implements IAsyncCallback {
	
	private int clientId;
	private Client client;
	private IClientRepository repository;
	Geocoder geocoder;
	List<Address> adresses = new ArrayList<Address>();
	List<String>notifications = new ArrayList<String>();
	Bitmap adressMap;

	public Bitmap getAdressMap() {
		return adressMap;
	}

	/**Constructor for Tests. Allows the injection a Stub Repository*/
	public ViewModelClient(int clientId, IClientView view, IClientRepository repository, Geocoder geocoder) {
		super(view);
		
		this.clientId = clientId;
		this.repository = repository;
		this.geocoder = geocoder;
	}
	
	/**Constructor for Production*/
	public ViewModelClient(int clientId, IClientView view, Geocoder geocoder){
		this(clientId, view, new ClientRepository(new GetClientesClient(), null), geocoder);
	}

	@Override
	protected boolean loadAsync() throws Exception {
		notifications.clear();
		client = repository.getClient(clientId);
		
//		try{
//			if(Geocoder.isPresent()){
//				//get the latitud and longitud from the adress		
//				adresses = geocoder.getFromLocationName(client.direccion, 1);
//			}else{
//				adresses = new ArrayList<Address>();
//				notifications.add("No se pudo determinar las coordenadas del cliente");
//			}
//		}
//		catch(Exception e){
//			notifications.add("No se pudo determinar las coordenadas de la dirección del cliente");
//			adresses = new ArrayList<Address>();
//			Log.d("ViewModelClient", e.getMessage(), e);
//		}
				
		return true;
	}
	
	public void loadMapAsync(){
		IClientView view = (IClientView) getView();
		if(view!=null && view.isValid())
			view.BeginDownloadMap();
		
		new AsyncNotifyTask(this) {
			
			@Override
			protected void doInBackground() throws Exception {
//				String query = "size=320x240&markers=size:mid|color:red|" +
//						  		client.direccion+
//						  		"&zoom=15&sensor=false";
//				
//				query= URLEncoder.encode(query,"UTF-8");				
//				String url = "http://maps.googleapis.com/maps/api/staticmap?"+query;
				
				String url = "http://maps.googleapis.com/maps/api/staticmap?size=320x240&markers=size:mid|color:red|" 
								+ URLEncoder.encode(client.direccion, "UTF-8") + "&zoom=15&sensor=false";
				
				adressMap = RestClient.downloadImage2(url);
				
			}
		}.run();
	}

	@Override
	public void operationCompleted(Exception e) {
		IClientView view= (IClientView) getView();		
		if(view==null || !view.isValid())
			return;
		
		if(e!=null){
			view.onFailure(e);
			view.EndDownLoadMap(null);
			return;
		}
	
		view.EndDownLoadMap(adressMap);
	}
	
	public List<Address> getAdresses() {
		return adresses;
	}

	public List<String> getNotifications() {
		return notifications;
	}

	public Client getClient() {		
		return client;
	}

	public boolean canUserEdit(AppUser currentUser) {
		return true;
	}

	

}
