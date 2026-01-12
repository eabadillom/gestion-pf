package mx.com.ferbo.model.n.catalogos;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cat_concepto_egreso")
public class CatConceptoEgreso implements Serializable, Catalogo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cb_cat_conc_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_conc_egre", length = 60, nullable = false, unique = true)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "st_conc_egre")
    private Boolean vigente;

    @Basic(optional = false)
    @Column(name = "cd_sat", length = 100, nullable = false)
    private String codigoSAT;

    @Basic(optional = false)
    @Column(name = "st_acti_fijo")
    private Boolean esActivoFijo;

    @Basic(optional = false)
    @Column(name = "st_iva")
    private Boolean tieneIVA;

    @Basic(optional = false)
    @Column(name = "st_ieps")
    private Boolean tieneIEPS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_cate_egre", nullable = false)
    private CategoriaEgreso categoriaEgreso;

    public CatConceptoEgreso() {
        // Constructor sin parametros
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDescripcion'");
    }

    @Override
    public Boolean getVigente() {
        return vigente;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public String getCodigoSAT() {
        return codigoSAT;
    }

    public void setCodigoSAT(String codigoSAT) {
        this.codigoSAT = codigoSAT;
    }

    public Boolean getEsActivoFijo() {
        return esActivoFijo;
    }

    public void setEsActivoFijo(Boolean esActivoFijo) {
        this.esActivoFijo = esActivoFijo;
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

    public boolean equals(Object o){
        if (this == o)
            return true;
        if (! (o instanceof CatConceptoEgreso))
            return false;

        CatConceptoEgreso that = (CatConceptoEgreso) o;
        return id != null && id.equals(that.id);
    }

    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "CatConceptoEgreso [id=" + id + ", nombre=" + nombre + ", vigente=" + vigente + ", codigoSAT="
                + codigoSAT + ", esActivoFijo=" + esActivoFijo + ", tieneIVA=" + tieneIVA + ", tieneIEPS=" + tieneIEPS
                + "]";
    }

    
}
