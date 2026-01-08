package mx.com.ferbo.model.n.egresos;

import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mx.com.ferbo.model.n.catalogos.CategoriaEgreso;

@NamedQueries({
    @NamedQuery(name = "ConceptoEgreso.findAllActivos", query = "SELECT ce FROM ConceptoEgreso ce WHERE ce.activo = :activo"),
    @NamedQuery(name = "ConceptoEgreso.findAllByCategoriaEgreso", query = "SELECT ce ConceptoEgreso ce WHERE ce.categoriaEgreso.id = :id"),
    @NamedQuery(name = "ConceptoEgreso.findAllByCategoriaEgresoYActivo", query = "SELECT ce ConceptoEgreso ce WHERE (ce.categoriaEgreso.id = :id) AND (ce.activo = :activo)")
})
@Entity
@Table(name = "concepto_egreso")
public class ConceptoEgreso implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cb_conc_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_conc_egre", length = 60, nullable = false)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "st_conc_egre")
    private Boolean activo;

    @Basic(optional = false)
    @Column(name = "cd_sat", length = 100, nullable = false)
    private String codigoSAT;

    @Basic(optional = false)
    @Column(name = "st_acti_fijo")
    private Boolean esActivoFijo;

    @Basic(optional = false)
    @Column(name = "st_iva")
    private Boolean tieneIVA;

    @Basic(optional = true)
    @Column(name = "pc_iva", precision = 6, scale = 3)
    private BigDecimal porcentajeIVA;

    @Basic(optional = false)
    @Column(name = "st_ieps")
    private Boolean tieneIEPS;

    @Basic(optional = true)
    @Column(name = "pc_ieps", precision = 6, scale = 3)
    private BigDecimal porcentajeIEPS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_cate_egre", nullable = false)
    private CategoriaEgreso categoriaEgreso;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cd_impo_egre", nullable = false)
    private ImporteEgreso importeEgreso;

    public ConceptoEgreso(){
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

    public BigDecimal getPorcentajeIVA() {
        return porcentajeIVA;
    }

    public void setPorcentajeIVA(BigDecimal porcentajeIVA) {
        this.porcentajeIVA = porcentajeIVA;
    }

    public Boolean getTieneIEPS() {
        return tieneIEPS;
    }

    public void setTieneIEPS(Boolean tieneIEPS) {
        this.tieneIEPS = tieneIEPS;
    }

    public BigDecimal getPorcentajeIEPS() {
        return porcentajeIEPS;
    }

    public void setPorcentajeIEPS(BigDecimal porcentajeIEPS) {
        this.porcentajeIEPS = porcentajeIEPS;
    }

    public CategoriaEgreso getCategoriaEgreso() {
        return categoriaEgreso;
    }

    public void setCategoriaEgreso(CategoriaEgreso categoriaEgreso) {
        this.categoriaEgreso = categoriaEgreso;
    }

    public ImporteEgreso getImporteEgreso() {
        return importeEgreso;
    }

    public void setImporteEgreso(ImporteEgreso importeEgreso) {
        this.importeEgreso = importeEgreso;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof ConceptoEgreso))
            return false;

        ConceptoEgreso that = (ConceptoEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "ConceptoEgreso [id=" + id + ", nombre=" + nombre + ", activo=" + activo + ", codigoSAT=" + codigoSAT
                + ", esActivoFijo=" + esActivoFijo + ", tieneIVA=" + tieneIVA + ", porcentajeIVA=" + porcentajeIVA
                + ", tieneIEPS=" + tieneIEPS + ", porcentajeIEPS=" + porcentajeIEPS + "]";
    }

}
