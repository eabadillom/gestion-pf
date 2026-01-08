package mx.com.ferbo.model.n.catalogos;

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
    @NamedQuery(name = "StatusActivoFijo.findAllVigentesONoVigentes", query = "SELECT saf FROm StatusActivoFijo saf WHERE saf.vigente = :vigente")
})
public class StatusActivoFijo implements Serializable, Catalogo {

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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public Boolean getVigente() {
        return vigente;
    }

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
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StatusActivoFijo [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", vigente="
                + vigente + "]";
    }

}
