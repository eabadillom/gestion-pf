package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ConstanciaSalidaServiciosPK implements Serializable{
	
	private static final long serialVersionUID = -7593443111857351832L;

	@JoinColumn(name = "id_constancia",referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private ConstanciaSalida constanciaSalidaCve;
	
	@JoinColumn(name = "id_servicio",referencedColumnName = "SERVICIO_CVE")
	@ManyToOne(optional = false)
	private Servicio servicioCve;

	public ConstanciaSalidaServiciosPK() {
		
	}
	
	public ConstanciaSalidaServiciosPK(ConstanciaSalida constanciaSalidaCve, Servicio servicioCve) {
		this.constanciaSalidaCve = constanciaSalidaCve;
		this.servicioCve = servicioCve;
	}

	public ConstanciaSalida getConstanciaSalidaCve() {
		return constanciaSalidaCve;
	}

	public void setConstanciaSalidaCve(ConstanciaSalida constanciaSalidaCve) {
		this.constanciaSalidaCve = constanciaSalidaCve;
	}

	public Servicio getServicioCve() {
		return servicioCve;
	}

	public void setServicioCve(Servicio servicioCve) {
		this.servicioCve = servicioCve;
	}

	@Override
	public String toString() {
		return "ConstanciaSalidaServiciosPK [constanciaSalidaCve=" + constanciaSalidaCve + ", servicioCve="
				+ servicioCve + "]";
	}
	
	
	
	
	//FALTA METODO HASHCODE Y EQUALS
	
	
}
