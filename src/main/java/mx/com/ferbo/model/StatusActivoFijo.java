package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(name = "StatusActivoFijo.findByNombre", query = "SELECT saf FROM StatusActivoFijo saf WHERE saf.nombre = :nombre"),
    @NamedQuery(name = "StatusActivoFijo.findAllActivos", query = "SELECT saf FROm StatusActivoFijo saf WHERE saf.activo = 1")
})
public class StatusActivoFijo implements Serializable {

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
    private Boolean activo = Boolean.TRUE;

    public StatusActivoFijo(){
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StatusActivoFijo))
            return false;

        StatusActivoFijo that = (StatusActivoFijo) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StatusActivoFijo [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", activo="
                + activo + "]";
    }

}
