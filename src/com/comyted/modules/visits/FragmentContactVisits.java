package com.comyted.modules.visits;

import junit.framework.Assert;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.ListFragment.MenuValue;
import com.comyted.conectivity.GetVisitasClient;
import com.comyted.generics.DefaultComparator;
import com.comyted.generics.ListAdapter;
import com.comyted.generics.ListFragment;
import com.comyted.models.ContactVisit;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class FragmentContactVisits extends ListFragment<ContactVisit> {
	
	public FragmentContactVisits() {
		setComparator(new DefaultComparator<ContactVisit>(){
			@Override
			public int compare(ContactVisit lhs, ContactVisit rhs) {				
				return lhs.fechavisita.compareTo(rhs.fechavisita)*Order;
			}
		});
	}
	

	@Override
	protected ICollectionRepository<ContactVisit> createRepository() {
		final int contactId = getActivity().getIntent().getIntExtra(Constants.ID, 0);		
		Assert.assertTrue(contactId > 0);
		
		return new ICollectionRepository<ContactVisit>() {
			@Override
			public ContactVisit[] getItems() throws InvalidOperationException {
				GetVisitasClient get = new GetVisitasClient();
				return get.ObtenerVisitasUsuarioContacto(contactId, MainApp.getCurrentUser().id);
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
		return getString(R.string.vd_no_ha_realizdo_visistas_al_contacto_seleccionado);
	}
	
	@Override
	protected String getFilterHint() {		
		return getString(R.string.buscar_por_responsable);
	}
	
	  @Override
	   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {	
		   inflater.inflate(R.menu.list_visits, menu);
		   setMenu(menu);
	   }
	
	@Override	
	protected MenuValue getMenuValue(int sortOrder) {
		return sortOrder == 0?  new MenuValue(R.drawable.ic_action_sort_1, R.string.ascendent):
				sortOrder == 1 ? 
				new MenuValue(R.drawable.ic_menu_download,R.string.ascendent):
				new MenuValue(R.drawable.ic_menu_upload, R.string.descendent);
	}

}
