package com.comyted.persistence;

public final class SQliteTables {
	
	public static final String SHEET = "sheets";
	public static final String TASKS = "tasks";
	public static final String USERS = "User";
	public static final String SIGNATURES = "firmas";
	public static final String DATABASE = "movility";	
	
	public static final String SHEETS_SQL = 
		"CREATE TABLE IF NOT EXISTS "+SHEET+" (" 
			+ "id integer primary key autoincrement,"
			+ "idSheet integer,"				
			+ "codusuario integer,"
			+ "codorden integer, "
			+ "codtecnico integer,"
			+ "codplanta integer,"
			+ "codpeticionario integer,"
			+ "codcargoregion integer,"
			+ "tipotrabajo integer,"
			+ "tipohoja integer,"			
			+ "fechafinmontaje char(50),"
			+ "fechahoja char(50),"
			+ "codmotorplanta integer,"
			+ "horasmotor text,"
			+ "codestado integer,"
			+ "numseriemotor text,"
			+ "expediente text,"
			+ "titulo text,"
			+ "direccion text,"
			+ "codpos text,"
			+ "pais text,"
			+ "provincia text,"
			+ "ciudad text,"
			+ "telefono text,"
			+ "refpedido text,"
			+ "action integer)";
	
	public static final String TASKS_SQL = 
			"CREATE TABLE IF NOT EXISTS "+TASKS+" ("
			+ "id integer primary key autoincrement,"
			+ "cod_tarea integer,"
			+ "Idhoja integer,"
			+ "titulo text,"
			+ "Idtecnico integer,"
			+ "fecha char(30),"
			+ "tdfecha char(30),"
			+ "thfecha char(30),"
			+ "ddfecha char(30),"
			+ "dhfecha char(30),"
			+ "horasviaje double,"
			+ "kilometros double,"
			+ "estado integer,"
			+ "action integer)";
	
	static final String SIGNATURE_SQL = 
			"CREATE TABLE IF NOT EXISTS "+SIGNATURES+"("
					+ "id integer primary key autoincrement,"
					+ "idHoja integer,"
					+ "imgFile char(256)"
					+ ") ";
	
	static final String USER_SQL = 
			"CREATE TABLE IF NOT EXISTS "+USERS+" ( "
					+ "id integer primary key autoincrement,"
					+ "username text,"
					+ "password text)";
	
	
}
