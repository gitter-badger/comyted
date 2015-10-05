package com.comyted.modules.offers;

import com.comyted.R;
import com.comyted.modules.visits.FragmentUserVisits;

import android.app.Activity;
import android.os.Bundle;


public class ActivityListOffers extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FragmentListOffers()).commit();
		}
		
		 // Title & SubTitle options,      
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}	
}
