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
@Table(name = "tipo_telefono")
@NamedQueries({ @NamedQuery(name = "TipoTelefono.findAll", query = "SELECT t FROM TipoTelefono t"),
		@NamedQuery(name = "TipoTelefono.findByTpTelefono", query = "SELECT t FROM TipoTelefono t WHERE t.tpTelefono = :tpTelefono"),
		@NamedQuery(name = "TipoTelefono.findByNbTelefono", query = "SELECT t FROM TipoTelefono t WHERE t.nbTelefono = :nbTelefono") })
public class TipoTelefono implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "tp_telefono")
	private Short tpTelefono;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "nb_telefono")
	private String nbTelefono;

	public TipoTelefono() {
	}

	public TipoTelefono(Short tpTelefono) {
		this.tpTelefono = tpTelefono;
	}

	public TipoTelefono(Short tpTelefono, String nbTelefono) {
		this.tpTelefono = tpTelefono;
		this.nbTelefono = nbTelefono;
	}

	public Short getTpTelefono() {
		return tpTelefono;
	}

	public void setTpTelefono(Short tpTelefono) {
		this.tpTelefono = tpTelefono;
	}

	public String getNbTelefono() {
		return nbTelefono;
	}

	public void setNbTelefono(String nbTelefono) {
		this.nbTelefono = nbTelefono;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tpTelefono != null ? tpTelefono.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TipoTelefono)) {
			return false;
		}
		TipoTelefono other = (TipoTelefono) object;
		if ((this.tpTelefono == null && other.tpTelefono != null)
				|| (this.tpTelefono != null && !this.tpTelefono.equals(other.tpTelefono))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.TipoTelefono[ tpTelefono=" + tpTelefono + " ]";
	}

}
