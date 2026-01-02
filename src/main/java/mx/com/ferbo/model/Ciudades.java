/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "ciudades")
@NamedQueries({
    @NamedQuery(name = "Ciudades.findAll", query = "SELECT c FROM Ciudades c"),
    @NamedQuery(name = "Ciudades.findByPaisCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve"),
    @NamedQuery(name = "Ciudades.findByEstadoCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve"),
    @NamedQuery(name = "Ciudades.findByMunicipioCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve"),
    @NamedQuery(name = "Ciudades.findByCiudadCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.ciudadCve = :ciudadCve"),
    @NamedQuery(name = "Ciudades.findByCiudadDs", query = "SELECT c FROM Ciudades c WHERE c.ciudadDs = :ciudadDs"),
    @NamedQuery(name = "Ciudades.findByPaisCveEstadoCveMunicipioCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve AND c.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve AND c.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve "),
    @NamedQuery(name = "Ciudades.findByTodo", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve AND c.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve AND c.ciudadesPK.ciudadCve = :ciudadCve"),
    @NamedQuery(name = "Ciudades.findByEstadoMunicipioCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve AND c.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve")
})

public class Ciudades implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected CiudadesPK ciudadesPK;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ciudad_ds")
    private String ciudadDs;
    
    @OneToMany(mappedBy = "asentamientoHumanoPK.ciudades")
    private List<AsentamientoHumano> asentamientos;

    public Ciudades() {
    }

    public Ciudades(Municipios municipios, int ciudadCve) {
        this.ciudadesPK = new CiudadesPK(municipios, ciudadCve);
    }

    public Ciudades(Municipios municipios, int ciudadCve, String ciudadDs) {
        this.ciudadesPK = new CiudadesPK(municipios, ciudadCve);
        this.ciudadDs = ciudadDs;
    }

    public CiudadesPK getCiudadesPK() {
        return ciudadesPK;
    }

    public void setCiudadesPK(CiudadesPK ciudadesPK) {
        this.ciudadesPK = ciudadesPK;
    }

    public String getCiudadDs() {
        return ciudadDs;
    }

    public void setCiudadDs(String ciudadDs) {
        this.ciudadDs = ciudadDs;
    }

    public List<AsentamientoHumano> getAsentamientos() {
        return asentamientos;
    }

    public void setAsentamientos(List<AsentamientoHumano> asentamientos) {
        this.asentamientos = asentamientos;
    }

    @Override
    public int hashCode() {
        if(this.ciudadesPK.getCiudadCve() == null){
            return System.identityHashCode(this);
        }
        return Objects.hash(this.getCiudadesPK().getCiudadCve());
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
        final Ciudades other = (Ciudades) obj;
        return Objects.equals(this.ciudadesPK.getCiudadCve(), other.ciudadesPK.getCiudadCve());
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Ciudades[ ciudadesPK=" + ciudadesPK.getCiudadCve() + ", ciudadDs=" + ciudadDs + " ]";
    }
    
}
