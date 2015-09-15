package com.comyted.modules.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.comyted.ListFragment;
import com.comyted.R;
import com.comyted.modules.clients.FragmentListClients;

public class ActivityListContacts extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_clients);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FragmentListContacts()).commit();
		}
		
		 // Title & SubTitle options,
        getActionBar().setTitle("Listado de Contactos");
        //getActionBar().setDisplayHomeAsUpEnabled(true);
	}	

}
