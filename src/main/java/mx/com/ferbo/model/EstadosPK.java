/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Embeddable
public class EstadosPK implements Serializable {

	@Basic(optional = false)
	@NotNull
	@Column(name = "pais_cve")
	private int paisCve;
	@Basic(optional = false)
	@NotNull
	@Column(name = "estado_cve")
	private int estadoCve;

	public EstadosPK() {
	}

	public EstadosPK(int paisCve, int estadoCve) {
		this.paisCve = paisCve;
		this.estadoCve = estadoCve;
	}

	public int getPaisCve() {
		return paisCve;
	}

	public void setPaisCve(int paisCve) {
		this.paisCve = paisCve;
	}

	public int getEstadoCve() {
		return estadoCve;
	}

	public void setEstadoCve(int estadoCve) {
		this.estadoCve = estadoCve;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) paisCve;
		hash += (int) estadoCve;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof EstadosPK)) {
			return false;
		}
		EstadosPK other = (EstadosPK) object;
		if (this.paisCve != other.paisCve) {
			return false;
		}
		if (this.estadoCve != other.estadoCve) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.EstadosPK[ paisCve=" + paisCve + ", estadoCve=" + estadoCve + " ]";
	}

}
