package com.comyted.modules.orders;

import com.comyted.models.Order;
import com.comyted.repository.IOrderRepository;
import com.comyted.repository.OrderRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IDataView;

public class ViewModelOrder extends DataViewModel<IDataView> {
	
	Order order;
	IOrderRepository repository;
	private int cod_orden;
	
	public ViewModelOrder(IDataView view, int cod_orden){
		this(new OrderRepository(), view, cod_orden);
	}
	
	public ViewModelOrder(IOrderRepository repository, IDataView view, int cod_orden) {
		super(view);
		
		this.repository = repository;
		this.cod_orden = cod_orden;
	}

	public Order getOrder() {
		return order;
	}

	@Override
	protected boolean loadAsync() throws Exception {
		order = repository.getOrder(cod_orden);
		return true;
	}

}
