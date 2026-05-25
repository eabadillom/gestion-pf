/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "factura_medio_pago")
@NamedQueries({
    @NamedQuery(name = "FacturaMedioPago.findAll", query = "SELECT f FROM FacturaMedioPago f"),
    @NamedQuery(name = "FacturaMedioPago.findByFacturaId", query = "SELECT f FROM FacturaMedioPago f WHERE f.facturaMedioPagoPK.facturaId = :facturaId"),
    @NamedQuery(name = "FacturaMedioPago.findByMpId", query = "SELECT f FROM FacturaMedioPago f WHERE f.mpId = :mpId"),
    @NamedQuery(name = "FacturaMedioPago.findByMpDescripcion", query = "SELECT f FROM FacturaMedioPago f WHERE f.mpDescripcion = :mpDescripcion"),
    @NamedQuery(name = "FacturaMedioPago.findByFmpPorcentaje", query = "SELECT f FROM FacturaMedioPago f WHERE f.fmpPorcentaje = :fmpPorcentaje"),
    @NamedQuery(name = "FacturaMedioPago.findByFmpReferencia", query = "SELECT f FROM FacturaMedioPago f WHERE f.fmpReferencia = :fmpReferencia")})
public class FacturaMedioPago implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected FacturaMedioPagoPK facturaMedioPagoPK;
    
    @JoinColumn(name = "mp_id", referencedColumnName = "mp_id")
    @ManyToOne(optional = false)
    private MedioPago mpId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "mp_descripcion")
    private String mpDescripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fmp_porcentaje")
    private int fmpPorcentaje;
    
    @Size(max = 50)
    @Column(name = "fmp_referencia")
    private String fmpReferencia;
    
//    @JoinColumn(name = "factura_id", referencedColumnName = "id", insertable = false, updatable = false)
//    @ManyToOne(optional = false)
//    private Factura factura;

    public FacturaMedioPago() {
    }

    public FacturaMedioPago(FacturaMedioPagoPK facturaMedioPagoPK) {
        this.facturaMedioPagoPK = facturaMedioPagoPK;
    }

    public FacturaMedioPago(FacturaMedioPagoPK facturaMedioPagoPK, String mpDescripcion, int fmpPorcentaje) {
        this.facturaMedioPagoPK = facturaMedioPagoPK;
        this.mpDescripcion = mpDescripcion;
        this.fmpPorcentaje = fmpPorcentaje;
    }

    public FacturaMedioPago(Factura facturaId, int fmpId) {
        this.facturaMedioPagoPK = new FacturaMedioPagoPK(facturaId, fmpId);
    }

    public FacturaMedioPagoPK getFacturaMedioPagoPK() {
        return facturaMedioPagoPK;
    }

    public void setFacturaMedioPagoPK(FacturaMedioPagoPK facturaMedioPagoPK) {
        this.facturaMedioPagoPK = facturaMedioPagoPK;
    }

    public MedioPago getMpId() {
        return mpId;
    }

    public void setMpId(MedioPago mpId) {
        this.mpId = mpId;
    }

    public String getMpDescripcion() {
        return mpDescripcion;
    }

    public void setMpDescripcion(String mpDescripcion) {
        this.mpDescripcion = mpDescripcion;
    }

    public int getFmpPorcentaje() {
        return fmpPorcentaje;
    }

    public void setFmpPorcentaje(int fmpPorcentaje) {
        this.fmpPorcentaje = fmpPorcentaje;
    }

    public String getFmpReferencia() {
        return fmpReferencia;
    }

    public void setFmpReferencia(String fmpReferencia) {
        this.fmpReferencia = fmpReferencia;
    }

//    public Factura getFactura() {
//        return factura;
//    }
//
//    public void setFactura(Factura factura) {
//        this.factura = factura;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturaMedioPagoPK != null ? facturaMedioPagoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaMedioPago)) {
            return false;
        }
        FacturaMedioPago other = (FacturaMedioPago) object;
        if ((this.facturaMedioPagoPK == null && other.facturaMedioPagoPK != null) || (this.facturaMedioPagoPK != null && !this.facturaMedioPagoPK.equals(other.facturaMedioPagoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.FacturaMedioPago[ facturaMedioPagoPK=" + facturaMedioPagoPK + " ]";
    }
    
}
