package com.comyted;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.comyted.R;
import com.comyted.models.BaseModel;
import com.comyted.models.IdNameValue;
import com.enterlib.TimeValue;
import com.enterlib.app.SimpleSpinnerAdapter;
import com.enterlib.threading.AsyncManager;

/**Utilities*/
public class Utils {
	
	  /**format: dd/MM/yyyy*/
	static SimpleDateFormat dfparse = new SimpleDateFormat("dd/MM/yyyy");
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat dfDateOnly = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat dfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	static SimpleDateFormat dfverbose =  new SimpleDateFormat("E dd 'DE' MMM 'DEL' yyyy");
	static SimpleDateFormat dftime =  new SimpleDateFormat("HH:mm");
	
   public static void showMessage(Context context,String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);       
        toast.show();
   }
 
		
  public static TimeValue getTime(Date date){
	  TimeValue v = new TimeValue();
		String[]values = dftime.format(date).split(":");
		v.Hours = Integer.parseInt(values[0]) ;
		v.Minutes =	 Integer.parseInt(values[1]);
		return v;
  }
	
	public static String TrimString(String texto)
	{
		String Cadena = "";
		int x = 0;
			while (texto.charAt(x) != ' '){
			      Cadena += texto.charAt(x);
			      x++;
			}
		return Cadena;
	}
	
	public static void setTextViewText(View view , int id , String text) {
		
		 TextView v = (TextView) view.findViewById(id);
		 v.setText(text);
	}
	
	public static void setCheckBoxValue(View view , int id ,boolean value) {
		
		 CheckBox v = (CheckBox)view.findViewById(id);
		 v.setChecked(value);
	}
		
	/**value in format yyyy-MM-dd HH:mm:ss*/
	@SuppressLint("SimpleDateFormat")	
	public static Date GetDate(String value){	
		try
		{
			return df.parse(value);
		}
		catch (ParseException e)
		{			
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date GetDate(String value, String time) throws ParseException{		
		return dfTime.parse(value + " " + time);		
	}
	
	public static Date GetDate(View view, int id){
		return GetDate((Button)view.findViewById(id));
	}
	
	public static Date GetDateTime(View view, int iddate, int idtime){
		return GetDateTime((Button)view.findViewById(iddate), (Button)view.findViewById(idtime));
	}
	
	public static Date GetDate(Button button){
		String dateStr = button.getText().toString();
		if(dateStr == null || dateStr.isEmpty())
			return null;
		try {
//			String[]values = dateStr.split("/");
//			int d  = Integer.parseInt(values[0]);//- 1;
//			int m =  Integer.parseInt(values[1]);
//			int y =  Integer.parseInt(values[2]);// - 1;
//			return dfparse.parse(d +"/" + m + "/" + y );
			return dfparse.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date GetDateTime(Button date , Button time){
		String dateStr = date .getText().toString();
		String timeStr = time.getText().toString(); 
		if(dateStr == null || dateStr.isEmpty() || timeStr == null || timeStr.isEmpty())
			return null;
		try {
//			String[]values = dateStr.split("/");
//			int d  = Integer.parseInt(values[0]);// - 1;
//			int m =  Integer.parseInt(values[1]) - 1;
//			int y =  Integer.parseInt(values[2]);// - 1;			
//			return dfTime.parse(d +"/" + m + "/" + y + " " + timeStr);
			return dfTime.parse(dateStr + " " + timeStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void SetupDatePicker(Activity act, final Button button ,Date date){
		
		if(date == null)
			date = new Date();
		String[] value = dfparse.format(date).split("/");
		if(value.length!=3)
			return;
		int day = Integer.parseInt(value[0]);
		int month =Integer.parseInt(value[1]);
		int year = Integer.parseInt(value[2]);
		
		final DatePickerDialog d = new DatePickerDialog(act, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {												
				button.setText( parse(day) + "/"+ parse(month + 1) + "/" + parse(year));
				button.refreshDrawableState();
			}
		} , year , Math.max( month - 1, 0), day);
		
//		d.setTitle("Fecha");
//		d.setButton(DatePickerDialog.BUTTON_POSITIVE, act.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				DatePicker picker = d.getDatePicker();
//				int year = picker.getYear();
//				int month = picker.getMonth();
//				int day = picker.getDayOfMonth();
//				
//				button.setText( parse(day) + "/"+ parse(month + 1) + "/" + parse(year));
//				button.refreshDrawableState();
//			}
//		});
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				d.show();				
			}
		});								
		
		button.setText( parse(day) + "/"+ parse(month) + "/" + parse(year));				
		button.refreshDrawableState();			
	}
	
public static void SetupTimePicker(Activity act, final Button button , Date date){
		
		if(date ==null)
			date = new Date();
		String[]values = dftime.format(date).split(":");
		final TimePickerDialog d = new TimePickerDialog(act, new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker arg0, int hour, int min) {							
				button.setText(parse(hour)+":"+ parse(min));
				button.refreshDrawableState();
			}
		} , Integer.parseInt(values[0]) ,Integer.parseInt(values[1]), true);
		
//		d.setTitle("Tiempo");
//		d.setButton(TimePickerDialog.BUTTON_POSITIVE, act.getString(R.string.aceptar), new DialogInterface.OnClickListener()
//		{
//
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				TimePicker picker = d.get				
//			}
//		
//		});
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				d.show();				
			}
		});				
		
		int hour = Integer.parseInt(values[0]);
		int min = Integer.parseInt(values[1]);					
		button.setText(parse(hour)+":"+ parse(min));
		button.refreshDrawableState();
		
	}
	
	/**returns string in format yyyy-MM-dd HH:mm:ss */
	public static String format(Date date){	
		if(date==null)
			return null;
		return df.format(date);		
	}	
	
	/**returns string in format  dd/MM/yyyy */
	public static String formatDateOnly(Date date){	
		if(date == null)
			return null;
		return dfparse.format(date);		
	}	
	/**returns string in format  E dd 'DE' MMM 'DEL' yyyy */
	public static String formatVerbose(Date date){
		if(date!=null)
			return dfverbose.format(date).toUpperCase();
		return null;
	}
	
	public static String formatTime(Date date){
		return dftime.format(date);
	}
		
	
	public static void SetEditViewText(Activity act, int viewId, String text) {
		EditText v = (EditText) act.findViewById(viewId);
		if(v!=null)
			v.setText(text);
	}	
	
	public static void SetEditViewText(View rootView, int viewId, String text) {
		EditText v = (EditText) rootView.findViewById(viewId);
		if(v!=null)
			v.setText(text);		
	}	
	
	public static String GetEditViewText(Activity view,int viewId) {
		EditText v = (EditText) view.findViewById(viewId);
		if(v!=null)
		   return	v.getText().toString();
		return null;
	}		

	public static String GetEditViewText(View view,int viewId) {
		EditText v = (EditText) view.findViewById(viewId);
		if(v!=null)
		   return v.getText().toString();
		return null;
	}	
	
	public static int GetEditViewInteger(View view,int viewId) {
		String str = GetEditViewText(view, viewId);		
		if(str!=null && !str.isEmpty()){
			try{
				return Integer.parseInt(str);
			}catch(NumberFormatException e){
				return 0;
			}
		}
		return -1;		
	}
	
	public static double GetEditViewDouble(View view,int viewId) {
		String str = GetEditViewText(view, viewId);		
		if(str!=null && !str.isEmpty()){
			try{
				return Double.parseDouble(str);
			}catch(NumberFormatException e){
				return 0;
			}
		}
		return -1;		
	}
	
	public static void SetSpinnerOptions(Activity activity, int viewId, IdNameValue[]values, String name){
		Spinner sp = (Spinner)activity.findViewById(viewId);
		if(values == null || values.length == 0){
			sp.setAdapter(null);
			return;		
		}		
		
		//ArrayAdapter<IDName>adapter = new ArrayAdapter<IDName>(activity.getApplicationContext(),android.R.layout.simple_dropdown_item_1line ,values);		
		sp.setAdapter(new SimpleSpinnerAdapter(activity, android.R.layout.simple_dropdown_item_1line , values));
		int selection = 0;
		if(name!=null){
			for (int i = 0; i < values.length; i++) {
				if(values[i].name.equals(name))
					selection = i;
			}
		}
		sp.setSelection(selection);		
	}
	
	public static void SetSpinnerOptions(View v, int viewId, IdNameValue[]values, String name){
		Spinner sp = (Spinner)v.findViewById(viewId);
		if(values == null || values.length == 0){
			sp.setAdapter(null);
			return ;		
		}			
		//ArrayAdapter<IDName>adapter = new ArrayAdapter<IDName>(activity.getApplicationContext(),android.R.layout.simple_dropdown_item_1line ,values);		
		sp.setAdapter(new SimpleSpinnerAdapter(v.getContext(), android.R.layout.simple_dropdown_item_1line , values));
		int selection = 0;
		if(name!=null){
			for (int i = 0; i < values.length; i++) {
				if(values[i].name.equals(name)){
					selection = i;
					break;
				}
			}
		}
		sp.setSelection(selection);		
	}
	
	public static void SetSpinnerOptions(View v, int viewId, BaseModel[]values, int id){
		Spinner sp = (Spinner)v.findViewById(viewId);
		if(values == null || values.length == 0){
			sp.setAdapter(null);
			return;
		}
		//ArrayAdapter<IDName>adapter = new ArrayAdapter<IDName>(activity.getApplicationContext(),android.R.layout.simple_dropdown_item_1line ,values);		
		sp.setAdapter(new SimpleSpinnerAdapter(v.getContext(), android.R.layout.simple_dropdown_item_1line , values));
		int selection = 0;
		if(id >= 0){
			for (int i = 0; i < values.length; i++) {
				if(values[i].id == id){
					selection = i;
					break;
				}
			}
		}
		sp.setSelection(selection);		
	}
	
//	public static void SetPopupBoxOptions(View v, int viewId, BaseModel[]values, int  id){
//		PopupBox sp = (PopupBox)v.findViewById(viewId);
//		if(values == null || values.length == 0){
//			sp.setItems(null);
//			return;
//		}
//		sp.setItems(values);		
//		int selection = 0;
//		if(id >= 0){
//			for (int i = 0; i < values.length; i++) {
//				if(values[i].id == id){
//					selection = i;
//					break;
//				}
//			}
//		}
//		sp.setPosition(selection);		
//	}
	
	public static int GetId(Activity act, int spinnerView){
		Spinner sp = (Spinner)act.findViewById(spinnerView);
		BaseModel item =  (BaseModel) sp.getSelectedItem();		
		return item!=null?item.id:-1;
	}
	
	public static int GetId(View act, int spinnerView){
		Spinner sp = (Spinner)act.findViewById(spinnerView);
		BaseModel item;
		try {
			item = (BaseModel)sp.getSelectedItem();
			if(item!=null)
				return item.id;
			return -1;
		} catch (Exception e) {		
			e.printStackTrace();
			return -1;
		}		
	}
	
//	public static int GetIdFromPopup(View act, int popupView){
//		PopupBox sp = (PopupBox)act.findViewById(popupView);
//		BaseModel item;
//		try {
//			item = (BaseModel)sp.getSelectedItem();
//			if(item!=null)
//				return item.id;
//			return -1;
//		} catch (Exception e) {		
//			e.printStackTrace();
//			return -1;
//		}		
//	}
		
	
	
	public static IdNameValue GetIdName(Activity act, int spinnerView){
		Spinner sp = (Spinner)act.findViewById(spinnerView);
		try {
			IdNameValue item =  (IdNameValue) sp.getSelectedItem();
			return item;			
		} catch (Exception e) {
			return new IdNameValue(-1, null);
		}		
	}
	
	public static IdNameValue GetIdName(View act, int spinnerView){
		Spinner sp = (Spinner)act.findViewById(spinnerView);		
		IdNameValue item =  (IdNameValue) sp.getSelectedItem();		
		return (item != null) ? item : new IdNameValue(-1, null);
	}
	
	/**returns 1 if the text property is null or empty, otherwise 0*/
	public static int ValidateString(View view, int viewID){
		EditText t= (EditText) view.findViewById(viewID);
		String s = t.getText().toString();
		t.setBackgroundResource(R.color.white);		
		if(s == null || s.isEmpty()){
			t.setBackgroundResource(R.drawable.custom_edit_text_error);			
			return 1;
		}
		return 0;
	}
		
	public static int ValidateSpinner(View view, int viewId){
		Spinner sp = (Spinner)view.findViewById(viewId);		
		Object item =  sp.getSelectedItem();
		sp.setBackgroundResource(R.color.white);		
		if(item == null){
			sp.setBackgroundResource(R.drawable.custom_edit_text_error);			
			return 1;
		}		
		return 0;
	}
	
//	public static int ValidatePopup(View view, int viewId){
//		PopupBox sp = (PopupBox)view.findViewById(viewId);		
//		Object item =  sp.getSelectedItem();
//		sp.setBackgroundResource(R.color.white);		
//		if(item == null){
//			sp.setBackgroundResource(R.drawable.custom_edit_text_error);			
//			return 1;
//		}		
//		return 0;
//	}
	
	
	public static int ValidateSpinner(Activity view, int viewId){
		Spinner sp = (Spinner)view.findViewById(viewId);		
		Object item =  sp.getSelectedItem();
		sp.setBackgroundResource(R.color.white);		
		if(item == null){
			sp.setBackgroundResource(R.drawable.custom_edit_text_error);			
			return 1;
		}		
		return 0;
	}
	
	public static int ValidatePositiveInteger(View view, int viewID){
		EditText t= (EditText) view.findViewById(viewID);
		String s = t.getText().toString();
		t.setBackgroundResource(R.color.white);
		try {
			int n = Integer.parseInt(s);
			if(n < 0)
			{
				t.setBackgroundResource(R.drawable.custom_edit_text_error);
				t.setHint(R.string.entrada_invalida);	
				return 1;
			}
			return 0;
		} catch (NumberFormatException e) {
			t.setBackgroundResource(R.drawable.custom_edit_text_error);
			t.setHint(R.string.entrada_invalida);
			return 1;
		}					
	}
	
	public static int ValidatePositiveDouble(View view, int viewID){
		EditText t= (EditText) view.findViewById(viewID);
		String s = t.getText().toString();
		t.setBackgroundResource(R.color.white);
		try {
			double n = Double.parseDouble(s);
			if(n < 0)
			{
				t.setBackgroundResource(R.drawable.custom_edit_text_error);
				t.setHint(R.string.entrada_invalida);	
				return 1;
			}
			return 0;
		} catch (NumberFormatException e) {
			t.setBackgroundResource(R.drawable.custom_edit_text_error);
			t.setHint(R.string.entrada_invalida);
			return 1;
		}					
	}
	
	
	
	public static void setErrorBackground(View view, int viewID){
		View t = view.findViewById(viewID);
		t.setBackgroundResource(R.drawable.custom_edit_text_error);		
	}
	
	public static void setDefaultBackground(View view, int viewID){
		View t = view.findViewById(viewID);
		t.setBackgroundResource(R.color.white);		
	}
	
	
	public static int ValidateDate(View view, int viewID){
		Button t= (Button) view.findViewById(viewID);
		String s = t.getText().toString();
		t.setBackgroundResource(android.R.drawable.edit_text);
		if(s == null || s.isEmpty()){
			t.setBackgroundResource(R.drawable.custom_edit_text_error);				
			return 1;
		}		 
		try {
			Date date = dfparse.parse(s);
		} catch (ParseException e) {
			t.setBackgroundResource(R.drawable.custom_edit_text_error);				
			return 1;
		}		
		return 0;
	}
	
	public static int ValidateDateTime(View view, int dateID, int timeId){
		
		Button etime = (Button)view.findViewById(timeId);		
		etime.setBackgroundResource(android.R.drawable.edit_text);
		
		if(ValidateDate(view, dateID) > 0)
			return 1;		
		try{
			
			Button eDate = (Button)view.findViewById(dateID);
			Date date = GetDate(eDate.getText().toString(), etime.getText().toString());			
			return date!=null?0:1;
			
		} catch (ParseException e) {			
			etime.setBackgroundResource(R.drawable.custom_edit_text_error);		
			return 1;
		}		
	}
	
	public static void finalizeActivity(final Activity activity, String message){
		
		Utils.showMessage(activity.getApplicationContext(), message);
		
		AsyncManager.getInstance().GetExecutor().execute(new Runnable() {				
			@Override
			public void run() {
				try {
					Thread.sleep(MainApp.SLEEP_TIME);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
				finally{
					activity.finish();						
				}
				
			}
		});
	}
	
	public static void showAlertDialog(Activity act, String title ,String message ,DialogInterface.OnClickListener acceptListener ){
		AlertDialog.Builder ab = new  AlertDialog.Builder(act);
		ab.setTitle(title);		
		ab.setMessage(message);		
		ab.setPositiveButton(R.string.cerrar, acceptListener);					
		ab.show();
		
	}
	
	public static void showErrorDialog(Activity act, String message ,DialogInterface.OnClickListener acceptListener ){
		AlertDialog.Builder ab = new  AlertDialog.Builder(act);
		ab.setTitle(R.string.error);
		ab.setMessage(message);
		ab.setPositiveButton(R.string.cerrar, acceptListener);
		ab.show();		
	}
	
	public static void showErrorDialog(Activity act, String message ){
		AlertDialog.Builder ab = new  AlertDialog.Builder(act);
		ab.setTitle(R.string.error);
		ab.setMessage(message);
		ab.setPositiveButton(R.string.cerrar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {				
				
			}
		});
		ab.show();		
	}
	
	public static void showErrorDialogAndFinish(Activity act, String message ){
		AlertDialog.Builder ab = new  AlertDialog.Builder(act);
		final Activity activity = act;
		ab.setTitle(R.string.error);
		ab.setMessage(message);		
		ab.setPositiveButton(R.string.cerrar, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				activity.finish();				
			}
		});
		ab.show();		
	}
	
	
	 
	@SuppressLint("DefaultLocale")
	public static String parse(int n){
		return  String.format("%02d", n);
	}
	
	@SuppressLint("DefaultLocale")
	public static String parse(double n){
		return  String.format("%02.2f", n);
	}
	
	public static IdNameValue getIdName(IdNameValue[]values, String name){
		if(values == null) 
			return null;
		for (int i = 0; i < values.length; i++) {
			if(values[i].name.equals(name))
				return values[i];
		}
		return null;
	}
	
	public static Date getDate(JSONObject json, String field) throws JSONException{
		if(json.has(field)){
			Date date = Utils.GetDate(json.getString(field));
			return date!= null? date : new Date();
		}
		return null;
	}
			
	public static Date getCurrentDate(){
		Date curret = new Date();
		try {
			return dfDateOnly.parse(dfDateOnly.format(curret));
		} catch (ParseException e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	public static int validateEmailAddress(View view , int viewId){
		EditText t= (EditText) view.findViewById(viewId);
		String s = t.getText().toString();
		t.setBackgroundResource(R.color.white);		
		if(s == null || s.isEmpty() || !ValidateEmailAddress(s)){
			t.setBackgroundResource(R.drawable.custom_edit_text_error);			
			return 1;
		}
		return 0;
	}
	public static boolean ValidateEmailAddress(String email){
		return Pattern.matches("(\\w+)(\\.(\\w+))*@(\\w+)(\\.(\\w+))*", email);
	}
	
	public static IdNameValue[] getStates(){
		return new IdNameValue[]{ new IdNameValue(1, "Abierta"), new IdNameValue(2, "Cerrada")};
	}
	
	public static boolean isNullOrWhitespace(String value){
		if(value == null)
			return true;
		
		int length = value.length();
		for (int i = 0; i < length; i++) {
			if(value.charAt(i) != ' '){
				return false;
			}
		}
		return true;
	}
	
}
