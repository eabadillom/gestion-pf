/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
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
@Table(name = "ciudades")
@NamedQueries({ @NamedQuery(name = "Ciudades.findAll", query = "SELECT c FROM Ciudades c"),
		@NamedQuery(name = "Ciudades.findByPaisCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.paisCve = :paisCve"),
		@NamedQuery(name = "Ciudades.findByEstadoCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.estadoCve = :estadoCve"),
		@NamedQuery(name = "Ciudades.findByMunicipioCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipioCve = :municipioCve"),
		@NamedQuery(name = "Ciudades.findByCiudadCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.ciudadCve = :ciudadCve"),
		@NamedQuery(name = "Ciudades.findByCiudadDs", query = "SELECT c FROM Ciudades c WHERE c.ciudadDs = :ciudadDs"),
		@NamedQuery(name = "Ciudades.findByPaisCveEstadoCveMunicipioCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.paisCve = :paisCve AND c.ciudadesPK.estadoCve = :estadoCve AND c.ciudadesPK.municipioCve = :municipioCve "),
		@NamedQuery(name = "Ciudades.findByTodo", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipioCve = :municipioCve AND c.ciudadesPK.estadoCve = :estadoCve AND c.ciudadesPK.ciudadCve = :ciudadCve"),
		@NamedQuery(name = "Ciudades.findByEstadoMunicipioCve", query = "SELECT c FROM Ciudades c WHERE c.ciudadesPK.municipioCve = :municipioCve AND c.ciudadesPK.estadoCve = :estadoCve") })

public class Ciudades implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected CiudadesPK ciudadesPK;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "ciudad_ds")
	private String ciudadDs;
	@OneToMany(mappedBy = "ciudades")
	private List<Domicilios> domiciliosList;
	@JoinColumns({
			@JoinColumn(name = "pais_cve", referencedColumnName = "pais_cve", insertable = false, updatable = false),
			@JoinColumn(name = "estado_cve", referencedColumnName = "estado_cve", insertable = false, updatable = false),
			@JoinColumn(name = "municipio_cve", referencedColumnName = "municipio_cve", insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private Municipios municipios;

	public Ciudades() {
	}

	public Ciudades(CiudadesPK ciudadesPK) {
		this.ciudadesPK = ciudadesPK;
	}

	public Ciudades(CiudadesPK ciudadesPK, String ciudadDs) {
		this.ciudadesPK = ciudadesPK;
		this.ciudadDs = ciudadDs;
	}

	public Ciudades(int paisCve, int estadoCve, int municipioCve, int ciudadCve) {
		this.ciudadesPK = new CiudadesPK(paisCve, estadoCve, municipioCve, ciudadCve);
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

	public List<Domicilios> getDomiciliosList() {
		return domiciliosList;
	}

	public void setDomiciliosList(List<Domicilios> domiciliosList) {
		this.domiciliosList = domiciliosList;
	}

	public Municipios getMunicipios() {
		return municipios;
	}

	public void setMunicipios(Municipios municipios) {
		this.municipios = municipios;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ciudadesPK != null ? ciudadesPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Ciudades)) {
			return false;
		}
		Ciudades other = (Ciudades) object;
		if ((this.ciudadesPK == null && other.ciudadesPK != null)
				|| (this.ciudadesPK != null && !this.ciudadesPK.equals(other.ciudadesPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.Ciudades[ ciudadesPK=" + ciudadesPK + " ]";
	}

}
