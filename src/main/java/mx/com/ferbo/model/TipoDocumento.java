package mx.com.ferbo.model;

import java.io.Serializable;

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
        @NamedQuery(name = "TipoDocumento.findByNombre", query = "SELECT td FROM TipoDocumento td WHERE td.nombre = :nombre"),
        @NamedQuery(name = "TipoDocumento.findAllActivos", query = "SELECT td FROM TipoDocumento td WHERE td.activo = 1"),
        @NamedQuery(name = "TipoDocumento.findAllNoActivos", query = "SELECT td FROM TipoDocumento td WHERE td.activo = 0")
})
@Entity
@Table(name = "cat_tipo_documento")
public class TipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_tipo_docu")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_tipo_docu", length = 50, nullable = false, unique = true)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "tx_tipo_docu", length = 250, nullable = false)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_fisc")
    private Boolean esFiscal;

    @Basic(optional = false)
    @Column(name = "st_obli")
    private Boolean esObligatorio;

    @Basic(optional = false)
    @Column(name = "st_tipo_docu")
    private Boolean activo = Boolean.TRUE;

    public TipoDocumento() {
        // Constructor sin parametro
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

    public Boolean getEsFiscal() {
        return esFiscal;
    }

    public void setEsFiscal(Boolean esFiscal) {
        this.esFiscal = esFiscal;
    }

    public Boolean getEsObligatorio() {
        return esObligatorio;
    }

    public void setEsObligatorio(Boolean esObligatorio) {
        this.esObligatorio = esObligatorio;
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
        if (!(o instanceof TipoDocumento))
            return false;

        TipoDocumento that = (TipoDocumento) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TipoDocumento [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", esFiscal="
                + esFiscal + ", esObligatorio=" + esObligatorio + ", activo=" + activo + "]";
    }

}
