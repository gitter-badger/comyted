package com.comyted.modules.sheets;

import java.util.ArrayList;

import com.comyted.R;
import com.comyted.Utils;
import com.comyted.conectivity.GetHojasClient;
import com.comyted.conectivity.GetTareasClient;
import com.comyted.conectivity.GetUsersClient;
import com.comyted.conectivity.PostHojasClient;
import com.comyted.models.AppUser;
import com.comyted.models.IdNameValue;
import com.comyted.models.Order;
import com.comyted.models.PlantEngine;
import com.comyted.models.SheetEdit;
import com.comyted.models.SheetSignatureState;
import com.comyted.modules.admin.UsersManager;
import com.comyted.persistence.LocalDatabase;
import com.comyted.repository.CargoRegionRepository;
import com.comyted.repository.ICargoRegionRepository;
import com.comyted.repository.IOrderRepository;
import com.comyted.repository.IPeticionariosRepository;
import com.comyted.repository.IPlantRepository;
import com.comyted.repository.ISheetTypesRepository;
import com.comyted.repository.IStateRepository;
import com.comyted.repository.IUserRepository;
import com.comyted.repository.IWorkTypeRepository;
import com.comyted.repository.OrderRepository;
import com.comyted.repository.PeticionariosRepository;
import com.comyted.repository.PlantRepository;
import com.comyted.repository.SheetTaskRepository;
import com.comyted.repository.SheetTypesRepository;
import com.comyted.repository.SheetsRepository;
import com.comyted.repository.StateRepository;
import com.comyted.repository.UserRepository;
import com.comyted.repository.WorkTypeRepository;
import com.enterlib.app.EditViewModel;
import com.enterlib.StringUtils;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.threading.AsyncManager;
import com.enterlib.threading.IWorkPost;

public class ViewModelEditSheet extends EditViewModel  {

	int orderId;
	int sheetId;
	IdNameValue[] plantas;
	IdNameValue[] trabajos;
	IdNameValue[] cargoregion;
	IdNameValue[] estados;
	IdNameValue[] tecnicos;
	IdNameValue[] tipohojas; 
	PlantEngine[] engines; 
	IdNameValue[] peticionarios;
	Order order;			
	SheetEdit sheetEdit;
	SheetSignatureState sheetStatus;		
	SheetsManager sheetsManager;	
	IPeticionariosRepository peticionarioRep;
	ISheetTypesRepository sheetTypeRep;
	ICargoRegionRepository cargoRegRep;
	IWorkTypeRepository workTypeRep;
	IStateRepository stateRep;
	IPlantRepository plantRep;
	IUserRepository userRep;
	IOrderRepository ordersRep;
	AppUser user;
	ArrayList<String>notifications = new  ArrayList<String>();

	public IdNameValue[] getPlantas(){ return plantas; }
	
	public IdNameValue[] getTrabajos(){ return trabajos; }
	
	public IdNameValue[] getCargoregion(){ return cargoregion; }
	
	public IdNameValue[] getEstados(){ return estados; }
	
	public IdNameValue[] getTecnicos(){ return tecnicos; }
	
	public IdNameValue[] getTipoHojas(){ return tipohojas; }
	
	public IdNameValue[] getPecticionarios(){ return peticionarios; }
	
	public PlantEngine[] getMotores(){return engines;}	
	
	public SheetEdit getSheetEdit(){
		return sheetEdit;
	}

	public Order getOrder() {
		return order;
	}
	
	public int getSheetId(){
		return  sheetId;
	}
	
	public ArrayList<String> getNotifications(){
		return notifications;
	}
	
	 /** For Production 
	  * */
	public ViewModelEditSheet(AppUser user, IViewSheetEdit view, int orderId, int sheetId, LocalDatabase localDb) {
		super(view);					
		
		this.orderId = orderId;
		this.sheetId = sheetId;
		this.user = user;
		GetHojasClient client = new GetHojasClient();
		PostHojasClient post = new PostHojasClient();
		GetUsersClient usersClient = new GetUsersClient();
		
		SheetsRepository rep = new SheetsRepository(client, post, localDb);		
				
		peticionarioRep = new PeticionariosRepository(client);
		sheetTypeRep = new SheetTypesRepository(client);
		cargoRegRep = new CargoRegionRepository(client);
		workTypeRep = new WorkTypeRepository(client);
		stateRep = new StateRepository();
		plantRep = new PlantRepository(client);
		userRep = new UserRepository();
		ordersRep = new OrderRepository();
		
		//only need get operations
		sheetsManager = new SheetsManager(rep, 
				new SheetTaskRepository(null, new GetTareasClient(), null), 
				new UsersManager(localDb, usersClient));		
	}
	
	/** For Dependency Injection Constructor
	 * @param view
	 * @param orderId
	 * @param sheetId
	 * @param sheetsManager
	 * @param peticionarioRep
	 * @param sheetTypeRep
	 * @param cargoRegRep
	 * @param workTypeRep
	 * @param stateRep
	 * @param plantRep
	 * @param userRep
	 */
	public ViewModelEditSheet(IViewSheetEdit view, AppUser user, int orderId, int sheetId,
			SheetsManager sheetsManager,
			IOrderRepository ordersRep,
			IPeticionariosRepository peticionarioRep,
			ISheetTypesRepository sheetTypeRep,
			ICargoRegionRepository cargoRegRep,
			IWorkTypeRepository workTypeRep, IStateRepository stateRep,
			IPlantRepository plantRep, IUserRepository userRep) {		
		super(view);
		
		this.user = user;
		this.orderId = orderId;
		this.sheetId = sheetId;
		this.sheetsManager = sheetsManager;
		this.ordersRep = ordersRep;
		this.peticionarioRep = peticionarioRep;
		this.sheetTypeRep = sheetTypeRep;
		this.cargoRegRep = cargoRegRep;
		this.workTypeRep = workTypeRep;
		this.stateRep = stateRep;
		this.plantRep = plantRep;
		this.userRep = userRep;
	}
	
	private void checkPlanta() throws InvalidOperationException {
		
		if(sheetEdit.codplanta == 0 && !StringUtils.isNullOrWhitespace(order.planta)){
			//poner el id de la planta de la orden en la hoja
			
			IdNameValue orderPlanta = Utils.getIdName(plantas, order.planta);
			
			if(orderPlanta != null){
				
				//si hay alguna planta en la orden .Obtener el peticionario asociado a la orden				
				sheetEdit.codplanta = orderPlanta.id;
					
//				//asociar el id del peticionario de la orden en la hoja.
//				if(sheetEdit.codpeticionario == 0 && !StringUtils.isNullOrWhitespace(order.peticionario))
//				{
//					peticionarios =  peticionarioRep.getPeticionariosByPlant(orderPlanta.id);				
//					IdNameValue orderPeticionario = Utils.getIdName(peticionarios, order.peticionario);
//					
//					if(orderPeticionario!=null)
//					{											
//						sheetEdit.codpeticionario = orderPeticionario.id;
//					}
//					else{
//						notifications.add(String.format("El peticionario '%s' de la orden '%s' no se encuentra o no está asociado a la planta '%s'.", 
//								order.peticionario, order.titulo ,orderPlanta.name));
//					}
//				}
			}else{
				notifications.add(String.format("La planta '%s' de la orden '%s' no se encuentra o no está activa.\nActivela para adicionar hojas a la orden.", 
										         order.planta, order.titulo));
			}
		}
	}
				
	@Override
	protected boolean saveAsync() throws Exception {
		boolean result;
		 if(sheetId > 0)
			result = sheetsManager.updateSheet(sheetEdit);
		else
			result = sheetsManager.createSheet(sheetEdit);
		 if(!result){
			 throw new InvalidOperationException(getView().getString(R.string.no_se_pudo_salvar));
		 }
		return true;
	}
	
	public void loadEngines(final int plantId, final String plantName){
		IViewSheetEdit view = (IViewSheetEdit) getView();
		if(view!=null && view.isValid())
			view.showProgressDialog();	
		
		AsyncManager.postAsync(new IWorkPost() {				
				@Override
				public boolean runWork() throws Exception {
					
					notifications.clear();
					engines = plantRep.getEnginesByPlant(plantId);
					peticionarios =  peticionarioRep.getPeticionariosByPlant(plantId);
					
					if(sheetEdit != null)
						//asociar la planta a la hoja
						sheetEdit.codplanta = plantId;
					
					if(order!=null){
						//validar el peticionario
						IdNameValue orderPeticionario = Utils.getIdName(peticionarios, order.peticionario);
						
						if(orderPeticionario != null){
							//la orden tiene peticionario ,el peticionario se queda fijo
							peticionarios = new IdNameValue[]{ orderPeticionario };
							
							if(sheetEdit!=null)
								//asociar el peticionario
								sheetEdit.codpeticionario = orderPeticionario.id;							
						}else{
							notifications.add(String.format("El peticionario '%s' asociado a la orden '%s' de la hoja, no se encuentra o no está asociado a la planta '%s'.", 
															order.peticionario, order.titulo, plantName));
						}
					}
					ViewModelEditSheet.this.engines = engines;
					ViewModelEditSheet.this.peticionarios = peticionarios;
					return true;
				}
				
				@Override
				public void onWorkFinish(Exception workException) {		
					IViewSheetEdit view = (IViewSheetEdit) getView();
					if(view !=null && view.isValid()){									
						view.dismisProgressDialog();
						if(workException!=null)
							view.onFailure(workException);
						else{
							view.onEnginesLoaded();
						}
					}
					onLoaded(workException);				
				}
			});
	}

	@Override
	protected boolean loadAsync() throws Exception {
		notifications.clear();
		
		if(user == null){
    		throw new InvalidOperationException("No hay usuario");
    	}
		
		if(sheetId > 0)
    	{    	    	
    		sheetEdit = sheetsManager.getSheetEdit(sheetId);
    		if(sheetEdit == null)
    			throw new InvalidOperationException("No se pudo cargar la hoja.");
    	}
    	else{
    		sheetEdit = new SheetEdit();
    		sheetEdit.codusuario = user.id;
    	}
    	
    	if(sheetEdit.codorden > 0)
    		orderId = sheetEdit.codorden;    		    	
    	else
    		sheetEdit.codorden = orderId;    	           			
		
    	//fetch the spliters data
    	trabajos  = workTypeRep.getWorkTypes();
    	cargoregion = cargoRegRep.getCargoRegions();	    	
    	estados = stateRep.getStates();
    	tipohojas = sheetTypeRep.getSheetTypes();	
    	plantas  = plantRep.getPlantIdName();
    	AppUser[] usersTecnicos = userRep.getTecnicos();    	
    	    
		if(orderId > 0)
		{	
			IViewSheetEdit view = (IViewSheetEdit) getView();
			//get the order for the sheet
			order = ordersRep.getOrder(orderId);
			if(order == null){
				throw new InvalidOperationException(view!=null?
						view.getString(R.string.order_not_found):"orden no encontrada");
			}
			
			if(sheetEdit.expediente == null)
				sheetEdit.expediente = order.expediente;
			if(sheetEdit.numseriemotor==null)
				sheetEdit.numseriemotor = order.numserie;
			
			checkPlanta();					
		}
		
		if(!user.jefeDepto)
		{					
			//Verificar si el usuario es tecnico , de lo contrario la operacion esta prohibida		
			if(!user.tecnico)
			{
				tecnicos = new IdNameValue[0];
				notifications.add(String.format("El usuario %s no es técnico", user.nombre));					
			}
			else 
			{
				// solo se puede seleccionar un usuario, el tecnico.
				usersTecnicos = new AppUser[]{ user };
				sheetEdit.codtecnico = user.id;
			}
		}
		
		tecnicos = new IdNameValue[usersTecnicos.length];
		for (int i = 0; i < usersTecnicos.length; i++) {
			tecnicos[i] = new IdNameValue(usersTecnicos[i].id, usersTecnicos[i].nombre);
		}
		
		return true;
	}
	
}
