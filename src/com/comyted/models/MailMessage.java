package com.comyted.models;

import java.io.Serializable;

public class MailMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String Sender;
	public String Receiver;
	public String CC;
	public String Subject;
	public String Text;
	
	public MailMessage() {
		 
	}
	
	public MailMessage(String sender, String receiver, String cC,
			String subject, String text) {
		Sender = sender;
		Receiver = receiver;
		CC = cC;
		Subject = subject;
		Text = text;
	}
	
}
