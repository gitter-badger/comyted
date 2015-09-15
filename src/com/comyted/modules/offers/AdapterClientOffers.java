package com.comyted.modules.offers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comyted.R;
import com.comyted.models.ClientOffert;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.PresentUtils;
import com.enterlib.converters.Converters;

public class AdapterClientOffers extends CollectionAdapter<ClientOffert> {
	
	private LayoutInflater _inflater;

	public AdapterClientOffers(Context context, ClientOffert[] objects) {
		super(context, R.layout.adapter_client_offers, objects);
	
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if(convertView != null)
			view = convertView;
		else
			view = _inflater.inflate(R.layout.adapter_client_offers, null);		
		
		ClientOffert c = getItem(position);
		PresentUtils.setTextViewText(view, R.id.oferta_codoferta, String.valueOf(c.codoferta));
		PresentUtils.setTextViewText(view, "Contacto: ",R.id.oferta_contacto, c.nombrecontacto);
		PresentUtils.setTextViewText(view, "Fecha: ", R.id.oferta_fecha_propuesta, Converters.DateToStringConverter.getString(c.fechapropuesta));
		PresentUtils.setTextViewText(view, "Responsable: ", R.id.oferta_responsable, c.responsable);
		PresentUtils.setTextViewText(view, "Tipo: ", R.id.oferta_tipo, c.tipooferta);
		PresentUtils.setTextViewText(view, "Estado: ", R.id.oferta_estado, c.estadooferta);
		
		return view;
	}
}
