package mx.com.ferbo.modulos.egresos.model.catsecundarios;

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

import mx.com.ferbo.modulos.egresos.model.CatEgreso;

@NamedQueries({
    @NamedQuery(name = "StatusActivoFijo.findByNombre", query = "SELECT saf FROM StatusActivoFijo saf WHERE saf.nombre = :nombre"),
    @NamedQuery(name = "StatusActivoFijo.findAllVigentesONoVigentes", query = "SELECT saf FROm StatusActivoFijo saf WHERE saf.vigente = :vigente")
})
@Entity
@Table(name = "cat_status_activo_fijo")
public class StatusActivoFijo implements Serializable, CatEgreso<Integer> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_status_acti_fijo")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_status_acti_fijo", length = 50, nullable = false, unique = true)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "tx_status_acti_fijo", length = 255, nullable = false)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_status_acti_fijo")
    private Boolean vigente = Boolean.TRUE;

    public StatusActivoFijo(){
        // Constructor sin parametros
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
        if (!(o instanceof StatusActivoFijo))
            return false;

        StatusActivoFijo that = (StatusActivoFijo) o;
        
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
        return "StatusActivoFijo [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", vigente="
                + vigente + "]";
    }

}
