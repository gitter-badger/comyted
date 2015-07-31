package com.comyted.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.models.AppUser;
import com.comyted.modules.orders.ActivityListOrders;
import com.comyted.modules.sheets.ActivityListSheets;


public class DashboardActivity extends FragmentActivity implements View.OnClickListener {
    
	public static int THEME = R.style.AppBaseTheme;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
               
        AppUser user = MainApp.getCurrentUser();
        
        AuthorizeOrders(user);
        
        AuthorizeSheets(user);
                
        // Title & SubTitle options
        getActionBar().setTitle(R.string.menu_principal);
    }

	private void AuthorizeSheets(AppUser user) {
		Button btn_hojas = (Button)findViewById(R.id.btn_hojas);
        
        if(!user.tecnico)
        {
        	btn_hojas.setEnabled(false);
        }
        else{        	
        	btn_hojas.setEnabled(true);
        	btn_hojas.setOnClickListener(this);
        }
        btn_hojas.refreshDrawableState();
	}

	private void AuthorizeOrders(AppUser user) {
		Button btn_ordenes = (Button)findViewById(R.id.btn_ordenes);
        if(!user.isServiceManager())
        {
        	btn_ordenes.setEnabled(false);
        }
        else
        {
        	btn_ordenes.setEnabled(true);
        	btn_ordenes.setOnClickListener(this);        	
        }
        btn_ordenes.refreshDrawableState();
	}
    
    @Override
	public void onClick(View view) 
    {
    	Intent i = null;
    	if (view.getId() == R.id.btn_ordenes) 
    	{
    		// Launching Ordenes Screen
    		i = new Intent(getApplicationContext(), ActivityListOrders.class);
    	}
    	else if (view.getId() == R.id.btn_hojas) 
    	{
            i = new Intent(getApplicationContext(), ActivityListSheets.class);
		} 
    	if (i != null)
    	{    		
    		startActivity(i);
    	}
    }
    
    public void showOfertas(View view){
    	Toast tast= Toast.makeText(this, "Click ofertas", Toast.LENGTH_LONG);
    	tast.show();
    	Log.d("DashboarActivity", "calling showOfertas");
    }
    
//     @Override
//	public boolean onCreateOptionsMenu(Menu menu) 
//    {
//        //Used to put dark icons on light action bar
//        boolean isLight = THEME == R.style.AppTheme;
//        menu.add("Configuración")
//            .setIcon(isLight ? R.drawable.ic_settings_inverse : R.drawable.ic_settings)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        return true;
//    } 
//	
//	 @Override
//	public boolean onOptionsItemSelected(MenuItem item) 
//	{
//		//This uses the imported MenuItem from ActionBarSherlock
//		if ("Configuración".equals(item.getTitle()))
//		{
//			Bundle extras = new Bundle();
//			Intent i = new Intent(getApplicationContext(), SettingActivity.class);
//			extras.putLong("Setting", 2);
//			i.putExtras(extras);
//			startActivity(i);
//		}
//	    return true;
//	}  
    
}