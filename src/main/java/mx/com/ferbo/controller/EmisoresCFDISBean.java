package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.model.EmisoresCFDIS;

@Named
@ViewScoped
public class EmisoresCFDISBean implements Serializable {
	private static final long serialVersionUID = 1L;
	String pmoral;
	String pfisica;
	EmisoresCFDIS emisor;
	String regimenFiscal;
	String tipoPersona;
	String regimenCapital;
	String RFC;
	String ultimoCambio;
	String padron;
	String nombreEmisor;
	Date iniOperacion;
	Date ultSAT;
	
	EmisoresCFDISDAO emisoresDAO;
	
	List<EmisoresCFDIS> listaEmisor;
	
	public EmisoresCFDISBean() {
	emisoresDAO = new EmisoresCFDISDAO();
	}
	
	
	public void guardaEmisor() {
		PrimeFaces.current().executeScript("PF('dialogEmisor').hide()");
		System.out.print(emisor);
		emisor.setNb_emisor(this.nombreEmisor);
		emisor.setTp_persona(this.tipoPersona);
		emisor.setNb_regimen_capital(this.regimenCapital);
		emisor.setNb_rfc(this.RFC);
		emisor.setFh_inicio_op(this.iniOperacion);
		emisor.setFh_ult_cambio(this.ultSAT);
		emisor.setSt_padron(this.padron);
		emisor.setCd_regimen(this.regimenFiscal);
		String em = emisoresDAO.guardar(emisor);
		if(em == null) {
			listaEmisor.clear();
			listaEmisor = emisoresDAO.findall();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Emisor agregado con exito" + emisor.getNb_emisor(),null));
			PrimeFaces.current().ajax().update("form:messages","form:dt-EmisoresCFDIS");
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error al agregar el emisor" + emisor.getNb_emisor(), em));
			PrimeFaces.current().ajax().update("form:messages");
		}
		this.emisor = new EmisoresCFDIS();
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
    
	public String getPfisica() {
		return pfisica;
	}
	public void setPfisica(String pfisica) {
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

	public String getRFC() {
		return RFC;
	}

	public void setRFC(String rFC) {
		RFC = rFC;
	}

	public String getUltimoCambio() {
		return ultimoCambio;
	}

	public void setUltimoCambio(String ultimoCambio) {
		this.ultimoCambio = ultimoCambio;
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
	
}
