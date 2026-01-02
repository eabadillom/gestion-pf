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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@NamedQueries({
    @NamedQuery(name = "CategoriaEgreso.findAllActivos", query = "SELECT ce FROM CategoriaEgreso ce WHERE ce.activo = 1")
})
@Entity
@Table(name = "categoria_egreso")
public class CategoriaEgreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_cate_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_cate_egre", length = 50, nullable = false)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "st_cate_egre")
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_tipo_egre", nullable = false)
    private TipoEgreso tipoEgreso;

    @OneToMany(mappedBy = "categoriaEgreso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConceptoEgreso> conceptos;

    public CategoriaEgreso() {
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public TipoEgreso getTipoEgreso() {
        return tipoEgreso;
    }

    public void setTipoEgreso(TipoEgreso tipoEgreso) {
        this.tipoEgreso = tipoEgreso;
    }

    public List<ConceptoEgreso> getConceptos() {
        return conceptos;
    }

    public void setConceptos(List<ConceptoEgreso> conceptos) {
        this.conceptos = conceptos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CategoriaEgreso))
            return false;

        CategoriaEgreso that = (CategoriaEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CategoriaEgreso [id=" + id + ", nombre=" + nombre + ", activo=" + activo + "]";
    }

    
}
