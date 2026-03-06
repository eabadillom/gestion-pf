package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "salida_detalle")
@NamedQueries({
    @NamedQuery(name = "SalidaDetalle.findAll", query = "SELECT sd FROM SalidaDetalle sd"),
    @NamedQuery(name = "SalidaDetalle.findById", query = "SELECT sd FROM SalidaDetalle sd WHERE sd.idSalidaDetalle = :idSalDetalle"),
    @NamedQuery(name = "SalidaDetalle.findByPlanta", query = "SELECT sd FROM SalidaDetalle sd INNER JOIN sd.partida pr INNER JOIN pr.camaraCve cm INNER JOIN cm.plantaCve pl WHERE pl.plantaCve = :idPlanta")
})
public class SalidaDetalle implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_salida_det")
    private Integer idSalidaDetalle;
    
    @ManyToOne
    @JoinColumn(name = "cd_salida", referencedColumnName = "cd_salida")
    private Salida salida;
    
    @ManyToOne
    @JoinColumn(name = "partida_cve", referencedColumnName = "PARTIDA_CVE")
    private Partida partida;
    
    @Column(name = "nu_cantidad")
    private Integer cantidad;
    
    @Column(name = "ct_peso_aprox")
    private BigDecimal pesoAprox;

    public SalidaDetalle() {
    }

    public Integer getIdSalidaDetalle() {
        return idSalidaDetalle;
    }

    public void setIdSalidaDetalle(Integer idSalidaDetalle) {
        this.idSalidaDetalle = idSalidaDetalle;
    }

    public Salida getSalida() {
        return salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPesoAprox() {
        return pesoAprox;
    }

    public void setPesoAprox(BigDecimal pesoAprox) {
        this.pesoAprox = pesoAprox;
    }

    @Override
    public int hashCode() {
        if (this.idSalidaDetalle == null) {
            return System.identityHashCode(this);
        }
        return Objects.hash(this.idSalidaDetalle);
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
        final SalidaDetalle other = (SalidaDetalle) obj;
        if(this.idSalidaDetalle == null || other.idSalidaDetalle == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        return Objects.equals(this.idSalidaDetalle, other.idSalidaDetalle);
    }

    @Override
    public String toString() {
        return "SalidaDetalle[" + "idSalidaDetalle=" + idSalidaDetalle + ", cantidad=" + cantidad + ", pesoAprox=" + pesoAprox + ']';
    }
    
}
