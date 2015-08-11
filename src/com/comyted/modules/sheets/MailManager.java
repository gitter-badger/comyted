package com.comyted.modules.sheets;

import com.comyted.Messages;
import com.comyted.conectivity.GetHojasClient;
import com.comyted.conectivity.PostHojasClient;
import com.comyted.models.SheetEdit;
import com.comyted.models.SheetEmailRequest;
import com.comyted.repository.IPeticionariosRepository;
import com.comyted.repository.ISheetsRepository;
import com.comyted.repository.IUserRepository;
import com.comyted.repository.PeticionariosRepository;
import com.comyted.repository.SheetsRepository;
import com.comyted.repository.UserRepository;
import com.enterlib.exceptions.InvalidOperationException;

public class MailManager {
	
	IUserRepository userRepository;	
	IPeticionariosRepository peticionarioRespository;
	ISheetsRepository sheetRepository;
	int userId;
	
	public MailManager(int userId,
			IUserRepository userRepository,
			IPeticionariosRepository peticionarioRespository,
			ISheetsRepository sheetRepository) {
		
		this.userRepository = userRepository;
		this.peticionarioRespository = peticionarioRespository;
		this.sheetRepository = sheetRepository;
	}
	
	public MailManager(int userId){		
		this(userId,
			 new UserRepository(),
			 new PeticionariosRepository(),
			 new SheetsRepository(new GetHojasClient(), new PostHojasClient(), null));
	}
	
	
	public String getUserEmail(String login) throws InvalidOperationException {
		return userRepository.getUserEmail(login);
	}
		
	
	public String getUserEmail(int id) throws InvalidOperationException {
		return userRepository.getUserEmail(id);
	}		
	
		
	public String getPeticionarioEmail(int peticionarioId) throws InvalidOperationException {
		return peticionarioRespository.getPeticionarioEmail(peticionarioId);
	}
		
	public boolean createSheetEmailRequest(SheetEmailRequest entry) throws InvalidOperationException{		
		return sheetRepository.createSheetEmailRequest(entry);						
	}

	public SheetEmailRequest getSheetEmailRequest(int sheetId) throws InvalidOperationException {
		   SheetEdit sheetEdit = sheetRepository.getSheetEdit(sheetId);
		   if(sheetEdit == null || 
			   sheetEdit.codtecnico <= 0 ||
			   sheetEdit.codpeticionario <= 0 ){
			   throw new InvalidOperationException(Messages.getString("MailingManager.hoja_no_existe")); //$NON-NLS-1$
		   }
		   		   
		   SheetEmailRequest request = new SheetEmailRequest();
		   request.codhoja = sheetId;				   				     				  
		   request.asunto =String.format(Messages.getString("MailingManager.RequestTitle"),sheetId); //$NON-NLS-1$
		   request.codpeticionario  = sheetEdit.codpeticionario;
		   request.codtecnico = sheetEdit.codtecnico;
		   request.codusuario = userId;
		   request.fechahoja  = sheetEdit.fechahoja;			 
		   request.emailtecnico = userRepository.getUserEmail(sheetEdit.codtecnico);	
		   request.emailcliente = peticionarioRespository.getPeticionarioEmail(sheetEdit.codpeticionario);
		   return request;
	}	
}
