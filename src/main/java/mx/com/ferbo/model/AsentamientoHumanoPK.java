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
public class AsentamientoHumanoPK implements Serializable {

	private static final long serialVersionUID = 1L;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipoasntmnto_cve")
    private short tipoasntmntoCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "entidadpostal_cve")
    private int entidadpostalCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asentamiento_cve")
    private int asentamientoCve;

    public AsentamientoHumanoPK() {
    }

    public AsentamientoHumanoPK(int paisCve, int estadoCve, int municipioCve, int ciudadCve, short tipoasntmntoCve, int entidadpostalCve, int asentamientoCve) {
        this.paisCve = paisCve;
        this.estadoCve = estadoCve;
        this.municipioCve = municipioCve;
        this.ciudadCve = ciudadCve;
        this.tipoasntmntoCve = tipoasntmntoCve;
        this.entidadpostalCve = entidadpostalCve;
        this.asentamientoCve = asentamientoCve;
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

    public short getTipoasntmntoCve() {
        return tipoasntmntoCve;
    }

    public void setTipoasntmntoCve(short tipoasntmntoCve) {
        this.tipoasntmntoCve = tipoasntmntoCve;
    }

    public int getEntidadpostalCve() {
        return entidadpostalCve;
    }

    public void setEntidadpostalCve(int entidadpostalCve) {
        this.entidadpostalCve = entidadpostalCve;
    }

    public int getAsentamientoCve() {
        return asentamientoCve;
    }

    public void setAsentamientoCve(int asentamientoCve) {
        this.asentamientoCve = asentamientoCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) paisCve;
        hash += (int) estadoCve;
        hash += (int) municipioCve;
        hash += (int) ciudadCve;
        hash += (int) tipoasntmntoCve;
        hash += (int) entidadpostalCve;
        hash += (int) asentamientoCve;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsentamientoHumanoPK)) {
            return false;
        }
        AsentamientoHumanoPK other = (AsentamientoHumanoPK) object;
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
        if (this.tipoasntmntoCve != other.tipoasntmntoCve) {
            return false;
        }
        if (this.entidadpostalCve != other.entidadpostalCve) {
            return false;
        }
        if (this.asentamientoCve != other.asentamientoCve) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.AsentamientoHumanoPK[ paisCve=" + paisCve + ", estadoCve=" + estadoCve + ", municipioCve=" + municipioCve + ", ciudadCve=" + ciudadCve + ", tipoasntmntoCve=" + tipoasntmntoCve + ", entidadpostalCve=" + entidadpostalCve + ", asentamientoCve=" + asentamientoCve + " ]";
    }
    
}
