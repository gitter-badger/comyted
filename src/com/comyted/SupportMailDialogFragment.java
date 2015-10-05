package com.comyted;

import com.comyted.models.MailMessage;
import com.enterlib.app.UIUtils;
import com.enterlib.exceptions.ConversionFailException;
import com.enterlib.exceptions.InvalidOperationException;
import com.enterlib.fields.Form;
import com.enterlib.fields.TextViewField;
import com.enterlib.mvvm.DefaultEditView;
import com.enterlib.mvvm.EditViewModel;
import com.enterlib.validations.ErrorInfo;
import com.enterlib.validations.validators.RegExValueValidator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SupportMailDialogFragment extends android.support.v4.app.DialogFragment {
	public static final String MESSAGE = "MESSAEGE";  
	MailMessage message;
	Form form;
	private View rootView;
	private ViewModel viewModel;
	
	public static SupportMailDialogFragment newInstance(MailMessage message){
		SupportMailDialogFragment fragment = new SupportMailDialogFragment();
		fragment.message = message;
		Bundle args = new Bundle();
		args.putSerializable(MESSAGE, message);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Bundle args = getArguments();
		message = (MailMessage) args.getSerializable(MESSAGE);		
		Activity activity = getActivity();
		
		AlertDialog.Builder ab = new  AlertDialog.Builder(activity);
		
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView=  inflater.inflate(R.layout.dialog_send_email, null);
		
		//View title = inflater.inflate(R.layout.dialog_send_email_title, null);
		//ab.setCustomTitle(title);
		ab.setIcon(R.drawable.ic_action_gmail);
		ab.setTitle("Enviar Email");
		
		createForm(activity);
		viewModel = new ViewModel(activity);
		
		if(savedInstanceState!=null){
			form.loadValues(savedInstanceState);
		}
		
		rootView.requestLayout();
		rootView.refreshDrawableState();
		ab.setView(rootView);
		
		Button cancel = (Button) rootView.findViewById(R.id.email_cancel);
		cancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dismiss();				
			}
		});
		
		Button accept = (Button) rootView.findViewById(R.id.email_send);
		accept.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				sendMail();		
			}
		});
		
		return ab.create();
	}
	
	
	protected void sendMail() {
	  if(form.validate()){
	    	if(message == null){
		    	Utils.showMessage(getActivity(), getActivity().getString(R.string.error));
		    	return;
		    }
	    	
	    	try {
				form.updateSource(message);
				viewModel.save();
			} catch (ConversionFailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		    	
	    }	    	    	    				
	}

	private void createForm(Activity activity){
		 form = new Form();
		 RegExValueValidator emailValidator =new RegExValueValidator("(\\w+)(\\.(\\w+))*@(\\w+)(\\.(\\w+))*", 
				 activity.getString(R.string.email_no_valido));
		 
		 RegExValueValidator emailCCValidator =new RegExValueValidator("(\\w+)(\\.(\\w+))*@(\\w+)(\\.(\\w+))*(\\s*;\\s*(\\w+)(\\.(\\w+))*@(\\w+)(\\.(\\w+))*)*", 
				 activity.getString(R.string.email_no_valido)); 
		 
		 form.addField(new TextViewField((TextView)rootView.findViewById(R.id.email_subject), "Subject",true));
		 form.addField(new TextViewField((TextView)rootView.findViewById(R.id.email_text), "Text", true));		 	     	    
	     form.addField(new TextViewField((TextView)rootView.findViewById(R.id.email_sender), "Sender" ,true).addValueValidator(emailValidator));
	     form.addField(new TextViewField((TextView)rootView.findViewById(R.id.email_receiver), "Receiver",true).addValueValidator(emailValidator));
	     form.addField(new TextViewField((TextView)rootView.findViewById(R.id.email_cc),"CC").addValueValidator(emailCCValidator));
	     
	     try {
			form.updateTargets(message);
		} catch (InvalidOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle arg0) {	
		super.onSaveInstanceState(arg0);
		form.saveValues(arg0);
	}
	
	class MailView extends DefaultEditView<Activity>{

		public MailView(Activity activity) {
			super(activity);
			setIsValid(true);
		}

		@Override
		public void onDataLoaded() {			
		}		
		
		@Override
		public void onSaved(ErrorInfo info) {			
			super.onSaved(info);
			
			if(info == null || !info.containsErrors()){
				 UIUtils.showMessage(getActivity(), getString(R.string.email_enviado_correctamente));
				 dismiss();
			}
		}
	}
	
	class ViewModel extends EditViewModel{
		protected ViewModel(Activity activity) {
			super(new MailView(activity));		
		}

		@Override
		protected boolean saveAsync() throws Exception {
			// TODO Send Email
			
			return true;
		}

		@Override
		protected boolean loadAsync() throws Exception {			
			return true;
		}
		
	}
}
