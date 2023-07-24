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
import mx.com.ferbo.dao.UsuarioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.utils.IOUtil;

public class SendMailFacturaBL {
	private static Logger log = LogManager.getLogger(SendMailFacturaBL.class);
	
	private List<Adjunto> alFiles = null;
	List<Correo> alTo = null;
	List<Correo> alCC = null;
    private String serie = null;
    private String folio = null;
    ClienteDAO clienteDAO = null;
    UsuarioDAO usuarioDAO = null;
    Cliente cliente = null;
    Integer idCliente = null;
    Usuario loggedUser = null;
	
	public SendMailFacturaBL(Integer idCliente) {
		alTo = new ArrayList<Correo>();
		alCC = new ArrayList<Correo>();
		clienteDAO = new ClienteDAO();
		usuarioDAO = new UsuarioDAO();
		this.idCliente = idCliente;
    }
	
	public Usuario getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(Usuario loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public List<Adjunto> getAlFiles() {
        return alFiles;
    }

    public void setAlFiles(List<Adjunto> alFiles) {
        this.alFiles = alFiles;
    }
    
    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }
    
    public void send() {
    	try {
    		this.processContacts();
    		this.processUsers();
    		this.sendMail();
    	} catch(Exception ex) {
    		log.error("Problema en el envío por correo de los documentos CFDI...", ex);
    	}
    	
    }
    
	public void processUsers() {
    	//Usuarios de facturacion
    	List<Usuario> usuarios = null;
    	usuarios = usuarioDAO.buscarPorPerfil(2);
    	for(Usuario usuario : usuarios) {
    		if( !"A".equalsIgnoreCase(usuario.getStUsuario() ) ) {
                log.info("El usuario " + usuario.getUsuario() + " no tiene status Activo. Se omitirá el envío de notificación de factura.");
                continue;
            }
            
            String nombreBuzon = String.format("%s %s %s", usuario.getNombre(), usuario.getApellido1(), usuario.getApellido2());
            Correo correo = new Correo(usuario.getMail(), nombreBuzon);
            alCC.add(correo);
    	}
    	
    	String destinatario = String.format("%s %s %s",
                this.loggedUser.getNombre() == null ? "" : this.loggedUser.getNombre(),
                this.loggedUser.getApellido1() == null ? "" : this.loggedUser.getApellido1(),
                this.loggedUser.getApellido2() == null ? "" : this.loggedUser.getApellido2());
        
        String sMail = this.loggedUser.getMail();
        Correo mailLoggedUser = new Correo(sMail, destinatario);
        
        alCC.add(mailLoggedUser);
    	
    }
    
    
    public void processContacts() {
    	cliente = clienteDAO.buscarPorId(idCliente, true);  	
    	List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();
    	for(ClienteContacto cteContacto : clienteContactoList) {
    		Contacto contacto = cteContacto.getIdContacto();
    		
    		if(cteContacto.getRecibeFacturacion() == false) {
    			continue;
    		}
    		processMail(contacto);
    	}
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
    	String     mailInventarioHTML = null;
        File       mailInventarioFile = null;
        FileReader mailInventarioReader = null;
        BufferedReader reader = null;
        StringBuilder sb = null;
        String subject = null;
        
        
        try {
            subject = String.format("Su factura electrónica de FERBO %s-%s", this.serie, this.folio);
            
            mailInventarioHTML = "/mail/mailFacturaElectronica.html";
            mailInventarioFile = new File( getClass().getResource(mailInventarioHTML).getFile());
            mailInventarioReader = new FileReader(mailInventarioFile);
            reader = new BufferedReader(mailInventarioReader);
            sb = new StringBuilder();
            String linea = null;
            while((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
            
            String html = sb.toString();
            html = html.replace("@serie@", serie);
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
	
	
}
