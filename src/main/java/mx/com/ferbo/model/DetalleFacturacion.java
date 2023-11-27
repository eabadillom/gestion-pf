/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "detalle_facturacion")
@NamedQueries({
    @NamedQuery(name = "DetalleFacturacion.findAll", query = "SELECT d FROM DetalleFacturacion d"),
    @NamedQuery(name = "DetalleFacturacion.findByDetalleCve", query = "SELECT d FROM DetalleFacturacion d WHERE d.detalleFacturacionPK.detalleCve = :detalleCve"),
    @NamedQuery(name = "DetalleFacturacion.findByCteCve", query = "SELECT d FROM DetalleFacturacion d WHERE d.detalleFacturacionPK.cteCve = :cteCve"),
    @NamedQuery(name = "DetalleFacturacion.findByDetFacSecuencia", query = "SELECT d FROM DetalleFacturacion d WHERE d.detalleFacturacionPK.detFacSecuencia = :detFacSecuencia")})
public class DetalleFacturacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleFacturacionPK detalleFacturacionPK;
    @JoinColumn(name = "CTE_CVE", referencedColumnName = "CTE_CVE", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "DATO_ESP_CVE", referencedColumnName = "DATO_ESP_CVE")
    @ManyToOne(optional = false)
    private DatosEspeciales datoEspCve;

    public DetalleFacturacion() {
    }

    public DetalleFacturacion(DetalleFacturacionPK detalleFacturacionPK) {
        this.detalleFacturacionPK = detalleFacturacionPK;
    }

    public DetalleFacturacion(int detalleCve, int cteCve, int detFacSecuencia) {
        this.detalleFacturacionPK = new DetalleFacturacionPK(detalleCve, cteCve, detFacSecuencia);
    }

    public DetalleFacturacionPK getDetalleFacturacionPK() {
        return detalleFacturacionPK;
    }

    public void setDetalleFacturacionPK(DetalleFacturacionPK detalleFacturacionPK) {
        this.detalleFacturacionPK = detalleFacturacionPK;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public DatosEspeciales getDatoEspCve() {
        return datoEspCve;
    }

    public void setDatoEspCve(DatosEspeciales datoEspCve) {
        this.datoEspCve = datoEspCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleFacturacionPK != null ? detalleFacturacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleFacturacion)) {
            return false;
        }
        DetalleFacturacion other = (DetalleFacturacion) object;
        if ((this.detalleFacturacionPK == null && other.detalleFacturacionPK != null) || (this.detalleFacturacionPK != null && !this.detalleFacturacionPK.equals(other.detalleFacturacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.DetalleFacturacion[ detalleFacturacionPK=" + detalleFacturacionPK + " ]";
    }
    
}
