package com.comyted.modules.contacts;

import junit.framework.Assert;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.conectivity.GetContactosClient;
import com.comyted.generics.DefaultComparator;
import com.comyted.generics.ListAdapter;
import com.comyted.generics.ListFragment;
import com.comyted.models.ClientContactSummary;
import com.enterlib.app.UIUtils;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class FragmentClientContacts extends ListFragment<ClientContactSummary> {	
	
	public FragmentClientContacts(){
		setComparator(new DefaultComparator<ClientContactSummary>(){
			@Override
			public int compare(ClientContactSummary lhs, ClientContactSummary rhs) {				
				return lhs.nombrecontacto.compareToIgnoreCase(rhs.nombrecontacto) * Order;
			}
		});		
	}
		
	private int clientId;		
		
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		
		inflater.inflate(R.menu.fragment_client_contacts, menu);
		setMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		if(super.onOptionsItemSelected(item)){
			return true;
		}
		
		int id = item.getItemId();	
		switch (id) {		
			case R.id.add:									
				startActivityForResult(new Intent(getActivity(), ActivityEditContact.class)
				.putExtra(Constants.CLIENT_ID, clientId), Constants.EDIT);
				return true;
		}
		return false;
	}	

	@Override
	protected ICollectionRepository<ClientContactSummary> createRepository() {
		clientId = getActivity().getIntent().getIntExtra(Constants.CLIENT_ID, 0);
		Assert.assertTrue(clientId > 0);
		
		return new ICollectionRepository<ClientContactSummary>() {
			GetContactosClient client = new GetContactosClient();
			@Override
			public ClientContactSummary[] getItems() throws InvalidOperationException {
				 return client.ObtenerContactosCliente(clientId);
			}
		};
	}

	@Override
	protected ListAdapter<ClientContactSummary> createAdapter(
			ClientContactSummary[] items) {
		return new ListAdapter<ClientContactSummary>(getActivity(),R.layout.adapter_client_contacts, items) {			
			@Override
			protected void updateView(View view, ClientContactSummary item, int position) {
				UIUtils.setTextViewTextOrCollapse(view, R.id.contact_nombrecontacto, item.nombrecontacto);
				UIUtils.setTextViewTextOrCollapse(view, R.id.contact_puesto, item.puesto);
				UIUtils.setTextViewTextOrCollapse(view, R.id.contact_departamento, item.departamento);
				UIUtils.setTextViewTextOrCollapse(view, R.id.contact_telefono, item.telefono);
				UIUtils.setTextViewTextOrCollapse(view, R.id.contact_fax, item.fax);
				UIUtils.setTextViewTextOrCollapse(view, R.id.contact_email, item.email);				
			}
		};
	}

	@Override
	protected Intent getItemViewIntent(ClientContactSummary c) {
		return new Intent(getActivity(), ActivityContact.class)
					.putExtra(Constants.ID, c.id)
					.putExtra(Constants.CLIENT_ID, clientId);		
	}

	@Override
	protected String getNoItemsAlertMessage() {
		return getString(R.string.el_cliente_seleccionado_no_posee_contactos);
	}
	
	@Override
	protected String getFilterHint() {		
		return getString(R.string.client_search);
	}
}
