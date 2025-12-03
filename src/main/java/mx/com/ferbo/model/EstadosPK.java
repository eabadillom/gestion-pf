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
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Embeddable
public class EstadosPK implements Serializable {

    private static final long serialVersionUID = -4501534761783764337L;
    
    @ManyToOne
    @NotNull
    @JoinColumn(name = "pais_cve")
    private Paises pais;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado_cve")
    private Integer estadoCve;

    public EstadosPK() {
    }

    public EstadosPK(Paises pais, Integer estadoCve) {
        this.pais = pais;
        this.estadoCve = estadoCve;
    }

    public Paises getPais() {
        return pais;
    }

    public void setPais(Paises pais) {
        this.pais = pais;
    }

    public Integer getEstadoCve() {
        return estadoCve;
    }

    public void setEstadoCve(Integer estadoCve) {
        this.estadoCve = estadoCve;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.estadoCve;
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
        final EstadosPK other = (EstadosPK) obj;
        return Objects.equals(this.estadoCve, other.estadoCve);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.EstadosPK[ paisCve=" + pais.getPaisCve() + ", estadoCve=" + estadoCve + " ]";
    }
    
}
