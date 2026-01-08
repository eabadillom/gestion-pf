package mx.com.ferbo.model.n.egresos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NamedQueries({
    @NamedQuery(name = "ImporteEgreso.findAllByMes", query = "SELECT ie FROM ImporteEgreso ie WHERE ie.fechaLimite BETWEEN :inicio AND :fin ORDER BY ie.fechaLimite ASC"),
    @NamedQuery(name = "ImporteEgreso.findAllByConcepto", query = "SELECT ie FROM ImporteEgreso ie WHERE ie.conceptoEgreso.nombre = :concepto"),
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

    @Basic(optional = false)
    @Column (name = "to_egreso", precision = 15, scale = 2)
    private BigDecimal total;

    @Basic(optional = true)
    @Column(name = "cb_refe_pago", length = 100, nullable = true)
    private String refenrencia;

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

    @OneToOne(mappedBy = "importe")
    private ConceptoEgreso conceptoEgreso;

    @OneToMany(mappedBy = "importeEgreso", cascade = CascadeType.ALL, orphanRemoval = true)
    private PagoEgreso pagos; 

    @OneToMany(mappedBy = "importeEgreso", cascade = CascadeType.ALL, orphanRemoval = true)
    private CargoEgreso cargos;

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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getRefenrencia() {
        return refenrencia;
    }

    public void setRefenrencia(String refenrencia) {
        this.refenrencia = refenrencia;
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

    public PagoEgreso getPagos() {
        return pagos;
    }

    public void setPagos(PagoEgreso pagos) {
        this.pagos = pagos;
    }

    public CargoEgreso getCargos() {
        return cargos;
    }

    public void setCargos(CargoEgreso cargos) {
        this.cargos = cargos;
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
        return "ImporteEgreso [id=" + id + ", subTotal=" + subTotal + ", iva=" + iva + ", ieps=" + ieps + ", total="
                + total + ", refenrencia=" + refenrencia + ", observaciones=" + observaciones + ", motivo=" + motivo
                + ", fechaAlta=" + fechaAlta + ", fechaLimitePago=" + fechaLimitePago + "]";
    }


}
