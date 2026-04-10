package mx.com.ferbo.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.mail.MailHelper;
import com.ferbo.mail.beans.Adjunto;
import com.ferbo.mail.beans.Correo;
import com.ferbo.tools.MailException;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.utils.IOUtil;

public class SendMailTicketSalida {
	
	private static Logger log = LogManager.getLogger(SendMailTicketSalida.class);
	
	private List<Adjunto> alFiles = null;
	private List<Correo>  alTo    = null;
	private List<Correo>  alCC    = null;
	
	private ClienteDAO clienteDAO = null;
    private Cliente    cliente    = null;
    private Integer    idCliente  = null;
    private Usuario    loggedUser = null;
    private String     folio      = null;
	
	public SendMailTicketSalida(Integer idCliente) {
		this.alTo = new ArrayList<Correo>();
		this.alCC = new ArrayList<Correo>();
		this.clienteDAO = new ClienteDAO();
		this.idCliente = idCliente;
	}
	
	public void send() {
		try {
			this.processContacts();
			this.processUsers();
			this.sendMail();
		} catch(Exception ex) {
			log.error("Problema en el envío de correo del ticket de salida...", ex);
		}
	}
	
	public void addAttachment(String nombreArchivo, String tipoArchivo, byte[] contenido)
	throws MailException {
		Adjunto archivo = null;
		
		if(alFiles == null)
			alFiles = new ArrayList<Adjunto>();
		
		if(nombreArchivo == null || "".equalsIgnoreCase(nombreArchivo.trim()))
			throw new MailException("Debe indicar un nombre de archivo.");
		
		if(tipoArchivo == null || "".equalsIgnoreCase(tipoArchivo.trim()))
			throw new MailException("Debe indicar un tipo de archivo");
		
		if(contenido == null || contenido.length <= 0)
			throw new MailException("El contenido del archivo se encuentra vacío.");
		
		switch(tipoArchivo) {
		case Adjunto.TP_ARCHIVO_JPEG:
		case Adjunto.TP_ARCHIVO_PDF:
		case Adjunto.TP_ARCHIVO_PNG:
		case Adjunto.TP_ARCHIVO_XLS:
		case Adjunto.TP_ARCHIVO_XML:
		case Adjunto.TP_ARCHIVO_ZIP:
			archivo = new Adjunto(nombreArchivo, tipoArchivo, contenido);
			alFiles.add(archivo);
			break;
		default:
			throw new MailException("Tipo de archivo no soportado.");	
		}
	}
	
	public void processContacts() {
		cliente = clienteDAO.buscarPorId(idCliente, true);  	
    	List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();
    	for(ClienteContacto cteContacto : clienteContactoList) {
    		Contacto contacto = cteContacto.getIdContacto();
    		
    		if(cteContacto.getRecibeInventario() == false) {
    			continue;
    		}
    		processMail(contacto);
    	}
	}
	
	public void processUsers() {
		String destinatario = null;
		String sMail = null;
		Correo mailLoggedUser = null;
		
		if(this.loggedUser == null) {
			return;
    	}
		destinatario = String.format("%s %s %s",
				this.loggedUser.getNombre() == null ? "" : this.loggedUser.getNombre(),
						this.loggedUser.getApellido1() == null ? "" : this.loggedUser.getApellido1(),
								this.loggedUser.getApellido2() == null ? "" : this.loggedUser.getApellido2());
		
		sMail = this.loggedUser.getMail();
		mailLoggedUser = new Correo(sMail, destinatario);
		
		alCC.add(mailLoggedUser);
	}
	
	private void processMail(Contacto contacto) {
		List<MedioCnt> medioCntList = contacto.getMedioCntList();
		for(MedioCnt medioCnt : medioCntList) {
			Mail mail = medioCnt.getIdMail();
			String nombreBuzon = String.format("%s, %s, %s", contacto.getNbNombre(), contacto.getNbApellido1(), contacto.getNbApellido2());
            Correo correo = new Correo(mail.getNbMail(), nombreBuzon);
            alTo.add(correo);
		}
	}
	
	private void sendMail() throws IOException, MailException {
    	String     mailHTML = null;
        File       mailFile = null;
        FileReader mailReader = null;
        BufferedReader reader = null;
        StringBuilder sb = null;
        String subject = null;
        
        
        try {
            subject = String.format("Confirmación de salida %s - FERBO ", this.folio);
            
            mailHTML = "/mail/mailConfirmacionSalida.html";
            mailFile = new File( getClass().getResource(mailHTML).getFile());
            mailReader = new FileReader(mailFile);
            reader = new BufferedReader(mailReader);
            sb = new StringBuilder();
            String linea = null;
            while((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
            
            String html = sb.toString();
            html = html.replace("@folio@", folio);
            
            MailHelper helper = new MailHelper();
            for(Correo m : alTo) {
                helper.addTo(m);
            }
            
            for(Correo m : alCC) {
                helper.addCC(m);
            }
            
            
            helper.setSubject(subject);
            helper.setMailBody(html);
            
            for(Adjunto adjunto : alFiles) {
                helper.addAttachment(adjunto);
            }
            
            helper.sendMessage(MailHelper.JNDI_MAIL_FACTURACION);
            
        } finally {
            IOUtil.close(reader);
        }
		
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public Usuario getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(Usuario loggedUser) {
		this.loggedUser = loggedUser;
	}
}
