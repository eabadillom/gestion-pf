package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.ferbo.facturama.business.CertificadosBL;
import com.ferbo.facturama.request.Csd;
import com.ferbo.facturama.response.CsdRsp;
import com.ferbo.facturama.tools.FacturamaException;

import mx.com.ferbo.dao.CertificadoDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.RegimenFiscalDAO;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.utils.IOUtil;


@Named
@ViewScoped
public class EmisoresCFDISBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private static Logger log = LogManager.getLogger(EmisoresCFDISBean.class);
		private UploadedFile certificadoFile;
		private UploadedFile llavePrivadaFile;
		private String nombrellavePrivada;
		private String password;
		private boolean estado;
		
		private Certificado certificadoConFechaMax;
		private StreamedContent fileDownloadCer;
		private StreamedContent fileDownloadKey;
		private EmisoresCFDIS emisor;
		private Certificado certificado;
		
		private EmisoresCFDISDAO emisoresDAO;
		private RegimenFiscalDAO regimenFiscalDAO;
		private CertificadoDAO certificadoDAO;
		
		private List<EmisoresCFDIS> listaEmisor;
		private List<RegimenFiscal> listaRegimenFiscal;
		private List<Certificado> listaCertificado;
		
		
	public EmisoresCFDISBean() {
		listaEmisor = new ArrayList<EmisoresCFDIS>();
		listaRegimenFiscal = new ArrayList<RegimenFiscal>();
		listaCertificado = new ArrayList<Certificado>();
		emisoresDAO = new EmisoresCFDISDAO();
		regimenFiscalDAO = new RegimenFiscalDAO();
		certificadoDAO = new CertificadoDAO();
	}

	@PostConstruct
	public synchronized void init() {
		listaRegimenFiscal = regimenFiscalDAO.findAll();
		listaEmisor = emisoresDAO.findall(false);
	}
	
	public void nuevoEmisor() {
		this.emisor = new EmisoresCFDIS();
	}
	
	private void validacionRFC(String tipoPersona, String rfc) throws InventarioException {
		if("F".equals(tipoPersona)) {
			if(rfc.length() != 13)
				throw new InventarioException("El formato del RFC es incorrecto.");
		}
		if("M".equals(tipoPersona)) {
			if(rfc.length() != 12) 
				throw new InventarioException("El formato del RFC es incorrecto.");
		}
	}
	
	public void guardarEmisor() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar emisor";

		String em = null;
		
		try {
			if(this.emisor.getTp_persona() == null)
				throw new InventarioException("Debe indicar el tipo de persona (Física o moral)");
			
			if(this.emisor.getNb_emisor() == null || "".equalsIgnoreCase(this.emisor.getNb_emisor().trim()))
				throw new InventarioException("Debe indicar el nombre o razón social.");
			
			if(this.emisor.getNb_rfc() == null || "".equalsIgnoreCase(this.emisor.getNb_rfc()))
				throw new InventarioException("Debe indicar el RFC");
			
			this.validacionRFC(this.emisor.getNb_rfc(), this.emisor.getTp_persona());
		
			if(this.emisor.getCd_emisor() == null)
				em = emisoresDAO.guardar(emisor);
			else
				em = emisoresDAO.actualizar(emisor);
			
			if(em != null)
				throw new InventarioException("Ocurrió un problema al guardar el emisor.");
			
			listaEmisor = emisoresDAO.findall(false);
			
			mensaje = "El emisor se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para cargar los regímenes fiscales...", ex);
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-emisor");
		}
	}
	
	public void guardarCertificado() {
		this.certificado = new Certificado();
		
		byte[] contenidoCertificado = null;
		byte[] contenidollavePrivada = null;;
		String nombreLPrivada = llavePrivadaFile.getFileName();
		try {
			contenidoCertificado = IOUtil.read(certificadoFile.getInputStream());
			contenidollavePrivada = IOUtil.read(llavePrivadaFile.getInputStream());
			certificado.setLlavePrivada(nombreLPrivada);
			certificado.setDt_llavePrivada(contenidollavePrivada);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String nombreCertificado = certificadoFile.getFileName();
		certificado.setCertificado(contenidoCertificado);
		certificado.setNombreCertificado(nombreCertificado);
		System.out.println("Guardando certificado");
		System.out.println(certificado);
		certificado.setFechaAlta(new Date());
		certificado.setPassword(password);
		certificado.setEmisor(emisor);
		String certi = certificadoDAO.guardar(certificado);
		
		CertificadosBL facturamaBo = new CertificadosBL();
		
		String sCertificado = new String(Base64.getEncoder().encode(certificado.getCertificado()));
        String sLlavePrivada = new String(Base64.getEncoder().encode(certificado.getDt_llavePrivada()));
		
		Csd csd = new Csd();
		csd.setRfc(emisor.getNb_rfc());
        csd.setCertificate(sCertificado);
        csd.setPrivateKey(sLlavePrivada);
        csd.setPrivateKeyPassword(certificado.getPassword());
		
		try {
			List<CsdRsp> certificados = facturamaBo.get();
			log.info(certificados);
			for(CsdRsp csdR : certificados) {
				if(csdR.getRfc().equals(certificado.getEmisor().getNb_rfc())){
		                facturamaBo.elimina(csdR.getRfc());
				}
			}
			facturamaBo.registra(csd);
			emisor.setUuid("OK");
			emisoresDAO.actualizar(emisor);
		} catch (IOException | FacturamaException e) {
			log.error("Problema para cargar el archivo CSD...", e);
		}
		
		if(certi == null) {
			listaCertificado.clear();
			listaCertificado = certificadoDAO.findAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Certificado agregado con exito" + certificado.getCdCertificado(), null));
			PrimeFaces.current().ajax().update("form:messages","form:dt-emisor");
			}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error, verificar datos" + certificado.getCdCertificado(), certi));
			PrimeFaces.current().ajax().update("form:messages");
			}
		this.certificado = new Certificado();
		this.password = null;
	}
	
	public void upload() {
		if (certificadoFile != null) {
			FacesMessage message = new FacesMessage("Successful", certificadoFile.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void cargaDeArchivos() {

		listaCertificado = certificadoDAO.buscarporcdEmisor(emisor.getCd_emisor());

		if (listaCertificado.isEmpty()) {
			certificado = new Certificado();
		} else {

			Integer size = listaCertificado.size() - 1;
			this.certificado = listaCertificado.get(size);
			Date fechaAltaMax = certificado.getFechaAlta();

			System.out.println("Fecha maxima" + fechaAltaMax);
			InputStream inputCertificado = new ByteArrayInputStream(certificado.getCertificado());
			fileDownloadCer = DefaultStreamedContent.builder().name(certificado.getNombreCertificado())
					.contentType("aplication/x-x509-user-cert").stream(() -> inputCertificado).build();
			
			InputStream inputLlavePrivada = new ByteArrayInputStream(certificado.getDt_llavePrivada());
			fileDownloadKey = DefaultStreamedContent.builder().name(certificado.getLlavePrivada())
					.contentType("aplication/x-x509-user-cert").stream(() -> inputLlavePrivada).build();

		}
	}
	
	public void regimenSelect() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Tipo de persona";
		
		try {
			log.info("Tipo de persona: {}", this.emisor.getTp_persona());
			
			if("M".equals(this.emisor.getTp_persona())) {
				estado = false;
				listaRegimenFiscal = regimenFiscalDAO.buscarPorPersonaMoral();
			} else if ("F".equals(this.emisor.getTp_persona())) {
				estado = true;
				listaRegimenFiscal = regimenFiscalDAO.buscarPorPersonaFisica();
			} else {
				throw new InventarioException("Tipo de persona no válido");
			}
			
			mensaje = "Rellene el formulario";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para cargar los regímenes fiscales...", ex);
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
		
	public void RegimenSelectModificar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Tipo de persona";
		
		try {
			log.info("Tipo de persona: {}", this.emisor.getTp_persona());
			log.info("RFC: {}", this.emisor.getNb_rfc());
			validacionRFC(emisor.getTp_persona(), emisor.getNb_rfc());
			mensaje = "RFC Correcto";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		} catch (Exception ex) {
			log.error("Problema para cargar los regímenes fiscales...", ex);
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
	}
	
	public EmisoresCFDIS getEmisor() {
		return emisor;
	}

	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}

	public List<EmisoresCFDIS> getListaEmisor() {
		return listaEmisor;
	}

	public void setListaEmisor(List<EmisoresCFDIS> listaEmisor) {
		this.listaEmisor = listaEmisor;
	}
	
	public List<RegimenFiscal> getListaRegimenFiscal() {
		return listaRegimenFiscal;
	}

	public void setListaRegimenFiscal(List<RegimenFiscal> listaRegimenFiscal) {
		this.listaRegimenFiscal = listaRegimenFiscal;
	}
	

	public UploadedFile getLlavePrivadaFile() {
		return llavePrivadaFile;
	}

	public void setLlavePrivadaFile(UploadedFile llavePrivadaFile) {
		this.llavePrivadaFile = llavePrivadaFile;
	}

	public UploadedFile getCertificadoFile() {
		return certificadoFile;
	}

	public void setCertificadoFile(UploadedFile certificadoFile) {
		this.certificadoFile = certificadoFile;
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public String getNombrellavePrivada() {
		return nombrellavePrivada;
	}

	public void setNombrellavePrivada(String nombrellavePrivada) {
		this.nombrellavePrivada = nombrellavePrivada;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Certificado> getListaCertificado() {
		return listaCertificado;
	}

	public void setListaCertificado(List<Certificado> listaCertificado) {
		this.listaCertificado = listaCertificado;
	}
	
	public StreamedContent getFileDownloadCer() {
		return fileDownloadCer;
	}

	public void setFileDownloadCer(StreamedContent fileDownloadCer) {
		this.fileDownloadCer = fileDownloadCer;
	}

	public StreamedContent getFileDownloadKey() {
		return fileDownloadKey;
	}

	public void setFileDownloadKey(StreamedContent fileDownloadKey) {
		this.fileDownloadKey = fileDownloadKey;
	}

	public Certificado getCertificadoConFechaMax() {
		return certificadoConFechaMax;
	}

	public void setCertificadoConFechaMax(Certificado certificadoConFechaMax) {
		this.certificadoConFechaMax = certificadoConFechaMax;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
