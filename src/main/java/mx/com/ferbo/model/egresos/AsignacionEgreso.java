package mx.com.ferbo.model.egresos;

import java.io.Serializable;
import java.math.BigDecimal;

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

import mx.com.ferbo.model.catalogos.TipoAsignacion;

@Entity
@Table(name = "asignacion_egreso")
public class AsignacionEgreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_asig_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "pc_asig_egre", precision = 6, scale = 3)
    private BigDecimal porcentaje;

    @Basic(optional = false) 
    @Column(name = "im_asig_egre", precision = 15, scale = 2)
    private BigDecimal importe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_impo_egre", nullable = false)
    private ImporteEgreso importeEgreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_tipo_asig", nullable = false)
    private TipoAsignacion tipoAsignacion;

    public AsignacionEgreso(){
        // Constructor sin parametros
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public ImporteEgreso getImporteEgreso() {
        return importeEgreso;
    }

    public void setImporteEgreso(ImporteEgreso importeEgreso) {
        this.importeEgreso = importeEgreso;
    }

    public TipoAsignacion getTipoAsignacion() {
        return tipoAsignacion;
    }

    public void setTipoAsignacion(TipoAsignacion tipoAsignacion) {
        this.tipoAsignacion = tipoAsignacion;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof AsignacionEgreso))
            return false;

        AsignacionEgreso that = (AsignacionEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "AsignacionEgreso [id=" + id + ", porcentaje=" + porcentaje + ", importe=" + importe + "]";
    }

}
