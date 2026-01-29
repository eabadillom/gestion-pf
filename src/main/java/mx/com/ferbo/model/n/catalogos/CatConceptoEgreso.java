package mx.com.ferbo.model.n.catalogos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
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
import javax.persistence.Table;

@NamedQueries({
    @NamedQuery(name = "CatConceptoEgreso.findByNombre", query = "SELECT cce FROM CatConceptoEgreso cce WHERE cce.nombre = :nombre"),
    @NamedQuery(name = "CatConceptoEgreso.findAllVigentesONoVigentes", query = "SELECT cce FROM CatConceptoEgreso cce WHERE cce.vigente = :vigente"),
    @NamedQuery(name = "CatConceptoEgreso.findAllByCategoriaEgreso", query = "SELECT cce FROM CatConceptoEgreso cce WHERE cce.categoriaEgreso.id = :id"),
    @NamedQuery(name = "CatConceptoEgreso.findAllByCategoriaEgresoYVigencia", query = "SELECT cce FROM CatConceptoEgreso cce WHERE cce.categoriaEgreso.id = :id and cce.vigente = :vigente")
})
@Entity
@Table(name = "cat_concepto_egreso")
public class CatConceptoEgreso implements Serializable, Catalogo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_cat_conc_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_conc_egre", length = 60, nullable = false, unique = true)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "st_conc_egre")
    private Boolean vigente;

    @Basic(optional = false)
    @Column(name = "cd_sat", length = 8, nullable = false)
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

    @Basic(optional =  false)
    @Column(name = "st_cfdi")
    private Boolean requiereCFDI;

    @Basic(optional = false)
    @Column(name = "st_dedu")
    private Boolean esDeducible;

    @Basic(optional = false)
    @Column(name = "st_cfdi_dedu")
    private Boolean requiereCFDIDeducible;

    @Basic(optional = false)
    @Column(name = "pc_iva", precision = 6, scale = 3)
    private BigDecimal porcentajeIVA = new BigDecimal("16.000");;

    @Basic(optional = false)
    @Column(name = "pc_ieps", precision = 6, scale = 3)
    private BigDecimal porcentajeIEPS = new BigDecimal("00.000");

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
        return "Descripción valida";
    }

    @Override
    public void setDescripcion(String descripcion) {
        // Vacío
    }

    @Override
    public Boolean getVigente() {
        return vigente;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
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

    public BigDecimal getPorcentajeIVA() {
        return porcentajeIVA;
    }

    public void setPorcentajeIVA(BigDecimal porcentajeIVA) {
        this.porcentajeIVA = porcentajeIVA;
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

    public Boolean getRequiereCFDI() {
        return requiereCFDI;
    }

    public void setRequiereCFDI(Boolean requiereCFDI) {
        this.requiereCFDI = requiereCFDI;
    }

    public Boolean getEsDeducible() {
        return esDeducible;
    }

    public void setEsDeducible(Boolean esDeducible) {
        this.esDeducible = esDeducible;
    }

    public Boolean getRequiereCFDIDeducible() {
        return requiereCFDIDeducible;
    }

    public void setRequiereCFDIDeducible(Boolean requiereCFDIDeducible) {
        this.requiereCFDIDeducible = requiereCFDIDeducible;
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
                + ", porcentajeIVA=" + porcentajeIVA + ", porcentajeIEPS=" + porcentajeIEPS + ", requiereCFDI="
                + requiereCFDI + ", esDeducible=" + esDeducible + ", requiereCFDIDecucible=" + requiereCFDIDeducible
                + "]";
    }

}
