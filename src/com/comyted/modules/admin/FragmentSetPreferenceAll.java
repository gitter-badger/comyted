package com.comyted.modules.admin;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.models.AppUser;
import com.enterlib.threading.IAsyncCallback;


public class FragmentSetPreferenceAll extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 	TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	  	addPreferencesFromResource(R.xml.configuracion);
	  	
	  	Preference logout = findPreference("LOGOUT");
		AppUser user = MainApp.getCurrentUser();
		
		if(user == null)
			logout.setEnabled(false);
		else {
			logout.setEnabled(true);		
			logout.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {					
					UsersManager userManager = new UsersManager(null);
					userManager.logOut(new IAsyncCallback() {						
						@Override
						public void operationCompleted(Exception e) {
							MainApp.setCurrentUser(null);
							getActivity().finish();			
						}
					});
					return true;
				}
			});
		}
	 }
	
	

}
