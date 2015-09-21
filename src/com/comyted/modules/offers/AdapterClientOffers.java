package com.comyted.modules.offers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comyted.R;
import com.comyted.generics.ListAdapter;
import com.comyted.models.ClientOffert;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.PresentUtils;
import com.enterlib.converters.Converters;

public class AdapterClientOffers extends ListAdapter<ClientOffert> {		
	public AdapterClientOffers(Context context, ClientOffert[] objects) {
		super(context, R.layout.adapter_client_offers, objects);		
	}		

	@Override
	protected void updateView(View view, ClientOffert item, int position) {
		PresentUtils.setTextViewText(view, R.id.oferta_codoferta, String.valueOf(item.id));
		PresentUtils.setTextViewText(view, R.id.oferta_descripcion, item.nombreproyecto);
		PresentUtils.setTextViewText(view, "Contacto: ",R.id.oferta_contacto, item.nombrecontacto);
		PresentUtils.setTextViewText(view, "Fecha: ", R.id.oferta_fecha_propuesta, item.fechapropuesta);
		PresentUtils.setTextViewText(view, "Responsable: ", R.id.oferta_responsable, item.responsable);
		PresentUtils.setTextViewText(view, "Tipo: ", R.id.oferta_tipo, item.tipooferta);
		PresentUtils.setTextViewText(view, "Estado: ", R.id.oferta_estado, item.estado);	
	}
}
