package com.comyted.persistence;

import com.comyted.Utils;
import com.comyted.models.LocalSheet;
import com.comyted.models.LocalTask;
import com.comyted.models.SheetEdit;
import com.comyted.models.TaskEdit;
import com.comyted.models.UserLogin;
import com.enterlib.converters.DateConverter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDatabase extends SQLiteOpenHelper {

	DateConverter dateConverter;
	
	public LocalDatabase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
		dateConverter = new DateConverter("yyyy-MM-dd HH:mm:ss");
	}
	
	public LocalDatabase(Context context) {
		this(context, SQliteTables.DATABASE, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try { 
			Log.d(getClass().getName(), "Creating Tables");
			db.execSQL(SQliteTables.SHEETS_SQL);
			db.execSQL(SQliteTables.TASKS_SQL);
			db.execSQL(SQliteTables.USER_SQL);
			Log.d(getClass().getName(), "Creating SUCCES");
			
		} catch (SQLException e) {						
			Log.d(getClass().getName(), "Creating Tables FAIL :" +e.getMessage());
			throw e;
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.d(getClass().getName(), "Droping Tables");
		try{
		db.execSQL("DROP TABLE IF IT EXISTS "+ SQliteTables.SHEET);
		db.execSQL("DROP TABLE IF IT EXISTS "+ SQliteTables.TASKS);
		db.execSQL("DROP TABLE IF IT EXISTS "+ SQliteTables.USERS);
		}
		catch(SQLException e){
			Log.d(getClass().getName(), "Droping Tables FAIL :" +e.getMessage());
			throw e;
		}
		onCreate(db);		
	}
	
	public long insertSheet(SheetEdit value , int action){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values  = new ContentValues();
		values.put("idSheet", value.id);
		values.put("codusuario", value.codusuario);
		values.put("codorden", value.codorden);
		values.put("codtecnico", value.codtecnico);
		values.put("codplanta", value.codplanta);
		values.put("codpeticionario", value.codpeticionario);
		values.put("codcargoregion", value.codcargoregion);
		values.put("tipotrabajo", value.tipotrabajo);
		values.put("tipohoja", value.tipohoja);
		values.put("fechafinmontaje", dateConverter.getString(value.fechafinmontaje));
		values.put("fechahoja",dateConverter.getString(value.fechahoja));
		values.put("codmotorplanta", value.codmotorplanta);
		values.put("horasmotor", value.horasmotor);
		values.put("codestado", value.codestado);
		values.put("numseriemotor", value.numseriemotor);
		values.put("expediente", value.expediente);
		values.put("titulo", value.titulo);
		values.put("direccion", value.direccion);
		values.put("codpos", value.codpos);
		values.put("pais", value.pais);
		values.put("provincia", value.provincia);
		values.put("ciudad", value.ciudad);
		values.put("telefono", value.telefono);
		values.put("refpedido", value.refpedido);
		values.put("action", action);
		
		return db.insert(SQliteTables.SHEET, null, values);
	}
	
	
	public long insertTask(TaskEdit value, int action)
	{
		SQLiteDatabase db = getWritableDatabase();
				
		ContentValues values  = new ContentValues();
		values.put("cod_tarea", value.codtarea);
		values.put("Idhoja", value.idhoja);
		values.put("titulo", value.titulo);
		values.put("Idtecnico", value.idtecnico);
		values.put("fecha", dateConverter.getString(value.fecha));
		values.put("tdfecha", dateConverter.getString(value.tdFecha));
		values.put("thfecha", dateConverter.getString(value.thFecha));
		values.put("ddfecha", dateConverter.getString(value.ddFecha));
		values.put("dhfecha", dateConverter.getString(value.dhFecha));
		values.put("horasviaje", value.horasviaje);
		values.put("kilometros", value.kilometros);
		values.put("estado", value.estado);
		values.put("action", action);
	
		return db.insert(SQliteTables.TASKS, null, values);
	}
	
	public int removeSheet(int sheetId){
		SQLiteDatabase db =getWritableDatabase(); 
		int count = removeSheet(db, sheetId);		
		return count;
	}
	
	public int removeTask(int taskId){
		return removeTask(getWritableDatabase(), taskId);
	}
	
	public int removeSheet(SQLiteDatabase db ,int sheetId){
		return db.delete(SQliteTables.SHEET, "id = "+ sheetId, null);
	}
	
	public int removeTask(SQLiteDatabase db, int taskId){
		return db.delete(SQliteTables.TASKS, "id = "+ taskId, null);
	}
	
	
	public long setLogin(UserLogin login){
		return setLogin(login.username, login.password);
	}
	
	public long setLogin(String username , String password) {
		UserLogin login = getLogin();		
		SQLiteDatabase db = getWritableDatabase();
		if(login!=null){
			db.delete(SQliteTables.USERS, "username = '"+ login.username+"'", null);
		}
				
		ContentValues values  = new ContentValues();
		values.put("username", username);
		values.put("password", password);				
		return db.insert(SQliteTables.USERS, null, values);
	}

	
	public UserLogin getLogin() {
		final SQLiteDatabase db = getWritableDatabase();
		String[] columns = new String[]{ "username" , "password"};
		
		Cursor cursor = db.query(SQliteTables.USERS, columns, null, null, null, null, null);
		
		if(cursor.getCount() == 0)
			return null;
		
		UserLogin login = new UserLogin();
		while(cursor.moveToNext()){
			login.username = cursor.getString(0);
			login.password = cursor.getString(1);
		}
		cursor.close();
		return login;
	}
	
	public LocalSheet[] getSheets(){
		String[] columns = new String[]{
				"id",
				"idSheet",
				"codusuario",
				"codorden",
				"codtecnico",
				"codplanta",
				"codpeticionario",
				"codcargoregion",
				"tipotrabajo",
				"tipohoja",
				"fechafinmontaje",
				"fechahoja",				
				"codmotorplanta",
				"horasmotor",
				"codestado",
				"numseriemotor",
				"expediente",
				"titulo",
				"direccion",
				"codpos",
				"pais",
				"provincia",
				"ciudad",
				"telefono",
				"refpedido",
				"action",
		};
		
		SQLiteDatabase db = getWritableDatabase();
	
		Cursor cursor = db.query(SQliteTables.SHEET, columns, null, null, null, null, null);						
		LocalSheet[] sheets = new LocalSheet[cursor.getCount()];		
		
		int k = 0;
		while(cursor.moveToNext()){			
			int i = 0;			
			SheetEdit s = new SheetEdit();			
			int id = cursor.getInt(i++);
			
			s.id = cursor.getInt(i++);
			s.codusuario = cursor.getInt(i++);
			s.codorden = cursor.getInt(i++);
			s.codtecnico = cursor.getInt(i++);
			s.codplanta = cursor.getInt(i++);
			s.codpeticionario = cursor.getInt(i++);
			s.codcargoregion = cursor.getInt(i++);
			s.tipotrabajo = cursor.getInt(i++);
			s.tipohoja = cursor.getInt(i++);
			s.fechafinmontaje =dateConverter.getDate(cursor.getString(i++));
			s.fechahoja =dateConverter.getDate(cursor.getString(i++));
			s.codmotorplanta = cursor.getInt(i++);
			s.horasmotor = cursor.getString(i++);
			s.codestado = cursor.getInt(i++);
			s.numseriemotor = cursor.getString(i++);
			s.expediente = cursor.getString(i++);
			s.titulo = cursor.getString(i++);
			s.direccion = cursor.getString(i++);
			s.codpos = cursor.getString(i++);
			s.pais = cursor.getString(i++);
			s.provincia = cursor.getString(i++);
			s.ciudad = cursor.getString(i++);
			s.telefono = cursor.getString(i++);
			s.refpedido = cursor.getString(i++);
			
			int action = cursor.getInt(i++);			
			
			sheets[k] = new LocalSheet(s, id, action);
			k++;
		}
		cursor.close();
		
		return sheets;
	}
	
	public LocalTask[] getTasks(){
		final SQLiteDatabase db = getWritableDatabase();
		String[] columns = new String[]{
				"id",
				"cod_tarea",
				"Idhoja",
				"titulo",
				"Idtecnico",
				"fecha",
				"tdfecha",
				"thfecha",
				"ddfecha",
				"dhfecha",					
				"horasviaje",
				"kilometros",
				"estado",
				"action"				
		};
		
		// fetchs sheets from the database		
		Cursor cursor = db.query(SQliteTables.TASKS, columns, null, null, null, null, null);
		final LocalTask[] tasks = new LocalTask[cursor.getCount()];	
		
		int k = 0;
		while(cursor.moveToNext()){			
			int i = 0;
			TaskEdit s = new TaskEdit();
			int id = cursor.getInt(i++);			
					
			s.codtarea = cursor.getInt(i++);
			s.idhoja = cursor.getInt(i++);
			s.titulo = cursor.getString(i++);
			s.idtecnico = cursor.getInt(i++);
			s.fecha = Utils.GetDate(cursor.getString(i++));
			s.tdFecha = Utils.GetDate(cursor.getString(i++));
			s.thFecha = Utils.GetDate(cursor.getString(i++));
			s.ddFecha = Utils.GetDate(cursor.getString(i++));
			s.dhFecha =Utils.GetDate(cursor.getString(i++));
			s.horasviaje = cursor.getDouble(i++);
			s.kilometros = cursor.getDouble(i++);
			s.estado = cursor.getInt(i++);
			
			int action = cursor.getInt(i++);
			tasks[k] = new LocalTask(s, id, action);
			k++;
		}
		cursor.close();
		return tasks;
	}

}
