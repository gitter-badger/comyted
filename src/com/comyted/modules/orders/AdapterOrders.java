package com.comyted.modules.orders;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.comyted.R;
import com.comyted.Utils;
import com.comyted.models.OrderSumary;
import com.comyted.modules.sheets.SheetsManager;

public class AdapterOrders extends ArrayAdapter<OrderSumary> {
	
	Activity activity = null;
	
	public AdapterOrders(Activity activity, OrderSumary[] listOrdenes) {
		super(activity, R.layout.adapter_orders, listOrdenes);		
		this.activity = activity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
		
			if(convertView ==null){
				LayoutInflater inflate = activity.getLayoutInflater();
				view = inflate.inflate(R.layout.adapter_orders, null);
			}
			else{
				view = convertView;
			}
			
			OrderSumary o = getItem(position);			
			
			TextView txtPlanta = (TextView)view.findViewById(R.id.txtplanta_orden);
			txtPlanta.setText(o.planta);
			
			TextView txtTitulo = (TextView)view.findViewById(R.id.txttitulo_orden);
			txtTitulo.setText(o.titulo);
			
			TextView txtTotalHoras = (TextView)view.findViewById(R.id.txttotalhoras_orden);
			txtTotalHoras.setText(Utils.parse(o.horastotales) + "h " + Utils.parse(o.minutostotales) + "m");
			
			ImageView img = (ImageView)view.findViewById(R.id.icon_orden);
			/*Object obj =  listOrdenes.get(position);
			MtoOrdenes mtoOrden = (MtoOrdenes)obj;
			String estado = (String)mtoOrden.getEstado().toString().trim(); */
			String estado = o.estado.toString().trim();
			CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox_orden);
			
			if (SheetsManager.IsFinish(estado)){
				img.setImageResource(R.drawable.icon_orden_cerrada); 
				img.setTag(R.drawable.icon_orden_cerrada);					
				cb.setChecked(true);
			}
			else{
				img.setImageResource(R.drawable.icon_orden_abierta); 
				img.setTag(R.drawable.icon_orden_abierta);
				cb.setChecked(false);
			}				
			Utils.setTextViewText(view, R.id.txtresponsable, o.responsable);						
			return view;
	}	
}


