package com.comyted.modules.offers;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.comyted.R;
import com.comyted.generics.ListAdapter;
import com.comyted.models.ClientOffert;
import com.enterlib.app.UIUtils;

public class AdapterClientOffers extends ListAdapter<ClientOffert> {		
	public AdapterClientOffers(Context context, ClientOffert[] objects) {
		super(context, R.layout.adapter_client_offers, objects);		
	}		

	@Override
	protected void updateView(View view, ClientOffert item, int position) {
		UIUtils.setTextViewText(view,R.id.oferta_codoferta, String.valueOf(item.id));
		UIUtils.setTextViewText(view,R.id.oferta_descripcion, item.nombreproyecto);
		UIUtils.setTextViewText(view,R.id.oferta_contacto, item.nombrecontacto);
		UIUtils.setTextViewText(view,R.id.oferta_fecha_propuesta, item.fechapropuesta);
		UIUtils.setTextViewText(view,R.id.oferta_responsable, item.responsable);
		UIUtils.setTextViewText(view,R.id.oferta_tipo, item.tipooferta);
		UIUtils.setTextViewText(view, R.id.oferta_estado, item.estado);	
		
		TextView estado = (TextView) view.findViewById(R.id.oferta_estado);		
		if(item.estado.equalsIgnoreCase("Aceptada")){
			estado.setBackgroundResource(R.color.aceptada);
		}else if(item.estado.equalsIgnoreCase("Aplazada")){
			estado.setBackgroundResource(R.color.aplazada);
		}else if(item.estado.equalsIgnoreCase("Bloqueada")){
			estado.setBackgroundResource(R.color.bloqueada);
		}else if(item.estado.equalsIgnoreCase("Denegada")){
			estado.setBackgroundResource(R.color.denegada);
		}else if(item.estado.equalsIgnoreCase("En Espera")){
			estado.setBackgroundResource(R.color.en_espera);
		}else if(item.estado.equalsIgnoreCase("Presentada")){
			estado.setBackgroundResource(R.color.presentada);
		}else
			view.setBackgroundResource(0);
	}
}
