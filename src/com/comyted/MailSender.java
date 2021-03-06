package com.comyted;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.util.Log;

import com.enterlib.StringUtils;
import com.enterlib.exceptions.InvalidOperationException;

public class MailSender extends javax.mail.Authenticator {
	private Session session;
	private String user;
	private String password;
	private String smptServer;
	private String smptPort;

//	static {   
//        Security.addProvider(new JSSEProvider());   
//    }  
	
	public MailSender() throws InvalidOperationException {		
		
		ConfigValues values= MainApp.getInstance().getConfigValues();
		
		smptServer =values.get(ConfigValues.CONST_EMAIL_SMTP);
		smptPort =values.get(ConfigValues.CONST_EMAIL_PORT);
		user =values.get(ConfigValues.CONST_EMAIL_USER);
		password =values.get(ConfigValues.CONST_EMAIL_PASSWORD);
		
		if(StringUtils.isNullOrWhitespace(smptServer) ||
			StringUtils.isNullOrWhitespace(smptPort) ||
			StringUtils.isNullOrWhitespace(user) ||
			StringUtils.isNullOrWhitespace(password))
			throw new InvalidOperationException(MainApp.getInstance().getString(R.string.error_configuracion_de_email));
		
//		Properties props = new Properties();
//		props.setProperty("mail.transport.protocol", "smtp");   
//	    props.setProperty("mail.host", smptServer);   
//	    props.put("mail.smtp.auth", "true");   
//	    props.put("mail.smtp.port", smptPort);   
//	    props.put("mail.smtp.socketFactory.port", smptPort);   
//	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
//	    props.put("mail.smtp.socketFactory.fallback", "false");   
//	    props.setProperty("mail.smtp.quitwait", "false");
		//session = Session.getDefaultInstance(props, this);
		
		Properties emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", smptPort);
		emailProperties.put("mail.smtp.auth", true);
		emailProperties.put("mail.smtp.starttls.enable", true);
		Log.i("Mail", "Mail server properties set.");

	   session = Session.getDefaultInstance(emailProperties, null);   
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		 return new PasswordAuthentication(user, password);   
	}
	
	 public synchronized void sendMail(String subject, String body, String sender, String recipients, String cc) 
			 throws Exception {   	 
		 
	        MimeMessage message = new MimeMessage(session);   	          
	        message.setSender(new InternetAddress(sender));   
	        message.setSubject(subject);
	        
	        if(!StringUtils.isNullOrWhitespace(cc)){
	        	if (cc.indexOf(',') > 0)
	        		 message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
	        	else
	        		 message.setRecipient(Message.RecipientType.CC,new InternetAddress(cc));
	        }
	        
	        //DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
	        //message.setDataHandler(handler);
	        message.setText(body);
	        
	        if (recipients.indexOf(',') > 0)   
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));   
	        else  
	            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
	        	        
        	//Transport.send(message);
	        Transport transport = session.getTransport("smtp");
			transport.connect(smptServer, user, password);
			Log.i("Mail","allrecipients: "+message.getAllRecipients());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			Log.i("Mail", "Email sent successfully.");
	    }   

	    public class ByteArrayDataSource implements DataSource {   
	        private byte[] data;   
	        private String type;   

	        public ByteArrayDataSource(byte[] data, String type) {   
	            super();   
	            this.data = data;   
	            this.type = type;   
	        }   

	        public ByteArrayDataSource(byte[] data) {   
	            super();   
	            this.data = data;   
	        }   

	        public void setType(String type) {   
	            this.type = type;   
	        }   

	        public String getContentType() {   
	            if (type == null)   
	                return "application/octet-stream";   
	            else  
	                return type;   
	        }   

	        public InputStream getInputStream() throws IOException {   
	            return new ByteArrayInputStream(data);   
	        }   

	        public String getName() {   
	            return "ByteArrayDataSource";   
	        }   

	        public OutputStream getOutputStream() throws IOException {   
	            throw new IOException("Not Supported");   
	        }   
	    }   
}
