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
public class DetallePartidaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "DET_PART_CVE")
    private int detPartCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PARTIDA_CVE")
    private int partidaCve;

    public DetallePartidaPK() {
    }

    public DetallePartidaPK(int detPartCve, int partidaCve) {
        this.detPartCve = detPartCve;
        this.partidaCve = partidaCve;
    }

    public int getDetPartCve() {
        return detPartCve;
    }

    public void setDetPartCve(int detPartCve) {
        this.detPartCve = detPartCve;
    }

    public int getPartidaCve() {
        return partidaCve;
    }

    public void setPartidaCve(int partidaCve) {
        this.partidaCve = partidaCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) detPartCve;
        hash += (int) partidaCve;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallePartidaPK)) {
            return false;
        }
        DetallePartidaPK other = (DetallePartidaPK) object;
        if (this.detPartCve != other.detPartCve) {
            return false;
        }
        if (this.partidaCve != other.partidaCve) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.DetallePartidaPK[ detPartCve=" + detPartCve + ", partidaCve=" + partidaCve + " ]";
    }
    
}
