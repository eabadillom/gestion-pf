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
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "tipo_cobro")
@NamedQueries({ @NamedQuery(name = "TipoCobro.findAll", query = "SELECT t FROM TipoCobro t"),
		@NamedQuery(name = "TipoCobro.findById", query = "SELECT t FROM TipoCobro t WHERE t.id = :id"),
		@NamedQuery(name = "TipoCobro.findByNombre", query = "SELECT t FROM TipoCobro t WHERE t.nombre = :nombre"),
		@NamedQuery(name = "TipoCobro.findByDescripcion", query = "SELECT t FROM TipoCobro t WHERE t.descripcion = :descripcion") })
public class TipoCobro implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 30)
	@Column(name = "nombre")
	private String nombre;
	@Size(max = 255)
	@Column(name = "descripcion")
	private String descripcion;
	@OneToMany(mappedBy = "cobro")
	private List<Servicio> servicioList;
	@OneToMany(mappedBy = "tipoCobro")
	private List<ServicioFactura> servicioFacturaList;

	public TipoCobro() {
	}

	public TipoCobro(Integer id) {
		this.id = id;
	}

	public TipoCobro(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Servicio> getServicioList() {
		return servicioList;
	}

	public void setServicioList(List<Servicio> servicioList) {
		this.servicioList = servicioList;
	}

	public List<ServicioFactura> getServicioFacturaList() {
		return servicioFacturaList;
	}

	public void setServicioFacturaList(List<ServicioFactura> servicioFacturaList) {
		this.servicioFacturaList = servicioFacturaList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TipoCobro)) {
			return false;
		}
		TipoCobro other = (TipoCobro) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.TipoCobro[ id=" + id + " ]";
	}

}
