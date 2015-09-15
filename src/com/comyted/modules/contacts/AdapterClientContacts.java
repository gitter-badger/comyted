package com.comyted.modules.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.comyted.R;
import com.comyted.models.ClientContactSummary;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.PresentUtils;

public class AdapterClientContacts extends CollectionAdapter<ClientContactSummary> {

	LayoutInflater _inflater;
	
	public AdapterClientContacts(Context context, ClientContactSummary[] objects) {
		super(context, R.layout.adapter_client_contacts, objects);
	
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if(convertView != null)
			view = convertView;
		else		
			view = _inflater.inflate(R.layout.adapter_client_contacts, null);		
		
		ClientContactSummary c = getItem(position);
		PresentUtils.setTextViewText(view, R.id.contact_nombrecontacto, c.nombrecontacto);
		PresentUtils.setTextViewText(view, R.id.contact_puesto, c.puesto);
		PresentUtils.setTextViewText(view, R.id.contact_departamento, c.departamento);
		PresentUtils.setTextViewText(view, R.id.contact_telefono, c.telefono);
		PresentUtils.setTextViewText(view, R.id.contact_fax, c.fax);
		PresentUtils.setTextViewText(view, R.id.contact_email, c.email);
		
		return view;
	}
	
	
}
