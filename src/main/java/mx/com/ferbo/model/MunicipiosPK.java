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
public class MunicipiosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "pais_cve")
    private int paisCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado_cve")
    private int estadoCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio_cve")
    private int municipioCve;

    public MunicipiosPK() {
    }

    public MunicipiosPK(int paisCve, int estadoCve, int municipioCve) {
        this.paisCve = paisCve;
        this.estadoCve = estadoCve;
        this.municipioCve = municipioCve;
    }

    public int getPaisCve() {
        return paisCve;
    }

    public void setPaisCve(int paisCve) {
        this.paisCve = paisCve;
    }

    public int getEstadoCve() {
        return estadoCve;
    }

    public void setEstadoCve(int estadoCve) {
        this.estadoCve = estadoCve;
    }

    public int getMunicipioCve() {
        return municipioCve;
    }

    public void setMunicipioCve(int municipioCve) {
        this.municipioCve = municipioCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) paisCve;
        hash += (int) estadoCve;
        hash += (int) municipioCve;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MunicipiosPK)) {
            return false;
        }
        MunicipiosPK other = (MunicipiosPK) object;
        if (this.paisCve != other.paisCve) {
            return false;
        }
        if (this.estadoCve != other.estadoCve) {
            return false;
        }
        if (this.municipioCve != other.municipioCve) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.MunicipiosPK[ paisCve=" + paisCve + ", estadoCve=" + estadoCve + ", municipioCve=" + municipioCve + " ]";
    }
    
}
