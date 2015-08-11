package com.comyted.modules.orders;

import junit.framework.Assert;

import com.comyted.models.AppUser;
import com.comyted.models.OrderSumary;
import com.comyted.repository.IOrderRepository;
import com.comyted.repository.OrderRepository;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.IReporterDataView;

public class ViewModelListOrders extends DataViewModel<IReporterDataView> {
	
	
	
	AppUser user;
//	AsyncResultTask<?> _loadTask;
//	AsyncResultTask<?> _loadSummarys;	
	OrdersCollection orders = new OrdersCollection();	
	IOrderRepository ordersRespository;	
	
	public OrdersCollection getOrders() {
		return orders;
	}

	public void setOrders(OrdersCollection orders) {
		this.orders = orders;
	}

	public ViewModelListOrders(
			IOrderRepository ordersRespository, 
			AppUser user,
			IReporterDataView view) {
		
		super(view);
		
		Assert.assertNotNull(user);
		Assert.assertNotNull(ordersRespository);
		
		this.ordersRespository = ordersRespository;
		this.user = user;
	}

	public ViewModelListOrders(AppUser user, IReporterDataView view){
		this(new OrderRepository(), user, view);
	}
	
	
	@Override
	protected boolean loadAsync() throws Exception{
		OrderSumary[] result = ordersRespository.getOrders();
		orders.SetOrders(result, user.nombre);
		return true;
	}
	
	
//	public void getOrders(final IResultNotifyCallback<OrderSumary[]>callback){							
//		if(user == null || !user.isServiceManager())
//		{
//			callback.operationCompleted(null, new AccessDeniedException());
//			return;
//		}
//	
//		_loadSummarys = new AsyncResultTask<OrderSumary[]>(callback){
//			@Override
//			protected OrderSumary[] doInBackground() throws InvalidParameterException, OperationApplicationException {							
//				OrderSumary[] sheet = ordersRespository.getOrders();
//				return sheet;
//			}			
//		};
//		_loadSummarys.run();
//	}
//	
//	public void getOrdersSplited(final IResultNotifyCallback<OrdersCollection>callback){		
//		final AppUser user = MainApp.getCurrentUser();			
//		if(user == null || !user.isServiceManager())
//		{
//			callback.operationCompleted(null, new AccessDeniedException(MainApp.getInstance().getString(R.string.no_tiene_acceso)));
//			return;
//		}
//	
//		_loadSummarys = new AsyncResultTask<OrdersCollection>(callback){
//			@Override
//			protected OrdersCollection doInBackground() throws InvalidParameterException, OperationApplicationException {												
//				OrderSumary[] result  = ordersRespository.getOrders();	
//				if(result == null || result.length == 0)
//					return null;
//						
//				orders.SetOrders(result, user.nombre);
//				return orders;
//			}			
//		};
//		_loadSummarys.run();
//	}	
//
//	public void getOrder(final int orderId, IResultNotifyCallback<Order>callback){
//		AppUser user = MainApp.getCurrentUser();				
//		if(user == null || !user.isServiceManager())
//		{
//			callback.operationCompleted(null, new AccessDeniedException());
//			return;
//		}
//		_loadTask = new AsyncResultTask<Order>(callback) {
//			@Override
//			protected Order doInBackground() throws InvalidParameterException,
//					OperationApplicationException {
//				return ordersRespository.getOrder(orderId);
//			}
//			
//		};
//		_loadTask.run();
//	}
}
