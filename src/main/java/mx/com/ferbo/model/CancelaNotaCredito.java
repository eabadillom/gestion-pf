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
@Table(name = "cancela_nota_credito")
@NamedQueries({ @NamedQuery(name = "CancelaNotaCredito.findAll", query = "SELECT c FROM CancelaNotaCredito c"),
		@NamedQuery(name = "CancelaNotaCredito.findById", query = "SELECT c FROM CancelaNotaCredito c WHERE c.id = :id"),
		@NamedQuery(name = "CancelaNotaCredito.findByDescripcion", query = "SELECT c FROM CancelaNotaCredito c WHERE c.descripcion = :descripcion") })
public class CancelaNotaCredito implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ID")
	private Integer id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 75)
	@Column(name = "DESCRIPCION")
	private String descripcion;
	@JoinColumn(name = "NOTA", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private NotaCredito nota;

	public CancelaNotaCredito() {
	}

	public CancelaNotaCredito(Integer id) {
		this.id = id;
	}

	public CancelaNotaCredito(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
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

	public NotaCredito getNota() {
		return nota;
	}

	public void setNota(NotaCredito nota) {
		this.nota = nota;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CancelaNotaCredito)) {
			return false;
		}
		CancelaNotaCredito other = (CancelaNotaCredito) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.CancelaNotaCredito[ id=" + id + " ]";
	}

}
