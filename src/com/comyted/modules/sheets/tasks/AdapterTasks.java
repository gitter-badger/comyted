package com.comyted.modules.sheets.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import com.comyted.Constants;
import com.comyted.R;
import com.comyted.Utils;
import com.comyted.models.SheetDetails;
import com.comyted.models.TaskDetails;
import com.comyted.modules.sheets.ViewModelSheet;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.IDisplayValueExtractor;

public class AdapterTasks extends CollectionAdapter<TaskDetails> 
	implements View.OnClickListener
{
	
	private Activity activity;
	private ViewModelTasks tasksViewModel;
	private ViewModelSheet sheetDetailsViewModel;
	private String filterValue;
	
	public AdapterTasks(Activity activity, 			
			TaskDetails[] tasks, 
			ViewModelTasks tasksViewModel,
			ViewModelSheet sheetDetailsViewModel) {	
		
		super(activity, 0, tasks );					
							
		this.activity = activity;
		this.tasksViewModel = tasksViewModel;
		this.sheetDetailsViewModel = sheetDetailsViewModel;
		
		setFilter(new TaskDetailsFilter());
	}
	
	private TaskDetailsFilter getTaskDetailsFilter(){
		return (TaskDetailsFilter) getFilter();
	}
	
	public void filterByEstado(int estado){
		getTaskDetailsFilter().Estado = estado;
        getTaskDetailsFilter().filter(filterValue);
	}
	
	public void filterByTitle(String value){
		filterValue = value;
        getTaskDetailsFilter().filter(value);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		View view = null;
	
		if(convertView == null){
			LayoutInflater inflate = activity.getLayoutInflater();
			view = inflate.inflate(R.layout.adapter_tasks, null);
		}else{
			view = convertView;
		}
		
		TaskDetails t = getItem(position);		
		
		if(t!=null){	
			
			Utils.setTextViewText(view, R.id.fecha, Utils.formatVerbose(t.fecha));
			Utils.setTextViewText(view, R.id.time, getContext().getString(R.string.horas_totales) +  Utils.parse(t.horastotales) );		
			Utils.setTextViewText(view, R.id.kilometros,getContext().getString(R.string.kilometros) + Utils.parse(t.kilometros));
			Utils.setTextViewText(view, R.id.tdFecha,  Utils.formatTime(t.tdFecha));
			Utils.setTextViewText(view, R.id.thFecha, Utils.formatTime(t.thFecha));
			Utils.setTextViewText(view, R.id.ddFecha, Utils.formatTime(t.ddFecha));
			Utils.setTextViewText(view, R.id.dhFecha, Utils.formatTime(t.dhFecha));
			
			Utils.setTextViewText(view, R.id.title,t.titulo);
			
			Utils.setTextViewText(view, R.id.tipo, t.estado == 1? getContext().getString(R.string.abierta):getContext().getString(R.string.cerrada));
			
			TextView v = (TextView) view.findViewById(R.id.tipo);			 
			v.setBackgroundResource(t.estado == 1? R.color.task_abierta: R.color.taskTitle);
						
			Utils.setTextViewText(view, R.id.horas_viaje, getContext().getString(R.string.horas_de_viaje_) + Utils.parse(t.horasviaje));
			
			Button bt = (Button) view.findViewById(R.id.editar);
			bt.setTag(t);
			bt.setOnClickListener(this);
		}
		
		return view;
	}

	@Override
	public void onClick(View v) {
		TaskDetails t  = (TaskDetails) v.getTag();		
		SheetDetails sheet = sheetDetailsViewModel.getSheet();
		
		if(sheet == null){
			Utils.showMessage(activity, activity.getString(R.string.no_se_ha_cargado_la_hoja));
		}		
		else if(!tasksViewModel.canEditTask(t ,sheet)){
			Utils.showMessage(activity, activity.getString(R.string.tarea_no_editar));
		}					
		else{
		     Intent i = new Intent(activity.getApplicationContext(), ActivityEditTask.class )
			.putExtra(Constants.TASK, t)
			.putExtra(Constants.TASK_ID, t.codtarea)
			.putExtra(Constants.SHEET, sheet)
			.putExtra(Constants.ORDER_ID, activity.getIntent().getIntExtra(Constants.ORDER_ID, -1));																				
			activity.startActivityForResult(i, Constants.EDIT_TASK);
		}
		
	}	
	
	public class TaskDetailsFilter extends Filter{
		
		public int Estado = 1;
			
		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			  FilterResults results = new FilterResults();

	            if (mOriginalValues == null) {
	                synchronized (mLock) {
	                    mOriginalValues = new ArrayList<TaskDetails>(mObjects);
	                }
	            }
	            
	            ArrayList<TaskDetails> list = new ArrayList<TaskDetails>();
	            int count = mOriginalValues.size();
	            for (int i = 0; i < count; i++) {
	            	TaskDetails s  = mOriginalValues.get(i);
					if(s.estado == Estado){
						list.add(s);
					}
				}
	            results.values = list;
                results.count = list.size();
	            
	            if (prefix != null && prefix.length() > 0){
	            	
	                String prefixString = prefix.toString().toLowerCase(Locale.US);	             
	                count = list.size();
	                
	                final ArrayList<TaskDetails> newValues = new ArrayList<TaskDetails>();
	                IDisplayValueExtractor textExtractor = getTextExtractor();
	                
	                for (int i = 0; i < count; i++) {
	                    final TaskDetails value = list.get(i);
	                    
	                    final String valueText = textExtractor!=null? textExtractor.getText(value).toLowerCase(Locale.getDefault()):
	                    											  value.toString().toLowerCase(Locale.getDefault());

	                    // First match against the whole, non-splitted value
	                    if (valueText.startsWith(prefixString)) {
	                        newValues.add(value);
	                    } else {
	                        final String[] words = valueText.split(" ");
	                        final int wordCount = words.length;

	                        // Start at index 0, in case valueText starts with space(s)
	                        for (int k = 0; k < wordCount; k++) {
	                            if (words[k].startsWith(prefixString)) {
	                                newValues.add(value);
	                                break;
	                            }
	                        }
	                    }
	                }

	                results.values = newValues;
	                results.count = newValues.size();
	            }

	            return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			 mObjects = (List<TaskDetails>)results.values;
	            if (results.count > 0) {
	                notifyDataSetChanged();
	            } else {
	                notifyDataSetInvalidated();
	                Utils.showMessage(activity, activity.getString(Estado ==1 ? 
	                		R.string.no_hay_tareas_abiertas : 
                			R.string.no_hay_tareas_cerradas));
	            }			
		}
		
	}
}
