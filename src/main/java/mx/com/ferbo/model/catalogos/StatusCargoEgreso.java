
package mx.com.ferbo.model.catalogos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
    @NamedQuery(name = "CatStatusCargoEgreso.findByNombre", query = "SELECT csce FROM CatStatusCargoEgreso csce WHERE csce.nombre = :nombre"),
    @NamedQuery(name = "CatStatusCargoEgreso.findAllVigentesONoVigentes", query = "SLECT csce FROM CatStatusCargoEgreso csce WHERE csce.vigente = :vigente")
})
@Entity
@Table (name="cat_status_cargo_egreso")
public class StatusCargoEgreso implements Catalogo, Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    @Basic(optional = false)
    @Column(name = "cd_status_carg_egre")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "nb_status_carg_egre", unique = true, length = 30, nullable = false)
    private String nombre;
    
    @Basic(optional = false)
    @Column(name = "tx_status_carg_egre", length = 100, nullable = false)
    private String descripcion;
    
    @Basic(optional = false)
    @Column(name = "st_status_carg_egre")
    private Boolean vigente = Boolean.TRUE;

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
    
    
    public StatusCargoEgreso(){
        
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if (!(o instanceof StatusCargoEgreso))
            return false;
        StatusCargoEgreso that = (StatusCargoEgreso) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "StatusCargoEgreso{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", vigente=" + vigente + '}';
    }
    
}
