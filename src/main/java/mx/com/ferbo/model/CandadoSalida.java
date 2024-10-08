/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "candado_salida")
@NamedQueries({ @NamedQuery(name = "CandadoSalida.findAll", query = "SELECT c FROM CandadoSalida c ORDER BY c.cliente.nombre ASC"),
		@NamedQuery(name = "CandadoSalida.findById", query = "SELECT c FROM CandadoSalida c WHERE c.id = :id"),
		@NamedQuery(name = "CandadoSalida.findByHabilitado", query = "SELECT c FROM CandadoSalida c WHERE c.habilitado = :habilitado"),
		@NamedQuery(name = "CandadoSalida.findByNumSalidas", query = "SELECT c FROM CandadoSalida c WHERE c.numSalidas = :numSalidas"),
		@NamedQuery(name = "CandadoSalida.findByCliente", query = "SELECT c FROM CandadoSalida c WHERE c.cliente.cteCve = :idCliente")
})
public class CandadoSalida implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	
	/**Habilita la salida de mercancía, bajo la condición de que el cliente no tenga saldo vencido.
	 * 
	 */
	@Basic(optional = false)
	@NotNull
	@Column(name = "habilitado")
	private boolean habilitado;
	
	/**Indica el número de salidas que tiene permitido el cliente, cuando se autorice con un saldo vencido.
	 * 
	 */
	@Basic(optional = false)
	@NotNull
	@Column(name = "num_salidas")
	private int numSalidas;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "salida_total")
	private boolean salidaTotal;
	
	public boolean isSalidaTotal() {
		return salidaTotal;
	}

	public void setSalidaTotal(boolean salidaTotal) {
		this.salidaTotal = salidaTotal;
	}

	@JoinColumn(name = "cte_cve", referencedColumnName = "CTE_CVE")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Cliente cliente;

	public CandadoSalida() {
	}

	public CandadoSalida(Integer id) {
		this.id = id;
	}

	public CandadoSalida(Integer id, boolean habilitado, int numSalidas) {
		this.id = id;
		this.habilitado = habilitado;
		this.numSalidas = numSalidas;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public int getNumSalidas() {
		return numSalidas;
	}

	public void setNumSalidas(int numSalidas) {
		this.numSalidas = numSalidas;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CandadoSalida)) {
			return false;
		}
		CandadoSalida other = (CandadoSalida) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.CandadoSalida[ id=" + id + " cliente: " + cliente + " ]";
	}

}
