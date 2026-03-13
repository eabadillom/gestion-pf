package mx.com.ferbo.model;

import java.io.Serializable;
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
@Table(name = "servicios_salida")
@NamedQueries({
    @NamedQuery(name = "ServiciosSalida.findAll", query = "SELECT ss FROM ServiciosSalida ss"),
    @NamedQuery(name = "ServiciosSalida.findById", query = "SELECT ss FROM ServiciosSalida ss WHERE ss.idSrvSalida = :idSrvSalida"),
    @NamedQuery(name = "ServiciosSalida.findByFolioSalida", query = "SELECT ss FROM ServiciosSalida ss INNER JOIN ss.salida s WHERE s.folioSalida = :folioSalida")
})
public class ServiciosSalida implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_srv_salida")
    private Integer idSrvSalida;
    
    @ManyToOne
    @JoinColumn(name = "cd_salida", referencedColumnName = "cd_salida")
    private Salida salida;
    
    @ManyToOne
    @JoinColumn(name = "servicio_cve", referencedColumnName = "servicio_cve")
    private Servicio servicio;
    
    @ManyToOne
    @JoinColumn(name = "unidad_de_manejo_cve", referencedColumnName = "unidad_de_manejo_cve")
    private UnidadDeManejo unidadDeManejo;
    
    @Column(name = "nu_cantidad")
    private Integer cantidad;

    public ServiciosSalida() {
    }

    public Integer getIdSrvSalida() {
        return idSrvSalida;
    }

    public void setIdSrvSalida(Integer idSrvSalida) {
        this.idSrvSalida = idSrvSalida;
    }

    public Salida getSalida() {
        return salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public UnidadDeManejo getUnidadDeManejo() {
        return unidadDeManejo;
    }

    public void setUnidadDeManejo(UnidadDeManejo unidadDeManejo) {
        this.unidadDeManejo = unidadDeManejo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
         if (this.idSrvSalida == null) {
            return System.identityHashCode(this);
        }
        return Objects.hash(this.idSrvSalida);
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
        final ServiciosSalida other = (ServiciosSalida) obj;
        if(this.idSrvSalida == null || other.idSrvSalida == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        
        return Objects.equals(this.idSrvSalida, other.idSrvSalida);
    }

    @Override
    public String toString() {
        return "ServiciosSalida[" + "idSrvSalida=" + idSrvSalida + ", cantidad=" + cantidad + ']';
    }
    
}
