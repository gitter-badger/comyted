package com.comyted.repository;

import com.comyted.conectivity.GetOrdenesClient;
import com.comyted.models.Order;
import com.comyted.models.OrderSumary;
import com.enterlib.exceptions.InvalidOperationException;

public class OrderRepository implements IOrderRepository {

	GetOrdenesClient client;
	
	public OrderRepository() {
		client = new GetOrdenesClient();
	}
	
	@Override
	public OrderSumary[] getOrders() throws InvalidOperationException {
		return client.getOrders();
	}

	@Override
	public Order getOrder(int cod_orden) throws InvalidOperationException{
		return client.getOrder(cod_orden);
	}

}
