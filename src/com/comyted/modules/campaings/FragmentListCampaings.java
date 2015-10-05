package com.comyted.modules.campaings;

import com.comyted.R;
import com.comyted.conectivity.GetCampainsClient;
import com.comyted.models.ContactCampaing;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class FragmentListCampaings extends FragmentContactCampaings {
	@Override
	protected ICollectionRepository<ContactCampaing> createRepository() {	
		
		return new ICollectionRepository<ContactCampaing>() {			
			@Override
			public ContactCampaing[] getItems() throws InvalidOperationException {
				GetCampainsClient client = new GetCampainsClient();
				return client.ObtenerCampanas();
			}
		};
	}
	

	@Override
	protected String getNoItemsAlertMessage() {
		return getString(R.string.no_hay_campanas);
	}
}
