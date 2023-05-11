/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "medio_pago")
@NamedQueries({ @NamedQuery(name = "MedioPago.findAll", query = "SELECT m FROM MedioPago m"),
		@NamedQuery(name = "MedioPago.findByMpId", query = "SELECT m FROM MedioPago m WHERE m.mpId = :mpId"),
		@NamedQuery(name = "MedioPago.findByMpDescripcion", query = "SELECT m FROM MedioPago m WHERE m.mpDescripcion = :mpDescripcion"),
		@NamedQuery(name = "MedioPago.findVigentes", query = "SELECT m FROM MedioPago m WHERE m.vigenciaInicio <= :fecha AND (m.vigenciaFin IS NULL OR m.vigenciaFin >= :fecha) ORDER BY m.mpDescripcion"),
		@NamedQuery(name = "MedioPago.findByMpReqReferencia", query = "SELECT m FROM MedioPago m WHERE m.mpReqReferencia = :mpReqReferencia"), 
		@NamedQuery(name = "MedioPago.findBympformaPago", query = "SELECT m FROM MedioPago m WHERE m.formaPago = :mpformaPago") 
})
public class MedioPago implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mp_id")
	private Integer mpId;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "mp_descripcion")
	private String mpDescripcion;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "mp_req_referencia")
	private boolean mpReqReferencia;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mpId")
    private List<FacturaMedioPago> facturaMedioPagoList;
	
	@Column(name="mp_forma_pago")
	@Size(min = 1, max = 5)
	private String formaPago = null;
	
	@Column(name = "mp_fh_vigencia_ini")
    @Temporal(TemporalType.DATE)
	private Date vigenciaInicio = null;
	
	@Column(name = "mp_fh_vigencia_fin")
    @Temporal(TemporalType.DATE)
	private Date vigenciaFin = null;

	public MedioPago() {
	}

	public MedioPago(Integer mpId) {
		this.mpId = mpId;
	}

	public MedioPago(Integer mpId, String mpDescripcion, boolean mpReqReferencia) {
		this.mpId = mpId;
		this.mpDescripcion = mpDescripcion;
		this.mpReqReferencia = mpReqReferencia;
	}

	public Integer getMpId() {
		return mpId;
	}

	public void setMpId(Integer mpId) {
		this.mpId = mpId;
	}

	public String getMpDescripcion() {
		return mpDescripcion;
	}

	public void setMpDescripcion(String mpDescripcion) {
		this.mpDescripcion = mpDescripcion;
	}

	public boolean getMpReqReferencia() {
		return mpReqReferencia;
	}

	public void setMpReqReferencia(boolean mpReqReferencia) {
		this.mpReqReferencia = mpReqReferencia;
	}
	
	public List<FacturaMedioPago> getFacturaMedioPagoList() {
        return facturaMedioPagoList;
    }

    public void setFacturaMedioPagoList(List<FacturaMedioPago> facturaMedioPagoList) {
        this.facturaMedioPagoList = facturaMedioPagoList;
    }
    
    public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	public Date getVigenciaFin() {
		return vigenciaFin;
	}

	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (mpId != null ? mpId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof MedioPago)) {
			return false;
		}
		MedioPago other = (MedioPago) object;
		if ((this.mpId == null && other.mpId != null) || (this.mpId != null && !this.mpId.equals(other.mpId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.MedioPago[ mpId=" + mpId + " ]";
	}

	

}
