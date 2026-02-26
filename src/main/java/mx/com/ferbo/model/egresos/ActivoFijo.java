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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import mx.com.ferbo.model.categresos.StatusActivoFijo;

@NamedQueries({
    @NamedQuery(name = "ActivoFijo.findAllByImporteEgreso", query = "SELECT af FROM ActivoFijo af WHERE af.importeEgreso = :idImporteEgreso"),
    @NamedQuery(name = "ActivoFijo.findAllByImporteEgresoYStatus", query = "SELECT af FROM ActivoFijo af WHERE af.importeEgreso = :idImporteEgreso AND af.status.nombre = :status")
})
@Entity
@Table(name = "activo_fijo")
public class ActivoFijo implements Serializable, Egreso<StatusActivoFijo>{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_acti_fijo")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_acti_fijo", length = 150, nullable = false)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "fh_adqui_acti")
    private Date fechaAdquisicion;

    @Basic(optional = false)
    @Column(name = "im_acti_fijo", precision = 15, scale = 2)
    private BigDecimal importe;

    @Basic(optional = false)
    @Column(name = "nu_vida_util")
    private Integer vidaUtil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_status_acti_fijo")
    private StatusActivoFijo status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_impo_egre")
    private ImporteEgreso importeEgreso;
    
    @Basic(optional = false)
    @Column(name = "fh_alta")
    private Date fechaAlta;
    
    @Basic(optional = false)
    @Column(name = "fh_modi")
    private Date fechaModificacion;

    public ActivoFijo(){
        // Constructor sin parametros
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Integer getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(Integer vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public StatusActivoFijo getStatus() {
        return status;
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

    @Override
    public void setStatus(StatusActivoFijo status) {
        this.status = status;
    }

    public ImporteEgreso getImporteEgreso() {
        return importeEgreso;
    }

    public void setImporteEgreso(ImporteEgreso importeEgreso) {
        this.importeEgreso = importeEgreso;
    }
    
    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof ActivoFijo))
            return false;

        ActivoFijo that = (ActivoFijo) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "ActivoFijo{" + "id=" + id + ", descripcion=" + descripcion + ", fechaAdquisicion=" + fechaAdquisicion + ", importe=" + importe + ", vidaUtil=" + vidaUtil + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion + '}';
    }

}
