package mx.com.ferbo.model.catalogos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mx.com.ferbo.model.egresos.ImporteEgreso;

@Entity
@Table(name = "concepto_egreso")
public class ConceptoEgreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_conc_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "to_conc_egre", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalConceptoEgreso;

    @Basic(optional = true)
    @Column(name = "cd_cfdi_uuid", length = 36, nullable = true)
    private String cfdiUUID;

    @Basic(optional = false)
    @Column(name = "st_dedu",nullable = false)
    private Boolean esDeducible = Boolean.TRUE;

    @Basic(optional = false)
    @Column(name = "st_cfdi", nullable = false)
    private Boolean requiereCFDI = Boolean.FALSE;

    @Basic(optional = false)
    @Column(name = "st_acti_fijo", nullable = false)
    private Boolean esActivoFijo = Boolean.FALSE;

    @Basic(optional = false)
    @Column(name = "st_iva", nullable = false)
    private Boolean tieneIVA = Boolean.FALSE;

    @Basic(optional = false)
    @Column(name = "st_ieps", nullable =  false)
    private Boolean tieneIEPS = Boolean.FALSE;

    @Basic(optional = true)
    @Column(name = "tx_no_dedu", nullable = true)
    private String noDeducible;

    @Basic(optional = false)
    @Column(name = "fh_regi")
    private Date fechaRegistro;

    @Basic(optional = true)
    @Column(name = "pc_iva", nullable = true)
    private BigDecimal porcentajeIVA;

    @Basic(optional = true)
    @Column(name = "pc_ieps", nullable = true)
    private BigDecimal porcentajeIEPS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_cat_conc_egre", nullable = false)
    private CatConceptoEgreso catConcepto;

    @OneToMany(mappedBy = "conceptoEgreso", cascade = CascadeType.ALL)
    private List<ImporteEgreso> importes;

    public ConceptoEgreso(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotalConceptoEgreso() {
        return totalConceptoEgreso;
    }

    public void setTotalConceptoEgreso(BigDecimal totalConceptoEgreso) {
        this.totalConceptoEgreso = totalConceptoEgreso;
    }

    public String getCfdiUUID() {
        return cfdiUUID;
    }

    public void setCfdiUUID(String cfdiUUID) {
        this.cfdiUUID = cfdiUUID;
    }

    public Boolean getEsDeducible() {
        return esDeducible;
    }

    public void setEsDeducible(Boolean esDeducible) {
        this.esDeducible = esDeducible;
    }

    public Boolean getRequiereCFDI() {
        return requiereCFDI;
    }

    public void setRequiereCFDI(Boolean requiereCFDI) {
        this.requiereCFDI = requiereCFDI;
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

    public String getNoDeducible() {
        return noDeducible;
    }

    public void setNoDeducible(String noDeducible) {
        this.noDeducible = noDeducible;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public CatConceptoEgreso getCatConcepto() {
        return catConcepto;
    }

    public void setCatConcepto(CatConceptoEgreso catConcepto) {
        this.catConcepto = catConcepto;
    }

    public List<ImporteEgreso> getImportes() {
        return importes;
    }

    public void setImportes(List<ImporteEgreso> importes) {
        this.importes = importes;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if(!(o instanceof ConceptoEgreso))
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
        return "ConceptoEgreso [id=" + id + ", totalConceptoEgreso=" + totalConceptoEgreso + ", cfdiUUID=" + cfdiUUID
                + ", esDeducible=" + esDeducible + ", requiereCFDI=" + requiereCFDI + ", esActivoFijo=" + esActivoFijo
                + ", tieneIVA=" + tieneIVA + ", tieneIEPS=" + tieneIEPS + ", noDeducible=" + noDeducible
                + ", fechaRegistro=" + fechaRegistro + ", porcentajeIVA=" + porcentajeIVA + ", porcentajeIEPS="
                + porcentajeIEPS + "]";
    }

}    
