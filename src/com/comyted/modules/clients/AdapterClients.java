package com.comyted.modules.clients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comyted.R;
import com.comyted.models.ClientSummary;
import com.enterlib.app.CollectionAdapter;
import com.enterlib.app.UIUtils;

public class AdapterClients extends CollectionAdapter<ClientSummary> {

	LayoutInflater _inflater;
	
	public AdapterClients(Context context, ClientSummary[] objects) {
		super(context, R.layout.adapter_clients, objects);
	
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if(convertView != null)
			view = convertView;
		else		
			view = _inflater.inflate(R.layout.adapter_clients, null);		
		
		ClientSummary c = getItem(position);
		UIUtils.setTextViewText(view, R.id.client_nombreempresa, c.nombreempresa);
		UIUtils.setTextViewTextOrCollapse(view, R.id.client_direccion, c.direccionempresa);
		UIUtils.setTextViewTextOrCollapse(view, R.id.client_telefono, c.telefono);
		UIUtils.setTextViewTextOrCollapse(view, R.id.client_fax, c.fax);
		UIUtils.setTextViewTextOrCollapse(view, R.id.client_email, c.email);
		
		return view;
	}
	
}
