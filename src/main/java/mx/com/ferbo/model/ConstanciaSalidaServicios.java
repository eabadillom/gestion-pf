package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "constancia_salida_srv")

@NamedQueries({
		@NamedQuery(name = "ConstanciaSalidaServicios.findAll", query = "SELECT c FROM ConstanciaSalidaServicios c"),
		@NamedQuery(name = "ConstanciaSalidaServicios.findByIdConstancia", query = "SELECT c FROM ConstanciaSalidaServicios c WHERE c.constanciaSalidaServiciosPK.constanciaSalidaCve = :constanciaSalidaCve"),
		@NamedQuery(name = "ConstanciaSalidaServicios.findByIdServicio", query = "SELECT c FROM ConstanciaSalidaServicios c WHERE c.constanciaSalidaServiciosPK.servicioCve = :servicioCve"),
		@NamedQuery(name = "ConstanciaSalidaServicios.findByCantidad", query = "SELECT c FROM ConstanciaSalidaServicios c WHERE c.numCantidad = :numCantidad") })

public class ConstanciaSalidaServicios implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected ConstanciaSalidaServiciosPK constanciaSalidaServiciosPK;

	@JoinColumn(name = "id_constancia", referencedColumnName = "ID", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private ConstanciaSalida idConstancia;

	@JoinColumn(name = "id_servicio", referencedColumnName = "SERVICIO_CVE", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Servicio servicioCve;// crear en model SERVICIO una variable constanciaSalida?

	@Column(name = "nu_cantidad")
	private BigDecimal numCantidad;

	public ConstanciaSalidaServicios() {
	}

	public ConstanciaSalidaServicios(ConstanciaSalidaServiciosPK constanciaSalidaServiciosPK) {
		this.constanciaSalidaServiciosPK = constanciaSalidaServiciosPK;
	}

	public ConstanciaSalidaServicios(ConstanciaSalida constanciaSalidaCve, Servicio servicioCve) {
		this.constanciaSalidaServiciosPK = new ConstanciaSalidaServiciosPK(constanciaSalidaCve, servicioCve);
	}

	public ConstanciaSalidaServiciosPK getConstanciaSalidaServiciosPK() {
		return constanciaSalidaServiciosPK;
	}

	public void setConstanciaSalidaServiciosPK(ConstanciaSalidaServiciosPK constanciaSalidaServiciosPK) {
		this.constanciaSalidaServiciosPK = constanciaSalidaServiciosPK;
	}

	public BigDecimal getNumCantidad() {
		return numCantidad;
	}

	public void setNumCantidad(BigDecimal numCantidad) {
		this.numCantidad = numCantidad;
	}

	public ConstanciaSalida getIdConstancia() {
		return idConstancia;
	}

	public void setIdConstancia(ConstanciaSalida idConstancia) {
		this.idConstancia = idConstancia;
	}

	public Servicio getServicioCve() {
		return servicioCve;
	}

	public void setServicioCve(Servicio servicioCve) {
		this.servicioCve = servicioCve;
	}

	@Override
	public String toString() {
		return "ConstanciaSalidaServicios [constanciaSalidaServiciosPK=" + constanciaSalidaServiciosPK
				+ ", numCantidad=" + numCantidad + "]";
	}
}
