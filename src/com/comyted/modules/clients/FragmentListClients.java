package com.comyted.modules.clients;

import java.util.Comparator;

import junit.framework.Assert;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.Utils;
import com.comyted.conectivity.GetClientesClient;
import com.comyted.models.ClientSummary;
import com.comyted.repository.ClientRepository;
import com.comyted.repository.IClientRepository;
import com.comyted.testing.repository.LocalJSONClientRepository;
import com.enterlib.app.DefaultDataView;
import com.enterlib.widgets.FilterDialog;

public class FragmentListClients extends android.support.v4.app.Fragment{

	private static final String SELECTION = "SELECTION";
	private View rootView;
	private DataView view;
	private ViewModelListClients vm;
	private int selectedPosition;
	private Menu menu;	

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
				
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) { 				
        rootView = inflater.inflate(R.layout.fragment_list_clients, container, false);                                             
        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
				
		view = new DataView(getActivity());
		
		IClientRepository repo;
		if(MainApp.TEST){
			//repositorio local de prueba
			repo = new LocalJSONClientRepository(getActivity());			
		}
		else{		
			//repositorio en servicios
			//solo se necesita hacer GET en esta actividad
			repo = new ClientRepository(new GetClientesClient(), null);
		}
		
		if(savedInstanceState!=null){
			selectedPosition = savedInstanceState.getInt(SELECTION);
		}
		
		vm = new ViewModelListClients(view, repo);
	}
			
	@Override
	public void onResume() {
		super.onResume();
		
		//activar la vista
		view.setIsValid(true);
		
		//cargar los datos
		vm.load();
	}
	
	@Override
	public void onStop() {
		//desabilitar la vista
		view.setIsValid(false);
		
		super.onStop();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.menu = menu;
		inflater.inflate(R.menu.fragment_list_client, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {		
			case R.id.refresh:
				vm.load();
				return true;
			case R.id.sort_az:				
				view.sortByName();
				if(view.comparator.Order == 1){
					item.setIcon(R.drawable.ic_menu_download);
					item.setTitle(R.string.a_z);
				}
				else{					
					item.setIcon(R.drawable.ic_menu_upload);
					item.setTitle(R.string.z_a);
				}					
				return true;
//			case R.id.sort_za:
//				view.sortByName(-1);
//				return true;				
			case R.id.filter:
				view.showSearchDialog();
				return true;				
		}
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Constants.CLIENT_MODIFIED){
			//si el cliente fue actualizado entonces se actualiza la lista y 
			//se marca como seleccionado el que estaba seleccionado anteriormente
			vm.load();			
		}    		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		
		outState.putInt(SELECTION, selectedPosition);
	}
	
	class DataView extends DefaultDataView<Activity> implements OnItemClickListener{
		
		private AdapterClients adapter;
		public ClientSummaryComparator comparator = new ClientSummaryComparator();
		private FilterDialog mDialog;
		
		public DataView(Activity activity) {		
			super(activity);			
		}

		@Override
		public void onDataLoaded() {
			ClientSummary []clients = vm.getClients();
			
			ListView listView = (ListView)rootView.findViewById(R.id.clients_listView);
			
			if(clients==null || clients.length == 0){
				Utils.showMessage(getActivity(), getActivity().getString(R.string.no_hay_clientes_registrados));
				adapter = null;
				listView.setAdapter(null);		
				listView.refreshDrawableState();
				return;
			}
								
			adapter = new AdapterClients(getActivity(), vm.getClients());	
			
			
			listView.setAdapter(adapter);
			
			//check that the previus selected position is valid
			if(selectedPosition >= clients.length)
				selectedPosition = 0;
			
			//set the previus selected position
			listView.setSelection(selectedPosition);
			
			listView.setOnItemClickListener(this);
			listView.refreshDrawableState();
			
			if(menu!=null){
				MenuItem menuItem = menu.findItem(R.id.sort_az);
				menuItem.setIcon(R.drawable.ic_menu_sort_alphabetically);
				menuItem.setTitle(R.string.a_z);
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				
			if(position < 0 || vm.getClients() == null) 
				return;
			
			selectedPosition = position;
			ClientSummary s = (ClientSummary)adapter.getItem(position);				
			if(s != null){
				Activity activity = getActivity();
				Intent  i = new Intent(activity, ActivityClient.class);
				i.putExtra(Constants.CLIENT_ID, s.id);
				i.putExtra(Constants.CLIENT, s);
				startActivity(i);
			}
			
		}
		
		public void sortByName(int order){
			if(adapter == null)
				return;
			
			comparator.Order =order;
			adapter.sort(comparator);
		}
		
		public void sortByName(){
			adapter.sort(comparator);
			comparator.Order = -comparator.Order;
		}
		
		public void showSearchDialog(){
			mDialog = new FilterDialog(getActivity(), adapter, selectedPosition);			
			
			mDialog.setOnSelectedItemlistener(new FilterDialog.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent,
						View itemView, int position, Object item) {
					onItemClick(parent, itemView, position, position);
					
				}
							
			});
			mDialog.setFilterCriteria(R.string.client_search);
			mDialog.show();
		}
	}
	
	public static class ClientSummaryComparator implements Comparator<ClientSummary>{
		public int Order = 1;// 1=Ascending -1=Descending
		
		@Override
		public int compare(ClientSummary lhs, ClientSummary rhs) {
			Assert.assertNotNull(lhs);
			Assert.assertNotNull(lhs.nombreempresa);
			Assert.assertNotNull(rhs);
			Assert.assertNotNull(rhs.nombreempresa);
			
			return lhs.nombreempresa.compareToIgnoreCase(rhs.nombreempresa) * Order;
		}
	}
	
}
