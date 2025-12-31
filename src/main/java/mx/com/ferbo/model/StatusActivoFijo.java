package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class StatusActivoFijo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_status_acti_fijo")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_status_acti_fijo", length = 50, nullable = false)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "tx_status_acti_fijo", length = 255, nullable = false)
    private String descripcion;

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
        return "StatusActivoFijo [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + "]";
    }

}
