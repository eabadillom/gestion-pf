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
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Embeddable
public class TmpFacturarInventarioPK implements Serializable {

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 15)
	@Column(name = "usuario")
	private String usuario;
	@Basic(optional = false)
	@NotNull
	@Column(name = "cte_cve")
	private int cteCve;
	@Basic(optional = false)
	@NotNull
	@Column(name = "tmp_cve")
	private int tmpCve;

	public TmpFacturarInventarioPK() {
	}

	public TmpFacturarInventarioPK(String usuario, int cteCve, int tmpCve) {
		this.usuario = usuario;
		this.cteCve = cteCve;
		this.tmpCve = tmpCve;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getCteCve() {
		return cteCve;
	}

	public void setCteCve(int cteCve) {
		this.cteCve = cteCve;
	}

	public int getTmpCve() {
		return tmpCve;
	}

	public void setTmpCve(int tmpCve) {
		this.tmpCve = tmpCve;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (usuario != null ? usuario.hashCode() : 0);
		hash += (int) cteCve;
		hash += (int) tmpCve;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof TmpFacturarInventarioPK)) {
			return false;
		}
		TmpFacturarInventarioPK other = (TmpFacturarInventarioPK) object;
		if ((this.usuario == null && other.usuario != null)
				|| (this.usuario != null && !this.usuario.equals(other.usuario))) {
			return false;
		}
		if (this.cteCve != other.cteCve) {
			return false;
		}
		if (this.tmpCve != other.tmpCve) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.TmpFacturarInventarioPK[ usuario=" + usuario + ", cteCve=" + cteCve + ", tmpCve="
				+ tmpCve + " ]";
	}

}
