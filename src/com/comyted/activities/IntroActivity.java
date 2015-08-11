package com.comyted.activities;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.comyted.ConfigValues;
import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.Utils;
import com.comyted.modules.admin.ActivityLogin;
import com.comyted.modules.admin.ActivitySetting;
import com.comyted.persistence.SyncronizationService;
import com.enterlib.exceptions.InvalidOperationException;

public class IntroActivity extends Activity {
	
	Thread syncronizationThread = new Thread(){
			public void run() {
				try {
					
					final SyncronizationService sync = new SyncronizationService(getApplicationContext());
					final int count = sync.syncronize();
					
					runOnUiThread(new Runnable() {
						public void run() {	
							if(count > 0)
								Utils.showMessage(getApplicationContext(), String.format(Locale.US, "Se han sincronizado %d Objetos" , count));
							startLoginActivity();
						}
					});
				} catch (InvalidOperationException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Utils.showMessage(getApplicationContext(), "Fallo en sincronizanción.\nVerifique la conexión a Internet");
							startLoginActivity();
						}
					});					
				}															
			}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		//getPreferences(Activity.MODE_PRIVATE);
		
		// Ocultamos ActionBar
		getActionBar().hide();
					     
		 Utils.showMessage(getApplicationContext(), "Sincronizando con el servidor...");
		 
		//Vamos a declarar un nuevo thread
        Thread timer = new Thread(){
            //El nuevo Thread exige el metodo run
            public void run(){
                try{
                	                	
                	sleep(1000);
                	
                	// TODO Initialize Global Configurations like ConfigValues.CONST_URL_SERVICE
    				/* SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); */
    				ConfigValues configValues = new ConfigValues(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
    				// Utils.showMessage(getApplicationContext(), configValues.get(ConfigValues.CONST_URL_SERVICE) , Toast.LENGTH_LONG); 		
    				
    				if (!configValues.contains(ConfigValues.CONST_URL_SERVICE)){
    					// configValues.save(ConfigValues.CONST_URL_SERVICE, "http://lemursolution-mwmres.dyndns-server.com/");
    					startSettingsActivity();			
    				}
    				else{
    					MainApp.getInstance().setConfigValues(configValues);
    					syncronizationThread.start();
    				}
                }catch(InterruptedException e){
                    
                }
            }

			
        };       
        timer.start();
	}

	private void startSettingsActivity() {
		// Llamamos a la Activiti SettingActivity pasando le un valor para que sepa que clase de fragment tiene que mostrar
		// "Intro" muestra SetPreferenceIntroActivity y "All" muestra SetPreferenceAllActivity
		Bundle extras = new Bundle();
		Intent i = new Intent(getApplicationContext(), ActivitySetting.class);				
		extras.putInt("Setting", 1);
		i.putExtras(extras);			
		startActivityForResult(i, 1);
	}

	private void startLoginActivity() {
		//Llamo a LoginActivity
		//IntroActivity recibe por parametro un objeto del tipo Intent
		//El Intent recibibe por parametro el NAME de la actividad que vamos a invocar
		//Es el mismo que colocamos en el manifiesto     
		Intent Login = new Intent(getApplicationContext(), ActivityLogin.class);
		startActivity(Login);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		
		ConfigValues configValues = new ConfigValues(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
		// Utils.showMessage(getApplicationContext(), configValues.get(ConfigValues.CONST_URL_SERVICE) , Toast.LENGTH_LONG); 		
		
		if (configValues.contains(ConfigValues.CONST_URL_SERVICE)){
			MainApp.getInstance().setConfigValues(configValues);
			syncronizationThread.start();									
		}
		else{
			Utils.showMessage(this, "Configuración Invalida");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finish();
		}
	}
}
