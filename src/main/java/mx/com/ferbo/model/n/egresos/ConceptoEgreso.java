package mx.com.ferbo.model.n.egresos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mx.com.ferbo.model.n.catalogos.CatConceptoEgreso;

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

    @Basic(optional = true)
    @Column(name = "pc_iva", precision = 6, scale = 3)
    private BigDecimal porcentajeIVA;

    @Basic(optional = true)
    @Column(name = "pc_ieps", precision = 6, scale = 3)
    private BigDecimal porcentajeIEPS;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cd_conc_egre", nullable = false)
    private CatConceptoEgreso catConceptoEgreso;

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
        return "ConceptoEgreso [id=" + id + ", porcentajeIVA=" + porcentajeIVA + ", porcentajeIEPS=" + porcentajeIEPS
                + "]";
    }

    
}
