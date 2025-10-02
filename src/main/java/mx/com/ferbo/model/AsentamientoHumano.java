/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "asentamiento_humano")
@NamedQueries({
    @NamedQuery(name = "AsentamientoHumano.findAll", query = "SELECT a FROM AsentamientoHumano a"),
    @NamedQuery(name = "AsentamientoHumano.findByPaisCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve"),
    @NamedQuery(name = "AsentamientoHumano.findByEstadoCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve"),
    @NamedQuery(name = "AsentamientoHumano.findByMunicipioCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve"),
    @NamedQuery(name = "AsentamientoHumano.findByCiudadCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudades.ciudadesPK.ciudadCve = :ciudadCve"),
    @NamedQuery(name = "AsentamientoHumano.findByTipoasntmntoCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.tipoAsentamiento.tipoasntmntoCve = :tipoasntmntoCve"),
    @NamedQuery(name = "AsentamientoHumano.findByEntidadpostalCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.entidadPostal.entidadpostalCve = :entidadpostalCve"),
    @NamedQuery(name = "AsentamientoHumano.findByAsentamientoCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.asentamientoCve = :asentamientoCve"),
    @NamedQuery(name = "AsentamientoHumano.findByAsentamientoDs", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoDs = :asentamientoDs"),
    @NamedQuery(name = "AsentamientoHumano.findByCp", query = "SELECT a FROM AsentamientoHumano a WHERE a.cp = :cp"),
    @NamedQuery(name = "AsentamientoHumano.findByDomicilio", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.ciudadCve = :ciudadCve and a.asentamientoHumanoPK.asentamientoCve = :asentamientoCve"),
    @NamedQuery(name = "AsentamientoHumano.findByDomicilioCompleto", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.ciudadCve = :ciudadCve and a.tipoAsentamiento.tipoasntmntoCve = :tipoasntmntoCve and a.entidadPostal.entidadpostalCve = :entidadpostalCve"),
    @NamedQuery(name = "AsentamientoHumano.findAsentamiento", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.ciudadCve = :ciudadCve and a.asentamientoHumanoPK.asentamientoCve =:asentamientoCve"),
    @NamedQuery(name = "AsentamientoHumano.findByPaisEstadoMunicipioCiudad", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve and a.asentamientoHumanoPK.ciudades.ciudadesPK.ciudadCve = :ciudadCve")
})
public class AsentamientoHumano implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected AsentamientoHumanoPK asentamientoHumanoPK;
    
    @Size(max = 150)
    @Column(name = "asentamiento_ds")
    private String asentamientoDs;
    
    @Size(max = 5)
    @Column(name = "cp")
    private String cp;
    
    @JoinColumn(name = "entidadpostal_cve", referencedColumnName = "entidadpostal_cve")
    @ManyToOne(optional = false)
    private EntidadPostal entidadPostal;
    
    @JoinColumn(name = "tipoasntmnto_cve", referencedColumnName = "tipoasntmnto_cve")
    @ManyToOne(optional = false)
    private TipoAsentamiento tipoAsentamiento;

    public AsentamientoHumano() {
    }

    public AsentamientoHumano(AsentamientoHumanoPK asentamientoHumanoPK) {
        this.asentamientoHumanoPK = asentamientoHumanoPK;
    }

    public AsentamientoHumano(Ciudades ciudades, int asentamientoCve) {
        this.asentamientoHumanoPK = new AsentamientoHumanoPK(ciudades, asentamientoCve);
    }
    
    public AsentamientoHumano(Ciudades ciudades, int asentamientoCve, String asentamientoDs){
        this.asentamientoHumanoPK = new AsentamientoHumanoPK(ciudades, asentamientoCve);
        this.asentamientoDs = asentamientoDs;
    }

    public AsentamientoHumanoPK getAsentamientoHumanoPK() {
        return asentamientoHumanoPK;
    }

    public void setAsentamientoHumanoPK(AsentamientoHumanoPK asentamientoHumanoPK) {
        this.asentamientoHumanoPK = asentamientoHumanoPK;
    }

    public String getAsentamientoDs() {
        return asentamientoDs;
    }

    public void setAsentamientoDs(String asentamientoDs) {
        this.asentamientoDs = asentamientoDs;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public EntidadPostal getEntidadPostal() {
        return entidadPostal;
    }

    public void setEntidadPostal(EntidadPostal entidadPostal) {
        this.entidadPostal = entidadPostal;
    }

    public TipoAsentamiento getTipoAsentamiento() {
        return tipoAsentamiento;
    }

    public void setTipoAsentamiento(TipoAsentamiento tipoAsentamiento) {
        this.tipoAsentamiento = tipoAsentamiento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.asentamientoHumanoPK.getAsentamientoCve());
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
        final AsentamientoHumano other = (AsentamientoHumano) obj;
        return Objects.equals(this.asentamientoHumanoPK.getAsentamientoCve(), other.asentamientoHumanoPK.getAsentamientoCve());
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.AsentamientoHumano[ asentamientoHumanoPK=" + asentamientoHumanoPK.getAsentamientoCve() + ", asentamientoDs=" + asentamientoDs + " ]";
    }
    
}
