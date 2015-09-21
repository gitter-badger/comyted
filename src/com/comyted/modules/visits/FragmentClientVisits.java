package com.comyted.modules.visits;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.conectivity.GetVisitasClient;
import com.comyted.generics.DefaultComparator;
import com.comyted.generics.ListAdapter;
import com.comyted.generics.ListFragment;
import com.comyted.models.ContactVisit;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

import android.content.Intent;

public class FragmentClientVisits extends ListFragment<ContactVisit> {
		
	private int clientId;

	public FragmentClientVisits(){
		setComparator(new DefaultComparator<ContactVisit>(){
			@Override
			public int compare(ContactVisit lhs, ContactVisit rhs) {
				return lhs.fechavisita.compareTo(rhs.fechavisita) * Order;
			}
		});
	}			

	@Override
	protected ICollectionRepository<ContactVisit> createRepository() {		
		clientId =getActivity().getIntent().getIntExtra(Constants.CLIENT_ID, 0);
		return new ICollectionRepository<ContactVisit>() {
			GetVisitasClient client = new GetVisitasClient();		
			@Override
			public ContactVisit[] getItems() throws InvalidOperationException {
				return client.ObtenerVisitasUsuarioCliente(clientId, 
						MainApp.getCurrentUser().id);
			}
		};
	}

	@Override
	protected ListAdapter<ContactVisit> createAdapter(ContactVisit[] items) {
		return new AdapterContactVisit(getActivity(), items);
	}

	@Override
	protected Intent getItemViewIntent(ContactVisit c) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String getNoItemsAlertMessage() {		
		return getString(R.string.vd_no_ha_realizado_visitas_al_cliente_seleccionado);
	}
	
	@Override
	protected String getFilterHint() {		
		return getString(R.string.buscar_por_responsable);
	}
}
