package com.comyted.repository;

import com.comyted.models.Order;
import com.comyted.models.OrderSumary;
import com.enterlib.exceptions.InvalidOperationException;

public interface IOrderRepository {
	
	OrderSumary[] getOrders()throws InvalidOperationException;
	
	Order getOrder(int cod_orden) throws InvalidOperationException;
}
