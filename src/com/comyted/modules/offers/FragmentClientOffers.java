package com.comyted.modules.offers;

import java.util.Comparator;

import com.comyted.Constants;
import com.comyted.SupportListFragment;
import com.comyted.models.ClientOffert;
import com.comyted.repository.IOffertRepository;
import com.comyted.repository.OffertRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

public class FragmentClientOffers extends SupportListFragment {
	
	ClientOffertComparator comparator = new ClientOffertComparator();
	AdapterClientOffers adapter;
	ViewModelClientOffers viewModel;
	
	public static class ClientOffertComparator implements Comparator<ClientOffert>{
		public int Order = 1;// 1=Ascending -1=Descending		
		@Override
		public int compare(ClientOffert lhs, ClientOffert rhs) {						
			return lhs.fechapropuesta.compareTo(rhs.fechapropuesta) * Order;
		}
	}
	@Override
	public void onDataLoaded() {
		ClientOffert[] items = viewModel.getOfferts();		 
		 if(items == null || items.length ==0){
			 adapter = null;
			 setAdapter(null);
			 return;
		 }	 
		 adapter = new AdapterClientOffers(getActivity(), items);
		 setAdapter(adapter);				
	}

	@Override
	protected void sortItems(int sortOrder) {
		if(adapter == null)
			return;
			
		comparator.Order = sortOrder;
		adapter.sort(comparator);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected DataViewModel createViewModel() {		
		IOffertRepository repository = new OffertRepository();
		viewModel = new ViewModelClientOffers(this, repository, getActivity()
				.getIntent()
				.getIntExtra(Constants.CLIENT_ID, 0));
		return viewModel;
	}
	
}
