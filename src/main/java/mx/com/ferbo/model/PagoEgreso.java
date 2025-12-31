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
import javax.persistence.Table;

import mx.com.ferbo.model.n.ImporteEgreso;

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

    @Basic(optional = false)
    @Column(name = "fh_pago", nullable =  false)
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_impo_egre")
    private ImporteEgreso importeEgreso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_status_pago")
    private StatusPago status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mp_id")
    private MedioPago medioPago;

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
                + referencia + ", observaciones=" + observaciones + ", fechaAlta=" + fechaAlta + "]";
    }

    
}
