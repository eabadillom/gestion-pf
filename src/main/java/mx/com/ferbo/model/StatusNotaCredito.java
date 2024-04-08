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
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "status_nota_credito")
@NamedQueries({ @NamedQuery(name = "StatusNotaCredito.findAll", query = "SELECT s FROM StatusNotaCredito s"),
		@NamedQuery(name = "StatusNotaCredito.findById", query = "SELECT s FROM StatusNotaCredito s WHERE s.id = :id"),
		@NamedQuery(name = "StatusNotaCredito.findByDescripcion", query = "SELECT s FROM StatusNotaCredito s WHERE s.descripcion = :descripcion") })
public class StatusNotaCredito implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final Integer STATUS_NOTA_CREDITO_ERROR = 0;
	public static final Integer STATUS_NOTA_CREDITO_NUEVA = 1;
	public static final Integer STATUS_NOTA_CREDITO_CANCELADA = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ID")
	private Integer id;

	@Size(max = 50)
	@Column(name = "DESCRIPCION")
	private String descripcion;

	public StatusNotaCredito() {
	}

	public StatusNotaCredito(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		if (!(object instanceof StatusNotaCredito)) {
			return false;
		}
		StatusNotaCredito other = (StatusNotaCredito) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.StatusNotaCredito[ id=" + id + " ]";
	}

}
