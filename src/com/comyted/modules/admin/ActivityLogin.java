package com.comyted.modules.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.comyted.R;
import com.comyted.Utils;
import com.comyted.activities.AboutActivity;
import com.comyted.activities.DashboardActivity;
import com.comyted.activities.FooterFragment;
import com.comyted.models.UserLogin;
import com.enterlib.exceptions.ConversionFailException;
import com.enterlib.fields.Form;
import com.enterlib.fields.TextViewField;
import com.enterlib.mvvm.DefaultDataView;


public class ActivityLogin extends Activity
						  implements OnClickListener
{	
	class DataView extends DefaultDataView<ActivityLogin>{
		public DataView(ActivityLogin activity) {
			super(activity);		
		}

		@Override
		public void onDataLoaded() {
			if(vm.getUser() == null){
				Utils.showMessage(getActivity(), getString(R.string.no_registrado));
				return;
			}		
			Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
			startActivity(i);				
		}
	}
	TextView txterror = null;
	ViewModelLogin vm;
	Form form;
	DataView view;
	
	public static int THEME = R.style.AppBaseTheme;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		FooterFragment.GO_HOME = false;		
		
		// Title & SubTitle options
        getActionBar().setTitle("");					
	
        view =new DataView(this);
        vm = new ViewModelLogin(this, view);        
        
        
        form = new Form();
        form.addValidator(new TextViewField((EditText)findViewById(R.id.txtlogin), true));
        form.addValidator(new TextViewField((EditText)findViewById(R.id.txtpassword), true));
        
        UserLogin login = vm.getLastLoggedUser();
        if(login!=null){
	        form.getFieldById(R.id.txtlogin).setValue(login.username);
	        form.getFieldById(R.id.txtpassword).setValue(login.password);
        }
		
        
        Button btn_login = (Button) findViewById(R.id.btn_login);        
        btn_login.setOnClickListener(this);                     
	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		view.setIsValid(true);
	}
	@Override
	protected void onStop() {
		view.setIsValid(false);
		super.onStop();
	}

	
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
        boolean isLight = THEME == R.style.AppTheme;
        menu.add("Conexión")        	
            .setIcon(isLight ? R.drawable.ic_settings_inverse : R.drawable.ic_settings)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    } 
	
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//This uses the imported MenuItem from ActionBarSherlock
		if ("Conexión".equals(item.getTitle()))
		{
			Bundle extras = new Bundle();
			Intent i = new Intent(getApplicationContext(), ActivitySetting.class);
			extras.putLong("Setting", 1);
			i.putExtras(extras);
			startActivity(i);
		}
	    return true;
	} 			

	
	
	public void showAbout(View v){
		Intent i = new Intent(getApplicationContext(), AboutActivity.class);
		startActivity(i);
		
	}


	@Override
	public void onClick(View v) {
		if(form.validate()){
			try {
				vm.setUsername(form.getFieldById(R.id.txtlogin).getString());
				vm.setPassword(form.getFieldById(R.id.txtlogin).getString());
			} catch (ConversionFailException e) {
				Log.d("LoginActivity", e.getMessage());
				return;
			}			
			vm.load();
		}								
	}
		
}
