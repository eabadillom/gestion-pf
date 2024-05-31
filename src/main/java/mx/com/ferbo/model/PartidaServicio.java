/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

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
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "partida_servicio")
@NamedQueries({
    @NamedQuery(name = "PartidaServicio.findAll", query = "SELECT p FROM PartidaServicio p"),
    @NamedQuery(name = "PartidaServicio.findByPartidaCve", query = "SELECT p FROM PartidaServicio p WHERE p.partidaCve = :partidaCve"),
    @NamedQuery(name = "PartidaServicio.findByCantidadDeCobro", query = "SELECT p FROM PartidaServicio p WHERE p.cantidadDeCobro = :cantidadDeCobro"),
    @NamedQuery(name = "PartidaServicio.findByCantidadTotal", query = "SELECT p FROM PartidaServicio p WHERE p.cantidadTotal = :cantidadTotal"),
    @NamedQuery(name = "PartidaServicio.findByFolio", query = "SELECT p FROM PartidaServicio p WHERE p.folio = :folio")})
public class PartidaServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PARTIDA_CVE")
    private Integer partidaCve;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CANTIDAD_DE_COBRO")
    private BigDecimal cantidadDeCobro;
    @Column(name = "CANTIDAD_TOTAL")
    private Integer cantidadTotal;
    @JoinColumn(name = "FOLIO", referencedColumnName = "FOLIO")
    @ManyToOne(fetch = FetchType.LAZY)
    private ConstanciaDeServicio folio;
    @JoinColumn(name = "PRODUCTO_CVE", referencedColumnName = "PRODUCTO_CVE")
    @ManyToOne
    private Producto productoCve;
    @JoinColumn(name = "UNIDAD_DE_MANEJO_CVE", referencedColumnName = "UNIDAD_DE_MANEJO_CVE")
    @ManyToOne
    private UnidadDeManejo unidadDeManejoCve;
    @JoinColumn(name = "UNIDAD_DE_COBRO", referencedColumnName = "UNIDAD_DE_MANEJO_CVE")
    @ManyToOne
    private UnidadDeManejo unidadDeCobro;

    public PartidaServicio() {
    }

    public PartidaServicio(Integer partidaCve) {
        this.partidaCve = partidaCve;
    }

    public Integer getPartidaCve() {
        return partidaCve;
    }

    public void setPartidaCve(Integer partidaCve) {
        this.partidaCve = partidaCve;
    }

    public BigDecimal getCantidadDeCobro() {
        return cantidadDeCobro;
    }

    public void setCantidadDeCobro(BigDecimal cantidadDeCobro) {
        this.cantidadDeCobro = cantidadDeCobro;
    }

    public Integer getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Integer cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public ConstanciaDeServicio getFolio() {
        return folio;
    }

    public void setFolio(ConstanciaDeServicio folio) {
        this.folio = folio;
    }

    public Producto getProductoCve() {
        return productoCve;
    }

    public void setProductoCve(Producto productoCve) {
        this.productoCve = productoCve;
    }

    public UnidadDeManejo getUnidadDeManejoCve() {
        return unidadDeManejoCve;
    }

    public void setUnidadDeManejoCve(UnidadDeManejo unidadDeManejoCve) {
        this.unidadDeManejoCve = unidadDeManejoCve;
    }

    public UnidadDeManejo getUnidadDeCobro() {
        return unidadDeCobro;
    }

    public void setUnidadDeCobro(UnidadDeManejo unidadDeCobro) {
        this.unidadDeCobro = unidadDeCobro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partidaCve != null ? partidaCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartidaServicio)) {
            return false;
        }
        PartidaServicio other = (PartidaServicio) object;
        if ((this.partidaCve == null && other.partidaCve != null) || (this.partidaCve != null && !this.partidaCve.equals(other.partidaCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.PartidaServicio[ partidaCve=" + partidaCve + " ]";
    }
    
}
