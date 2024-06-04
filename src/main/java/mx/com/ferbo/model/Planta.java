/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "planta")
@NamedQueries({ @NamedQuery(name = "Planta.findAll", query = "SELECT p FROM Planta p"),
		@NamedQuery(name = "Planta.findByPlantaCve", query = "SELECT p FROM Planta p WHERE p.plantaCve = :plantaCve"),
		@NamedQuery(name = "Planta.findByPlantaDs", query = "SELECT p FROM Planta p WHERE p.plantaDs = :plantaDs"),
		@NamedQuery(name = "Planta.findByPlantaAbrev", query = "SELECT p FROM Planta p WHERE p.plantaAbrev = :plantaAbrev"),
		@NamedQuery(name = "Planta.findByPlantaSufijo", query = "SELECT p FROM Planta p WHERE p.plantaSufijo = :plantaSufijo"),
		@NamedQuery(name = "Planta.findByPlantaCod", query = "SELECT p FROM Planta p WHERE p.plantaCod = :plantaCod") })
public class Planta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "PLANTA_CVE")
	private Integer plantaCve;
	
	@Size(max = 80)
	@Column(name = "PLANTA_DS")
	private String plantaDs;
	
	@Size(max = 6)
	@Column(name = "planta_abrev")
	private String plantaAbrev;
	
	@Size(max = 6)
	@Column(name = "planta_sufijo")
	private String plantaSufijo;
	
	@Size(max = 10)
	@Column(name = "PLANTA_COD")
	private String plantaCod;
	
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	@ManyToOne
	private Usuario idUsuario;
	
	@OneToMany(mappedBy = "plantaCve", fetch = FetchType.LAZY)
	private List<Camara> camaraList;
	
	@OneToMany(mappedBy = "serieConstanciaPK.planta", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<SerieConstancia> serieConstanciaList;
	
	@OneToMany(mappedBy = "plantaCve", fetch = FetchType.LAZY)
	private List<Aviso> avisoList;
	
	@OneToMany(mappedBy = "planta", fetch = FetchType.LAZY)
    private List<Factura> facturaList;
	
	@Column(name = " id_pais")
	private Integer idPais;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column (name = "id_estado")
	private Integer idEstado;
	
	@Column(name ="id_municipio")
	private Integer idMunicipio;
	
	@Column(name ="id_ciudad")
	private Integer idCiudad;
	
	@Column(name ="id_asentamiento")
	private Integer idAsentamiento;
	
	@Column(name ="tp_asentamiento")
	private Integer tipoasentamiento;
	
	@Column(name ="nb_cp")
	private String codigopostal;
	
	@Size (max = 20)
	@Column(name ="nb_calle")
	private String calle;
	
	@Column(name ="nu_exterior")
	private String numexterior;
	
	@Column(name ="nu_interior")
	private String numinterior;
	
	@JoinColumn(name = "cd_emisor", referencedColumnName = "cd_emisor")
	@ManyToOne(optional = false)
	private EmisoresCFDIS idEmisoresCFDIS;
	
	@JoinColumn(name = "id_sf", referencedColumnName = "id")
	@ManyToOne(optional = true)
	private SerieFactura serieFacturaDefault;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "planta", fetch = FetchType.LAZY)
	private List<Posicion> posicionList;
	
	public Planta() {
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getIdCiudad() {
		return idCiudad;
	} 

	public void setIdCiudad(Integer idCiudad) {
		this.idCiudad = idCiudad;
	}

	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public Integer getIdPais() {
		return idPais;
	}

	public void setIdPais(Integer idPais) {
		this.idPais = idPais;
	}

	public Integer getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public String getNumexterior() {
		return numexterior;
	}

	public void setNumexterior(String numexterior) {
		this.numexterior = numexterior;
	}

	public String getNuminterior() {
		return numinterior;
	}

	public void setNuminterior(String numinterior) {
		this.numinterior = numinterior;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getCodigopostal() {
		return codigopostal;
	}

	public void setCodigopostal(String codigopostal) {
		this.codigopostal = codigopostal;
	}

	public Integer getIdAsentamiento() {
		return idAsentamiento;
	}

	public void setIdAsentamiento(Integer idAsentamiento) {
		this.idAsentamiento = idAsentamiento;
	}

	public Integer getTipoasentamiento() {
		return tipoasentamiento;
	}

	public void setTipoasentamiento(Integer tipoasentamiento) {
		this.tipoasentamiento = tipoasentamiento;
	}

	public Planta(Integer plantaCve) {
		this.plantaCve = plantaCve;
	}

	public Integer getPlantaCve() {
		return plantaCve;
	}

	public void setPlantaCve(Integer plantaCve) {
		this.plantaCve = plantaCve;
	}

	public String getPlantaDs() {
		return plantaDs;
	}

	public void setPlantaDs(String plantaDs) {
		this.plantaDs = plantaDs;
	}

	public String getPlantaAbrev() {
		return plantaAbrev;
	}

	public void setPlantaAbrev(String plantaAbrev) {
		this.plantaAbrev = plantaAbrev;
	}

	public String getPlantaSufijo() {
		return plantaSufijo;
	}

	public void setPlantaSufijo(String plantaSufijo) {
		this.plantaSufijo = plantaSufijo;
	}

	public String getPlantaCod() {
		return plantaCod;
	}

	public void setPlantaCod(String plantaCod) {
		this.plantaCod = plantaCod;
	}

	
	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public List<Camara> getCamaraList() {
		return camaraList;
	}

	public void setCamaraList(List<Camara> camaraList) {
		this.camaraList = camaraList;
	}

	public List<Aviso> getAvisoList() {
		return avisoList;
	}

	public void setAvisoList(List<Aviso> avisoList) {
		this.avisoList = avisoList;
	}
	
	public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }	

	public EmisoresCFDIS getIdEmisoresCFDIS() {
		return idEmisoresCFDIS;
	}

	public void setIdEmisoresCFDIS(EmisoresCFDIS idEmisoresCFDIS) {
		this.idEmisoresCFDIS = idEmisoresCFDIS;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (plantaCve != null ? plantaCve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Planta)) {
			return false;
		}
		Planta other = (Planta) object;
		if ((this.plantaCve == null && other.plantaCve != null)
				|| (this.plantaCve != null && !this.plantaCve.equals(other.plantaCve))) {
			return false;
		}
		return true;
	}
	

	@Override
	public String toString() {
		return "mx.com.ferbo.model.Planta[ plantaCve=" + plantaCve + " ]";
	}
	
	public List<Posicion> getPosicionList() {
		return posicionList;
	}

	public void setPosicionList(List<Posicion> posicionList) {
		this.posicionList = posicionList;
	}

	public List<SerieConstancia> getSerieConstanciaList() {
		return serieConstanciaList;
	}

	public void setSerieConstanciaList(List<SerieConstancia> serieConstanciaList) {
		this.serieConstanciaList = serieConstanciaList;
	}
	
	public void add(SerieConstancia serieConstancia) {
		if(this.serieConstanciaList == null)
			this.serieConstanciaList = new ArrayList<SerieConstancia>();
		
		if(serieConstancia.getSerieConstanciaPK() == null)
			serieConstancia.setSerieConstanciaPK(new SerieConstanciaPK());
		
		serieConstancia.getSerieConstanciaPK().setPlanta(this);
		this.serieConstanciaList.add(serieConstancia);
	}

	public SerieFactura getSerieFacturaDefault() {
		return serieFacturaDefault;
	}

	public void setSerieFacturaDefault(SerieFactura serieFacturaDefault) {
		this.serieFacturaDefault = serieFacturaDefault;
	}

}
