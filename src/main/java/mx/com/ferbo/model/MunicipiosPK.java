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
public class MunicipiosPK implements Serializable {

    @JoinColumns({
        @JoinColumn(name = "pais_cve", referencedColumnName = "pais_cve"),
        @JoinColumn(name = "estado_cve", referencedColumnName = "estado_cve")})
    @ManyToOne
    private Estados estados;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio_cve")
    private Integer municipioCve;

    public MunicipiosPK() {
    }

    public MunicipiosPK(Estados estados, Integer municipioCve) {
        this.estados = estados;
        this.municipioCve = municipioCve;
    }

    public Integer getMunicipioCve() {
        return municipioCve;
    }

    public void setMunicipioCve(Integer municipioCve) {
        this.municipioCve = municipioCve;
    }

    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.municipioCve;
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
        final MunicipiosPK other = (MunicipiosPK) obj;
        return Objects.equals(this.municipioCve, other.municipioCve);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.MunicipiosPK[ municipioCve=" + municipioCve + " ]";
    }
    
}
