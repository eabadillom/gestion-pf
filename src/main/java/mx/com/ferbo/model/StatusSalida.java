package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "status_salida")
@NamedQueries({
    @NamedQuery(name = "StatusSalida.findAll", query = "SELECT s FROM StatusSalida s"),
    @NamedQuery(name = "StatusSalida.findById", query = "SELECT s FROM StatusSalida s WHERE s.idStatus = :idStatus"),
    @NamedQuery(name = "StatusSalida.findByClave", query = "SELECT s FROM StatusSalida s WHERE s.clave = :clave")
})
public class StatusSalida implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_status")
    private Integer idStatus;
    
    @Size(max = 25)
    @Column(name = "nb_descripcion")
    private String descripcion;
    
    @Size(max = 2)
    @Column(name = "nb_clave")
    private String clave;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "status")
    private List<Salida> listSalidas;
    
    public StatusSalida() {
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<Salida> getListSalidas() {
        return listSalidas;
    }

    public void setListSalidas(List<Salida> listSalidas) {
        this.listSalidas = listSalidas;
    }

    @Override
    public int hashCode() {
        if (this.idStatus == null) {
            return System.identityHashCode(this);
        }
        return Objects.hash(this.idStatus);
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
        final StatusSalida other = (StatusSalida) obj;
        if(this.idStatus == null || other.idStatus == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        
        return Objects.equals(this.idStatus, other.idStatus);
    }

    @Override
    public String toString() {
        return "StatusSalida[" + "idStatus=" + idStatus + ", descripcion=" + descripcion + ", clave=" + clave + ']';
    }
    
}
