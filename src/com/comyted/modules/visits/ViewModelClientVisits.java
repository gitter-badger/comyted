package com.comyted.modules.visits;

import com.comyted.MainApp;
import com.comyted.models.ClientVisit;
import com.comyted.repository.IVisitRepository;
import com.comyted.repository.VisitRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

public class ViewModelClientVisits extends DataViewModel {
	int clientId;
	IVisitRepository repository;
	ClientVisit[]visits;
	
	public ViewModelClientVisits(int clientId, IDataView view) {
		this(clientId, new VisitRepository(), view);
	}

	public ViewModelClientVisits(int clientId, IVisitRepository repository, IDataView view) {
		super(view);
		this.clientId = clientId;
		this.repository = repository;
	}

	@Override
	protected boolean loadAsync() throws Exception {
		visits = repository.getClientVisits(clientId, MainApp.getCurrentUser().id);
		return true;
	}

	public int getClientId() {
		return clientId;
	}

	public ClientVisit[] getVisits() {
		return visits;
	}
	
	
}
