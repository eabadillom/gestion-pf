/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Embeddable
public class DetalleFacturacionPK implements Serializable {

    private static final long serialVersionUID = -2888234836056967308L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "DETALLE_CVE")
    private int detalleCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CTE_CVE")
    private int cteCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "det_fac_secuencia")
    private int detFacSecuencia;

    public DetalleFacturacionPK() {
    }

    public DetalleFacturacionPK(int detalleCve, int cteCve, int detFacSecuencia) {
        this.detalleCve = detalleCve;
        this.cteCve = cteCve;
        this.detFacSecuencia = detFacSecuencia;
    }

    public int getDetalleCve() {
        return detalleCve;
    }

    public void setDetalleCve(int detalleCve) {
        this.detalleCve = detalleCve;
    }

    public int getCteCve() {
        return cteCve;
    }

    public void setCteCve(int cteCve) {
        this.cteCve = cteCve;
    }

    public int getDetFacSecuencia() {
        return detFacSecuencia;
    }

    public void setDetFacSecuencia(int detFacSecuencia) {
        this.detFacSecuencia = detFacSecuencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) detalleCve;
        hash += (int) cteCve;
        hash += (int) detFacSecuencia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleFacturacionPK)) {
            return false;
        }
        DetalleFacturacionPK other = (DetalleFacturacionPK) object;
        if (this.detalleCve != other.detalleCve) {
            return false;
        }
        if (this.cteCve != other.cteCve) {
            return false;
        }
        if (this.detFacSecuencia != other.detFacSecuencia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.DetalleFacturacionPK[ detalleCve=" + detalleCve + ", cteCve=" + cteCve + ", detFacSecuencia=" + detFacSecuencia + " ]";
    }
    
}
