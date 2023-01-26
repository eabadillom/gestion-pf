/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
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
//    @NamedQuery(name = "AsentamientoHumano.findAllTodos", query = "SELECT p, m, c, a FROM paises p INNER JOIN estados e ON p.pais_cve = e.pais_cve "
//    															+ "INNER JOIN municipios m ON p.pais_cve = m.pais_cve AND e.estado_cve = m.estado_cve\r\n"
//    															+ "INNER JOIN ciudades c ON p.pais_cve = c.pais_cve AND e.estado_cve = c.estado_cve AND m.municipio_cve = c.municipio_cve\r\n"
//    															+ "INNER JOIN asentamiento_humano a ON p.pais_cve = a.pais_cve AND e.estado_cve = a.estado_cve AND m.municipio_cve = a.municipio_cve AND c.ciudad_cve = a.ciudad_cve"),
    @NamedQuery(name = "AsentamientoHumano.findByPaisCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.paisCve = :paisCve"),
    @NamedQuery(name = "AsentamientoHumano.findByEstadoCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.estadoCve = :estadoCve"),
    @NamedQuery(name = "AsentamientoHumano.findByMunicipioCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.municipioCve = :municipioCve"),
    @NamedQuery(name = "AsentamientoHumano.findByCiudadCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.ciudadCve = :ciudadCve"),
    @NamedQuery(name = "AsentamientoHumano.findByTipoasntmntoCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.tipoasntmntoCve = :tipoasntmntoCve"),
    @NamedQuery(name = "AsentamientoHumano.findByEntidadpostalCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.entidadpostalCve = :entidadpostalCve"),
    @NamedQuery(name = "AsentamientoHumano.findByAsentamientoCve", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.asentamientoCve = :asentamientoCve"),
    @NamedQuery(name = "AsentamientoHumano.findByAsentamientoDs", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoDs = :asentamientoDs"),
    @NamedQuery(name = "AsentamientoHumano.findByCp", query = "SELECT a FROM AsentamientoHumano a WHERE a.cp = :cp"),
	@NamedQuery(name = "AsentamientoHumano.findByDomicilio", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.paisCve = :paisCve and a.asentamientoHumanoPK.estadoCve = :estadoCve and a.asentamientoHumanoPK.municipioCve = :municipioCve and a.asentamientoHumanoPK.ciudadCve = :ciudadCve"),
	@NamedQuery(name = "AsentamientoHumano.findAsentamiento", query = "SELECT a FROM AsentamientoHumano a WHERE a.asentamientoHumanoPK.paisCve = :paisCve and a.asentamientoHumanoPK.estadoCve = :estadoCve and a.asentamientoHumanoPK.municipioCve = :municipioCve and a.asentamientoHumanoPK.ciudadCve = :ciudadCve and a.asentamientoHumanoPK.asentamientoCve =:asentamientoCve")})

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
    @JoinColumn(name = "entidadpostal_cve", referencedColumnName = "entidadpostal_cve", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EntidadPostal entidadPostal;
    @JoinColumn(name = "tipoasntmnto_cve", referencedColumnName = "tipoasntmnto_cve", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoAsentamiento tipoAsentamiento;

    public AsentamientoHumano() {
    }

    public AsentamientoHumano(AsentamientoHumanoPK asentamientoHumanoPK) {
        this.asentamientoHumanoPK = asentamientoHumanoPK;
    }

    public AsentamientoHumano(int paisCve, int estadoCve, int municipioCve, int ciudadCve, short tipoasntmntoCve, int entidadpostalCve, int asentamientoCve) {
        this.asentamientoHumanoPK = new AsentamientoHumanoPK(paisCve, estadoCve, municipioCve, ciudadCve, tipoasntmntoCve, entidadpostalCve, asentamientoCve);
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
        int hash = 0;
        hash += (asentamientoHumanoPK != null ? asentamientoHumanoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsentamientoHumano)) {
            return false;
        }
        AsentamientoHumano other = (AsentamientoHumano) object;
        if ((this.asentamientoHumanoPK == null && other.asentamientoHumanoPK != null) || (this.asentamientoHumanoPK != null && !this.asentamientoHumanoPK.equals(other.asentamientoHumanoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.AsentamientoHumano[ asentamientoHumanoPK=" + asentamientoHumanoPK + " ]";
    }
    
}
