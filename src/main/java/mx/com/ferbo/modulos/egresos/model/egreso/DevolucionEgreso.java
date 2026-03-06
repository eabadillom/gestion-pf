
package mx.com.ferbo.modulos.egresos.model.egreso;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mx.com.ferbo.modulos.egresos.model.Egreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusDevolucionEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoDevolucionEgreso;

@Entity
@Table(name = "devolucion_egreso")
public class DevolucionEgreso implements Serializable, Egreso<Integer, StatusDevolucionEgreso>{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_devo_egre")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "im_devo", precision = 12, scale = 2)
    private BigDecimal importeDevolucion;
    
    @Basic(optional = false)
    @Column(name = "fh_alta")
    private Date fechaAlta;
    
    @Basic(optional = false)
    @Column(name = "fh_devo")
    private Date fechaDevolucion; 
    
    @Basic(optional = false)
    @Column(name = "fh_modi")
    private Date fechaModificacion;
    
    @Basic(optional = false)
    @Column(name = "tx_obser", length = 250)
    private String observaciones;
    
    @ManyToOne
    @JoinColumn(name = "cd_status_devo", nullable = false)
    private StatusDevolucionEgreso status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_tipo_devo", nullable = false)
    private TipoDevolucionEgreso tipo;
    
    @OneToMany(mappedBy = "devolucion")
    private List<DocumentoMovimientoEgreso> movimientos;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_impo_egre", nullable = false)
    private ImporteEgreso importeEgreso;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_pago_egre")
    private PagoEgreso pagoEgreso;
    
    public DevolucionEgreso(){
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getImporteDevolucion() {
        return importeDevolucion;
    }

    public void setImporteDevolucion(BigDecimal importeDevolucion) {
        this.importeDevolucion = importeDevolucion;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public StatusDevolucionEgreso getStatus() {
        return status;
    }

    @Override
    public void setStatus(StatusDevolucionEgreso status) {
        this.status = status;
    }

    public TipoDevolucionEgreso getTipo() {
        return tipo;
    }

    public void setTipo(TipoDevolucionEgreso tipo) {
        this.tipo = tipo;
    }

    public List<DocumentoMovimientoEgreso> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<DocumentoMovimientoEgreso> movimientos) {
        this.movimientos = movimientos;
    }

    public ImporteEgreso getImporteEgreso() {
        return importeEgreso;
    }

    public void setImporteEgreso(ImporteEgreso importeEgreso) {
        this.importeEgreso = importeEgreso;
    }

    public PagoEgreso getPagoEgreso() {
        return pagoEgreso;
    }

    public void setPagoEgreso(PagoEgreso pagoEgreso) {
        this.pagoEgreso = pagoEgreso;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DevolucionEgreso))
            return false;
        
        DevolucionEgreso that = (DevolucionEgreso) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode(){
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "DevolucionEgreso{" + "id=" + id + ", importeDevolucion=" + importeDevolucion + ", fechaAlta=" + fechaAlta + ", fechaDevolucion=" + fechaDevolucion + ", fechaModificacion=" + fechaModificacion + ", observaciones=" + observaciones + '}';
    }
    
    
}
