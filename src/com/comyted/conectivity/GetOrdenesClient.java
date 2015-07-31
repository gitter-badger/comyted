package com.comyted.conectivity;

import com.comyted.models.Order;
import com.comyted.models.OrderSumary;
import com.enterlib.exceptions.InvalidOperationException;

public class GetOrdenesClient extends AppServiceClient{
	public GetOrdenesClient(){
		super("GetOrdenes.svc");
	}
	
	 public OrderSumary[] getOrders() throws InvalidOperationException {		 
		 return get(OrderSumary[].class, "ObtenerOrdenes");
	 }
	 
	 public Order getOrder(int cod_orden) throws InvalidOperationException{		 		 
		 return get(Order.class, "ObtenerOrden",
				 new RequestParameter("cod_orden", cod_orden));
	 }
}
