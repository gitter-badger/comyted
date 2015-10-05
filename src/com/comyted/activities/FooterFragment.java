package com.comyted.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.Utils;
import com.comyted.models.AppUser;
import com.comyted.modules.admin.ActivitySetting;
import com.comyted.modules.admin.IUserManager;
import com.comyted.modules.admin.UsersManager;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.generics.IObserver;
import com.enterlib.threading.IAsyncInvocator;
import com.google.android.gms.internal.ho;


public class FooterFragment extends Fragment 
	implements View.OnClickListener , IAsyncInvocator, IObserver<AppUser>	
{
		View view;
		AppUser user;
		IUserManager usersManager;
		public static boolean GO_HOME;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {			
			super.onCreate(savedInstanceState);			
		}
		
		//Dialog dialog;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
		{
			view = inflater.inflate(R.layout.fragment_footer, container);			
			return view;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {			
			super.onActivityCreated(savedInstanceState);
						
			user = MainApp.getCurrentUser();
			Activity activity = getActivity();
			if(user == null){
				GO_HOME = false;
				activity.finish();			
			}
			else if(GO_HOME){
				if(activity instanceof DashboardActivity){
					GO_HOME= false;
				}else{
					activity.finish();
				}
			}
			else{									
				MainApp.addCurrentUserObserver(this);
				usersManager = new UsersManager(null);
				
				TextView username = (TextView) view.findViewById(R.id.lblUsuarioConectado);
				username.setText(user.nombre);
				
				ImageButton button = (ImageButton) view.findViewById(R.id.imsettings);
				button.setOnClickListener(this);
			/*	
				dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.settings_dialog);
			
				Button bt = (Button) dialog.findViewById(R.id.settings);
				bt.setOnClickListener(this);
				bt = (Button)dialog.findViewById(R.id.logout);
				bt.setOnClickListener(this);
				
				dialog.findViewById(R.id.about).setOnClickListener(this);*/
				
				ImageButton home = (ImageButton) view.findViewById(R.id.im_home);
				if(activity instanceof DashboardActivity){
					home.setVisibility(View.GONE);
				}else{
					home.setOnClickListener(new View.OnClickListener() {						
						@Override
						public void onClick(View v) {
							GO_HOME = true;
							getActivity().finish();							
						}
					});
				}
			}	
		}
		
		@Override
		public void onResume() {		
			super.onResume();
			
			user = MainApp.getCurrentUser(); 
			if(user == null){
				getActivity().finish();			
			}			
			else if(GO_HOME){
 				Activity activity = getActivity();
				if(activity instanceof DashboardActivity){
					GO_HOME= false;
				}else{
					activity.finish();
				}
			}
		}
		
		@Override
		public void onStart() {	
			super.onStart();
			
			user = MainApp.getCurrentUser(); 
			if(user == null){
				getActivity().finish();			
			}
		}

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.imsettings:
				//dialog.show();
				Bundle extras = new Bundle();
				extras.putInt("Setting", 2);
				startActivity(new Intent(getActivity().getApplicationContext(),
				    					ActivitySetting.class).putExtras(extras));
				break;		
			}
		}

		@Override
		public void DoAsyncOperation() throws InvalidOperationException {					
				usersManager.logOut();			
		}

		@Override
		public void OnAsyncOperationComplete() {			
			getActivity().finish();
		}

		@Override
		public void OnAsyncOperationException(Exception e) {
			Utils.showMessage(getActivity(), e.getMessage());			
		}

		@Override
		public void onNotify(AppUser value) {
			user = value;			
			final TextView username = (TextView) view.findViewById(R.id.lblUsuarioConectado);			
			username.post(new Runnable() {				
				@Override
				public void run() {					
					username.setText(user!=null?user.nombre:null);					
				}
			});						
		}

		@Override
		public void onDestroy() {
			MainApp.removeCurrentUserObserver(this);
			super.onDestroy();
		}
		
}
