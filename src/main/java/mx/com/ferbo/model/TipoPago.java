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
@Table(name = "tipo_pago")
@NamedQueries({ @NamedQuery(name = "TipoPago.findAll", query = "SELECT t FROM TipoPago t"),
		@NamedQuery(name = "TipoPago.findById", query = "SELECT t FROM TipoPago t WHERE t.id = :id"),
		@NamedQuery(name = "TipoPago.findByNombre", query = "SELECT t FROM TipoPago t WHERE t.nombre = :nombre"),
		@NamedQuery(name = "TipoPago.findByDescripcion", query = "SELECT t FROM TipoPago t WHERE t.descripcion = :descripcion") })
public class TipoPago implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Integer TIPO_PAGO_CHEQUE = 1;
	public static final Integer TIPO_PAGO_FICHA_DE_DEPOSITO = 2;
	public static final Integer TIPO_PAGO_CHEQUE_DE_CAJA = 3;
	public static final Integer TIPO_PAGO_DOCUMENTO_DE_TRANSFERENCIA = 4;
	public static final Integer TIPO_PAGO_NOTA_CREDITO = 5;
	public static final Integer TIPO_PAGO_EFECTIVO = 6;
	public static final Integer TIPO_PAGO_DOLARES = 7;

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

	public TipoPago() {
	}

	public TipoPago(Integer id) {
		this.id = id;
	}

	public TipoPago(Integer id, String nombre) {
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
		if (!(object instanceof TipoPago)) {
			return false;
		}
		TipoPago other = (TipoPago) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.TipoPago[ id=" + id + " ]";
	}

}
