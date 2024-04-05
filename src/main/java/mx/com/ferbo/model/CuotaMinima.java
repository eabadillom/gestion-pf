/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "cuota_minima")
@NamedQueries({ @NamedQuery(name = "CuotaMinima.findAll", query = "SELECT c FROM CuotaMinima c"),
		@NamedQuery(name = "CuotaMinima.findByCteCve", query = "SELECT c FROM CuotaMinima c WHERE c.cuotaMinimaPK.cteCve = :cteCve"),
		@NamedQuery(name = "CuotaMinima.findByCuotaId", query = "SELECT c FROM CuotaMinima c WHERE c.cuotaMinimaPK.cuotaId = :cuotaId"),
		@NamedQuery(name = "CuotaMinima.findByCuotaEnabled", query = "SELECT c FROM CuotaMinima c WHERE c.cuotaEnabled = :cuotaEnabled"),
		@NamedQuery(name = "CuotaMinima.findByCuotaValue", query = "SELECT c FROM CuotaMinima c WHERE c.cuotaValue = :cuotaValue") })
public class CuotaMinima implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected CuotaMinimaPK cuotaMinimaPK;
	@Basic(optional = false)
	@NotNull
	@Column(name = "cuota_enabled")
	private boolean cuotaEnabled;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "cuota_value")
	private BigDecimal cuotaValue;
	@JoinColumn(name = "cte_cve", referencedColumnName = "CTE_CVE", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Cliente cliente;

	public CuotaMinima() {
	}

	public CuotaMinima(CuotaMinimaPK cuotaMinimaPK) {
		this.cuotaMinimaPK = cuotaMinimaPK;
	}

	public CuotaMinima(CuotaMinimaPK cuotaMinimaPK, boolean cuotaEnabled, BigDecimal cuotaValue) {
		this.cuotaMinimaPK = cuotaMinimaPK;
		this.cuotaEnabled = cuotaEnabled;
		this.cuotaValue = cuotaValue;
	}

	public CuotaMinima(int cteCve, int cuotaId) {
		this.cuotaMinimaPK = new CuotaMinimaPK(cteCve, cuotaId);
	}

	public CuotaMinimaPK getCuotaMinimaPK() {
		return cuotaMinimaPK;
	}

	public void setCuotaMinimaPK(CuotaMinimaPK cuotaMinimaPK) {
		this.cuotaMinimaPK = cuotaMinimaPK;
	}

	public boolean getCuotaEnabled() {
		return cuotaEnabled;
	}

	public void setCuotaEnabled(boolean cuotaEnabled) {
		this.cuotaEnabled = cuotaEnabled;
	}

	public BigDecimal getCuotaValue() {
		return cuotaValue;
	}

	public void setCuotaValue(BigDecimal cuotaValue) {
		this.cuotaValue = cuotaValue;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (cuotaMinimaPK != null ? cuotaMinimaPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CuotaMinima)) {
			return false;
		}
		CuotaMinima other = (CuotaMinima) object;
		if ((this.cuotaMinimaPK == null && other.cuotaMinimaPK != null)
				|| (this.cuotaMinimaPK != null && !this.cuotaMinimaPK.equals(other.cuotaMinimaPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.CuotaMinima[ cuotaMinimaPK=" + cuotaMinimaPK + " ]";
	}

}
