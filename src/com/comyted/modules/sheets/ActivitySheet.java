package com.comyted.modules.sheets;

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

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.modules.sheets.tasks.FragmentTasks;
 
public class ActivitySheet extends FragmentActivity
			implements  ActionBar.TabListener{
 	
	
    public static int THEME = R.style.AppTheme;
    
    private int orderId = -1;	
	private ViewModelSheet vm;		
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
   
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);        
                
        final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.hojapager);
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
					.setTabListener(ActivitySheet.this));
		} 
		
		orderId = getIntent().getIntExtra(Constants.ORDER_ID, -1);
		vm = new ViewModelSheet(getIntent().getIntExtra(Constants.SHEET_ID, -1), null);
		

    }
    
    @Override
    public boolean onNavigateUp() {
    	finish();    	
    	return true;
    }
        
    @Override
    protected void onResume() {    	
    	super.onResume();  
    	//load the SheetDetails and set it in the FragmentSheet if visible
    	vm.load();
    }      
    
    public ViewModelSheet getViewModel(){
		return vm;
	}
    
    public int getOrderId(){
    	return orderId;
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
			switch (position) {
			case 0:
				return new FragmentSheet();
			case 1:
				return new FragmentTasks();
			case 2:
				return new FragmentSignature();
			case 3:
				return new FragmentEmail();		
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();		
			switch (position) {
			case 0:
				return getString(R.string.hoja).toUpperCase(l);
			case 1:
				return getString(R.string.tareas).toUpperCase(l);		
			case 2:
				return getString(R.string.firma).toUpperCase(l);
			case 3:
				return getString(R.string.email).toUpperCase(l);
			}
			return null;
		}
  	}
    
}