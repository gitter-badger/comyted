package com.comyted.modules.visits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comyted.R;
import com.comyted.models.ClientVisit;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.PresentUtils;
import com.enterlib.converters.Converters;

public class AdapterClientVisits extends CollectionAdapter<ClientVisit>{
	private LayoutInflater _inflater;

	public AdapterClientVisits(Context context, ClientVisit[] objects) {
		super(context, R.layout.adapter_client_visits, objects);
	
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if(convertView != null)
			view = convertView;
		else
			view = _inflater.inflate(R.layout.adapter_client_visits, null);		
		
		ClientVisit c = getItem(position);
		PresentUtils.setTextViewText(view, "Fecha: ", R.id.visit_fecha, Converters.DateToStringConverter.getString(c.fechavisita));
		PresentUtils.setTextViewText(view, "Contacto: ",R.id.visit_contacto, c.contacto);
		PresentUtils.setTextViewText(view, "Estado: ", R.id.visit_estado, c.estado);
		PresentUtils.setTextViewText(view, R.id.contact_telefono, c.telefono);
		PresentUtils.setTextViewText(view, R.id.contact_fax, c.fax);
		PresentUtils.setTextViewText(view, R.id.contact_email, c.email);
		
		return view;
	}
		
}
