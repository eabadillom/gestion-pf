/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "cheque_devuelto")
@NamedQueries({ @NamedQuery(name = "ChequeDevuelto.findAll", query = "SELECT c FROM ChequeDevuelto c"),
		@NamedQuery(name = "ChequeDevuelto.findById", query = "SELECT c FROM ChequeDevuelto c WHERE c.id = :id"),
		@NamedQuery(name = "ChequeDevuelto.findByCheque", query = "SELECT c FROM ChequeDevuelto c WHERE c.cheque = :cheque"),
		@NamedQuery(name = "ChequeDevuelto.findByFechaDevuelto", query = "SELECT c FROM ChequeDevuelto c WHERE c.fechaDevuelto = :fechaDevuelto"),
		@NamedQuery(name = "ChequeDevuelto.findByFechaPago", query = "SELECT c FROM ChequeDevuelto c WHERE c.fechaPago = :fechaPago"),
		@NamedQuery(name = "ChequeDevuelto.findByMontoPago", query = "SELECT c FROM ChequeDevuelto c WHERE c.montoPago = :montoPago"),
		@NamedQuery(name = "ChequeDevuelto.findByBanco", query = "SELECT c FROM ChequeDevuelto c WHERE c.banco = :banco"),
		@NamedQuery(name = "ChequeDevuelto.findByReferencia", query = "SELECT c FROM ChequeDevuelto c WHERE c.referencia = :referencia"),
		@NamedQuery(name = "ChequeDevuelto.findByMotivo", query = "SELECT c FROM ChequeDevuelto c WHERE c.motivo = :motivo") })
public class ChequeDevuelto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ID")
	private Integer id;
	@Size(max = 10)
	@Column(name = "CHEQUE")
	private String cheque;
	@Basic(optional = false)
	@NotNull
	@Column(name = "FECHA_DEVUELTO")
	@Temporal(TemporalType.DATE)
	private Date fechaDevuelto;
	@Basic(optional = false)
	@NotNull
	@Column(name = "FECHA_PAGO")
	@Temporal(TemporalType.DATE)
	private Date fechaPago;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "MONTO_PAGO")
	private BigDecimal montoPago;
	@Basic(optional = false)
	@NotNull
	@Column(name = "BANCO")
	private int banco;
	@Size(max = 20)
	@Column(name = "REFERENCIA")
	private String referencia;
	@Size(max = 80)
	@Column(name = "MOTIVO")
	private String motivo;
	@JoinColumn(name = "FACTURA_ID", referencedColumnName = "id")
	@ManyToOne
	private Factura facturaId;

	public ChequeDevuelto() {
	}

	public ChequeDevuelto(Integer id) {
		this.id = id;
	}

	public ChequeDevuelto(Integer id, Date fechaDevuelto, Date fechaPago, BigDecimal montoPago, int banco) {
		this.id = id;
		this.fechaDevuelto = fechaDevuelto;
		this.fechaPago = fechaPago;
		this.montoPago = montoPago;
		this.banco = banco;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCheque() {
		return cheque;
	}

	public void setCheque(String cheque) {
		this.cheque = cheque;
	}

	public Date getFechaDevuelto() {
		return fechaDevuelto;
	}

	public void setFechaDevuelto(Date fechaDevuelto) {
		this.fechaDevuelto = fechaDevuelto;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public BigDecimal getMontoPago() {
		return montoPago;
	}

	public void setMontoPago(BigDecimal montoPago) {
		this.montoPago = montoPago;
	}

	public int getBanco() {
		return banco;
	}

	public void setBanco(int banco) {
		this.banco = banco;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Factura getFacturaId() {
		return facturaId;
	}

	public void setFacturaId(Factura facturaId) {
		this.facturaId = facturaId;
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
		if (!(object instanceof ChequeDevuelto)) {
			return false;
		}
		ChequeDevuelto other = (ChequeDevuelto) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.ChequeDevuelto[ id=" + id + " ]";
	}

}
