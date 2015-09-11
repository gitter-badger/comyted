package com.comyted;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.comyted.modules.clients.AdapterClients;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.DefaultDataView;
import com.enterlib.app.IDataView;
import com.enterlib.app.IFilterableAdapter;
import com.enterlib.widgets.FilterDialog;

public abstract class SupportListFragment extends SupportViewFragment
		implements OnItemClickListener {

	public class DataView extends DefaultDataView<Activity>  {		
		
		private FilterDialog mDialog;		
		
		public DataView(Activity activity) {
			super(activity);
			
		}

		@Override
		public  void onDataLoaded() {
			updateUI();			
		}
		
		public void showSearchDialog(){
			mDialog = new FilterDialog(getActivity(), adapter, getSelectedPosition());	
			
			mDialog.setOnSelectedItemlistener(new FilterDialog.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,
						View itemView, int position, Object item) {
					onItemClick(parent, itemView, position, position);					
				}
							
			});
			mDialog.show();
		}

	}

	private Menu menu;
	private int SortOrder = 1;
	private IFilterableAdapter adapter;
	private View rootView;
	private ListView listView;
	
	public String EmptyMessage = "No hay datos";
	
	
	protected Menu getMenu() {
		return menu;
	}

	protected IFilterableAdapter getAdapter() {
		return adapter;
	}

	protected void setAdapter(IFilterableAdapter adapter) {
		this.adapter = adapter;
		
		if(adapter ==null || adapter.getCount() == 0){
			Utils.showMessage(getActivity(), EmptyMessage);		
			listView.setAdapter(null);		
			listView.refreshDrawableState();
			return;
		}												
		
		listView.setAdapter(adapter);
		int elements = adapter.getCount();
		
		//check that the previus selected position is valid
		int selectedPosition = getSelectedPosition();
		if(selectedPosition >= elements)
			selectedPosition = 0;
		
		//set the previus selected position
		listView.setSelection(selectedPosition);
		
		listView.setOnItemClickListener(this);
		listView.refreshDrawableState();
		
		if(menu!=null){
			MenuItem menuItem = menu.findItem(R.id.sort_az);
			if(menuItem!=null){
				menuItem.setIcon(R.drawable.ic_menu_sort_alphabetically);
				menuItem.setTitle(R.string.a_z);
			}
		}
	}
	
	protected View getRootView() {
		return rootView;
	}

	protected ListView getListView() {
		return listView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
				
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) { 				
        rootView = inflater.inflate(R.layout.fragment_list_clients, container, false); 
        listView = (ListView)rootView.findViewById(R.id.clients_listView);
        return rootView;
    }


	@Override
	protected DataView createView(FragmentActivity activity) {
		return new DataView(activity);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.menu = menu;
		inflater.inflate(R.menu.fragment_list_client, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		DataViewModel vm = getViewModel();
		DataView view= (DataView) getDataView();
		switch (id) {		
			case R.id.refresh:
				vm.load();
				return true;
			case R.id.sort_az:				
				sortItems(SortOrder);
				SortOrder = -SortOrder;
				if(SortOrder == 1){
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
	
	protected abstract void updateUI();
	
	protected abstract void sortItems(int sortOrder);

	@Override
	public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
	
}
