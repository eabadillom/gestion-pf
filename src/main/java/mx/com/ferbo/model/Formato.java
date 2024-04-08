/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "formato")
@NamedQueries({ @NamedQuery(name = "Formato.findAll", query = "SELECT f FROM Formato f"),
		@NamedQuery(name = "Formato.findByFormatoCve", query = "SELECT f FROM Formato f WHERE f.formatoCve = :formatoCve"),
		@NamedQuery(name = "Formato.findByNombre", query = "SELECT f FROM Formato f WHERE f.nombre = :nombre"),
		@NamedQuery(name = "Formato.findByLongitud", query = "SELECT f FROM Formato f WHERE f.longitud = :longitud"),
		@NamedQuery(name = "Formato.findByPrecision", query = "SELECT f FROM Formato f WHERE f.precision = :precision") })
public class Formato implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "FORMATO_CVE")
	private Integer formatoCve;
	@Size(max = 30)
	@Column(name = "NOMBRE")
	private String nombre;
	@Column(name = "LONGITUD")
	private Short longitud;
	@Column(name = "PRECISION")
	private Short precision;
	@OneToMany(mappedBy = "formatoCve")
	private List<DatosEspeciales> datosEspecialesList;

	public Formato() {
	}

	public Formato(Integer formatoCve) {
		this.formatoCve = formatoCve;
	}

	public Integer getFormatoCve() {
		return formatoCve;
	}

	public void setFormatoCve(Integer formatoCve) {
		this.formatoCve = formatoCve;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Short getLongitud() {
		return longitud;
	}

	public void setLongitud(Short longitud) {
		this.longitud = longitud;
	}

	public Short getPrecision() {
		return precision;
	}

	public void setPrecision(Short precision) {
		this.precision = precision;
	}

	public List<DatosEspeciales> getDatosEspecialesList() {
		return datosEspecialesList;
	}

	public void setDatosEspecialesList(List<DatosEspeciales> datosEspecialesList) {
		this.datosEspecialesList = datosEspecialesList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (formatoCve != null ? formatoCve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Formato)) {
			return false;
		}
		Formato other = (Formato) object;
		if ((this.formatoCve == null && other.formatoCve != null)
				|| (this.formatoCve != null && !this.formatoCve.equals(other.formatoCve))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.Formato[ formatoCve=" + formatoCve + " ]";
	}

}
