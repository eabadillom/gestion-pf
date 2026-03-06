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
    @NamedQuery(name = "StatusDevolucion.findByNombre", query = "SELECT sd FROM StatusDevolucion sd WHERE sd.nombre = :nombre"),
    @NamedQuery(name = "StatusDevolucion.findAllVigentesONoVigentes", query = "SELECT sd FROM StatusDevolucion sd WHERE sd.vigente = :vigente")
})
@Entity
@Table(name = "cat_status_devolucion")
public class StatusDevolucionEgreso implements Serializable, CatEgreso<Integer> {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_status_devo")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_status_devo", unique = true, length = 50, nullable = false)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "tx_status_devo", length = 250, nullable = false)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_activo")
    private Boolean vigente = Boolean.TRUE;

    public StatusDevolucionEgreso() {

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
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusDevolucionEgreso)) {
            return false;
        }

        StatusDevolucionEgreso that = (StatusDevolucionEgreso) o;
        
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
        return "StatusDevolucion{" + "id=" + id + ", nombre=" + nombre + ", desripcion=" + descripcion + ", vigente=" + vigente + '}';
    }

}
