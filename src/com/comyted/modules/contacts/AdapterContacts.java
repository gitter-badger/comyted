package com.comyted.modules.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comyted.R;
import com.comyted.models.ClientContactSummary;
import com.comyted.models.ContactSummary;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.UIUtils;

public class AdapterContacts extends CollectionAdapter<ContactSummary> {

	private LayoutInflater _inflater;

	public AdapterContacts(Context context, ContactSummary[]items) {
		super(context, R.layout.adapter_contacts , items);
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if(convertView != null)
			view = convertView;
		else		
			view = _inflater.inflate(R.layout.adapter_contacts, null);		
		
		ContactSummary c = getItem(position);
		UIUtils.setTextViewTextOrCollapse(view, R.id.contact_nombrecontacto, c.nombrecontacto);
		UIUtils.setTextViewTextOrCollapse(view, R.id.contact_nombreempresa, c.nombreempresa);
		UIUtils.setTextViewTextOrCollapse(view, R.id.contact_puesto, c.puesto);
		UIUtils.setTextViewTextOrCollapse(view, R.id.contact_telefono, c.telefono);
		UIUtils.setTextViewTextOrCollapse(view, R.id.contact_fax, c.fax);
		UIUtils.setTextViewTextOrCollapse(view, R.id.contact_email, c.email);
		
		return view;
	}

}
