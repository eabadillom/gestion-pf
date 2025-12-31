package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import mx.com.ferbo.model.n.ImporteEgreso;

public class ActivoFijo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_acti_fijo")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_acti_fijo", length = 30, nullable = false)
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

    public ActivoFijo(){
        // Constructor sin parametros
    }

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

    public void setStatus(StatusActivoFijo status) {
        this.status = status;
    }

    public ImporteEgreso getImporteEgreso() {
        return importeEgreso;
    }

    public void setImporteEgreso(ImporteEgreso importeEgreso) {
        this.importeEgreso = importeEgreso;
    }

    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof ActivoFijo))
            return false;

        ActivoFijo that = (ActivoFijo) o;
        return id != null && id.equals(that.id);
    }

    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "ActivoFijo [id=" + id + ", descripcion=" + descripcion + ", fechaAdquisicion=" + fechaAdquisicion
                + ", importe=" + importe + ", vidaUtil=" + vidaUtil + "]";
    }
    
}
