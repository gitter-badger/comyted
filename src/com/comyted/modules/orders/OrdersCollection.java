package com.comyted.modules.orders;

import java.util.ArrayList;

import com.comyted.OpenCloseResult;
import com.comyted.models.OrderSumary;
import com.comyted.modules.sheets.SheetsManager;

public class OrdersCollection {

	ArrayList<OrderSumary> listOpens  = new ArrayList<OrderSumary>();
	ArrayList<OrderSumary> listClose  = new ArrayList<OrderSumary>();
	
	public OpenCloseResult<OrderSumary> UserOrders = new OpenCloseResult<OrderSumary>();
	public OpenCloseResult<OrderSumary> OtherOrders = new OpenCloseResult<OrderSumary>();
	
	public OrdersCollection() {
		
	}
	
	public void SetOrders(OrderSumary[]orders , String username) {
		SetOrdenes(true, username, orders, UserOrders);
		SetOrdenes(false, username, orders, OtherOrders);
	}
	
	private void SetOrdenes(boolean onlyUser, 
			String username , 
			OrderSumary[] orders, 
			OpenCloseResult<OrderSumary> result)
	{	
		listOpens.clear();
		listClose.clear();	
		
		for (OrderSumary o : orders) {
				if(o.responsable != null)
				{						
					if( (onlyUser  && o.responsable.equals(username)) || 
						(!onlyUser && !o.responsable.equals(username)) )
					{
							if(SheetsManager.IsFinish(o.estado))
								listClose.add(o);
							else
								listOpens.add(o);
					}
				}
		}
		result.Opens= new OrderSumary[listOpens.size()];
		listOpens.toArray(result.Opens);
		
		result.Closed = new OrderSumary[listClose.size()];
		listClose.toArray(result.Closed);
	}

}
