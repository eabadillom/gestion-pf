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
public class CiudadesPK implements Serializable {

    private static final long serialVersionUID = 3833888070514352421L;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciudad_cve")
    private int ciudadCve;

    public CiudadesPK() {
    }

    public CiudadesPK(int paisCve, int estadoCve, int municipioCve, int ciudadCve) {
        this.paisCve = paisCve;
        this.estadoCve = estadoCve;
        this.municipioCve = municipioCve;
        this.ciudadCve = ciudadCve;
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

    public int getCiudadCve() {
        return ciudadCve;
    }

    public void setCiudadCve(int ciudadCve) {
        this.ciudadCve = ciudadCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) paisCve;
        hash += (int) estadoCve;
        hash += (int) municipioCve;
        hash += (int) ciudadCve;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CiudadesPK)) {
            return false;
        }
        CiudadesPK other = (CiudadesPK) object;
        if (this.paisCve != other.paisCve) {
            return false;
        }
        if (this.estadoCve != other.estadoCve) {
            return false;
        }
        if (this.municipioCve != other.municipioCve) {
            return false;
        }
        if (this.ciudadCve != other.ciudadCve) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.CiudadesPK[ paisCve=" + paisCve + ", estadoCve=" + estadoCve + ", municipioCve=" + municipioCve + ", ciudadCve=" + ciudadCve + " ]";
    }
    
}
