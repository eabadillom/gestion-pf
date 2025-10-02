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
import javax.persistence.CascadeType;
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
@Table(name = "estados")
@NamedQueries({
    @NamedQuery(name = "Estados.findAll", query = "SELECT e FROM Estados e"),
    @NamedQuery(name = "Estados.findByPaisCve", query = "SELECT e FROM Estados e WHERE e.estadosPK.pais.paisCve = :paisCve"),
    @NamedQuery(name = "Estados.findByEstadoCve", query = "SELECT e FROM Estados e WHERE e.estadosPK.estadoCve = :estadoCve"),
    @NamedQuery(name = "Estados.findByEstadoDsCorta", query = "SELECT e FROM Estados e WHERE e.estadoDsCorta = :estadoDsCorta"),
    @NamedQuery(name = "Estados.findByEstadoDesc", query = "SELECT e FROM Estados e WHERE e.estadoDesc = :estadoDesc"),
    @NamedQuery(name = "Estados.findByCriterios", query = "SELECT e FROM Estados e WHERE e.estadosPK.pais.paisCve = :paisCve AND e.estadosPK.estadoCve = :estadoCve")
})
public class Estados implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected EstadosPK estadosPK;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "estado_ds_corta")
    private String estadoDsCorta;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado_desc")
    private String estadoDesc;
    
    @OneToMany(mappedBy = "municipiosPK.estados")
    private List<Municipios> municipiosList;

    public Estados() {
    }

    public Estados(EstadosPK estadosPK) {
        this.estadosPK = estadosPK;
    }
    
    public Estados(Paises pais, int estadoCve) {
        this.estadosPK = new EstadosPK(pais, estadoCve);
    }

    public Estados(Paises pais, int estadoCve, String estadoDesc) {
        this.estadosPK = new EstadosPK(pais, estadoCve);
        this.estadoDesc = estadoDesc;
    }

    public EstadosPK getEstadosPK() {
        return estadosPK;
    }

    public void setEstadosPK(EstadosPK estadosPK) {
        this.estadosPK = estadosPK;
    }

    public String getEstadoDsCorta() {
        return estadoDsCorta;
    }

    public void setEstadoDsCorta(String estadoDsCorta) {
        this.estadoDsCorta = estadoDsCorta;
    }

    public String getEstadoDesc() {
        return estadoDesc;
    }

    public void setEstadoDesc(String estadoDesc) {
        this.estadoDesc = estadoDesc;
    }

    public List<Municipios> getMunicipiosList() {
        return municipiosList;
    }

    public void setMunicipiosList(List<Municipios> municipiosList) {
        this.municipiosList = municipiosList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.estadosPK.getEstadoCve());
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
        final Estados other = (Estados) obj;
        return Objects.equals(this.estadosPK.getEstadoCve(), other.estadosPK.getEstadoCve());
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Estados[ estadosPK=" + estadosPK.getEstadoCve() + ", estadoDesc=" + estadoDesc + " ]";
    }
    
}
