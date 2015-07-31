package com.comyted.modules.orders;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.modules.sheets.FragmentListSheets;
 
public class ActivityOrder extends FragmentActivity 
	implements  ActionBar.TabListener {
 
      
	public static int THEME = R.style.AppTheme;	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
    int orderId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
         
        if(MainApp.getCurrentUser() == null)
        	finish();
        
        Bundle extras = getIntent().getExtras();
        orderId = extras.getInt(Constants.ORDER_ID, 0);
        
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
        
        try{
        	if (extras != null){
        		String Titulo = extras.get("titulo").toString();
                getActionBar().setTitle(Titulo);
        	}
        }catch (Exception e) {
	        Log.e("Error en ejecuci�n de opciones", "Fallo en Opciones", e);}
 
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	for (android.support.v4.app.Fragment frag : getSupportFragmentManager().getFragments()) {
			frag.onActivityResult(requestCode, resultCode, data);
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
   
    
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
				return FragmentOrder.newInstance(orderId);
			case 1:
				return FragmentListSheets.newInstance(orderId);		
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
				return getString(R.string.orden).toUpperCase(l);
			case 1:
				return getString(R.string.hojas).toUpperCase(l);		
			}
			return null;
		}
	}
   
}