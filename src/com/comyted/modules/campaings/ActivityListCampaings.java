package com.comyted.modules.campaings;

import com.comyted.R;
import android.app.Activity;
import android.os.Bundle;

public class ActivityListCampaings extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FragmentListCampaings()).commit();
		}
		
		 // Title & SubTitle options,      
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}	
}
