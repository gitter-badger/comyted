package com.comyted.modules.contacts;

import java.util.Comparator;

import junit.framework.Assert;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.SupportListFragment;
import com.comyted.models.ClientContactSummary;
import com.comyted.models.Contact;
import com.comyted.repository.ContactsRepository;
import com.comyted.repository.IContactsRepository;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class FragmentClientContacts extends SupportListFragment {

	public static class ContactSummaryComparator implements Comparator<ClientContactSummary>{
		public int Order = 1;// 1=Ascending -1=Descending
		
		@Override
		public int compare(ClientContactSummary lhs, ClientContactSummary rhs) {
			Assert.assertNotNull(lhs);
			Assert.assertNotNull(lhs.nombrecontacto);
			Assert.assertNotNull(rhs);
			Assert.assertNotNull(rhs.nombrecontacto);
			
			return lhs.nombrecontacto.compareToIgnoreCase(rhs.nombrecontacto) * Order;
		}
	}
	
	CollectionAdapter<ClientContactSummary> adapter;
	private ContactSummaryComparator comparator;
	private int clientId;	
	
	@Override
	public void onDataLoaded() {
		
		 ViewModelClientContacts viewModel = (ViewModelClientContacts) getViewModel();
		 ClientContactSummary[] contacts = viewModel.getContacts();
		 
		 if(contacts == null || contacts.length ==0){
			 adapter = null;
			 setAdapter(null);
			 return;
		 }	 
		 adapter = new AdapterClientContacts(getActivity(), contacts);
		 setAdapter(adapter);
	}

	@Override
	protected void sortItems(int sortOrder) {
		if(adapter == null)
			return;
		
		if(comparator == null){
			 comparator = new ContactSummaryComparator();
		}
		comparator.Order = sortOrder;
		adapter.sort(comparator);		
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
				startActivityForResult(new Intent(getActivity(), ActivityEditContact.class)
				.putExtra(Constants.CLIENT_ID, clientId), Constants.EDIT);
				return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ClientContactSummary c = (ClientContactSummary) parent.getItemAtPosition(position);
		Intent intent = new Intent(getActivity(), ActivityContact.class)
		.putExtra(Constants.ID, c.id)
		.putExtra(Constants.CLIENT_ID, clientId);		
		
		startActivityForResult(intent, Constants.EDIT);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(resultCode == Constants.MODIFIED)
			getViewModel().load();
	}

	@Override
	protected DataViewModel createViewModel() {		
		clientId = getActivity().getIntent().getIntExtra(Constants.CLIENT_ID, 0);
		IContactsRepository contactRepository = new ContactsRepository();		
		return new ViewModelClientContacts(this, contactRepository, clientId);
	}

	
}
