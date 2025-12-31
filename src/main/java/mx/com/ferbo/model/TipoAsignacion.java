package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cat_tipo_asignacion")
public class TipoAsignacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_asig_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_tipo_asig", length = 50, nullable = false, unique = true)
    private String nombre;

    public TipoAsignacion(){
        // Constructor sin parametros
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof TipoAsignacion))
            return false;

        TipoAsignacion that = (TipoAsignacion) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 32;
    }

    @Override
    public String toString() {
        return "TipoAsignacion [id=" + id + ", nombre=" + nombre + "]";
    }

    
}
