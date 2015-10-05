package com.comyted.modules.visits;

import com.comyted.R;
import android.app.Activity;
import android.os.Bundle;

public class ActivityListVisits extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FragmentUserVisits()).commit();
		}
		
		 // Title & SubTitle options,
        getActionBar().setTitle(R.string.listado_de_visitas);
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}	
}
