package com.comyted.modules.visits;

import junit.framework.Assert;
import android.content.Intent;
import android.view.View;
import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.conectivity.GetVisitasClient;
import com.comyted.generics.DefaultComparator;
import com.comyted.generics.ListAdapter;
import com.comyted.generics.ListFragment;
import com.comyted.models.ContactVisit;
import com.enterlib.app.PresentUtils;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class FragmentContactVisits extends ListFragment<ContactVisit> {
	
	public FragmentContactVisits() {
		setComparator(new DefaultComparator<ContactVisit>(){
			@Override
			public int compare(ContactVisit lhs, ContactVisit rhs) {				
				return lhs.fechavisita.compareTo(rhs.fechavisita)*Order;
			}
		});
	}
	

	@Override
	protected ICollectionRepository<ContactVisit> createRepository() {
		final int contactId = getActivity().getIntent().getIntExtra(Constants.ID, 0);		
		Assert.assertTrue(contactId > 0);
		
		return new ICollectionRepository<ContactVisit>() {
			@Override
			public ContactVisit[] getItems() throws InvalidOperationException {
				GetVisitasClient get = new GetVisitasClient();
				return get.ObtenerVisitasUsuarioContacto(contactId, MainApp.getCurrentUser().id);
			}
		};
		
	}
	
	@Override
	protected ListAdapter<ContactVisit> createAdapter(ContactVisit[] items) {
		return new ListAdapter<ContactVisit>(getActivity(),R.layout.adapter_contact_visits, items) {			
			@Override
			protected void updateView(View view, ContactVisit item, int position) {
				PresentUtils.setTextViewText(view, "Fecha: " ,R.id.visit_fecha, item.fechavisita);
				PresentUtils.setTextViewText(view, "Fecha Limite: " ,R.id.visit_fecha_limite, item.fechalimite);
				PresentUtils.setTextViewText(view, "Fecha Alta: " ,R.id.visit_fecha_alta, item.fechaalta);
				PresentUtils.setTextViewText(view, "Responsable: " ,R.id.visit_responsable, item.responsable);
				PresentUtils.setTextViewText(view, "Estado: " ,R.id.visit_estado, item.estado);
				
			}
		};
	}
	
	@Override
	protected Intent getItemViewIntent(ContactVisit c) {
		// TODO Auto-generated method stub
		return null;
	}

}
