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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "traspaso_partida")
@NamedQueries({ @NamedQuery(name = "TraspasoPartida.findAll", query = "SELECT t FROM TraspasoPartida t"),
		@NamedQuery(name = "TraspasoPartida.findById", query = "SELECT t FROM TraspasoPartida t WHERE t.id = :id"),
		@NamedQuery(name = "TraspasoPartida.findByConstancia", query = "SELECT t FROM TraspasoPartida t WHERE t.constancia = :constancia"),
		@NamedQuery(name = "TraspasoPartida.findByPartida", query = "SELECT t FROM TraspasoPartida t WHERE t.partida.partidaCve = :partidaCve"),
		@NamedQuery(name = "TraspasoPartida.findByDescripcion", query = "SELECT t FROM TraspasoPartida t WHERE t.descripcion = :descripcion"),
		@NamedQuery(name = "TraspasoPartida.findByCantidad", query = "SELECT t FROM TraspasoPartida t WHERE t.cantidad = :cantidad"),
		@NamedQuery(name = "TraspasoPartida.findByOrigen", query = "SELECT t FROM TraspasoPartida t WHERE t.origen = :origen"),
		@NamedQuery(name = "TraspasoPartida.findByTraspaso", query = "SELECT t FROM TraspasoPartida t WHERE t.traspaso.id = :traspaso"),
		@NamedQuery(name = "TraspasoPartida.findByDestino", query = "SELECT t FROM TraspasoPartida t WHERE t.destino = :destino") })
public class TraspasoPartida implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "constancia")
	private String constancia;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "descripcion")
	private String descripcion;

	@Basic(optional = false)
	@NotNull
	@Column(name = "cantidad")
	private long cantidad;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "origen")
	private String origen;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "destino")
	private String destino;

	@JoinColumn(name = "traspaso", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private ConstanciaTraspaso traspaso;

	@JoinColumn(name = "partida")
	@ManyToOne(optional = false, cascade = { CascadeType.MERGE })
	private Partida partida;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "partidatraspaso")
	private PartidasAfectadas partidasAfectadas;

	public TraspasoPartida() {
	}

	public TraspasoPartida(Integer id) {
		this.id = id;
	}

	public TraspasoPartida(Integer id, String constancia, Partida partida, String descripcion, long cantidad,
			String origen, String destino) {
		this.id = id;
		this.constancia = constancia;
		this.partida = partida;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.origen = origen;
		this.destino = destino;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConstancia() {
		return constancia;
	}

	public void setConstancia(String constancia) {
		this.constancia = constancia;
	}

	/*
	 * public int getPartida() { return partida; }
	 * 
	 * public void setPartida(int partida) { this.partida = partida; }
	 */
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public long getCantidad() {
		return cantidad;
	}

	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public ConstanciaTraspaso getTraspaso() {
		return traspaso;
	}

	public void setTraspaso(ConstanciaTraspaso traspaso) {
		this.traspaso = traspaso;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TraspasoPartida)) {
			return false;
		}
		TraspasoPartida other = (TraspasoPartida) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.TraspasoPartida[ id=" + id + " ]";
	}

	public PartidasAfectadas getPartidasAfectadas() {
		return partidasAfectadas;
	}

	public void setPartidasAfectadas(PartidasAfectadas partidasAfectadas) {
		this.partidasAfectadas = partidasAfectadas;
	}

}
