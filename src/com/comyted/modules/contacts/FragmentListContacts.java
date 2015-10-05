package com.comyted.modules.contacts;

import java.util.Comparator;

import junit.framework.Assert;

import com.comyted.Constants;
import com.comyted.ListFragment;
import com.comyted.R;
import com.comyted.models.ContactSummary;
import com.comyted.repository.ContactsRepository;
import com.enterlib.mvvm.CollectionViewModel;
import com.enterlib.mvvm.DataViewModel;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class FragmentListContacts extends ListFragment {

	private CollectionViewModel<ContactSummary> viewModel;
	private ItemComparator comparator;
	private AdapterContacts adapter;

	@Override
	protected void sortItems(int sortOrder) {
		if(comparator == null){
			comparator = new ItemComparator();
		}
		if(adapter == null)
			return;
		
		comparator.Order = sortOrder;
		adapter.sort(comparator);
	}	

	@Override
	public void onDataLoaded() {
				
		 ContactSummary[] contacts = viewModel.getItems();
		 
		 if(contacts == null || contacts.length ==0){
			 adapter = null;
			 setAdapter(null);
			 return;
		 }	 
		 adapter = new AdapterContacts(getActivity(), contacts);
		 setAdapter(adapter);
	}	
	
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
				startActivityForResult(new Intent(getActivity(), ActivityEditContact.class), Constants.EDIT);
				return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ContactSummary c = (ContactSummary) parent.getItemAtPosition(position);
		Intent intent = new Intent(getActivity(), ActivityContact.class)
		.putExtra(Constants.ID, c.id);		
		startActivityForResult(intent, Constants.EDIT);
	}
		

	@Override
	protected DataViewModel createViewModel() {
		viewModel = new CollectionViewModel<ContactSummary>(this,new ContactsRepository());
		return viewModel;
	}
	
	public static class ItemComparator implements Comparator<ContactSummary>{
		public int Order = 1;// 1=Ascending -1=Descending
		
		@Override
		public int compare(ContactSummary lhs, ContactSummary rhs) {
			Assert.assertNotNull(lhs);
			Assert.assertNotNull(lhs.nombreempresa);
			Assert.assertNotNull(rhs);
			Assert.assertNotNull(rhs.nombreempresa);
			
			return lhs.nombreempresa.compareToIgnoreCase(rhs.nombreempresa) * Order;
		}
	}
	
	@Override
	protected String getFilterHint() {
		return getString(R.string.buscar_por_cliente);
	}

}
