/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "datos_especiales")
@NamedQueries({ @NamedQuery(name = "DatosEspeciales.findAll", query = "SELECT d FROM DatosEspeciales d"),
		@NamedQuery(name = "DatosEspeciales.findByDatoEspCve", query = "SELECT d FROM DatosEspeciales d WHERE d.datoEspCve = :datoEspCve"),
		@NamedQuery(name = "DatosEspeciales.findByNombre", query = "SELECT d FROM DatosEspeciales d WHERE d.nombre = :nombre"),
		@NamedQuery(name = "DatosEspeciales.findByLongitud", query = "SELECT d FROM DatosEspeciales d WHERE d.longitud = :longitud"),
		@NamedQuery(name = "DatosEspeciales.findByPrecision", query = "SELECT d FROM DatosEspeciales d WHERE d.precision = :precision") })
public class DatosEspeciales implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "DATO_ESP_CVE")
	private Integer datoEspCve;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "NOMBRE")
	private String nombre;
	@Column(name = "LONGITUD")
	private Integer longitud;
	@Column(name = "\u00b4PRECISION\u00b4")
	private Integer precision;
	@JoinColumn(name = "FORMATO_CVE", referencedColumnName = "FORMATO_CVE")
	@ManyToOne
	private Formato formatoCve;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "datoEspCve")
	private List<DetalleFacturacion> detalleFacturacionList;
	@OneToMany(mappedBy = "datoEspCve")
	private List<DetalleCteProd> detalleCteProdList;

	public DatosEspeciales() {
	}

	public DatosEspeciales(Integer datoEspCve) {
		this.datoEspCve = datoEspCve;
	}

	public DatosEspeciales(Integer datoEspCve, String nombre) {
		this.datoEspCve = datoEspCve;
		this.nombre = nombre;
	}

	public Integer getDatoEspCve() {
		return datoEspCve;
	}

	public void setDatoEspCve(Integer datoEspCve) {
		this.datoEspCve = datoEspCve;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getLongitud() {
		return longitud;
	}

	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Formato getFormatoCve() {
		return formatoCve;
	}

	public void setFormatoCve(Formato formatoCve) {
		this.formatoCve = formatoCve;
	}

	public List<DetalleFacturacion> getDetalleFacturacionList() {
		return detalleFacturacionList;
	}

	public void setDetalleFacturacionList(List<DetalleFacturacion> detalleFacturacionList) {
		this.detalleFacturacionList = detalleFacturacionList;
	}

	public List<DetalleCteProd> getDetalleCteProdList() {
		return detalleCteProdList;
	}

	public void setDetalleCteProdList(List<DetalleCteProd> detalleCteProdList) {
		this.detalleCteProdList = detalleCteProdList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (datoEspCve != null ? datoEspCve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DatosEspeciales)) {
			return false;
		}
		DatosEspeciales other = (DatosEspeciales) object;
		if ((this.datoEspCve == null && other.datoEspCve != null)
				|| (this.datoEspCve != null && !this.datoEspCve.equals(other.datoEspCve))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.DatosEspeciales[ datoEspCve=" + datoEspCve + " ]";
	}

}
