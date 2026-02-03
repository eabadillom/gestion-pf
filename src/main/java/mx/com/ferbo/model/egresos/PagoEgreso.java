package mx.com.ferbo.model.egresos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.catalogos.StatusPago;

@Entity
@Table(name = "pago_egreso")
public class PagoEgreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_pago_egre")
    private Integer id;

    @Basic(optional =  false)
    @Column(name = "im_pago", precision = 15, scale = 2, nullable = false)
    private BigDecimal importe;

    @Basic(optional = true)
    @Column(name = "fh_pago", nullable =  true)
    private Date fechaPago;

    @Basic(optional = false)
    @Column(name = "cd_refe_pago", length = 100,nullable = false)
    private String referencia;

    @Basic(optional = true)
    @Column(name = "tx_obser", length = 250)
    private String observaciones;

    @Basic(optional = false)
    @Column(name = "fh_alta", nullable =  false)
    private Date fechaAlta;

    @Basic(optional = false)
    @Column(name = "fh_limite", nullable = false)
    private Date fechaLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_impo_egre", nullable = false)
    private ImporteEgreso importeEgreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_status_pago", nullable = false)
    private StatusPago status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mp_id", nullable = false)
    private MedioPago medioPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_docu_egre")
    private DocumentoEgreso documentoEgreso;

    public PagoEgreso(){
        // Constructor sin parametros
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public ImporteEgreso getImporteEgreso() {
        return importeEgreso;
    }

    public void setImporteEgreso(ImporteEgreso importeEgreso) {
        this.importeEgreso = importeEgreso;
    }

    public StatusPago getStatus() {
        return status;
    }

    public void setStatus(StatusPago status) {
        this.status = status;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public DocumentoEgreso getDocumentoEgreso() {
        return documentoEgreso;
    }

    public void setDocumentoEgreso(DocumentoEgreso documentoEgreso) {
        this.documentoEgreso = documentoEgreso;
    }

    @Override 
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof PagoEgreso))
            return false;

        PagoEgreso that = (PagoEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "PagoEgreso [id=" + id + ", importe=" + importe + ", fechaPago=" + fechaPago + ", referencia="
                + referencia + ", observaciones=" + observaciones + ", fechaAlta=" + fechaAlta + ", fechaLimite="
                + fechaLimite + "]";
    }

    

    
}
