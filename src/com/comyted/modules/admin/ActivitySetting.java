package com.comyted.modules.admin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class ActivitySetting extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int option = 0;
        try{
        	// La opcion de Bundle es para asegurarnos que no viene nunca null y ademas porque la llamada proviene de bundle
        	/*option = Integer.parseInt(intent.getExtras().get("Setting").toString());*/
        	Bundle extras = getIntent().getExtras();
        	if (extras != null){
        		option = Integer.parseInt(extras.get("Setting").toString());
        	}
        }
        catch (Exception e) {
	        Log.e("Error en ejecución de opciones", "Fallo en Opciones", e);}
		switch (option){
			case 1:
				getFragmentManager().beginTransaction()
		        .replace(android.R.id.content, new FragmentSetPreferenceIntro())
		        .commit();				
				break;
			case 2:
				getFragmentManager().beginTransaction()
		        .replace(android.R.id.content, new FragmentSetPreferenceAll())
		        .commit();
				break;
		}
		
	}
}
