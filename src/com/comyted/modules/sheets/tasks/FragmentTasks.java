package com.comyted.modules.sheets.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.Utils;
import com.comyted.models.SheetDetails;
import com.comyted.models.TaskDetails;
import com.comyted.modules.sheets.ActivitySheet;
import com.comyted.modules.sheets.ViewModelSheet;
import com.enterlib.mvvm.DefaultDataView;

public class FragmentTasks extends Fragment 
							implements OnCheckedChangeListener{
	
	View rootView;
	ViewModelTasks vm;	
	ActivitySheet activity;
	DataView view;	
	SheetDetails sheetDetail;
	ViewModelSheet detailsViewModel;
	AdapterTasks adapter;	
	
	
	class DataView extends DefaultDataView<Activity> implements IViewTasks{

		public DataView(Activity activity) {
			super(activity);			
		}

		@Override
		public void onDataLoaded() {
			Switch estado  = (Switch) rootView.findViewById(R.id.switchEstado);
			ListView listView = (ListView) rootView.findViewById(R.id.listView_tasks);
			TaskDetails[] tasks = vm.getTasks();
			if(tasks == null || tasks.length == 0){
				Utils.showMessage(getActivity(), getString(R.string.no_tareas));
				listView.setAdapter(null);			
				return;
			}
			
			adapter = new AdapterTasks(activity, tasks, vm, detailsViewModel);									
			listView.setAdapter(adapter);			
			
			if(estado.isChecked()){
				estado.setChecked(false);
			}else{
				adapter.filterByEstado(1);							
			}
		}

		@Override
		public void showTaskEditView(TaskDetails task, SheetDetails sheet) {
			Intent i = new Intent(activity.getApplicationContext(), ActivityEditTask.class )
			.putExtra(Constants.TASK, task)
			.putExtra(Constants.TASK_ID, task.codtarea)
			.putExtra(Constants.SHEET, sheet)
			.putExtra(Constants.SHEET_ID, sheet.id)
			.putExtra(Constants.ORDER_ID, vm.getOrderId());																
			activity.startActivityForResult(i, Constants.EDIT_TASK);			
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		 rootView = inflater.inflate(R.layout.fragment_tasks, container, false);		 
		 Switch estado  = (Switch) rootView.findViewById(R.id.switchEstado);
		 estado.setOnCheckedChangeListener(this);		
		 return rootView;
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);
					
		 activity = (ActivitySheet) getActivity();
		 detailsViewModel = activity.getViewModel();		 
		 
		 int sheetId = activity.getIntent().getIntExtra(Constants.SHEET_ID, -1);
		 view = new DataView(activity); 		
		 vm = new ViewModelTasks(sheetId, activity.getOrderId(), view);		 
	}
	
	@Override
	public void onResume() {			
		super.onResume();
				
		view.setIsValid(true);		
		vm.load();			
	}
	
	@Override
	public void onStop() {		
		view.setIsValid(false);
		super.onStop();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.tasks_fragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {						
		switch (item.getItemId()) {
		case R.id.add:
			SheetDetails sheet =detailsViewModel.getSheet();
			if(sheet == null){
				Utils.showMessage(getActivity(), getActivity().getString(
						R.string.espere_a_que_ser_cargen_los_datos));
				return false;
			}			
			if(sheet.isClosed()){
				Utils.showMessage(getActivity(), getActivity()
						.getString(R.string.no_se_permite_adicionar_tareas_a_una_hoja_cerrada));
				return false;
			}							
			Intent i =new Intent(getActivity(), ActivityEditTask.class);
			i.putExtra(Constants.SHEET,sheet);
			i.putExtra(Constants.SHEET_ID,sheet.id);	
			i.putExtra(Constants.ORDER_ID, vm.getOrderId());			
			startActivityForResult(i, Constants.ADD_TASK);
			return true;
		case R.id.refresh:
			 vm.load();
			return true;
		}
		return false;
	}				

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean checked) {		
		if(adapter!=null)
			adapter.filterByEstado(checked?2:1);					
	}
		
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Constants.TASK_OK){		
			vm.load();
		}
	}	
}
