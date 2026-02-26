
package mx.com.ferbo.model.categresos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
    @NamedQuery(name = "TipoMovimientoEgreso.findByNombre", query = "SELECT tme FROM TipoMovimientoEgreso tme WHERE tme.nombre = :nombre"),
    @NamedQuery(name = "TipoMovimientoEgreso.findAllVigenteONoVigentes", query = "SELECT tme FROM TipoMovimientoEgreso tme WHERE tme.vigente = :vigente")
})
@Entity
@Table (name = "cat_tipo_movimiento_egreso")
public class TipoMovimientoEgreso implements Serializable, CatEgreso{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_tipo_movi")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "nb_tipo_movi", unique = true, length = 50, nullable = false)
    private String nombre;
    
    @Basic(optional = false)
    @Column(name = "tx_tipo_movi", length = 250, nullable = false)
    private String descripcion;
    
    @Basic(optional = false)
    @Column(name = "st_activo")
    private Boolean vigente = Boolean.TRUE;
    
    public TipoMovimientoEgreso() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public Boolean getVigente() {
        return vigente;
    }

    @Override
    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }
    
    @Override
    public boolean equals(Object o) { 
        if (this == o)
            return true;
        if (!(o instanceof TipoMovimientoEgreso))
            return false;
        
        TipoMovimientoEgreso that = (TipoMovimientoEgreso) o;
                                
        if (this.id != null && that.id != null) {
            return this.id.equals(that.id);
        }

        return Objects.equals(this.nombre, that.nombre);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return "TipoMovimientoEgreso{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", vigente=" + vigente + '}';
    }
  
}
