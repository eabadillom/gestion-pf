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
import javax.persistence.Entity;
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

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "PLANTA")
@NamedQueries({ @NamedQuery(name = "Planta.findAll", query = "SELECT p FROM Planta p"),
		@NamedQuery(name = "Planta.findByPlantaCve", query = "SELECT p FROM Planta p WHERE p.plantaCve = :plantaCve"),
		@NamedQuery(name = "Planta.findByPlantaDs", query = "SELECT p FROM Planta p WHERE p.plantaDs = :plantaDs"),
		@NamedQuery(name = "Planta.findByPlantaAbrev", query = "SELECT p FROM Planta p WHERE p.plantaAbrev = :plantaAbrev"),
		@NamedQuery(name = "Planta.findByPlantaSufijo", query = "SELECT p FROM Planta p WHERE p.plantaSufijo = :plantaSufijo"),
		@NamedQuery(name = "Planta.findByPlantaCod", query = "SELECT p FROM Planta p WHERE p.plantaCod = :plantaCod") })
public class Planta implements Serializable {

	private static final long serialVersionUID = 1L;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "planta")
	private List<Posicion> posicionList;

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
	@OneToMany(mappedBy = "plantaCve")
	private List<Camara> camaraList;
	@OneToMany(mappedBy = "plantaCve")
	private List<Aviso> avisoList;
	@OneToMany(mappedBy = "planta")
    private List<Factura> facturaList;

	public Planta() {

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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (plantaCve != null ? plantaCve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
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

}
