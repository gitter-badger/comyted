package com.comyted.models;

public class AppUser {
		
	public int id;
	
	public String login;
	
	public String nombre;
	
	public boolean activo;
	
	public int perfil;
		  
    public boolean jefeDepto;
   
    public String email ;
    
    public boolean tecnico ;
		
	public AppUser(){
		
	}
	
	public AppUser(int id, String login, String name, int perfil) {
		this.id = id;
		this.login = login;
		this.nombre = name;	
		this.perfil = perfil;
	}
	@Deprecated 
	public int getId() {
		return id;
	}
	@Deprecated
	public void setId(int id) {
		this.id = id;
	}
	@Deprecated
	public String getLogin() {
		return login;
	}
	@Deprecated
	public void setLogin(String login) {
		this.login = login;
	}
	@Deprecated
	public String getName() {
		return nombre;
	}
	@Deprecated
	public void setName(String name) {
		this.nombre = name;
	}
	@Deprecated
	public int getPerfil() {
		return perfil;
	}
	@Deprecated
	public void setPerfil(int perfil) {
		this.perfil = perfil;
	}
	
	public boolean isServiceManager() {
		return jefeDepto;
	}
	
	@Override
	public String toString() {	
		return this.nombre;
	}
	
}
