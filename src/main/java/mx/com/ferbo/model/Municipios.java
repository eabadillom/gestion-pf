/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
    @NamedQuery(name = "Municipios.findByPaisCve", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.paisCve = :paisCve"),
    @NamedQuery(name = "Municipios.findByEstadoCve", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.estadoCve = :estadoCve"),
    @NamedQuery(name = "Municipios.findByMunicipioCve", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.municipioCve = :municipioCve"),
    @NamedQuery(name = "Municipios.findByMunicipioDs", query = "SELECT m FROM Municipios m WHERE m.municipioDs = :municipioDs"),
	@NamedQuery(name = "Municipios.findByPaisCveEstadoCve", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.paisCve = :paisCve AND m.municipiosPK.estadoCve = :estadoCve"),
    //@NamedQuery(name = "Municipios.findByMunicipioDs", query = "SELECT m FROM Municipios m WHERE m.municipioDs = :municipioDs"),
    @NamedQuery(name = "Municipios.findByTodo", query = "SELECT m FROM Municipios m WHERE m.municipiosPK.municipioCve = :municipioCve AND m.municipiosPK.estadoCve = :estadoCve AND m.municipiosPK.paisCve = :paisCve")})

public class Municipios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MunicipiosPK municipiosPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "municipio_ds")
    private String municipioDs;
    @JoinColumns({
        @JoinColumn(name = "pais_cve", referencedColumnName = "pais_cve", insertable = false, updatable = false),
        @JoinColumn(name = "estado_cve", referencedColumnName = "estado_cve", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Estados estados;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipios")
    private List<Ciudades> ciudadesList;

    public Municipios() {
    }

    public Municipios(MunicipiosPK municipiosPK) {
        this.municipiosPK = municipiosPK;
    }

    public Municipios(MunicipiosPK municipiosPK, String municipioDs) {
        this.municipiosPK = municipiosPK;
        this.municipioDs = municipioDs;
    }

    public Municipios(int paisCve, int estadoCve, int municipioCve) {
        this.municipiosPK = new MunicipiosPK(paisCve, estadoCve, municipioCve);
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

    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }

    public List<Ciudades> getCiudadesList() {
        return ciudadesList;
    }

    public void setCiudadesList(List<Ciudades> ciudadesList) {
        this.ciudadesList = ciudadesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (municipiosPK != null ? municipiosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Municipios)) {
            return false;
        }
        Municipios other = (Municipios) object;
        if ((this.municipiosPK == null && other.municipiosPK != null) || (this.municipiosPK != null && !this.municipiosPK.equals(other.municipiosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Municipios[ municipiosPK=" + municipiosPK + " ]";
    }
    
}
