package mx.com.ferbo.model.catalogos;

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
    @NamedQuery(name = "CategoriaEgreso.findAllVigentesONoVigentes", query = "SELECT ce FROM CategoriaEgreso ce WHERE ce.vigente = :vigente"),
    @NamedQuery(name = "CategoriaEgreso.findAllByTipoEgreso", query = "SELECT ce FROM CategoriaEgreso ce WHERE ce.tipoEgreso.id = :id"),
    @NamedQuery(name = "CategoriaEgreso.findAllByTipoEgresoYVigenteONoVigente", query = "SELECT ce FROM CategoriaEgreso ce WHERE (ce.tipoEgreso.id = :id) AND (ce.vigente = :vigente)")
})
@Entity
@Table(name = "categoria_egreso")
public class CategoriaEgreso implements Serializable, Catalogo {

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
    private Boolean vigente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_tipo_egre", nullable = false)
    private TipoEgreso tipoEgreso;

    @OneToMany(mappedBy = "categoriaEgreso", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CatConceptoEgreso> catConceptos;

    public CategoriaEgreso() {
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
    public Boolean getVigente() {
        return vigente;
    }

    @Override
    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public TipoEgreso getTipoEgreso() {
        return tipoEgreso;
    }

    public void setTipoEgreso(TipoEgreso tipoEgreso) {
        this.tipoEgreso = tipoEgreso;
    }

    public List<CatConceptoEgreso> getCatConceptos() {
        return catConceptos;
    }

    public void setCatConceptos(List<CatConceptoEgreso> catConceptos) {
        this.catConceptos = catConceptos;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CategoriaEgreso))
            return false;

        final CategoriaEgreso that = (CategoriaEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CategoriaEgreso [id=" + id + ", nombre=" + nombre + ", vigente=" + vigente + "]";
    }

    @Override
    public String getDescripcion() {
        return "Descripción valida";
    }

    @Override
    public void setDescripcion(String descripcion) {
        // vacía
    }
    
}
