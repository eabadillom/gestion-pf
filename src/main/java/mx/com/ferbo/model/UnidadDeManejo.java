/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "unidad_de_manejo")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "UnidadDeManejo.findAll", query = "SELECT u FROM UnidadDeManejo u"),
		@NamedQuery(name = "UnidadDeManejo.findByUnidadDeManejoCve", query = "SELECT u FROM UnidadDeManejo u WHERE u.unidadDeManejoCve = :unidadDeManejoCve"),
		@NamedQuery(name = "UnidadDeManejo.findByUnidadDeManejoDs", query = "SELECT u FROM UnidadDeManejo u WHERE u.unidadDeManejoDs = :unidadDeManejoDs") })
public class UnidadDeManejo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "UNIDAD_DE_MANEJO_CVE")
	private Integer unidadDeManejoCve;

	@Size(max = 100)
	@Column(name = "UNIDAD_DE_MANEJO_DS")
	private String unidadDeManejoDs;

	@OneToMany(mappedBy = "uMedidaCve")
	private Collection<DetallePartida> detallePartidaCollection;

	@OneToMany(mappedBy = "unidadDeCobro")
	private Collection<Partida> partidaCollection;

	@OneToMany(mappedBy = "unidadDeManejoCve")
	private Collection<PartidaServicio> partidaServicioCollection;

	@OneToMany(mappedBy = "unidadDeCobro")
	private Collection<PartidaServicio> partidaServicioCollection1;

	@OneToMany(cascade = CascadeType.DETACH, mappedBy = "unidad", fetch = FetchType.LAZY)
	private Collection<PrecioServicio> precioServicioCollection;

	@OneToMany(cascade = CascadeType.DETACH, mappedBy = "unidadDeManejoCve", fetch = FetchType.LAZY)
	private Collection<UnidadDeProducto> unidadDeProductoCollection;

	public UnidadDeManejo() {
	}

	public UnidadDeManejo(Integer unidadDeManejoCve) {
		this.unidadDeManejoCve = unidadDeManejoCve;
	}

	public Integer getUnidadDeManejoCve() {
		return unidadDeManejoCve;
	}

	public void setUnidadDeManejoCve(Integer unidadDeManejoCve) {
		this.unidadDeManejoCve = unidadDeManejoCve;
	}

	public String getUnidadDeManejoDs() {
		return unidadDeManejoDs;
	}

	public void setUnidadDeManejoDs(String unidadDeManejoDs) {
		this.unidadDeManejoDs = unidadDeManejoDs;
	}

	@XmlTransient
	public Collection<DetallePartida> getDetallePartidaCollection() {
		return detallePartidaCollection;
	}

	public void setDetallePartidaCollection(Collection<DetallePartida> detallePartidaCollection) {
		this.detallePartidaCollection = detallePartidaCollection;
	}

	@XmlTransient
	public Collection<Partida> getPartidaCollection() {
		return partidaCollection;
	}

	public void setPartidaCollection(Collection<Partida> partidaCollection) {
		this.partidaCollection = partidaCollection;
	}

	@XmlTransient
	public Collection<PartidaServicio> getPartidaServicioCollection() {
		return partidaServicioCollection;
	}

	public void setPartidaServicioCollection(Collection<PartidaServicio> partidaServicioCollection) {
		this.partidaServicioCollection = partidaServicioCollection;
	}

	@XmlTransient
	public Collection<PartidaServicio> getPartidaServicioCollection1() {
		return partidaServicioCollection1;
	}

	public void setPartidaServicioCollection1(Collection<PartidaServicio> partidaServicioCollection1) {
		this.partidaServicioCollection1 = partidaServicioCollection1;
	}

	@XmlTransient
	public Collection<PrecioServicio> getPrecioServicioCollection() {
		return precioServicioCollection;
	}

	public void setPrecioServicioCollection(Collection<PrecioServicio> precioServicioCollection) {
		this.precioServicioCollection = precioServicioCollection;
	}

	@XmlTransient
	public Collection<UnidadDeProducto> getUnidadDeProductoCollection() {
		return unidadDeProductoCollection;
	}

	public void setUnidadDeProductoCollection(Collection<UnidadDeProducto> unidadDeProductoCollection) {
		this.unidadDeProductoCollection = unidadDeProductoCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (unidadDeManejoCve != null ? unidadDeManejoCve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof UnidadDeManejo)) {
			return false;
		}
		UnidadDeManejo other = (UnidadDeManejo) object;
		if ((this.unidadDeManejoCve == null && other.unidadDeManejoCve != null)
				|| (this.unidadDeManejoCve != null && !this.unidadDeManejoCve.equals(other.unidadDeManejoCve))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.UnidadDeManejo[ unidadDeManejoCve=" + unidadDeManejoCve + " ]";
	}

}
