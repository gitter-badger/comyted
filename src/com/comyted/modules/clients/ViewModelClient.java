package com.comyted.modules.clients;

import java.util.ArrayList;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.comyted.conectivity.GetClientesClient;
import com.comyted.models.AppUser;
import com.comyted.models.Client;
import com.comyted.repository.ClientRepository;
import com.comyted.repository.IClientRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

public class ViewModelClient extends DataViewModel<IDataView> {
	
	private int clientId;
	private Client client;
	private IClientRepository repository;
	Geocoder geocoder;
	List<Address> adresses = new ArrayList<Address>();
	List<String>notifications = new ArrayList<String>();

	/**Constructor for Tests. Allows the injection a Stub Repository*/
	public ViewModelClient(int clientId, IDataView view, IClientRepository repository, Geocoder geocoder) {
		super(view);
		
		this.clientId = clientId;
		this.repository = repository;
		this.geocoder = geocoder;
	}
	
	/**Constructor for Production*/
	public ViewModelClient(int clientId, IDataView view, Geocoder geocoder){
		this(clientId, view, new ClientRepository(new GetClientesClient(), null), geocoder);
	}

	@Override
	protected boolean loadAsync() throws Exception {
		notifications.clear();
		client = repository.getClient(clientId);		
		try{
			if(Geocoder.isPresent()){
				//get the latitud and longitud from the adress		
				adresses = geocoder.getFromLocationName(client.direccion, 1);
			}else{
				adresses = new ArrayList<Address>();
				notifications.add("No se pudo determinar las coordenadas del cliente");
			}
		}
		catch(Exception e){
			notifications.add("No se pudo determinar las coordenadas de la dirección del cliente");
			adresses = new ArrayList<Address>();
			Log.d("ViewModelClient", e.getMessage(), e);
		}
		
		return true;
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
