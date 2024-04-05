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
@Table(name = "paises")
@NamedQueries({ @NamedQuery(name = "Paises.findAll", query = "SELECT p FROM Paises p"),
		@NamedQuery(name = "Paises.findByPaisCve", query = "SELECT p FROM Paises p WHERE p.paisCve = :paisCve"),
		@NamedQuery(name = "Paises.findByPaisDesc", query = "SELECT p FROM Paises p WHERE p.paisDesc = :paisDesc"),
		@NamedQuery(name = "Paises.findByPaisDsCorta", query = "SELECT p FROM Paises p WHERE p.paisDsCorta = :paisDsCorta") })
public class Paises implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "pais_cve")
	private Integer paisCve;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 30)
	@Column(name = "pais_desc")
	private String paisDesc;
	@Size(max = 4)
	@Column(name = "pais_ds_corta")
	private String paisDsCorta;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paises")
	private List<Estados> estadosList;

	public Paises() {
	}

	public Paises(Integer paisCve) {
		this.paisCve = paisCve;
	}

	public Paises(Integer paisCve, String paisDesc) {
		this.paisCve = paisCve;
		this.paisDesc = paisDesc;
	}

	public Integer getPaisCve() {
		return paisCve;
	}

	public void setPaisCve(Integer paisCve) {
		this.paisCve = paisCve;
	}

	public String getPaisDesc() {
		return paisDesc;
	}

	public void setPaisDesc(String paisDesc) {
		this.paisDesc = paisDesc;
	}

	public String getPaisDsCorta() {
		return paisDsCorta;
	}

	public void setPaisDsCorta(String paisDsCorta) {
		this.paisDsCorta = paisDsCorta;
	}

	public List<Estados> getEstadosList() {
		return estadosList;
	}

	public void setEstadosList(List<Estados> estadosList) {
		this.estadosList = estadosList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (paisCve != null ? paisCve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Paises)) {
			return false;
		}
		Paises other = (Paises) object;
		if ((this.paisCve == null && other.paisCve != null)
				|| (this.paisCve != null && !this.paisCve.equals(other.paisCve))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.Paises[ paisCve=" + paisCve + " ]";
	}

}
