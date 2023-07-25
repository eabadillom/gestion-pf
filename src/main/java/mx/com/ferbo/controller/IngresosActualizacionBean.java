package mx.com.ferbo.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped

public class IngresosActualizacionBean {

	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = Logger.getLogger(IngresosActualizacionBean.class);
	
	
	public IngresosActualizacionBean() {
		
	}
	
	@PostConstruct
	public void init(){
		
	}
	
	
	public void updatePago() {
		
		System.out.println(pagoSelected);
		
		String messages = null;
		Severity severity = null;		
		
		try {
			
			if(pagofactDAO.actualizar(pagoSelected)==null) {
				severity = FacesMessage.SEVERITY_INFO;
				messages = "El pago fue actualizado";
			}
			
		} catch (Exception e) {
			severity = FacesMessage.SEVERITY_ERROR;
			messages = "Error al actualizar pago";
			log.error(e.getMessage());
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Actualización", messages));
		PrimeFaces.current().ajax().update("form:messages");
	}
	
	public void deletePago() {
		
		String mensaje = null;
		Severity severity = null;
		
		try {
			
			if(pagofactDAO.eliminar(pagoSelected)==null) {
				mensaje = "El pago fue eliminado correctamente";
				severity = FacesMessage.SEVERITY_INFO;
				filtraPagos();
			}
			
		} catch (Exception e) {
			mensaje = "Ocurrio un error al querer eliminar el pago";
			severity = FacesMessage.SEVERITY_ERROR;
			log.error(e.getMessage());
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Eliminar", mensaje));
		PrimeFaces.current().ajax().update("form:messages");
	}
	
	public void filtraPagos() {
		
		listaPago = pagofactDAO.buscaPorClienteFechas(cteSelect, startDate, endDate);
		
	}
	
	
}
