package com.comyted.modules.sheets.tasks;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.comyted.Constants;
import com.comyted.R;

//Activity for editinga task. A task is always attached to a working sheet
//the activity implements an asyncronous behavior for loading the data
public class ActivityEditTask extends FragmentActivity {

	FragmentEditTask fr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_task);
		
		if (savedInstanceState == null) {
			fr = new FragmentEditTask();
			getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.container, fr)
				.commit();
		}
		else{
			fr= (FragmentEditTask)getSupportFragmentManager()
					.findFragmentById(R.id.container);
		}
			
		
	   int taskId= getIntent().getIntExtra(Constants.TASK_ID, 0);
	   getActionBar().setTitle(taskId > 0? 
			   R.string.editar_tarea: 
			   R.string.nueva_tarea);	
	   
	   getActionBar().setDisplayHomeAsUpEnabled(true);
	   
	   setResult(RESULT_CANCELED);
	}
	
	@Override
	public boolean onNavigateUp() {
		finish();
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
