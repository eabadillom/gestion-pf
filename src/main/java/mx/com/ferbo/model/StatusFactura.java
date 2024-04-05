/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "status_factura")
@NamedQueries({ @NamedQuery(name = "StatusFactura.findAll", query = "SELECT s FROM StatusFactura s"),
		@NamedQuery(name = "StatusFactura.findById", query = "SELECT s FROM StatusFactura s WHERE s.id = :id"),
		@NamedQuery(name = "StatusFactura.findByNombre", query = "SELECT s FROM StatusFactura s WHERE s.nombre = :nombre"),
		@NamedQuery(name = "StatusFactura.findByDescripcion", query = "SELECT s FROM StatusFactura s WHERE s.descripcion = :descripcion") })
public class StatusFactura implements Serializable {

	public static final int STATUS_ERROR = 0;
	public static final int STATUS_POR_COBRAR = 1;
	public static final int STATUS_CANCELADA = 2;
	public static final int STATUS_PAGADA = 3;
	public static final int STATUS_PAGO_PARCIAL = 4;

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
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

	public StatusFactura() {
	}

	public StatusFactura(Integer id) {
		this.id = id;
	}

	public StatusFactura(Integer id, String nombre) {
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof StatusFactura)) {
			return false;
		}
		StatusFactura other = (StatusFactura) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.StatusFactura[ id=" + id + " ]";
	}

}
