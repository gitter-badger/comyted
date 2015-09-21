package com.comyted.modules.visits;

import android.content.Context;
import android.view.View;

import com.comyted.R;
import com.comyted.generics.ListAdapter;
import com.comyted.models.ContactVisit;
import com.enterlib.app.PresentUtils;

public class AdapterContactVisit extends ListAdapter<ContactVisit> {
	public AdapterContactVisit(Context context, ContactVisit[] objects) {
		super(context, R.layout.adapter_contact_visits, objects);
	}

	@Override
	protected void updateView(View view, ContactVisit item, int position) {
		PresentUtils.setTextViewText(view, "Fecha: " ,R.id.visit_fecha, item.fechavisita);
		PresentUtils.setTextViewText(view, "Fecha Limite: " ,R.id.visit_fecha_limite, item.fechaproxima);
		PresentUtils.setTextViewText(view, "Fecha Alta: " ,R.id.visit_fecha_alta, item.fechaalta);
		PresentUtils.setTextViewText(view, "Responsable: " ,R.id.visit_responsable, item.responsable);
		PresentUtils.setTextViewText(view, "Estado: " ,R.id.visit_estado, item.estado);
		
	}
}