/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "ud_cobro")
@NamedQueries({ @NamedQuery(name = "UdCobro.findAll", query = "SELECT u FROM UdCobro u"),
		@NamedQuery(name = "UdCobro.findByIdUnidad", query = "SELECT u FROM UdCobro u WHERE u.idUnidad = :idUnidad"),
		@NamedQuery(name = "UdCobro.findByNbUnidad", query = "SELECT u FROM UdCobro u WHERE u.nbUnidad = :nbUnidad"),
		@NamedQuery(name = "UdCobro.findByCdUnidad", query = "SELECT u FROM UdCobro u WHERE u.cdUnidad = :cdUnidad") })
public class UdCobro implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "id_unidad")
	private String idUnidad;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "nb_unidad")
	private String nbUnidad;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "cd_unidad")
	private String cdUnidad;

	public UdCobro() {
	}

	public UdCobro(String idUnidad) {
		this.idUnidad = idUnidad;
	}

	public UdCobro(String idUnidad, String nbUnidad, String cdUnidad) {
		this.idUnidad = idUnidad;
		this.nbUnidad = nbUnidad;
		this.cdUnidad = cdUnidad;
	}

	public String getIdUnidad() {
		return idUnidad;
	}

	public void setIdUnidad(String idUnidad) {
		this.idUnidad = idUnidad;
	}

	public String getNbUnidad() {
		return nbUnidad;
	}

	public void setNbUnidad(String nbUnidad) {
		this.nbUnidad = nbUnidad;
	}

	public String getCdUnidad() {
		return cdUnidad;
	}

	public void setCdUnidad(String cdUnidad) {
		this.cdUnidad = cdUnidad;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idUnidad != null ? idUnidad.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof UdCobro)) {
			return false;
		}
		UdCobro other = (UdCobro) object;
		if ((this.idUnidad == null && other.idUnidad != null)
				|| (this.idUnidad != null && !this.idUnidad.equals(other.idUnidad))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.UdCobro[ idUnidad=" + idUnidad + " ]";
	}

}
