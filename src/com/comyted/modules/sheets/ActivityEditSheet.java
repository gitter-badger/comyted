package com.comyted.modules.sheets;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.comyted.Constants;
import com.comyted.R;

public class ActivityEditSheet extends FragmentActivity										   
{				
	FragmentEditSheet fr;	
	int sheetId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_sheet);
		
		sheetId = getIntent().getIntExtra(Constants.SHEET_ID, 0);		
		getActionBar().setTitle(sheetId== 0?R.string.nueva_hoja_de_tarea:
											R.string.editar_hoja);		
						
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if(savedInstanceState == null){
			//Initialize the UI fragment
			fr =  new FragmentEditSheet();			
			getSupportFragmentManager().beginTransaction().add(R.id.container, fr).commit();
		}
		else
			fr= (FragmentEditSheet) getSupportFragmentManager().findFragmentById(R.id.container);
		
		 this.setResult(RESULT_CANCELED);
	}	
	
	@Override
	public boolean onNavigateUp() {
		finish();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		getMenuInflater().inflate(R.menu.edit_sheet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.save) 
		{
			fr.Save();
			return true;
		}
		return false;
	}	
		
}
