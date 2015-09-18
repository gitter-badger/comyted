package com.comyted.modules.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.comyted.Constants;
import com.comyted.EditFragment;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.ToUpperConverter;
import com.comyted.Utils;
import com.comyted.conectivity.GetContactosClient;
import com.comyted.conectivity.PostContactsClient;
import com.comyted.models.BaseModelComparer;
import com.comyted.models.BaseModelConverter;
import com.comyted.models.ContactEdit;
import com.comyted.models.IdNameValue;
import com.enterlib.app.DataViewModel;
import com.enterlib.app.EditableRepositoryViewModel;
import com.enterlib.app.IEditView;
import com.enterlib.data.IRepository;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.fields.FilterableSpinnerField;
import com.enterlib.fields.Form;
import com.enterlib.fields.SpinnerField;
import com.enterlib.fields.TextViewField;
import com.enterlib.validations.validators.IntegerValidator;
import com.enterlib.validations.validators.RegExValueValidator;
import com.enterlib.widgets.FilterableSpinner;

public class ActivityEditContact extends Activity {	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_clients);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FragmentEditContact()).commit();
		}	
		
		int id = getIntent().getIntExtra(Constants.ID, 0);			
		if(id > 0){
			getActionBar().setTitle("Editar");
		}
		else{
			getActionBar().setTitle("Nuevo Contacto");
		}        
	}
	
	public static class FragmentEditContact extends EditFragment {
		private ViewModel viewModel;
		private View rootView;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_edit_contact, null);
			return rootView;
		}
		
		@Override
		protected Form createForm() {			
			BaseModelComparer modelComparer = new BaseModelComparer();		
			BaseModelConverter modelToIdConverter = new  BaseModelConverter();
			RegExValueValidator emailValidator =new RegExValueValidator("(\\w+)(\\.(\\w+))*@(\\w+)(\\.(\\w+))*", getString(R.string.email_no_valido));
			ToUpperConverter toUpper =new ToUpperConverter();
			
			Form form = new Form();
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_nombrecontacto),"nombrecontacto", "Nombre", true));			
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_apellido1contacto), "apellido1contacto" , "Primer Apellido", true));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_apellido2contacto), "apellido2contacto", "Segundo Apellido", false));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_puesto), "puesto", "Puesto", false));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_direccion), "direccion", "Dirección", false));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_ciudad), "ciudad", "Ciudad", false).setValueConverter(toUpper));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_provincia), "provincia", "Provincia", false).setValueConverter(toUpper));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_codpos), "codpos", "Código Postal", false));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_pais), "pais", "País", false).setValueConverter(toUpper));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_telefono), "telefono", "Teléfono", false)
						.addValueValidator(new IntegerValidator(0, Integer.MAX_VALUE, getString(R.string.entero_positivo))));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_fax), "fax", "Fax", false)
						.addValueValidator(new IntegerValidator(0, Integer.MAX_VALUE, getString(R.string.entero_positivo))));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_email), "email", "Email", false).addValueValidator(emailValidator));
			form.addField(new TextViewField((TextView)findViewById(R.id.contact_observations), "observaciones", "Observaciones", false));													
			form.addValidator(new FilterableSpinnerField((FilterableSpinner)findViewById(R.id.contact_cliente), "codcliente", "Cliente", true).setComparer(modelComparer).setValueConverter(modelToIdConverter));			
			form.addValidator(new SpinnerField((Spinner)findViewById(R.id.contact_departamento), "coddpto", "Departamento",true).setComparer(modelComparer).setValueConverter(modelToIdConverter));
			return form;
		}
		
		private View findViewById(int id){
			return rootView.findViewById(id);
		}

		@Override
		protected Object getData() {
			return viewModel.getEntity();
		}

		@Override
		protected DataViewModel createViewModel() {				
			this.viewModel = new ViewModel(this, 
					getEntityId(), 
					getActivity().getIntent().getIntExtra(Constants.CLIENT_ID, 0));
			return viewModel;
		}
		
		@Override
		public void onDataLoaded() {
			Form form = getForm();
			((FilterableSpinnerField)form.getFieldById(R.id.contact_cliente)).setElements(viewModel.getClientes(), null);
			((SpinnerField)form.getFieldById(R.id.contact_departamento)).setElements(viewModel.getDepartamentos());
			
			super.onDataLoaded();
		}

	}
		
	
	static class ViewModel extends EditableRepositoryViewModel<ContactEdit>{

		public IdNameValue[] getDepartamentos() {
			return departamentos;
		}

		public IdNameValue[] getClientes() {
			return clientes;
		}

		private Repository rep;
		private IdNameValue[] departamentos;
		private IdNameValue[] clientes;
		private int clientId;

		public ViewModel(IEditView view, int id, int clientId) {
			super(view, new Repository(), id, new ContactEdit());
			this.rep = (Repository) getRepository();
			this.clientId= clientId;
			
		}
		
		@Override
		protected boolean loadAsync() throws Exception {
			departamentos = rep.ObtenerDepartamentos();
			clientes = rep.ObtenerListaClientes();
			if(clientId>0 && clientes!=null){
				
				for (int i = 0; i < clientes.length; i++) {
					if(clientes[i].id == clientId){
						clientes = new IdNameValue[]{clientes[i]};
						break;
					}
				}
			}
						
			return super.loadAsync();
		}
		
		@Override
		protected boolean saveAsync() throws Exception {
			ContactEdit c= getEntity();
			c.codusuario =MainApp.getCurrentUser().id;
			return super.saveAsync();
		}
		
	}
	
	static class Repository implements IRepository<ContactEdit>{
		GetContactosClient get = new GetContactosClient();
		PostContactsClient post = new PostContactsClient();
		
		@Override
		public ContactEdit getItem(int id) throws InvalidOperationException {
			return get.ObtenerContactoEdit(id);
		}

		public IdNameValue[] ObtenerDepartamentos()
				throws InvalidOperationException {
			return get.ObtenerDepartamentos();
		}

		public IdNameValue[] ObtenerListaClientes()
				throws InvalidOperationException {
			return get.ObtenerListaClientes();
		}

		@Override
		public boolean updateItem(ContactEdit item) throws InvalidOperationException {			
			return post.ActualizarContacto(item);
		}

		@Override
		public boolean createItem(ContactEdit item) throws InvalidOperationException {
			return post.GrabarContacto(item);
		}
				
		
	}
	
}
