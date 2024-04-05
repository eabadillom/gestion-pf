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
@Table(name = "tipo_movimiento")
@NamedQueries({ @NamedQuery(name = "TipoMovimiento.findAll", query = "SELECT t FROM TipoMovimiento t"),
		@NamedQuery(name = "TipoMovimiento.findByClave", query = "SELECT t FROM TipoMovimiento t WHERE t.clave = :clave"),
		@NamedQuery(name = "TipoMovimiento.findByTipo", query = "SELECT t FROM TipoMovimiento t WHERE t.tipo = :tipo") })
public class TipoMovimiento implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "CLAVE")
	private Integer clave;
	@Size(max = 50)
	@Column(name = "TIPO")
	private String tipo;
	@OneToMany(mappedBy = "tipoMovCve")
	private List<DetallePartida> detallePartidaList;

	public TipoMovimiento() {
	}

	public TipoMovimiento(Integer clave) {
		this.clave = clave;
	}

	public Integer getClave() {
		return clave;
	}

	public void setClave(Integer clave) {
		this.clave = clave;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<DetallePartida> getDetallePartidaList() {
		return detallePartidaList;
	}

	public void setDetallePartidaList(List<DetallePartida> detallePartidaList) {
		this.detallePartidaList = detallePartidaList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (clave != null ? clave.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TipoMovimiento)) {
			return false;
		}
		TipoMovimiento other = (TipoMovimiento) object;
		if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.TipoMovimiento[ clave=" + clave + " ]";
	}

}
