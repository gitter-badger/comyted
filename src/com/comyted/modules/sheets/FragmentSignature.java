package com.comyted.modules.sheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.comyted.Constants;
import com.comyted.R;
import com.comyted.MainApp;
import com.comyted.Utils;
import com.comyted.activities.PaintActivity;
import com.comyted.models.SheetSignatureState;
import com.comyted.modules.sheets.ViewModelSignature.ISavedNotifyCallback;
import com.comyted.modules.sheets.ViewModelSignature.ISavingNotifyCallback;
import com.enterlib.app.DefaultDataView;


public class FragmentSignature extends Fragment 								
{
		private static final int PICTURE_CLIENT = 10;
		private static final int PICTURE_TECH = 11;		
		private static final int REQUEST_IMAGE_CLIENT = 12;
		private static final int REQUEST_IMAGE_TECH = 13;
		private static final int PAINT_IMAGE_CLIENT = 14;
		private static final int PAINT_IMAGE_TECH = 15;
		
		
		private View rootView;
		private View firmacliente;
		private View firmatecnico;
		private File outputFileUri;
		private Bitmap clientSignature;
		private Bitmap techSignature;
		int sheetId;
		private ViewModelSignature vm;	
		private ProgressDialog dialog;
		private DataView view;
		
		ISavingNotifyCallback savingNotify = new ISavingNotifyCallback() {	
			public void onNotify(int type) {
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
				
				switch (type) {
					case ViewModelSignature.CLIENT:
						dialog = MainApp.getProgressDialog(getActivity(), "Enviando firma del cliente...");						
					break;
					case ViewModelSignature.TECNITIAN:
						dialog = MainApp.getProgressDialog(getActivity(), "Enviando firma del técnico...");
					break;
				}				
				dialog.show();
			}
		};
		
		ISavedNotifyCallback savedNotify = new ISavedNotifyCallback() {			
			@Override
			public void onNotify(int type, Exception e) {
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
				
				SheetSignatureState state = vm.getState();
				if(state == null){
					state =new SheetSignatureState();
					vm.setState(state);
				}
				switch (type) {
					case ViewModelSignature.CLIENT:
						Utils.showMessage(getActivity(), "Firma del cliente enviada");						
						state.firmacliente++;
						state.fechafirmacliente = Utils.getCurrentDate();									
						Utils.setCheckBoxValue(rootView, R.id.firmadaCliente, true);
						Utils.setTextViewText(rootView, R.id.fechafirmacliente, Utils.formatDateOnly(state.fechafirmacliente));
					break;
					
					case ViewModelSignature.TECNITIAN:
						Utils.showMessage(getActivity(), "Firma del técnico enviada");						
						state.firmatecnico++;
						state.fechafirmatecnico = Utils.getCurrentDate();										
						Utils.setCheckBoxValue(rootView, R.id.firmadaTecnico, true);
						Utils.setTextViewText(rootView, R.id.fechafirmatecnico, Utils.formatDateOnly(state.fechafirmatecnico));
					break;
				}				
			}
		};
		
		class DataView extends DefaultDataView<Activity>{

			public DataView(Activity activity) {
				super(activity);				
			}
			
			@Override
			public void onFailure(Exception workException) {
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
				
				super.onFailure(workException);
			}

			@Override
			public void onDataLoaded() {
				SheetSignatureState state = vm.getState();
				if(state == null){
					Utils.showMessage(getActivity(), getActivity().getString(R.string.no_firmada));
					return;
				}
					
				CheckBox firmadaCliente = (CheckBox) rootView.findViewById(R.id.firmadaCliente);
				CheckBox firmadaTecnico = (CheckBox) rootView.findViewById(R.id.firmadaTecnico);
						
				firmadaCliente.setChecked(state.firmadaPorCliente());
				firmadaTecnico.setChecked(state.firmadaPorTecnico());
				
				if(state.firmadaPorCliente() && state.fechafirmacliente!=null)
					Utils.setTextViewText(rootView, R.id.fechafirmacliente, 
							Utils.formatDateOnly(state.fechafirmacliente));
				
				if(state.firmadaPorTecnico() && state.fechafirmatecnico!=null)
					Utils.setTextViewText(rootView, R.id.fechafirmatecnico, 
							Utils.formatDateOnly(state.fechafirmatecnico));
				
			}
			
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {	
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
			
			outputFileUri = null;
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				outputFileUri = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						"cat_movility_signature.jpg");
				outputFileUri.setWritable(true);
				outputFileUri.setReadable(true);
			}
						
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
		{
			menu.add(getActivity().getString(R.string.refresh))
			.setIcon(R.drawable.ic_refresh_inverse)						
			.setOnMenuItemClickListener(new OnMenuItemClickListener() {				
				@Override
				public boolean onMenuItemClick(MenuItem item) {					
					vm.load();			
					return true;
				}			
			}).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);	
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			rootView = inflater.inflate(R.layout.fragment_signature, container, false);
						
			firmacliente = rootView.findViewById(R.id.firma_cliente);
			firmatecnico = rootView.findViewById(R.id.fima_tecnico);					
						
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {		
			super.onActivityCreated(savedInstanceState);
			
			sheetId = getActivity().getIntent().getIntExtra(Constants.SHEET_ID, -1);
			view = new  DataView(getActivity());
			vm = new ViewModelSignature(sheetId, view);
										
			if(savedInstanceState!=null){
				//take the bitmaps from the savedInstance 
				clientSignature = savedInstanceState.getParcelable("clientSignature");
				techSignature = savedInstanceState.getParcelable("techSignature");									
			}
			
			//displays the images
			if(clientSignature!=null){
				((ImageView) firmacliente.findViewById(R.id.imageCliente)).setImageBitmap(clientSignature);
			}
			if(techSignature!=null){
				((ImageView) firmatecnico.findViewById(R.id.imageCliente)).setImageBitmap(techSignature);
			}					
			
			SetupEvents(PICTURE_CLIENT, firmacliente);
			SetupEvents(PICTURE_TECH, firmatecnico);
			
			
			vm.setSavingCallback(savingNotify);
			vm.setSavedCallback(savedNotify);
		}

		private void SetupEvents(final int request , View view) {
			ImageButton button = (ImageButton) view.findViewById(R.id.capture_img);
			//set capture onclick
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					takePicture(request);					
				}
			});
			//set guardar onclick
			((Button)view.findViewById(R.id.orderAscending)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					switch (request) {
					case PICTURE_CLIENT:
						if(clientSignature == null){
							Utils.showMessage(getActivity(), "firme el cliente primero");
							return;
						}
						break;				
					case PICTURE_TECH:
						 if(techSignature == null){
							 Utils.showMessage(getActivity(), "firme técnico primero");
								return;
						 }
						break;					
					}
					saveImage(request);
				}				
			});
			//set pick image onClick
			((ImageButton)view.findViewById(R.id.search_img)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(request == PICTURE_CLIENT)
						pickImage(REQUEST_IMAGE_CLIENT);
					else
						pickImage(REQUEST_IMAGE_TECH);
				}
			});
			
			//set paint image onClick
			((ImageButton)view.findViewById(R.id.paint_img)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(request == PICTURE_CLIENT)
						paintImage(PAINT_IMAGE_CLIENT);
					else
						paintImage(PAINT_IMAGE_TECH);
				}
			});
		}		
		
		//**Starts the painting activity and waits for the result to be posted*/
		protected void paintImage(int requestCode) {		
			Intent intent = new Intent(getActivity(), PaintActivity.class);
			startActivityForResult(intent, requestCode);
		}
		
		@Override
		public void onResume() {			
			super.onResume();
			
			//activate the view to receive callbacks
			view.setIsValid(true);
			vm.load();			
		}
		
		@Override
		public void onStop() {
			//deactivate the view to receive callbacks
			view.setIsValid(false);
			
			super.onStop();
		}

		//**Take a picture from the camera activity*/
		private void takePicture(int request){									
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				outputFileUri = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						 "cat_movility_signature.jpg");
				outputFileUri.setWritable(true);
				outputFileUri.setReadable(true);
			}			
			
			if(outputFileUri!=null){
				Uri uriStoreFile = Uri.fromFile(outputFileUri);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uriStoreFile);
			}
			
			startActivityForResult(intent, request);
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode != Activity.RESULT_OK)
				return;			
			//The image come from the CameraActivity
			if (requestCode == PICTURE_CLIENT) {				
				clientSignature = getImage(data);
				if(clientSignature!=null)
					((ImageView) firmacliente.findViewById(R.id.imageCliente)).setImageBitmap(clientSignature);				
			}
			else if(requestCode == PICTURE_TECH){
				techSignature = getImage(data);
				if(techSignature!=null)				
					((ImageView) firmatecnico.findViewById(R.id.imageCliente)).setImageBitmap(techSignature);
			}
			//The image come from the GalleryActivity
			else if(requestCode == REQUEST_IMAGE_CLIENT){
				//the picture come from the gallery activity				
				Uri uriToImage = data.getData(); 
				ContentResolver cr = getActivity().getContentResolver();
				 try {
				   InputStream is =	cr.openInputStream(uriToImage);
				   clientSignature = BitmapFactory.decodeStream(is);
				   ((ImageView) firmacliente.findViewById(R.id.imageCliente)).setImageBitmap(clientSignature);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}				
			}
			else if (requestCode == REQUEST_IMAGE_TECH){
				//the picture come from the gallery activity
				Uri uriToImage = data.getData(); 
				ContentResolver cr = getActivity().getContentResolver();
				 try {
				   InputStream is =	cr.openInputStream(uriToImage);
				   techSignature = BitmapFactory.decodeStream(is);
				   ((ImageView) firmatecnico.findViewById(R.id.imageCliente)).setImageBitmap(techSignature);
				} catch (FileNotFoundException e) {					
					e.printStackTrace();
				}
			}
			//The image come from the PaintActitivy
			else if (requestCode == PAINT_IMAGE_CLIENT){
				clientSignature = data.getParcelableExtra(PaintActivity.PaintFragment.BITMAP);
				((ImageView) firmacliente.findViewById(R.id.imageCliente)).setImageBitmap(clientSignature);
				
			}
			else if (requestCode == PAINT_IMAGE_TECH){
				techSignature = data.getParcelableExtra(PaintActivity.PaintFragment.BITMAP);
			   ((ImageView) firmatecnico.findViewById(R.id.imageCliente)).setImageBitmap(techSignature);
			}				
		}

		Bitmap getImage(Intent data){
			if (data != null && data.hasExtra("data")) {
					return  data.getParcelableExtra("data");							
			}else if(outputFileUri!=null){
				//Decode the image from the TakePictureActity			
				Bitmap bmp = BitmapFactory.decodeFile(outputFileUri.getPath());
				bmp = Bitmap.createScaledBitmap(bmp, SignatureManager.SIGNATURE_WIDTH, SignatureManager.SIGNATURE_HEIGHT, true);
			
				outputFileUri.delete();
				return bmp;
			}
			else
				return null;
		}
		
		private void saveImage(int request){
			switch (request) {
			case PICTURE_CLIENT:					
				vm.save(ViewModelSignature.CLIENT, clientSignature);							
				break;				
			case PICTURE_TECH:
				vm.save(ViewModelSignature.TECNITIAN, techSignature);
				break;					
			}		
		}
		
		//**Pick a picture from the Gallery*/
		private void pickImage(int request){
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*"); 
			startActivityForResult(intent, request); 						
		}			
					
		@Override
		public void onSaveInstanceState(Bundle outState) {			
			super.onSaveInstanceState(outState);			
			
			outState.putParcelable("clientSignature", clientSignature);
			outState.putParcelable("techSignature", techSignature);
		}	
		
}
