package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ingreso_servicio")
@NamedQueries({ @NamedQuery(name = "IngresoServicio.findAll", query = "SELECT inS FROM IngresoServicio inS"),
		@NamedQuery(name = "IngresoServicio.findByIngreso", query = "SELECT inS FROM IngresoServicio inS WHERE inS.ingreso.idIngreso = :idIngreso") })

public class IngresoServicio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_ingreso_servicio")
	private Integer idIngresoServicio;

	@JoinColumn(name = "id_servicio", referencedColumnName = "SERVICIO_CVE")
	@ManyToOne
	private Servicio servicio;

	@Column(name = "cantidad")
	private BigDecimal cantidad;

	@JoinColumn(name = "id_unidad", referencedColumnName = "UNIDAD_DE_MANEJO_CVE")
	@ManyToOne
	private UnidadDeManejo unidadDeManejo;

	@JoinColumn(name = "id_ingreso", referencedColumnName = "id_ingreso")
	@ManyToOne
	private Ingreso ingreso;

	public Integer getIdIngresoServicio() {
		return idIngresoServicio;
	}

	public void setIdIngresoServicio(Integer idIngresoServicio) {
		this.idIngresoServicio = idIngresoServicio;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public UnidadDeManejo getUnidadDeManejo() {
		return unidadDeManejo;
	}

	public void setUnidadDeManejo(UnidadDeManejo unidadDeManejo) {
		this.unidadDeManejo = unidadDeManejo;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	@Override
	public String toString() {
		return "IngresoServicio [idIngresoServicio=" + idIngresoServicio + ", servicio=" + servicio + ", cantidad="
				+ cantidad + ", unidadDeManejo=" + unidadDeManejo + ", ingreso=" + ingreso + "]";
	}

}
