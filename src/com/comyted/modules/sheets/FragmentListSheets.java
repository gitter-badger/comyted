package com.comyted.modules.sheets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.Utils;
import com.comyted.models.SheetSumary;
import com.comyted.repository.Messages;
import com.comyted.repository.SheetsRepository;
import com.enterlib.app.DefaultDataView;

public class FragmentListSheets extends android.support.v4.app.Fragment 
							implements OnCheckedChangeListener					
{
		
	public static int THEME = R.style.AppTheme;	
	
	public static final String ESTADO = "ESTADO";
	
	View rootView;	
	ViewModelListSheets vm;	
	AdapterSheets adapter;
	DataView view;
	String Estado;
		
	public static FragmentListSheets newInstance(int orderId) {
		FragmentListSheets fr = new FragmentListSheets();
		Bundle b = new Bundle();
		b.putInt(Constants.ORDER_ID, orderId);
		b.putString(ESTADO, Messages.getString("StateRepository.Abierta"));
		fr.setArguments(b);				
		return fr;
	}		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
				
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) { 				
        rootView = inflater.inflate(R.layout.fragment_list_sheets, container, false);                                             
        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Estado = getArguments().getString(ESTADO);
		view = new DataView(getActivity());
		vm = new ViewModelListSheets(view, 
				new SheetsRepository(), 
				getArguments().getInt(Constants.ORDER_ID),
				MainApp.getCurrentUser().id);		
		
		 Switch swEstado  = (Switch) rootView.findViewById(R.id.switchEstado);
	     swEstado.setOnCheckedChangeListener(this);        
	}
			
	@Override
	public void onResume() {
		super.onResume();
		view.setIsValid(true);
		vm.load();
	}
	
	@Override
	public void onStop() {
		view.setIsValid(false);
		super.onStop();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_list_sheets, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.add:
			Activity activity = getActivity();
			Intent i =new Intent(activity.getApplicationContext(), ActivityEditSheet.class);
			i.putExtra(Constants.ORDER_ID, vm.getOrderId());	
			activity.startActivityForResult(i, Constants.ADD_SHEET);
			return true;		
		case R.id.refresh:
			vm.load();
			return true;
		}
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Constants.SHEET_OK){
			vm.load();			
		}    		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean checked) {				
		if(!checked){		
			Estado = Messages.getString("StateRepository.Abierta");			
		}
		else{
			Estado  = Messages.getString("StateRepository.Cerrada");
		}
		if(adapter!=null){
			adapter.getSheetSumaryFilter().Estado = Estado;
			adapter.getSheetSumaryFilter().filter(null);
		}
	}
		

	class DataView extends DefaultDataView< Activity> implements OnItemClickListener{

		
		public DataView(Activity activity) {
			super(activity);			
		}

		@Override
		public void onDataLoaded() {
			SheetSumary []sheets = vm.getSheets();
			ListView listView = (ListView)rootView.findViewById(R.id.listView_hojas);
			
			if(sheets==null || sheets.length == 0){
				Utils.showMessage(getActivity(), getString(vm.getOrderId()>0? R.string.no_hojas_en_orden : R.string.no_hojas));
				adapter = null;
				listView.setAdapter(null);		
				listView.refreshDrawableState();
				return;
			}
								
			adapter = new AdapterSheets(getActivity(), vm.getSheets());
			adapter.getSheetSumaryFilter().Estado = Estado;			
			listView.setAdapter(adapter);		
			listView.setOnItemClickListener(this);
			listView.refreshDrawableState();
			
			adapter.getSheetSumaryFilter().filter(null);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				
			if(position < 0 || vm.getSheets() == null) return;
			
			SheetSumary s = (SheetSumary) parent.getItemAtPosition(position);				
			if(s != null){
				Activity activity = getActivity();
				Intent  i = new Intent(activity, ActivitySheet.class);
				i.putExtra(Constants.SHEET_ID, s.id);
				i.putExtra(Constants.ORDER_ID, vm.getOrderId());
				i.putExtra(Constants.ORDER, activity.getIntent().getSerializableExtra(Constants.ORDER));
				startActivity(i);
			}
			
		}
		
	}


	

	
		
}
