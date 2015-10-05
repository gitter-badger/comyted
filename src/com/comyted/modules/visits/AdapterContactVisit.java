package com.comyted.modules.visits;

import android.content.Context;
import android.view.View;

import com.comyted.R;
import com.comyted.generics.ListAdapter;
import com.comyted.models.ContactVisit;
import com.enterlib.app.UIUtils;

public class AdapterContactVisit extends ListAdapter<ContactVisit> {
	public AdapterContactVisit(Context context, ContactVisit[] objects) {
		super(context, R.layout.adapter_contact_visits, objects);
	}

	@Override
	protected void updateView(View view, ContactVisit item, int position) {
		UIUtils.setTextViewText(view, R.id.visit_fecha, item.fechavisita);
		UIUtils.setTextViewText(view, R.id.visit_fecha_limite, item.fechaproxima);
		UIUtils.setTextViewText(view, R.id.visit_fecha_alta, item.fechaalta);
		UIUtils.setTextViewText(view, R.id.visit_responsable, item.responsable);
		UIUtils.setTextViewText(view, R.id.visit_estado, item.estado);
		
	}
}