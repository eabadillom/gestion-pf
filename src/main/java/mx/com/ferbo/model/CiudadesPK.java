/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
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
public class CiudadesPK implements Serializable {

    private static final long serialVersionUID = 3833888070514352421L;
    
    @JoinColumns({
        @JoinColumn(name = "pais_cve", referencedColumnName = "pais_cve"),
        @JoinColumn(name = "estado_cve", referencedColumnName = "estado_cve"),
        @JoinColumn(name = "municipio_cve", referencedColumnName = "municipio_cve")})
    @ManyToOne
    private Municipios municipios;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciudad_cve")
    private Integer ciudadCve;

    public CiudadesPK() {
    }

    public CiudadesPK(Municipios municipios, Integer ciudadCve) {
        this.municipios = municipios;
        this.ciudadCve = ciudadCve;
    }

    public Municipios getMunicipios() {
        return municipios;
    }

    public void setMunicipios(Municipios municipios) {
        this.municipios = municipios;
    }

    public Integer getCiudadCve() {
        return ciudadCve;
    }

    public void setCiudadCve(Integer ciudadCve) {
        this.ciudadCve = ciudadCve;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + this.ciudadCve;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CiudadesPK other = (CiudadesPK) obj;
        return Objects.equals(this.ciudadCve, other.ciudadCve);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.CiudadesPK[ ciudadCve=" + ciudadCve + " ]";
    }
    
}
