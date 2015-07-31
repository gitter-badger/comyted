package com.comyted.repository;

import com.comyted.models.IdNameValue;

public class StateRepository implements IStateRepository {
	
	IdNameValue[]states;
	
	public StateRepository() {
		states = new IdNameValue[]{
				new IdNameValue(1, Messages.getString("StateRepository.Abierta")), //$NON-NLS-1$
				new IdNameValue(2, Messages.getString("StateRepository.Cerrada")) //$NON-NLS-1$
		};
	}
	
	@Override
	public IdNameValue[] getStates() {
		return states;
	}

}
