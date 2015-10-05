/**
 * 
 */
package com.comyted.modules.clients;

import java.util.ArrayList;

import junit.framework.Assert;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.R;
import com.comyted.Utils;
import com.comyted.conectivity.GetClientesClient;
import com.comyted.conectivity.PostClientesClient;
import com.comyted.models.Client;
import com.comyted.repository.ClientRepository;
import com.comyted.testing.repository.LocalJSONClientRepository;
import com.enterlib.DialogUtil;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.fields.Form;
import com.enterlib.fields.TextViewField;
import com.enterlib.mvvm.DefaultEditView;
import com.enterlib.validations.ErrorInfo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author ansel
 *
 */
public class ActivityEditClient extends Activity {	
	private Form form;
	private ViewModelEditClient vm;
	private EditView view;
	private boolean FromSavedInstance;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_client);		
		setResult(RESULT_CANCELED);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);      		
		
		Client client = (Client) getIntent().getSerializableExtra(Constants.CLIENT);
		Assert.assertNotNull(client);
		
		view = new EditView(this);
		
		if(MainApp.TEST){
			LocalJSONClientRepository repository = new LocalJSONClientRepository(this);
			ClientManager manager = new ClientManager(repository);
			vm = new ViewModelEditClient(client, manager, view);
		}
		else{
			ClientRepository repository = new ClientRepository(new GetClientesClient(), new PostClientesClient());
			ClientManager manager = new ClientManager(repository);
			vm = new ViewModelEditClient(client, manager, view);
		}
		
		//********************* BEGIN FORM ********************************
		form = new Form();
		{
			//*********** TEXT FIELDS **********************
			form.addField(new TextViewField((TextView)findViewById(R.id.client_nombreempresa), "nombreempresa", getString(R.string.nombre_de_empresa_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_codigo), "codigo", getString(R.string.codigo_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_cif), "cif", getString(R.string.c_i_f_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_direccion), "direccion", getString(R.string.direccion_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_ciudad), "ciudad", getString(R.string.ciudad_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_provincia), "provincia", getString(R.string.provincia_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_codpos), "codpos", getString(R.string.codigo_postal_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_pais), "pais", getString(R.string.pais_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_telefono), "telefono", getString(R.string.telefono_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_fax), "fax", getString(R.string.fax_), true));
			form.addField(new TextViewField((TextView)findViewById(R.id.client_email), "email", getString(R.string.email_), true));
		}		
		//*********** END FORM **********************************

		if(savedInstanceState!=null){
			form.loadValues(savedInstanceState);
			FromSavedInstance = true;
		}
		
	}
	
	@Override
	public boolean onNavigateUp() {
		finish();
		return true;
	}
	
	 @Override
	   public void onResume() {
			super.onResume();
			
			view.setIsValid(true);
			vm.load();
		}
		
	   @Override
		public void onStop() {
		    view.setIsValid(false);
		   
			super.onStop();
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch (id) {
		case R.id.refresh:
			vm.load();
			return true;
		case R.id.save:	
			saveClient();
			return true;
		default:
			break;
		}
			
		return super.onOptionsItemSelected(item);
	}	
	
	private void saveClient() {
		if(!form.validate()){	
			Utils.showMessage(this, getString(R.string.noti_errores_validacion));
			form.showErrorDialog(this, null);
			return;
		}
		
		Client client = vm.getClient();
		try {
			form.updateSource(client);
		} catch (Exception e) {
			DialogUtil.showErrorDialog(this, e.getMessage());
			return;
		}						
		vm.save();		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {			
		super.onSaveInstanceState(outState);						
		form.saveValues(outState);
	}

	class EditView extends DefaultEditView<Activity> implements OnClickListener{

		public EditView(Activity activity) {
			super(activity);			
		}

		@Override
		public void onDataLoaded() {
			Client client = vm.getClient();
						
			if(!FromSavedInstance){
				try {
					form.updateTargets(client);
				} catch (InvalidOperationException e) {
					DialogUtil.showErrorDialog(getActivity(), e.getMessage());
					return;
				}	
			}
			
			ArrayList<String>notifications = vm.getNotifications();
			if(notifications.size() > 0){
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < notifications.size(); i++) {
					sb.append(notifications.get(i));
					sb.append('\n');
				}
				Utils.showAlertDialog(getActivity(), getString(R.string.aviso), sb.toString(),null);
			}						
		}
		
		@Override
		public void onSaved(ErrorInfo info) {				
			super.onSaved(info);
			
			if(info == null || !info.containsErrors()){
				Utils.showMessage(getActivity(), getActivity().getString(R.string.cliente_salvado));
				try {
					//esperar un segundo para mostrar el mensaje
					Thread.sleep(1000);
				} catch (InterruptedException e) {											
				}
				
				getActivity().setResult(Constants.CLIENT_MODIFIED);
				getActivity().finish();
				return;
			}
			else{
				Utils.showMessage(getActivity(), getActivity().getString(R.string.noti_errores_validacion));
				form.setFieldErrors(info);
				form.showErrorDialog(getActivity(),null);					
			}
		}
		
		@Override
		public void onFailure(Exception workException) {
			DialogUtil.showErrorDialog(getActivity(), workException.getMessage(), this);
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			getActivity().finish();				
		}
		
	}
}
