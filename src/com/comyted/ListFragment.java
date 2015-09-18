package com.comyted;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.enterlib.app.DataViewModel;
import com.enterlib.app.IFilterableAdapter;
import com.enterlib.widgets.FilterDialog;

public abstract class ListFragment extends RefreshableFragment
									implements OnItemClickListener{
	public static final String SELECTION = "SELECTION";
	
	private FilterDialog mDialog;	
	private Menu menu;
	private int SortOrder = 1;
	private IFilterableAdapter adapter;
	private View rootView;
	private ListView listView;
	private int selectedPosition;
	
	public String EmptyMessage = "No hay datos";
	
	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	protected Menu getMenu() {
		return menu;
	}
	protected void setMenu(Menu value){
		this.menu =value;
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
		
		if(savedInstanceState!=null){
			selectedPosition = savedInstanceState.getInt(SELECTION);
		}
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) { 				
        rootView = inflater.inflate(R.layout.fragment_list_clients, container, false); 
        listView = (ListView)rootView.findViewById(R.id.clients_listView);
        return rootView;
    }


	public final void showSearchDialog(){
		if(adapter == null)
			return;
		
		mDialog = createFilterDialog();	
		if(mDialog == null)
			return;
		
		mDialog.setOnSelectedItemlistener(new FilterDialog.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View itemView, int position, Object item) {
				onItemClick(parent, itemView, position, position);					
			}
						
		});
		mDialog.show();
	}
	
	protected FilterDialog createFilterDialog(){
		return new FilterDialog(getActivity(), adapter, getSelectedPosition());	
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.menu = menu;
		inflater.inflate(R.menu.fragment_list_client, menu);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		
		outState.putInt(SELECTION, selectedPosition);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(super.onOptionsItemSelected(item))
			return true;
		
		int id = item.getItemId();
		DataViewModel vm = getViewModel();	
		switch (id) {				
			case R.id.sort_az:
				sortItems(SortOrder);
				SortOrder = -SortOrder;
				MenuValue value = getMenuValue(SortOrder);
				item.setIcon(value.iconRes);
				item.setTitle(value.titleRes);					
				return true;			
			case R.id.filter:
				showSearchDialog();
				return true;				
		}
		return false;
	}
	
	protected MenuValue getMenuValue(int sortOrder){		
		return sortOrder == 1 ? 
				new MenuValue(R.drawable.ic_menu_download,R.string.a_z):
				new MenuValue(R.drawable.ic_menu_upload, R.string.z_a);
	}
	
	protected abstract void sortItems(int sortOrder);
	
	public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
	
	class MenuValue{
		public MenuValue(int iconRes, int titleRes) {
			this.iconRes = iconRes;
			this.titleRes = titleRes;
		}
		public int iconRes;
		public int titleRes;
	}
	
}
