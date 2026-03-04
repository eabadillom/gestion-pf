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
    @NamedQuery(name = "TipoAsignacion.findByNombre", query = "SELECT ta FROM TipoAsignacion ta WHERE ta.nombre = :nombre"),
    @NamedQuery(name = "TipoAsignacion.findAllVigentesONoVigentes", query = "SELECT ta FROM TipoAsignacion ta WHERE ta.vigente = :vigente")
})
@Entity
@Table(name = "cat_tipo_asignacion")
public class TipoAsignacionEgreso implements Serializable, CatEgreso {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_tipo_asig")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_tipo_asig", length = 50, nullable = false, unique = true)
    private String nombre;

    @Basic(optional = true)
    @Column(name = "tx_tipo_asig", length = 150, nullable = true)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_tipo_asig")
    private Boolean vigente = Boolean.TRUE;

    public TipoAsignacionEgreso(){
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
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof TipoAsignacionEgreso))
            return false;

        TipoAsignacionEgreso that = (TipoAsignacionEgreso) o;
                
        if (this.id != null && that.id != null) {
            return this.id.equals(that.id);
        }

        return Objects.equals(this.nombre, that.nombre);
    }

    @Override
    public int hashCode(){
        return id != null ? id.hashCode() : Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return "TipoAsignacion [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", vigente="
                + vigente + "]";
    }

    
    
}
