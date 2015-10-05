package com.comyted.modules.offers;

import com.comyted.R;
import com.comyted.conectivity.GetOfertasClient;
import com.comyted.models.ClientOffert;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class FragmentListOffers extends FragmentClientOffers {
	@Override
	protected ICollectionRepository<ClientOffert> createRepository() {			
		return new ICollectionRepository<ClientOffert>() {						
			@Override
			public ClientOffert[] getItems() throws InvalidOperationException {
				GetOfertasClient client = new GetOfertasClient();
				return client.ObtenerOfertas();
			}
		};
	}
	
	@Override
	protected String getNoItemsAlertMessage() {
		return getString(R.string.no_hay_ofertas);
	}
	

}
