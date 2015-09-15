package com.comyted.modules.offers;

import com.comyted.models.ClientOffert;
import com.comyted.repository.IOffertRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

public class ViewModelClientOffers extends DataViewModel {
	IOffertRepository repository;
	int clientId;
	ClientOffert[]offerts;
	 
	protected ClientOffert[] getOfferts() {
		return offerts;
	}

	public ViewModelClientOffers(IDataView view, IOffertRepository repository,
			int clientId) {
		super(view);
		this.repository = repository;
		this.clientId = clientId;
	}



	protected int getClientId() {
		return clientId;
	}

	@Override
	protected boolean loadAsync() throws Exception {
		offerts = repository.getClientOfferts(clientId);
		return true;
	}
	
}
