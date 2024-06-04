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
public class CuotaMinimaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cte_cve")
    private int cteCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuota_id")
    private int cuotaId;

    public CuotaMinimaPK() {
    }

    public CuotaMinimaPK(int cteCve, int cuotaId) {
        this.cteCve = cteCve;
        this.cuotaId = cuotaId;
    }

    public int getCteCve() {
        return cteCve;
    }

    public void setCteCve(int cteCve) {
        this.cteCve = cteCve;
    }

    public int getCuotaId() {
        return cuotaId;
    }

    public void setCuotaId(int cuotaId) {
        this.cuotaId = cuotaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cteCve;
        hash += (int) cuotaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuotaMinimaPK)) {
            return false;
        }
        CuotaMinimaPK other = (CuotaMinimaPK) object;
        if (this.cteCve != other.cteCve) {
            return false;
        }
        if (this.cuotaId != other.cuotaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.CuotaMinimaPK[ cteCve=" + cteCve + ", cuotaId=" + cuotaId + " ]";
    }
    
}
