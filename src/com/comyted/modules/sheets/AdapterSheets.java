package com.comyted.modules.sheets;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comyted.R;
import com.comyted.Utils;
import com.comyted.models.SheetSumary;
import com.comyted.repository.Messages;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.IDisplayValueExtractor;

public class AdapterSheets extends CollectionAdapter<SheetSumary> {
	
	Activity activity = null;
	
	public AdapterSheets(Activity activity, SheetSumary[] hojas){		
		 super(activity, R.layout.adapter_sheets, hojas);
		 		 
		 this.activity = activity;
		 setFilter(new SheetSumaryFilter());
	}
	
	
	
	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		try{											
			if(convertView != null)
				view = convertView;
			else{
				LayoutInflater inflate = activity.getLayoutInflater();
				view = inflate.inflate(R.layout.adapter_sheets, null);
			}
			
			SheetSumary sheet = getItem(position);
			
			/*TextView txtid = (TextView)view.findViewById(R.id.txtid);
			txtid.setText(listAccounts.get(position).getId()); */
			TextView txtTitulo = (TextView)view.findViewById(R.id.txttitulo_hoja);
			txtTitulo.setText(sheet.titulo);
			
			TextView txtPlanta = (TextView)view.findViewById(R.id.txtplanta_hoja);
			txtPlanta.setText(sheet.planta);
						
			
			TextView txtTotalHoras = (TextView)view.findViewById(R.id.txttotalhoras_hoja);
			txtTotalHoras.setText(Utils.parse(sheet.horastotales) + "h " + Utils.parse(sheet.minutostotales) + "m");
			
			ImageView img = (ImageView)view.findViewById(R.id.icon_hoja);														
			if(SheetsManager.IsFinish(sheet.estado)){				
				img.setImageResource(R.drawable.icon_orden_cerrada); 				
			}else{
				img.setImageResource(R.drawable.icon_orden_abierta); 				
			}
			
			ImageView cb = (ImageView) view.findViewById(R.id.checkbox_hoja);
			if(sheet.tipohoja!=null){
				if(sheet.tipohoja.toLowerCase().equals(activity.getString(R.string.taller))){
					cb.setImageResource(R.drawable.icon_hoja_taller); 				
				}
				else if(sheet.tipohoja.toLowerCase().equals(activity.getString(R.string.planta))){
					cb.setImageResource(R.drawable.icon_hoja_planta); 
				}
				else if(sheet.tipohoja.toLowerCase().equals(activity.getString(R.string.viaje))){
					cb.setImageResource(R.drawable.icon_hoja_viaje); 
				}											
			}
			
			//Utils.setTextViewText(view R.id.fecha, Utils.formatDateOnly(sheet.g) );
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			Log.e("Exception", exc.getMessage());
		}
		return view;
	}

	public SheetSumaryFilter getSheetSumaryFilter(){
		return (SheetSumaryFilter) getFilter();
	}
	
	public class SheetSumaryFilter extends Filter{
		
		public String Estado = Messages.getString("StateRepository.Abierta");
			
		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			  FilterResults results = new FilterResults();

	            if (mOriginalValues == null) {
	                synchronized (mLock) {
	                    mOriginalValues = new ArrayList<SheetSumary>(mObjects);
	                }
	            }
	            
	            ArrayList<SheetSumary> list = new ArrayList<SheetSumary>();
	            int count = mOriginalValues.size();
	            for (int i = 0; i < count; i++) {
					SheetSumary s  = mOriginalValues.get(i);
					if(s.estado.equalsIgnoreCase(Estado)){
						list.add(s);
					}
				}
	            results.values = list;
                results.count = list.size();
	            
	            if (prefix != null && prefix.length() > 0){
	            	
	                String prefixString = prefix.toString().toLowerCase(Locale.US);	             
	                count = list.size();
	                
	                final ArrayList<SheetSumary> newValues = new ArrayList<SheetSumary>();
	                 IDisplayValueExtractor textExtractor = getTextExtractor();
	                
	                for (int i = 0; i < count; i++) {
	                    final SheetSumary value = list.get(i);
	                    
	                    final String valueText = textExtractor!=null? textExtractor.getText(value).toLowerCase():
	                    											  value.toString().toLowerCase();

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
			 mObjects = (List<SheetSumary>)results.values;
	            if (results.count > 0) {
	                notifyDataSetChanged();
	            } else {
	                notifyDataSetInvalidated();
	                Utils.showMessage(activity, activity.getString(Estado.equals(Messages.getString("StateRepository.Abierta")) ? 
	                		R.string.no_hay_hojas_abiertas : 
                			R.string.no_hay_hojas_cerradas));
	            }
			
		}
		
	}
}


