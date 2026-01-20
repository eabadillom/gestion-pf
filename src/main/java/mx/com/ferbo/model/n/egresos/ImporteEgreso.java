package mx.com.ferbo.model.n.egresos;

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

import mx.com.ferbo.model.n.catalogos.StatusEgreso;

@NamedQueries({
    @NamedQuery(name = "ImporteEgreso.findAllByMes", query = "SELECT ie FROM ImporteEgreso ie WHERE ie.fechaLimitePago BETWEEN :inicio AND :fin ORDER BY ie.fechaLimitePago ASC"),
    @NamedQuery(name = "ImporteEgreso.findAllByConcepto", query = "SELECT ie FROM ImporteEgreso ie WHERE ie.conceptoEgreso.catConceptoEgreso.id = :idConcepto"),
})
@Entity
@Table(name = "importe_egreso")
public class ImporteEgreso implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_impo_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "im_egreso", precision = 15, scale = 2)
    private BigDecimal subTotal;

    @Basic(optional = true)
    @Column(name = "im_iva", precision = 15, scale = 2)
    private BigDecimal iva; 

    @Basic(optional = true)
    @Column(name = "im_ieps", precision = 15, scale = 2)
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
    @Column(name = "fh_limi_pago")
    private Date fechaLimitePago;

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

    public ImporteEgreso(){
        // Constructor sin parametros
    }

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

    public void setStatus(StatusEgreso status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof ImporteEgreso))
            return false;

        ImporteEgreso that = (ImporteEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "ImporteEgreso [id=" + id + ", subTotal=" + subTotal + ", iva=" + iva + ", ieps=" + ieps
                + ", observaciones=" + observaciones + ", motivo=" + motivo + ", fechaAlta=" + fechaAlta
                + ", fechaLimitePago=" + fechaLimitePago + "]";
    }

}
