package com.comyted.modules.orders;

import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.comyted.Constants;
import com.comyted.OpenCloseResult;
import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.Utils;
import com.comyted.models.AppUser;
import com.comyted.models.OrderSumary;
import com.enterlib.app.DefaultDataView;

public class ActivityListOrders extends FragmentActivity 
			implements ActionBar.TabListener {
	
	public static int THEME = R.style.AppBaseTheme;
	
	ViewModelListOrders viewModel;		
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private AppUser user;
	DataView dataView;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
        
        user = MainApp.getCurrentUser();
        if(user == null)
        	finish();
        
        //Title        
        getActionBar().setTitle(R.string.ordenes_de_trabajo);
        
        dataView = new DataView(this);
        viewModel = new ViewModelListOrders(user, dataView); 
        
        final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.ordenpager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}                  		                        		         
         
    } 
    
    @Override
   	public void onTabSelected(ActionBar.Tab tab,
   			FragmentTransaction fragmentTransaction) {
   		// When the given tab is selected, switch to the corresponding page in
   		// the ViewPager.
   		mViewPager.setCurrentItem(tab.getPosition());
   	}

   	@Override
   	public void onTabUnselected(ActionBar.Tab tab,
   			FragmentTransaction fragmentTransaction) {   
   	}

   	@Override
   	public void onTabReselected(ActionBar.Tab tab,
   			FragmentTransaction fragmentTransaction) {   		
   	}
    
   	@Override
   	protected void onResume() {   		
   		super.onResume();
   		
   		dataView.setIsValid(true);
   		viewModel.load();
   	}
   	
   	@Override
   	protected void onStop() {
   		dataView.setIsValid(false);
   		super.onStop();
   	}
  	
  	public boolean onCreateOptionsMenu(Menu menu) {  	   
  		 menu.add(R.string.update)     	
         .setIcon(R.drawable.ic_refresh_inverse)
         .setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				viewModel.load();
				return true;
			}
		})
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
  		return true;
  	};
  	  
  	public class SectionsPagerAdapter extends FragmentPagerAdapter {
  		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			
			switch (position) {
			case 0:
				return OrdenListFragment.newInstance(false);
			case 1:
				return OrdenListFragment.newInstance(true);		
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.mis_ordenes).toUpperCase(l);
			case 1:
				return getString(R.string.ordenes_equipo).toUpperCase(l);		
			}
			return null;
		}
  	}
	
	public static class OrdenListFragment extends Fragment 
										 implements OnItemClickListener,
										 			OnCheckedChangeListener, TextWatcher, OnEditorActionListener 
	{
		
		 private static final String ENABLE_USER_FILTER = "enableUserFilter";
		 
		OrderSumary[] opensOrders;
		OrderSumary[] closeOrders;
		ListView listViewOrdenes;
		View view;		
		Switch estado;
		boolean enableUserFilter;
		EditText searh;
		AdapterOrders adapter;
		EditText searchEdit;
		boolean ordersSetted;
		
		public static OrdenListFragment newInstance(boolean enableFilter){
				OrdenListFragment fragment = new OrdenListFragment();
	            Bundle args = new Bundle();
	            args.putBoolean(ENABLE_USER_FILTER, enableFilter);
	            fragment.setArguments(args);
	            return fragment;
		}
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.fragment_list_orders, container, false);
			
			estado = (Switch) view.findViewById(R.id.switchEstado);			
			listViewOrdenes = (ListView)view.findViewById(R.id.listView_ordenes);						
			searchEdit = (EditText) view.findViewById(R.id.nameFilterEdit);
			
			enableUserFilter = getArguments().getBoolean(ENABLE_USER_FILTER, false);
			
			if(!enableUserFilter){
				searchEdit.setVisibility(View.GONE);
				searchEdit.setFocusable(false);
				searchEdit.setEnabled(false);
			}else{
				searchEdit.addTextChangedListener(this);
				searchEdit.setOnEditorActionListener(this);
			}
			
			return view;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {			
			super.onActivityCreated(savedInstanceState);			
			
			listViewOrdenes.setOnItemClickListener(this);			
			estado.setOnCheckedChangeListener(this);					
			
		}
		
		public void setOrders(OpenCloseResult<OrderSumary> orders) {
			ordersSetted = true;
			if(orders == null){
				opensOrders = null;
				closeOrders = null;
				adapter = null;
				if(listViewOrdenes!=null){
					listViewOrdenes.setAdapter(adapter);
					listViewOrdenes.refreshDrawableState();
				}
				return;
			}
			
			opensOrders = orders.Opens;
			closeOrders = orders.Closed;
			adapter = null;
			if(listViewOrdenes!=null){
				if(!estado.isChecked()){
					if(opensOrders == null || opensOrders.length == 0)
						Utils.showMessage(getActivity(), getActivity().getString(R.string.no_hay_ordenes_abiertas));										
					else
						adapter = new AdapterOrders(getActivity(), opensOrders);																										
				}
				else{
					if(closeOrders == null || closeOrders.length == 0){
						Utils.showMessage(getActivity(), getActivity().getString(R.string.no_hay_ordenes_cerradas));						
					}
					else{
						adapter = new AdapterOrders(getActivity(), closeOrders);
					}
				}				
				listViewOrdenes.setAdapter(adapter);
				listViewOrdenes.refreshDrawableState();
			}
		}
		
		/**
		 * Se llama cuando se seleciona una orden
		 * */
  		@Override
  		public void onItemClick(android.widget.AdapterView<?> arg0, View arg1, int position, long id)
  		{
  			if (arg0 != null){
  				Object obj =  arg0.getItemAtPosition(position);
	  			if (obj != null && obj instanceof OrderSumary){
	  				//llamar a la actividad de vista de detalle de una hoja
	  				Intent i = new Intent(getActivity().getApplicationContext(), ActivityOrder.class);
	  				
	  				OrderSumary mtoAt = (OrderSumary)obj;
	  				Bundle extras = new Bundle();
	  				i.putExtra(Constants.ORDER_ID, mtoAt.id);
	  				i.putExtra(Constants.ORDER, mtoAt);
	  				extras.putString(Constants.TITLE, mtoAt.titulo);	  				
	  				i.putExtras(extras);
	  				startActivity(i);
	  			}
  			}
  		}

  		//**Se llama cuando se cambia valor del swich*/
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean checked) {
			if(!ordersSetted)
				return;
			
			if(!checked){
				//mostrar las ordenes abiertas
				if(opensOrders == null || opensOrders.length == 0){
					//si no hay ordenes abiertas mostrar un mensaje
					Utils.showMessage(getActivity(), getActivity().getString(R.string.no_hay_ordenes_abiertas));
					adapter = null;
				}
				else
					adapter = new AdapterOrders(getActivity(), opensOrders);	
			}
			else{
				//mostrar las ordenes cerradas
				if(closeOrders==null || closeOrders.length == 0){
					//si no hay ordenes cerradas mostrar un mensaje
					Utils.showMessage(getActivity(), getActivity().getString(R.string.no_hay_ordenes_cerradas));
					adapter = null;
				}
				else
					adapter = new AdapterOrders(getActivity(), closeOrders);
			}	
			
			if(adapter!=null && enableUserFilter){
				 String text = searchEdit.getText().toString();	        			 
				 android.widget.Filter filter = adapter.getFilter();		        			 
				 filter.filter(text);
			}
			
			listViewOrdenes.setAdapter(adapter);
			listViewOrdenes.refreshDrawableState();						
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			if(adapter!=null){				
				 String text =s.toString();	        			 
				 android.widget.Filter filter = adapter.getFilter();		        			 
				 filter.filter(text);
			}
			
		}


		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(actionId == EditorInfo.IME_ACTION_SEARCH) { 
				//Handle search key click 
				if(adapter!=null){
					 String text = v.getText().toString();	        			 
					 android.widget.Filter filter = adapter.getFilter();		        			 
					 filter.filter(text);					
				}
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService( 
												Context.INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return true; 
		    }
			return false;
		}		
	  	
	}

	
	class DataView extends DefaultDataView<ActivityListOrders>		
	{

		public DataView(ActivityListOrders activity) {
			super(activity);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onDataLoaded() {
			OrdersCollection value = viewModel.getOrders();
			if(value == null)
	    		Utils.finalizeActivity(getActivity(), getString(R.string.no_ordenes));
	    	else
	    	{
	    		FragmentManager fm = getSupportFragmentManager();
	    		List<Fragment>fragments = fm.getFragments();
	    		OrdenListFragment myOrders = (OrdenListFragment)fragments.get(0);
	    		OrdenListFragment otherOrders =(OrdenListFragment)fragments.get(1);
	    		
	    		myOrders.setOrders(value.UserOrders);
	    		otherOrders.setOrders(value.OtherOrders);
	    	}    			
		}
		
	}
}
