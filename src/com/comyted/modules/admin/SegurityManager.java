package com.comyted.modules.admin;

public class SegurityManager {
	
	/* (non-Javadoc)
	 * @see com.comyted.ISecurityManager#hasRole(int, java.lang.String)
	 */
	
	public boolean hasRole(int userId, String roleName){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.comyted.ISecurityManager#hasPermission(int, java.lang.String, java.lang.String)
	 */
	
	public boolean hasPermission(int userId, String roleName, String permission){
		return true;
	}
}
