/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "cuota_mensual_servicio")
@NamedQueries({
    @NamedQuery(name = "CuotaMensualServicio.findAll", query = "SELECT c FROM CuotaMensualServicio c"),
    @NamedQuery(name = "CuotaMensualServicio.findById", query = "SELECT c FROM CuotaMensualServicio c WHERE c.id = :id"),
    @NamedQuery(name = "CuotaMensualServicio.findByCuota", query = "SELECT c FROM CuotaMensualServicio c WHERE c.cuota = :cuota"),
    @NamedQuery(name = "CuotaMensualServicio.findByUnidadDeManejoCve", query = "SELECT c FROM CuotaMensualServicio c WHERE c.unidadDeManejoCve = :unidadDeManejoCve")})
public class CuotaMensualServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuota")
    private BigDecimal cuota;
    @Column(name = "unidad_de_manejo_cve")
    private Integer unidadDeManejoCve;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cuotaMensualServicio")
    private CuotaMensualServicioDet cuotaMensualServicioDet;
    @JoinColumn(name = "cte_cve", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente cteCve;
    @JoinColumn(name = "servicio_cve", referencedColumnName = "SERVICIO_CVE")
    @ManyToOne(optional = false)
    private Servicio servicioCve;
    @JoinColumn(name = "aviso_cve", referencedColumnName = "aviso_cve")
    @ManyToOne(optional = false)
    private Aviso avisoCve;

    public CuotaMensualServicio() {
    }

    public CuotaMensualServicio(Integer id) {
        this.id = id;
    }

    public CuotaMensualServicio(Integer id, BigDecimal cuota) {
        this.id = id;
        this.cuota = cuota;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCuota() {
        return cuota;
    }

    public void setCuota(BigDecimal cuota) {
        this.cuota = cuota;
    }

    public Integer getUnidadDeManejoCve() {
        return unidadDeManejoCve;
    }

    public void setUnidadDeManejoCve(Integer unidadDeManejoCve) {
        this.unidadDeManejoCve = unidadDeManejoCve;
    }

    public CuotaMensualServicioDet getCuotaMensualServicioDet() {
        return cuotaMensualServicioDet;
    }

    public void setCuotaMensualServicioDet(CuotaMensualServicioDet cuotaMensualServicioDet) {
        this.cuotaMensualServicioDet = cuotaMensualServicioDet;
    }

    public Cliente getCteCve() {
        return cteCve;
    }

    public void setCteCve(Cliente cteCve) {
        this.cteCve = cteCve;
    }

    public Servicio getServicioCve() {
        return servicioCve;
    }

    public void setServicioCve(Servicio servicioCve) {
        this.servicioCve = servicioCve;
    }

    public Aviso getAvisoCve() {
        return avisoCve;
    }

    public void setAvisoCve(Aviso avisoCve) {
        this.avisoCve = avisoCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuotaMensualServicio)) {
            return false;
        }
        CuotaMensualServicio other = (CuotaMensualServicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.CuotaMensualServicio[ id=" + id + " ]";
    }
    
}
