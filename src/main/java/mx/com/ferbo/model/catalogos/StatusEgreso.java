package mx.com.ferbo.model.catalogos;

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
    @NamedQuery(name = "StatusEgreso.findByNombre", query = "SELECT se FROM StatusEgreso se WHERE se.nombre = :nombre"),
    @NamedQuery(name = "StatusEgreso.findAllVigentesONoVigentes", query = "SELECT se FROM StatusEgreso se WHERE se.vigente = :vigente")
})
@Entity
@Table(name = "cat_status_egreso")
public class StatusEgreso implements Serializable, Catalogo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_status_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_status_egre", unique = true, length = 50, nullable = false)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "tx_status_egre", length = 250, nullable = false)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_activo")
    private Boolean vigente = Boolean.TRUE;

    public StatusEgreso(){

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
        if(this == o)
            return true;
        if(!(o instanceof StatusEgreso))
            return false;

        StatusEgreso that = (StatusEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "StatusEgreso [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", vigente=" + vigente
                + "]";
    }

    
}
