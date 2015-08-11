package com.comyted.modules.admin;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.comyted.R;

public class FragmentSetPreferenceIntro extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.conexion);
			
	}

}
