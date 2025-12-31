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
@Table(name = "cat_tipo_cargo")
public class TipoCargo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_tipo_carg")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_tipo_carg", length = 50, nullable = false, unique = true)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "tx_tipo_carg", length = 250, nullable = false)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_tipo_carg")
    private Boolean activo;

    @Basic(optional = false)
    @Column(name = "st_iva")
    private Boolean tieneIVA;

    @Basic(optional = false)
    @Column(name = "st_ieps")
    private Boolean tieneIEPS;

    public TipoCargo(){
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

    public Boolean getTieneIVA() {
        return tieneIVA;
    }

    public void setTieneIVA(Boolean tieneIVA) {
        this.tieneIVA = tieneIVA;
    }

    public Boolean getTieneIEPS() {
        return tieneIEPS;
    }

    public void setTieneIEPS(Boolean tieneIEPS) {
        this.tieneIEPS = tieneIEPS;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof TipoCargo))
            return false;

        TipoCargo that = (TipoCargo) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "TipoCargo [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", activo=" + activo
                + ", tieneIVA=" + tieneIVA + ", tieneIEPS=" + tieneIEPS + "]";
    }
       
}