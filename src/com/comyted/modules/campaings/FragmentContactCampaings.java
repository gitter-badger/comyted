package com.comyted.modules.campaings;

import junit.framework.Assert;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.comyted.R;
import com.comyted.conectivity.GetCampainsClient;
import com.comyted.generics.DefaultComparator;
import com.comyted.generics.ListAdapter;
import com.comyted.generics.ListFragment;
import com.comyted.models.ContactCampaing;
import com.enterlib.app.UIUtils;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class FragmentContactCampaings extends ListFragment<ContactCampaing> {

	public FragmentContactCampaings() {
		setComparator(new DefaultComparator<ContactCampaing>());
	}
	
	@Override
	protected ICollectionRepository<ContactCampaing> createRepository() {
		Assert.assertTrue(getModelId() > 0);
		
		return new ICollectionRepository<ContactCampaing>() {
			GetCampainsClient client = new GetCampainsClient();
			@Override
			public ContactCampaing[] getItems() throws InvalidOperationException {
				return client.ObtenerCampanasContacto(getModelId());
			}
		};
	}

	@Override
	protected ListAdapter<ContactCampaing> createAdapter(ContactCampaing[] items) {
		return new ListAdapter<ContactCampaing>(getActivity(),R.layout.adapter_contact_campaings,items) {			
			@Override
			protected void updateView(View view, ContactCampaing item, int position) {
				UIUtils.setTextViewText(view,R.id.campaing_nombrecampana, item.nombrecampana);
				UIUtils.setTextViewText(view,"Inicio: ",R.id.campaing_fecha_ini, item.fechaini);
				UIUtils.setTextViewText(view,"Fin: ",R.id.campaing_fecha_fin, item.fechafin);
				UIUtils.setTextViewText(view,"Tipo de Campaña: ",R.id.campaing_tipocampana, item.tipocampana);
			}
		};
	}

	@Override
	protected Intent getItemViewIntent(ContactCampaing c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getNoItemsAlertMessage() {
		return getString(R.string.contacto_no_posee_campanas);
	}
	@Override
	protected String getFilterHint() {
		return getString(R.string.buscar_por_nombre);
	}
}
