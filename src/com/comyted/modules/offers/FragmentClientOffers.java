package com.comyted.modules.offers;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.conectivity.GetOfertasClient;
import com.comyted.generics.DefaultComparator;
import com.comyted.generics.ListAdapter;
import com.comyted.generics.ListFragment;
import com.comyted.models.ClientOffert;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

import android.content.Intent;

public class FragmentClientOffers extends ListFragment<ClientOffert> {
			
	private int clientId;
	
	public FragmentClientOffers() {
		setComparator(new DefaultComparator<ClientOffert>(){
			@Override
			public int compare(ClientOffert lhs, ClientOffert rhs) {						
				return lhs.fechapropuesta.compareTo(rhs.fechapropuesta) * Order;
			}
		});		
	}
	

	@Override
	protected ICollectionRepository<ClientOffert> createRepository() {
		clientId = getActivity() .getIntent().getIntExtra(Constants.CLIENT_ID, 0);
		
		return new ICollectionRepository<ClientOffert>() {
			GetOfertasClient client = new GetOfertasClient();
			@Override
			public ClientOffert[] getItems() throws InvalidOperationException {
				return client.ObtenerOfertasCliente(clientId);
			}
		};
	}

	@Override
	protected ListAdapter<ClientOffert> createAdapter(ClientOffert[] items) {
		return new AdapterClientOffers(getActivity(), items);
	}

	@Override
	protected Intent getItemViewIntent(ClientOffert c) {
		// TODO Auto-generated method stub
		return null;
	}	
	
	@Override
	protected String getNoItemsAlertMessage() {
		return getString(R.string.el_cliente_seleccionado_no_posee_ofertas);
	}
	
	@Override
	protected String getFilterHint() {	
		return getString(R.string.buscar_por_nombre_de_proyecto);
	}
}
