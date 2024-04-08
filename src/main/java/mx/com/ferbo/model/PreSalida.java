/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "pre_salida")
@NamedQueries({ @NamedQuery(name = "PreSalida.findAll", query = "SELECT p FROM PreSalida p"),
		@NamedQuery(name = "PreSalida.findByIdPreSalida", query = "SELECT p FROM PreSalida p WHERE p.idPreSalida = :idPreSalida"),
		@NamedQuery(name = "PreSalida.findByCdFolioSalida", query = "SELECT p FROM PreSalida p WHERE p.cdFolioSalida = :cdFolioSalida"),
		@NamedQuery(name = "PreSalida.findByStEstado", query = "SELECT p FROM PreSalida p WHERE p.stEstado = :stEstado"),
		@NamedQuery(name = "PreSalida.findByFhSalida", query = "SELECT p FROM PreSalida p WHERE p.fhSalida = :fhSalida"),
		@NamedQuery(name = "PreSalida.findByTmSalida", query = "SELECT p FROM PreSalida p WHERE p.tmSalida = :tmSalida"),
		@NamedQuery(name = "PreSalida.findByNbPlacaTte", query = "SELECT p FROM PreSalida p WHERE p.nbPlacaTte = :nbPlacaTte"),
		@NamedQuery(name = "PreSalida.findByNbOperadorTte", query = "SELECT p FROM PreSalida p WHERE p.nbOperadorTte = :nbOperadorTte"),
		@NamedQuery(name = "PreSalida.findByPartidaCve", query = "SELECT p FROM PreSalida p WHERE p.partidaCve = :partidaCve"),
		@NamedQuery(name = "PreSalida.findByFolio", query = "SELECT p FROM PreSalida p WHERE p.folio = :folio"),
		@NamedQuery(name = "PreSalida.findByNuCantidad", query = "SELECT p FROM PreSalida p WHERE p.nuCantidad = :nuCantidad") })
public class PreSalida implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_pre_salida")
	private Integer idPreSalida;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "cd_folio_salida")
	private String cdFolioSalida;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "st_estado")
	private String stEstado;
	@Basic(optional = false)
	@NotNull
	@Column(name = "fh_salida")
	@Temporal(TemporalType.DATE)
	private Date fhSalida;
	@Basic(optional = false)
	@NotNull
	@Column(name = "tm_salida")
	@Temporal(TemporalType.TIME)
	private Date tmSalida;
	@Size(max = 8)
	@Column(name = "nb_placa_tte")
	private String nbPlacaTte;
	@Size(max = 100)
	@Column(name = "nb_operador_tte")
	private String nbOperadorTte;
	@Basic(optional = false)
	@NotNull
	@Column(name = "partida_cve")
	private int partidaCve;
	@Basic(optional = false)
	@NotNull
	@Column(name = "folio")
	private int folio;
	@Basic(optional = false)
	@NotNull
	@Column(name = "nu_cantidad")
	private int nuCantidad;

	public PreSalida() {
	}

	public PreSalida(Integer idPreSalida) {
		this.idPreSalida = idPreSalida;
	}

	public PreSalida(Integer idPreSalida, String cdFolioSalida, String stEstado, Date fhSalida, Date tmSalida,
			int partidaCve, int folio, int nuCantidad) {
		this.idPreSalida = idPreSalida;
		this.cdFolioSalida = cdFolioSalida;
		this.stEstado = stEstado;
		this.fhSalida = fhSalida;
		this.tmSalida = tmSalida;
		this.partidaCve = partidaCve;
		this.folio = folio;
		this.nuCantidad = nuCantidad;
	}

	public Integer getIdPreSalida() {
		return idPreSalida;
	}

	public void setIdPreSalida(Integer idPreSalida) {
		this.idPreSalida = idPreSalida;
	}

	public String getCdFolioSalida() {
		return cdFolioSalida;
	}

	public void setCdFolioSalida(String cdFolioSalida) {
		this.cdFolioSalida = cdFolioSalida;
	}

	public String getStEstado() {
		return stEstado;
	}

	public void setStEstado(String stEstado) {
		this.stEstado = stEstado;
	}

	public Date getFhSalida() {
		return fhSalida;
	}

	public void setFhSalida(Date fhSalida) {
		this.fhSalida = fhSalida;
	}

	public Date getTmSalida() {
		return tmSalida;
	}

	public void setTmSalida(Date tmSalida) {
		this.tmSalida = tmSalida;
	}

	public String getNbPlacaTte() {
		return nbPlacaTte;
	}

	public void setNbPlacaTte(String nbPlacaTte) {
		this.nbPlacaTte = nbPlacaTte;
	}

	public String getNbOperadorTte() {
		return nbOperadorTte;
	}

	public void setNbOperadorTte(String nbOperadorTte) {
		this.nbOperadorTte = nbOperadorTte;
	}

	public int getPartidaCve() {
		return partidaCve;
	}

	public void setPartidaCve(int partidaCve) {
		this.partidaCve = partidaCve;
	}

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio = folio;
	}

	public int getNuCantidad() {
		return nuCantidad;
	}

	public void setNuCantidad(int nuCantidad) {
		this.nuCantidad = nuCantidad;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPreSalida != null ? idPreSalida.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PreSalida)) {
			return false;
		}
		PreSalida other = (PreSalida) object;
		if ((this.idPreSalida == null && other.idPreSalida != null)
				|| (this.idPreSalida != null && !this.idPreSalida.equals(other.idPreSalida))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.PreSalida[ idPreSalida=" + idPreSalida + " ]";
	}

}
