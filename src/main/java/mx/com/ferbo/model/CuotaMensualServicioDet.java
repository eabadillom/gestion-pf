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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "cuota_mensual_servicio_det")
@NamedQueries({
		@NamedQuery(name = "CuotaMensualServicioDet.findAll", query = "SELECT c FROM CuotaMensualServicioDet c"),
		@NamedQuery(name = "CuotaMensualServicioDet.findById", query = "SELECT c FROM CuotaMensualServicioDet c WHERE c.id = :id"),
		@NamedQuery(name = "CuotaMensualServicioDet.findByAvisoCve", query = "SELECT c FROM CuotaMensualServicioDet c WHERE c.avisoCve = :avisoCve"),
		@NamedQuery(name = "CuotaMensualServicioDet.findByCteCve", query = "SELECT c FROM CuotaMensualServicioDet c WHERE c.cteCve = :cteCve"),
		@NamedQuery(name = "CuotaMensualServicioDet.findByServicioCve", query = "SELECT c FROM CuotaMensualServicioDet c WHERE c.servicioCve = :servicioCve"),
		@NamedQuery(name = "CuotaMensualServicioDet.findByCuotaDetCve", query = "SELECT c FROM CuotaMensualServicioDet c WHERE c.cuotaDetCve = :cuotaDetCve"),
		@NamedQuery(name = "CuotaMensualServicioDet.findByCuota", query = "SELECT c FROM CuotaMensualServicioDet c WHERE c.cuota = :cuota") })
public class CuotaMensualServicioDet implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Column(name = "aviso_cve")
	private int avisoCve;
	@Basic(optional = false)
	@NotNull
	@Column(name = "cte_cve")
	private int cteCve;
	@Basic(optional = false)
	@NotNull
	@Column(name = "servicio_cve")
	private int servicioCve;
	@Basic(optional = false)
	@NotNull
	@Column(name = "cuota_det_cve")
	private int cuotaDetCve;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "cuota")
	private BigDecimal cuota;
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	@OneToOne(optional = false)
	private CuotaMensualServicio cuotaMensualServicio;

	public CuotaMensualServicioDet() {
	}

	public CuotaMensualServicioDet(Integer id) {
		this.id = id;
	}

	public CuotaMensualServicioDet(Integer id, int avisoCve, int cteCve, int servicioCve, int cuotaDetCve,
			BigDecimal cuota) {
		this.id = id;
		this.avisoCve = avisoCve;
		this.cteCve = cteCve;
		this.servicioCve = servicioCve;
		this.cuotaDetCve = cuotaDetCve;
		this.cuota = cuota;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getAvisoCve() {
		return avisoCve;
	}

	public void setAvisoCve(int avisoCve) {
		this.avisoCve = avisoCve;
	}

	public int getCteCve() {
		return cteCve;
	}

	public void setCteCve(int cteCve) {
		this.cteCve = cteCve;
	}

	public int getServicioCve() {
		return servicioCve;
	}

	public void setServicioCve(int servicioCve) {
		this.servicioCve = servicioCve;
	}

	public int getCuotaDetCve() {
		return cuotaDetCve;
	}

	public void setCuotaDetCve(int cuotaDetCve) {
		this.cuotaDetCve = cuotaDetCve;
	}

	public BigDecimal getCuota() {
		return cuota;
	}

	public void setCuota(BigDecimal cuota) {
		this.cuota = cuota;
	}

	public CuotaMensualServicio getCuotaMensualServicio() {
		return cuotaMensualServicio;
	}

	public void setCuotaMensualServicio(CuotaMensualServicio cuotaMensualServicio) {
		this.cuotaMensualServicio = cuotaMensualServicio;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CuotaMensualServicioDet)) {
			return false;
		}
		CuotaMensualServicioDet other = (CuotaMensualServicioDet) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.CuotaMensualServicioDet[ id=" + id + " ]";
	}

}
