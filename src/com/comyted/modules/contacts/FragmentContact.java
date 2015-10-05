package com.comyted.modules.contacts;

import junit.framework.Assert;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.comyted.Constants;
import com.comyted.MainApp;
import com.comyted.MapView;
import com.comyted.MailDialogFragment;
import com.comyted.R;
import com.comyted.RefreshableFragment;
import com.comyted.models.Contact;
import com.comyted.models.MailMessage;
import com.comyted.modules.clients.ActivityEditClient;
import com.comyted.repository.ContactsRepository;
import com.enterlib.StringUtils;
import com.enterlib.app.UIUtils;
import com.enterlib.mvvm.DataViewModel;
import com.enterlib.mvvm.RepositoryViewModel;

public class FragmentContact extends RefreshableFragment 
							 implements OnClickListener {
	private ViewGroup rootView;	
	private RepositoryViewModel<Contact> viewModel;
	protected Bitmap adressMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);		
	}	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 rootView = (ViewGroup)inflater.inflate(R.layout.fragment_contact, container, false);
		 TextView lbPhone = (TextView) rootView.findViewById(R.id.lbContact_telefono);
		 lbPhone.setOnClickListener(this);
		 TextView lbEmail = (TextView) rootView.findViewById(R.id.lb_contact_email);
		 
		 lbEmail.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				sendMail();
			}
		});		
		 return rootView;
	}
	
	@Override
	public void onDataLoaded() {
		Contact c = viewModel.getEntity();	
		if(c == null){
			UIUtils.showMessage(getActivity(), "no hay cliente");
			return;
		}
		getActivity().setTitle(c.nombrecontacto);
			
		setText(R.id.contact_id, String.valueOf(c.id));
		setText(R.id.contact_nombrecontacto, c.nombrecontacto);
		setText(R.id.contact_nombrecliente, c.nombreempresa);
		setText(R.id.contact_puesto, c.puesto);
		setText(R.id.contact_departamento, c.dpto);
		setText(R.id.contact_direccion, c.direccion);
		setText(R.id.contact_ciudad, c.ciudad);
		setText(R.id.contact_provincia, c.provincia);
		setText(R.id.contact_codpos, c.codpos);
		setText(R.id.contact_pais, c.pais);
		setText(R.id.contact_telefono, c.telefono);
		setText(R.id.contact_fax, c.fax);
		setText(R.id.contact_email, c.email);
		setText(R.id.contact_observations, c.observaciones);
		
		MapView frame = (MapView) rootView.findViewById(R.id.framelayout);
		frame.loadMapAsync(c.direccion);
		
	}

	@Override
	protected DataViewModel createViewModel() {
		int id =getActivity().getIntent().getIntExtra(Constants.ID, 0);
		Assert.assertTrue(id > 0);
		
		viewModel = new RepositoryViewModel<Contact>(this,id,new ContactsRepository());
		
		return viewModel;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{		
		inflater.inflate(R.menu.fragment_contact, menu);		
	}	

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if(super.onOptionsItemSelected(item))
			return true;		
		switch (item.getItemId()) {
		case R.id.edit:			
			Contact c = viewModel.getEntity();			
			Intent intent = new Intent(getActivity(), ActivityEditContact.class)
					 			.putExtra(Constants.ID, c.id)
					 			.putExtra(Constants.CLIENT_ID, getActivity().getIntent().getIntExtra(Constants.CLIENT_ID, 0));			
			startActivityForResult(intent, Constants.EDIT);
			return true;
		case R.id.call:
			callContact();
			return true;
		case R.id.send_mail:
			sendMail();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		callContact();
	}
	
	private void callContact(){
		String phoneNumber= getViewText(R.id.contact_telefono);
		if(StringUtils.isNullOrWhitespace(phoneNumber)){
			UIUtils.showMessage(getActivity(), getActivity().getString(R.string.el_contacto_no_tiene_tel_fono));
			return;
		}
		
		Intent i = new Intent(android.content.Intent.ACTION_DIAL, 
								Uri.parse("tel:+"+ phoneNumber)); 
		startActivity(i);
	}


	private void sendMail() {
		Contact entity =viewModel.getEntity();
		if(entity == null){
			UIUtils.showMessage(getActivity(), getActivity().getString(R.string.espere_a_que_se_cargen_los_datos));
			return;
		}
		
		MailMessage message=new MailMessage();
		message.Sender = MainApp.getCurrentUser().email;
		message.Receiver =entity.email;
		
		MailDialogFragment frag = MailDialogFragment.newInstance(message);
		frag.show(getFragmentManager(), "com.comyted.MailDialogFragment");
	}


}
