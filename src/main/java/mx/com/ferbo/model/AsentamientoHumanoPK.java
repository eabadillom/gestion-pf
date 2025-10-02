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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Embeddable
public class AsentamientoHumanoPK implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne()
    @NotNull
    @JoinColumns({
        @JoinColumn(name = "pais_cve", referencedColumnName = "pais_cve"),
        @JoinColumn(name = "estado_cve", referencedColumnName = "estado_cve"),
        @JoinColumn(name = "municipio_cve", referencedColumnName = "municipio_cve"),
        @JoinColumn(name = "ciudad_cve", referencedColumnName = "ciudad_cve")
    })
    private Ciudades ciudades;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "asentamiento_cve")
    private int asentamientoCve;
    
    public AsentamientoHumanoPK() {
    }

    public AsentamientoHumanoPK(Ciudades ciudades, int asentamientoCve) {
        this.ciudades = ciudades;
        this.asentamientoCve = asentamientoCve;
    }

    public Ciudades getCiudades() {
        return ciudades;
    }

    public void setCiudades(Ciudades ciudades) {
        this.ciudades = ciudades;
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
        return this.asentamientoCve == other.asentamientoCve;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.AsentamientoHumanoPK[ asentamientoCve=" + asentamientoCve + " ]";
    }
    
}
