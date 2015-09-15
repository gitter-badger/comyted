package com.comyted.modules.visits;

import java.util.Comparator;

import com.comyted.Constants;
import com.comyted.SupportListFragment;
import com.comyted.models.ClientVisit;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

public class FragmentClientVisits extends SupportListFragment {

	public static class ClientVisitComparator implements Comparator<ClientVisit>{
		public int Order = 1;// 1=Ascending -1=Descending		
		@Override
		public int compare(ClientVisit lhs, ClientVisit rhs) {						
			return lhs.fechavisita.compareTo(rhs.fechavisita) * Order;
		}
	}
	
	ClientVisitComparator comparator = new ClientVisitComparator();
	AdapterClientVisits adapter;
	ViewModelClientVisits viewModel;
	
	
	@Override
	public void onDataLoaded() {		
		 ClientVisit[] visits = viewModel.getVisits();
		 
		 if(visits == null || visits.length ==0){
			 adapter = null;
			 setAdapter(null);
			 return;
		 }	 
		 adapter = new AdapterClientVisits(getActivity(), visits);
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
		viewModel = new ViewModelClientVisits(getActivity()
				.getIntent().getIntExtra(Constants.CLIENT_ID, 0), 
				this);
		return viewModel;
	}	
}
