package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.mail.MailHelper;
import com.ferbo.mail.beans.Correo;

@Named
@ViewScoped
public class TestMailBean implements Serializable {
	
	private static final long serialVersionUID = -4533920022153841278L;

	private static Logger log = LogManager.getLogger(TestMailBean.class);
	
	private String mailAddress;
	private String name;
	private List<Correo> alTo;
	private MailHelper helper;
	
	public TestMailBean() {
		this.name = "Test user";
	}
	
	@PostConstruct
	public void init() {
		helper = new MailHelper();
	}
	
	public void sendMail() {
		Correo destino = null;
		String subject = null;
		String htmlBody = null;
		
		try {
			destino = new Correo(mailAddress, name);
			this.alTo = new ArrayList<Correo>();
			this.alTo.add(destino);
			subject = "Test FERBO";
			htmlBody = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>TEST FERBO</title></head><body style=\"font-family: sans-serif; background-color: #ffffff;color: #000000;\"><p>Esta es una prueba del servicio de envío de correo de FERBO.<p></body></html>";
			
			
			for(Correo m : alTo) {
                helper.addTo(m);
            }
			
			helper.setSubject(subject);
            helper.setMailBody(htmlBody);
            
            helper.sendMessage(MailHelper.JNDI_MAIL_AVISOS);
			
		} catch(Exception ex) {
			log.error("Problema con el envío de notificaciones por correo : " + MailHelper.JNDI_MAIL_FACTURACION, ex);
			ex.printStackTrace();
		}
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

}
