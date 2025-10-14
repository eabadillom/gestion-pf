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
@Table(name = "municipios")
@NamedQueries({
    @NamedQuery(name = "Municipios.findAll", query = "SELECT m FROM Municipios m"),
    @NamedQuery(name = "Municipios.findByPaisCve", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve"),
    @NamedQuery(name = "Municipios.findByEstadoCve", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.estados.estadosPK.estadoCve = :estadoCve"),
    @NamedQuery(name = "Municipios.findByMunicipioCve", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.municipioCve = :municipioCve"),
    @NamedQuery(name = "Municipios.findByMunicipioDs", query = "SELECT m FROM Municipios m WHERE m.municipioDs = :municipioDs"),
    @NamedQuery(name = "Municipios.findByPaisCveEstadoCve", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve AND m.municipiosPK.estados.estadosPK.estadoCve = :estadoCve"),
    @NamedQuery(name = "Municipios.findByTodo", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.municipioCve = :municipioCve AND m.municipiosPK.estados.estadosPK.estadoCve = :estadoCve AND m.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve")
})
public class Municipios implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected MunicipiosPK municipiosPK;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "municipio_ds")
    private String municipioDs;
    
    @OneToMany(mappedBy = "ciudadesPK.municipios")
    private List<Ciudades> ciudadesList;

    public Municipios() {
    }

    public Municipios(Estados estados, int municipioCve) {
        this.municipiosPK = new MunicipiosPK(estados, municipioCve);
    }

    public Municipios(Estados estados, int municipioCve, String municipioDs) {
        this.municipiosPK = new MunicipiosPK(estados, municipioCve);
        this.municipioDs = municipioDs;
    }

    public MunicipiosPK getMunicipiosPK() {
        return municipiosPK;
    }

    public void setMunicipiosPK(MunicipiosPK municipiosPK) {
        this.municipiosPK = municipiosPK;
    }

    public String getMunicipioDs() {
        return municipioDs;
    }

    public void setMunicipioDs(String municipioDs) {
        this.municipioDs = municipioDs;
    }

    public List<Ciudades> getCiudadesList() {
        return ciudadesList;
    }

    public void setCiudadesList(List<Ciudades> ciudadesList) {
        this.ciudadesList = ciudadesList;
    }

    @Override
    public int hashCode() {
        if(this.municipiosPK.getMunicipioCve() == null){
            return System.identityHashCode(this);
        }
        return Objects.hash(this.municipiosPK.getMunicipioCve());
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
        final Municipios other = (Municipios) obj;
        return Objects.equals(this.municipiosPK.getMunicipioCve(), other.municipiosPK.getMunicipioCve());
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Municipios[ municipiosPK=" + municipiosPK.getMunicipioCve() + ", municipioDs=" + municipioDs + " ]";
    }
    
}
