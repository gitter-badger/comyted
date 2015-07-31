package com.comyted.modules.sheets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.comyted.R;

public class ActivityListSheets extends FragmentActivity {
		
	public static int THEME = R.style.AppBaseTheme;	
		
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sheets);
                      
        if(savedInstanceState == null){
        	FragmentManager fm =  this.getSupportFragmentManager();        	        	 
        	fm.beginTransaction().add(R.id.container, FragmentListSheets.newInstance(0)).commit();
        }
       
        // Title & SubTitle options,
        getActionBar().setTitle(R.string.hojas_de_trabajo);				               				
    }   
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	for (android.support.v4.app.Fragment frag : getSupportFragmentManager().getFragments()) {
			frag.onActivityResult(requestCode, resultCode, data);
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
