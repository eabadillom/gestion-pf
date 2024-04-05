/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "posicion")
@NamedQueries({ @NamedQuery(name = "Posicion.findAll", query = "SELECT p FROM Posicion p"),
		@NamedQuery(name = "Posicion.findByIdPosicion", query = "SELECT p FROM Posicion p WHERE p.idPosicion = :idPosicion"),
//    @NamedQuery(name = "Posicion.findByIdPlanta", query = "SELECT p FROM Posicion p WHERE p.idPlanta = :idPlanta"),
		@NamedQuery(name = "Posicion.findByCamara", query = "SELECT p FROM Posicion p WHERE p.camara.camaraCve = :camaraCve"),
		@NamedQuery(name = "Posicion.findByPlantaCamara", query = "SELECT p FROM Posicion p WHERE p.planta.plantaCve = :plantaCve AND p.camara.camaraCve = :camaraCve"),
		@NamedQuery(name = "Posicion.findByCodPosicion", query = "SELECT p FROM Posicion p WHERE p.codPosicion = :codPosicion"),
		@NamedQuery(name = "Posicion.findByDescPosicion", query = "SELECT p FROM Posicion p WHERE p.descPosicion = :descPosicion"),
		@NamedQuery(name = "Posicion.findByTempIni", query = "SELECT p FROM Posicion p WHERE p.tempIni = :tempIni"),
		@NamedQuery(name = "Posicion.findByTempFin", query = "SELECT p FROM Posicion p WHERE p.tempFin = :tempFin"),
		@NamedQuery(name = "Posicion.findByHabilitada", query = "SELECT p FROM Posicion p WHERE p.habilitada = :habilitada") })
public class Posicion implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_posicion")
	private Integer idPosicion;

	@JoinColumn(name = "id_planta", referencedColumnName = "PLANTA_CVE")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Planta planta;

	@JoinColumn(name = "id_camara", referencedColumnName = "CAMARA_CVE")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Camara camara;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "cod_posicion")
	private String codPosicion;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 200)
	@Column(name = "desc_posicion")
	private String descPosicion;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "temp_ini")
	private BigDecimal tempIni;
	@Basic(optional = false)
	@NotNull
	@Column(name = "temp_fin")
	private BigDecimal tempFin;
	@Basic(optional = false)
	@NotNull
	@Column(name = "habilitada")
	private boolean habilitada;

	public Posicion() {
	}

	public Posicion(Integer idPosicion) {
		this.idPosicion = idPosicion;
	}

	public Posicion(Integer idPosicion, Planta planta, Camara camara, String codPosicion, String descPosicion,
			BigDecimal tempIni, BigDecimal tempFin, boolean habilitada) {
		this.idPosicion = idPosicion;
		this.planta = planta;
		this.camara = camara;
		this.codPosicion = codPosicion;
		this.descPosicion = descPosicion;
		this.tempIni = tempIni;
		this.tempFin = tempFin;
		this.habilitada = habilitada;
	}

	public Integer getIdPosicion() {
		return idPosicion;
	}

	public void setIdPosicion(Integer idPosicion) {
		this.idPosicion = idPosicion;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

	public String getCodPosicion() {
		return codPosicion;
	}

	public void setCodPosicion(String codPosicion) {
		this.codPosicion = codPosicion;
	}

	public String getDescPosicion() {
		return descPosicion;
	}

	public void setDescPosicion(String descPosicion) {
		this.descPosicion = descPosicion;
	}

	public BigDecimal getTempIni() {
		return tempIni;
	}

	public void setTempIni(BigDecimal tempIni) {
		this.tempIni = tempIni;
	}

	public BigDecimal getTempFin() {
		return tempFin;
	}

	public void setTempFin(BigDecimal tempFin) {
		this.tempFin = tempFin;
	}

	public boolean getHabilitada() {
		return habilitada;
	}

	public void setHabilitada(boolean habilitada) {
		this.habilitada = habilitada;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPosicion != null ? idPosicion.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Posicion)) {
			return false;
		}
		Posicion other = (Posicion) object;
		if ((this.idPosicion == null && other.idPosicion != null)
				|| (this.idPosicion != null && !this.idPosicion.equals(other.idPosicion))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.Posicion[ idPosicion=" + idPosicion + " ]";
	}

}
