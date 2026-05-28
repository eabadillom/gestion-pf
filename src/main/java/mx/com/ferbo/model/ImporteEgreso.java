package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "importe_egreso")
@NamedQueries({ 
    @NamedQuery(name = "ImporteEgreso.findByAll", query = "SELECT i FROM ImporteEgreso i"),
    @NamedQuery(name = "ImporteEgreso.findById", query = "SELECT i FROM ImporteEgreso i WHERE i.idImporte = :idImporte"),
    @NamedQuery(name = "ImporteEgreso.findByFecha", query = "SELECT i FROM ImporteEgreso i WHERE i.fecha = :fecha"),
    @NamedQuery(name = "ImporteEgreso.findByidEgreso", query = "SELECT i FROM ImporteEgreso i WHERE i.egreso.idEgreso = :idEgreso"),
    @NamedQuery(name = "ImporteEgreso.findBycdEmisor", query = "SELECT i FROM ImporteEgreso i WHERE i.cdEmisor = :cdEmisor"),
    @NamedQuery(name = "ImporteEgreso.findByParametros", query = "SELECT i FROM ImporteEgreso i WHERE (:emisor IS NULL OR i.cdEmisor.nb_emisor = :emisor) AND (i.fecha BETWEEN :fechaInicio and :fechaFin)")
})
public class ImporteEgreso implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_importe")
    private Integer idImporte;

    @Column(name = "fecha")
    private Date fecha;

    @JoinColumn(name = "id_egreso", referencedColumnName = "id_egreso")
    @ManyToOne(optional = false)
    private Egresos egreso;

    @JoinColumn(name = "cd_emisor", referencedColumnName = "cd_emisor")
    @ManyToOne(optional = false)
    private EmisoresCFDIS cdEmisor;

    @Column(name = "subTotal")
    private BigDecimal subTotal;

    @Column(name = "IVA")
    private BigDecimal iva;

    @Column(name = "IEPS")
    private BigDecimal ieps;

    @Column(name = "importe")
    private BigDecimal importe;

    public Integer getIdImporte() {
        return idImporte;
    }

    public void setIdImporte(Integer idImporte) {
        this.idImporte = idImporte;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Egresos getEgreso() {
        return egreso;
    }

    public void setEgreso(Egresos egreso) {
        this.egreso = egreso;
    }

    public EmisoresCFDIS getCdEmisor() {
        return cdEmisor;
    }

    public void setCdEmisor(EmisoresCFDIS cdEmisor) {
        this.cdEmisor = cdEmisor;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
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

    @Override
    public int hashCode() {
        return Objects.hash(cdEmisor, fecha, egreso, idImporte, importe);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ImporteEgreso other = (ImporteEgreso) obj;
        return Objects.equals(cdEmisor, other.cdEmisor) && Objects.equals(fecha, other.fecha)
                && Objects.equals(egreso, other.egreso) && Objects.equals(idImporte, other.idImporte)
                && Objects.equals(importe, other.importe);
    }

    @Override
    public String toString() {
        return "ImporteEgreso [idImporte=" + idImporte + ", fecha=" + fecha + ", idEgreso=" + egreso + ", cdEmisor="
                + cdEmisor + ", importe=" + importe + "]";
    }

}
