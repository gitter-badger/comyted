package com.comyted.modules.clients;

import java.util.Locale;

import junit.framework.Assert;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.conectivity.GetClientesClient;
import com.comyted.models.ClientSummary;
import com.comyted.modules.contacts.FragmentContacts;
import com.comyted.modules.offers.FragmentOffers;
import com.comyted.modules.visits.FragmentVisits;
import com.comyted.repository.ClientRepository;
import com.comyted.repository.IClientRepository;
import com.comyted.testing.repository.LocalJSONClientRepository;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ActivityClient extends Activity implements ActionBar.TabListener {

	public static int THEME = R.style.AppTheme;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	ViewModelClient viewModel;
	private int clientId;

	public ViewModelClient getViewModel(){
		return viewModel;
	}
    
    public int getClientId(){
    	return clientId;	    
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
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
		
		clientId = getIntent().getIntExtra(Constants.CLIENT_ID, 0);
		ClientSummary clientSummary = (ClientSummary) getIntent().getSerializableExtra(Constants.CLIENT);
		
		Assert.assertNotNull(clientSummary);
		setTitle(clientSummary.nombreempresa);
		
		IClientRepository repo;
		if(MainApp.TEST){
			//repositorio local de prueba
			repo = new LocalJSONClientRepository(this);			
		}
		else{		
			//repositorio en servicios
			//solo se necesita hacer GET en esta actividad
			repo = new ClientRepository(new GetClientesClient(), null);
		}
		viewModel = new ViewModelClient(clientId, null, repo);
	}
	
	 @Override
    protected void onResume() {    	
    	super.onResume();  
    	//load the SheetDetails and set it in the FragmentSheet if visible
    	viewModel.load();
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

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new FragmentClient();
			case 1:
				return new FragmentContacts();
			case 2:
				return new FragmentVisits();
			case 3:
				return new FragmentOffers();		
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
				return "Datos";
			case 1:
				return "Contactos";
			case 2:
				return "Visitas";
			case 3:
				return "Ofertas";
			}
			return null;
		}
	}

	
}
