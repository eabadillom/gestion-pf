package mx.com.ferbo.model.n;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_egreso")
public class TipoEgreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_tipo_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_tipo_egre", length = 50, nullable = false)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "st_tipo_egre")
    private Boolean status;

    @OneToMany(mappedBy = "tipoEgreso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategoriaEgreso> categorias;

    public TipoEgreso() {
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<CategoriaEgreso> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaEgreso> categorias) {
        this.categorias = categorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TipoEgreso that = (TipoEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TipoEgreso [id=" + id + ", nombre=" + nombre + ", status=" + status + "]";
    }
}
