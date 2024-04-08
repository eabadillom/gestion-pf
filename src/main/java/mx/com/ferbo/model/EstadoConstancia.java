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
@Table(name = "estado_constancia")
@NamedQueries({ @NamedQuery(name = "EstadoConstancia.findAll", query = "SELECT e FROM EstadoConstancia e"),
		@NamedQuery(name = "EstadoConstancia.findByEdoCve", query = "SELECT e FROM EstadoConstancia e WHERE e.edoCve = :edoCve"),
		@NamedQuery(name = "EstadoConstancia.findByDescripcion", query = "SELECT e FROM EstadoConstancia e WHERE e.descripcion = :descripcion") })
public class EstadoConstancia implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "edo_cve")
	private Integer edoCve;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "descripcion")
	private String descripcion;

	public EstadoConstancia() {
	}

	public EstadoConstancia(Integer edoCve) {
		this.edoCve = edoCve;
	}

	public EstadoConstancia(Integer edoCve, String descripcion) {
		this.edoCve = edoCve;
		this.descripcion = descripcion;
	}

	public Integer getEdoCve() {
		return edoCve;
	}

	public void setEdoCve(Integer edoCve) {
		this.edoCve = edoCve;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (edoCve != null ? edoCve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof EstadoConstancia)) {
			return false;
		}
		EstadoConstancia other = (EstadoConstancia) object;
		if ((this.edoCve == null && other.edoCve != null)
				|| (this.edoCve != null && !this.edoCve.equals(other.edoCve))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.EstadoConstancia[ edoCve=" + edoCve + " ]";
	}

}
