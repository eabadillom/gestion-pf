package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;

import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.RegimenFiscalDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.RegimenFiscal;

@Named
@ViewScoped
public class EmisoresCFDISBean implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String pmoral;
		private Integer pfisica;
		private String tipoPersona;
		private String regimenCapital;
		private String rfc;
		private String padron;
		private String nombreEmisor;
		private Date iniOperacion;
		private Date ultSAT;
		private EmisoresCFDIS emisor;
		//RegimenFiscal regimenFiscal;
		
		private String regimenFiscal;
		private EmisoresCFDISDAO emisoresDAO;
		private RegimenFiscalDAO regimenFiscalDAO;
		private List<EmisoresCFDIS> listaEmisor;
		private List<RegimenFiscal> listaRegimenFiscal;
		private UploadedFile file;
		
	public EmisoresCFDISBean() {
		listaEmisor = new ArrayList<EmisoresCFDIS>();
		listaRegimenFiscal = new ArrayList<RegimenFiscal>();
		
		emisoresDAO = new EmisoresCFDISDAO();
		regimenFiscalDAO = new RegimenFiscalDAO();
		
		listaRegimenFiscal = regimenFiscalDAO.findAll();
		listaEmisor = emisoresDAO.findall();
	}

@PostConstruct

public void init() {

}

	public void newEmisor() {
		this.emisor = new EmisoresCFDIS();
		this.emisor.setNb_emisor(nombreEmisor);
		this.emisor.setCd_regimen(null);
		this.emisor.setFh_inicio_op(iniOperacion);
		this.emisor.setFh_ult_cambio(ultSAT);
		this.emisor.setNb_regimen_capital(regimenCapital);
		this.emisor.setNb_rfc(rfc);
		this.emisor.setSt_padron(padron);
		this.emisor.setTp_persona(tipoPersona);
		this.emisor.setUuid(null);
		
		System.out.println("NombreEmisor: " + this.nombreEmisor);
		System.out.println("Generando nuevo emisor");
	}

	public void agregaEmisor() {
		System.out.println("Guardando...");
		PrimeFaces.current().executeScript("PF('dialogEmisor').hide()");
		System.out.println("Emisor: " + this.emisor);
		System.out.println("Regimen Fiscal" + this.regimenFiscal);
		System.out.println("Fecha inicio" + this.iniOperacion);
		String em = emisoresDAO.actualizar(emisor);
		if(em == null) {
			listaEmisor.clear();
			listaEmisor = emisoresDAO.findall();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Emisor agregado con exito" + emisor.getCd_emisor(), null));
			}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error al agregar el emisor" + emisor.getCd_emisor(), em));
			PrimeFaces.current().ajax().update("form:messages");
			}
	}

	public void guardaEmisor() {
		PrimeFaces.current().executeScript("PF('dialogEmisor').hide()");
		emisor.setNb_emisor(this.nombreEmisor);
		emisor.setTp_persona(this.tipoPersona);
		emisor.setNb_regimen_capital(this.regimenCapital);
		emisor.setNb_rfc(this.rfc);
		emisor.setFh_inicio_op(this.iniOperacion);
		emisor.setFh_ult_cambio(this.ultSAT);
		emisor.setSt_padron(this.padron);
		RegimenFiscal regimenObj = regimenFiscalDAO.buscarPorId(this.regimenFiscal);
		emisor.setCd_regimen(regimenObj);
		String em = emisoresDAO.guardar(emisor);
		if(em == null) {
		listaEmisor.clear();
		listaEmisor = emisoresDAO.findall();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Emisor agregado con exito" + emisor.getCd_emisor(), null));
		PrimeFaces.current().ajax().update("form:messages","form:dt-emisor");
		}else{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error al agregar el emisor" + emisor.getCd_emisor(), em));
		PrimeFaces.current().ajax().update("form:messages");
		}

this.emisor = new EmisoresCFDIS();
}

	public void upload() {
		if (file != null) {
			FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

		public void RegimenSelect() {
			System.out.println("Tipo de persona: " + this.tipoPersona);
			if("M".equals(tipoPersona)) {				
 				listaRegimenFiscal = regimenFiscalDAO.buscarPorPersonaMoral();	
			}else if("F".equals(tipoPersona)) {
				listaRegimenFiscal = regimenFiscalDAO.buscarPorPersonaFisica();
			}		
		}

	public void actualizaEmisor() {
		PrimeFaces.current().executeScript("PF('dialogEmisor').hide()");
		System.out.println(emisor);
	}
	
	

	public void addMessage() {
		String summary = pfisica != null ? "Checked" : "Unchecked";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
		String summary2 = pmoral != null ? "Checked" : "Unchecked";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary2));
	}

	public void eliminarEmisor() {
		PrimeFaces.current().executeScript("PF('deleteEmisorDialog').hide()");
		String em = emisoresDAO.eliminar(emisor);
		if(em == null) {
		listaEmisor.remove(emisor);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Emisor eliminado con exito" + emisor.getNb_emisor(), null));
		PrimeFaces.current().ajax().update("form:messages", "form:dt-EmisoresCFDIS");
		}else {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al eliminar al emisor"+ emisor.getNb_emisor(),em));
		PrimeFaces.current().ajax().update(("form:messages"));
		}
	}

	public Integer getPfisica() {
		return pfisica;
	}

	public void setPfisica(Integer pfisica) {
		this.pfisica = pfisica;
	}

	public String getPmoral() {
		return pmoral;
	}

	public void setPmoral(String pmoral) {
		this.pmoral = pmoral;
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



	public String getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getRegimenCapital() {
		return regimenCapital;
	}

	public void setRegimenCapital(String regimenCapital) {
		this.regimenCapital = regimenCapital;
	}

	public String getRfc() {
		return rfc;
	}
	
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getPadron() {
		return padron;
	}

	public void setPadron(String padron) {
		this.padron = padron;
	}
	
	public String getNombreEmisor() {
		return nombreEmisor;
	}
	
	public void setNombreEmisor(String nombreEmisor) {
		this.nombreEmisor = nombreEmisor;
	}

	public Date getIniOperacion() {
		return iniOperacion;
	}
	
	public void setIniOperacion(Date iniOperacion) {
		this.iniOperacion = iniOperacion;
	}
	
	public Date getUltSAT() {
		return ultSAT;
	}

	public void setUltSAT(Date ultSAT) {
		this.ultSAT = ultSAT;
	}
	
	public List<RegimenFiscal> getListaRegimenFiscal() {
		return listaRegimenFiscal;
	}

	public void setListaRegimenFiscal(List<RegimenFiscal> listaRegimenFiscal) {
		this.listaRegimenFiscal = listaRegimenFiscal;
	}
	
	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
	this.file = file;
	}

}