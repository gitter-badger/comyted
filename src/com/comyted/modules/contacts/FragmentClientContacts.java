package com.comyted.modules.contacts;

import java.util.Comparator;

import junit.framework.Assert;

import com.comyted.Constants;
import com.comyted.SupportListFragment;
import com.comyted.models.ClientContactSummary;
import com.comyted.repository.ContactsRepository;
import com.comyted.repository.IContactsRepository;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

import android.support.v4.app.FragmentActivity;
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
	
	@Override
	protected void updateUI() {
		
		 ViewModelClientContacts viewModel = (ViewModelClientContacts) getViewModel();
		 ClientContactSummary[] contacts = viewModel.getContacts();
		 
		 if(contacts == null || contacts.length ==0){
			 adapter = null;
			 setAdapter(null);
			 return;
		 }	 
		 adapter = new AdapterContacts(getActivity(), contacts);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub		
	}

	@Override
	protected DataViewModel createViewModel(IDataView view, FragmentActivity activity) {		
		IContactsRepository contactRepository = new ContactsRepository();
		int clientId = activity.getIntent().getIntExtra(Constants.CLIENT_ID, 0);
		return new ViewModelClientContacts(view, contactRepository, clientId);
	}

	
}
