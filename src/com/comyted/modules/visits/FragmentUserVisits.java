package com.comyted.modules.visits;

import junit.framework.Assert;

import com.comyted.MainApp;
import com.comyted.conectivity.GetVisitasClient;
import com.comyted.models.AppUser;
import com.comyted.models.ContactVisit;
import com.enterlib.data.ICollectionRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class FragmentUserVisits extends FragmentContactVisits {

	@Override
	protected ICollectionRepository<ContactVisit> createRepository() {				
		return new ICollectionRepository<ContactVisit>() {
			@Override
			public ContactVisit[] getItems() throws InvalidOperationException {
				GetVisitasClient get = new GetVisitasClient();
				AppUser user = MainApp.getCurrentUser();			
				Assert.assertNotNull(user);
				return get.ObtenerVisitas(user.id);
			}
		};
	}
}
