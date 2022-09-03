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
@Table(name = "pago")
@NamedQueries({
        @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p"),
        @NamedQuery(name = "Pago.findById", query = "SELECT p FROM Pago p WHERE p.id = :id"),
        @NamedQuery(name = "Pago.findByMonto", query = "SELECT p FROM Pago p WHERE p.monto = :monto"),
        @NamedQuery(name = "Pago.findByFecha", query = "SELECT p FROM Pago p WHERE p.fecha = :fecha"),
        @NamedQuery(name = "Pago.findByReferencia", query = "SELECT p FROM Pago p WHERE p.referencia = :referencia"),
        @NamedQuery(name = "Pago.findByCheque", query = "SELECT p FROM Pago p WHERE p.cheque = :cheque"),
        @NamedQuery(name = "Pago.findByChequeDevuelto", query = "SELECT p FROM Pago p WHERE p.chequeDevuelto = :chequeDevuelto"),
        @NamedQuery(name = "Pago.findByFacturaId", query = "SELECT p FROM Pago p WHERE p.factura.id = :facturaId"),
        @NamedQuery(name = "Pago.findByClienteFechas", query = "SELECT p FROM Pago p WHERE p.factura.cliente.cteCve = :cteCve AND p.fecha BETWEEN :startDate AND :endDate") })
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private BigDecimal monto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 20)
    @Column(name = "referencia")
    private String referencia;
    @Size(max = 10)
    @Column(name = "cheque")
    private String cheque;
    @Column(name = "cheque_devuelto")
    private Boolean chequeDevuelto;
    @JoinColumn(name = "banco", referencedColumnName = "id")
    @ManyToOne
    private Bancos banco;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Factura factura;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoPago tipo;

    public Pago() {
    }

    public Pago(Integer id) {
        this.id = id;
    }

    public Pago(Integer id, BigDecimal monto, Date fecha) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public Boolean getChequeDevuelto() {
        return chequeDevuelto;
    }

    public void setChequeDevuelto(Boolean chequeDevuelto) {
        this.chequeDevuelto = chequeDevuelto;
    }

    public Bancos getBanco() {
        return banco;
    }

    public void setBanco(Bancos banco) {
        this.banco = banco;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public TipoPago getTipo() {
        return tipo;
    }

    public void setTipo(TipoPago tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Pago[ id=" + id + " ]";
    }

}
