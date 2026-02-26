package mx.com.ferbo.model.egresos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.categresos.StatusEgreso;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;

@NamedQueries({
    @NamedQuery(
            name = "ImporteEgreso.findByFiltros",
            query = "SELECT ie FROM ImporteEgreso ie "
            + "WHERE ie.fechaLimitePago BETWEEN :inicio AND :fin "
            + "AND (:idConcepto IS NULL OR ie.conceptoEgreso.catConcepto.id = :idConcepto) "
            + "AND (:idEmisor IS NULL OR ie.emisor.id = :idEmisor) "
            + "ORDER BY ie.fechaLimitePago ASC"
    )
})
@Entity
@Table(name = "importe_egreso")
public class ImporteEgreso implements Serializable, Egreso<StatusEgreso> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_impo_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "im_egreso", precision = 12, scale = 2)
    private BigDecimal subTotal;

    @Basic(optional = false)
    @Column(name = "to_egreso", precision = 12, scale = 2)
    private BigDecimal total;

    @Basic(optional = true)
    @Column(name = "im_iva", precision = 12, scale = 2)
    private BigDecimal iva;

    @Basic(optional = true)
    @Column(name = "im_ieps", precision = 12, scale = 2)
    private BigDecimal ieps;

    @Basic(optional = true)
    @Column(name = "tx_obser", length = 250, nullable = true)
    private String observaciones;

    @Basic(optional = true)
    @Column(name = "tx_motivo", length = 250, nullable = true)
    private String motivo;

    @Basic(optional = false)
    @Column(name = "fh_alta")
    private Date fechaAlta;

    @Basic(optional = false)
    @Column(name = "fh_modi")
    private Date fechaModificacion;

    @Basic(optional = false)
    @Column(name = "fh_limi_pago")
    private Date fechaLimitePago;

    @Basic(optional = false)
    @Column(name = "nu_pagos")
    private Integer numeroPagos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_conc_egre", nullable = false)
    private ConceptoEgreso conceptoEgreso;

    @OneToMany(mappedBy = "importeEgreso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoEgreso> pagos;

    @OneToMany(mappedBy = "importeEgreso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CargoEgreso> cargos;

    @ManyToOne
    @JoinColumn(name = "cd_status_egre", nullable = false)
    private StatusEgreso status;

    @ManyToOne(optional = false) // muchos egresos -> un emisor
    @JoinColumn(name = "id_emisor", nullable = false)
    private NEmisoresCFDIS emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mp_id", nullable = false)
    private MedioPago medioPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    public ImporteEgreso() {
        // Constructor sin parametros
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getIeps() {
        return ieps;
    }

    public void setIeps(BigDecimal ieps) {
        this.ieps = ieps;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Date getFechaLimitePago() {
        return fechaLimitePago;
    }

    public void setFechaLimitePago(Date fechaLimitePago) {
        this.fechaLimitePago = fechaLimitePago;
    }

    public ConceptoEgreso getConceptoEgreso() {
        return conceptoEgreso;
    }

    public void setConceptoEgreso(ConceptoEgreso conceptoEgreso) {
        this.conceptoEgreso = conceptoEgreso;
    }

    public List<PagoEgreso> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoEgreso> pagos) {
        this.pagos = pagos;
    }

    public List<CargoEgreso> getCargos() {
        return cargos;
    }

    public void setCargos(List<CargoEgreso> cargos) {
        this.cargos = cargos;
    }

    public StatusEgreso getStatus() {
        return status;
    }

    @Override
    public void setStatus(StatusEgreso status) {
        this.status = status;
    }

    public NEmisoresCFDIS getEmisor() {
        return emisor;
    }

    public void setEmisor(NEmisoresCFDIS emisor) {
        this.emisor = emisor;
    }

    public Integer getNumeroPagos() {
        return numeroPagos;
    }

    public void setNumeroPagos(Integer numeroPagos) {
        this.numeroPagos = numeroPagos;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImporteEgreso)) {
            return false;
        }

        ImporteEgreso that = (ImporteEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "ImporteEgreso{" + "id=" + id + ", subTotal=" + subTotal + ", total=" + total + ", iva=" + iva + ", ieps=" + ieps + ", observaciones=" + observaciones + ", motivo=" + motivo + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion + ", fechaLimitePago=" + fechaLimitePago + ", numeroPagos=" + numeroPagos + '}';
    }

}
