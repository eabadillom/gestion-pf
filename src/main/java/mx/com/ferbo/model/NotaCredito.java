/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "NOTA_CREDITO")
@NamedQueries({
    @NamedQuery(name = "NotaCredito.findAll", query = "SELECT n FROM NotaCredito n"),
    @NamedQuery(name = "NotaCredito.findById", query = "SELECT n FROM NotaCredito n WHERE n.id = :id"),
    @NamedQuery(name = "NotaCredito.findByNumero", query = "SELECT n FROM NotaCredito n WHERE n.numero = :numero"),
    @NamedQuery(name = "NotaCredito.findByIdcliente", query = "SELECT n FROM NotaCredito n WHERE n.idcliente = :idcliente"),
    @NamedQuery(name = "NotaCredito.findByCliente", query = "SELECT n FROM NotaCredito n WHERE n.cliente = :cliente"),
    @NamedQuery(name = "NotaCredito.findByDomicilio", query = "SELECT n FROM NotaCredito n WHERE n.domicilio = :domicilio"),
    @NamedQuery(name = "NotaCredito.findByRfc", query = "SELECT n FROM NotaCredito n WHERE n.rfc = :rfc"),
    @NamedQuery(name = "NotaCredito.findBySubtotal", query = "SELECT n FROM NotaCredito n WHERE n.subtotal = :subtotal"),
    @NamedQuery(name = "NotaCredito.findByIva", query = "SELECT n FROM NotaCredito n WHERE n.iva = :iva"),
    @NamedQuery(name = "NotaCredito.findByTotal", query = "SELECT n FROM NotaCredito n WHERE n.total = :total"),
    @NamedQuery(name = "NotaCredito.findByTotalLetra", query = "SELECT n FROM NotaCredito n WHERE n.totalLetra = :totalLetra"),
    @NamedQuery(name = "NotaCredito.findByServicios", query = "SELECT n FROM NotaCredito n WHERE n.servicios = :servicios"),
    @NamedQuery(name = "NotaCredito.findByConstancia", query = "SELECT n FROM NotaCredito n WHERE n.constancia = :constancia"),
    @NamedQuery(name = "NotaCredito.findByPeriodo", query = "SELECT n FROM NotaCredito n WHERE n.periodo = :periodo"),
    @NamedQuery(name = "NotaCredito.findByObservaciones", query = "SELECT n FROM NotaCredito n WHERE n.observaciones = :observaciones"),
    @NamedQuery(name = "NotaCredito.findByFecha", query = "SELECT n FROM NotaCredito n WHERE n.fecha = :fecha"),
    @NamedQuery(name = "NotaCredito.findByCajero", query = "SELECT n FROM NotaCredito n WHERE n.cajero = :cajero")})
public class NotaCredito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "IDCLIENTE")
    private Integer idcliente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CLIENTE")
    private String cliente;
    @Size(max = 255)
    @Column(name = "DOMICILIO")
    private String domicilio;
    @Size(max = 20)
    @Column(name = "RFC")
    private String rfc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "SUBTOTAL")
    private BigDecimal subtotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IVA")
    private BigDecimal iva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TOTAL_LETRA")
    private String totalLetra;
    @Size(max = 50)
    @Column(name = "SERVICIOS")
    private String servicios;
    @Size(max = 15)
    @Column(name = "CONSTANCIA")
    private String constancia;
    @Size(max = 20)
    @Column(name = "PERIODO")
    private String periodo;
    @Size(max = 200)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 40)
    @Column(name = "CAJERO")
    private String cajero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nota")
    private List<CancelaNotaCredito> cancelaNotaCreditoList;
    @JoinColumn(name = "STATUS", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private StatusNotaCredito status;

    public NotaCredito() {
    }

    public NotaCredito(Integer id) {
        this.id = id;
    }

    public NotaCredito(Integer id, String numero, String cliente, BigDecimal iva, BigDecimal total, String totalLetra, Date fecha) {
        this.id = id;
        this.numero = numero;
        this.cliente = cliente;
        this.iva = iva;
        this.total = total;
        this.totalLetra = totalLetra;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(Integer idcliente) {
        this.idcliente = idcliente;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getTotalLetra() {
        return totalLetra;
    }

    public void setTotalLetra(String totalLetra) {
        this.totalLetra = totalLetra;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios(String servicios) {
        this.servicios = servicios;
    }

    public String getConstancia() {
        return constancia;
    }

    public void setConstancia(String constancia) {
        this.constancia = constancia;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCajero() {
        return cajero;
    }

    public void setCajero(String cajero) {
        this.cajero = cajero;
    }

    public List<CancelaNotaCredito> getCancelaNotaCreditoList() {
        return cancelaNotaCreditoList;
    }

    public void setCancelaNotaCreditoList(List<CancelaNotaCredito> cancelaNotaCreditoList) {
        this.cancelaNotaCreditoList = cancelaNotaCreditoList;
    }

    public StatusNotaCredito getStatus() {
        return status;
    }

    public void setStatus(StatusNotaCredito status) {
        this.status = status;
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
        if (!(object instanceof NotaCredito)) {
            return false;
        }
        NotaCredito other = (NotaCredito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.NotaCredito[ id=" + id + " ]";
    }
    
}
