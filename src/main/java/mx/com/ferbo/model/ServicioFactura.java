/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "servicio_factura")
@NamedQueries({ @NamedQuery(name = "ServicioFactura.findAll", query = "SELECT s FROM ServicioFactura s"),
		@NamedQuery(name = "ServicioFactura.findById", query = "SELECT s FROM ServicioFactura s WHERE s.id = :id"),
		@NamedQuery(name = "ServicioFactura.findByDescripcion", query = "SELECT s FROM ServicioFactura s WHERE s.descripcion = :descripcion"),
		@NamedQuery(name = "ServicioFactura.findByCantidad", query = "SELECT s FROM ServicioFactura s WHERE s.cantidad = :cantidad"),
		@NamedQuery(name = "ServicioFactura.findByUnidad", query = "SELECT s FROM ServicioFactura s WHERE s.unidad = :unidad"),
		@NamedQuery(name = "ServicioFactura.findByCosto", query = "SELECT s FROM ServicioFactura s WHERE s.costo = :costo"),
		@NamedQuery(name = "ServicioFactura.findByTarifa", query = "SELECT s FROM ServicioFactura s WHERE s.tarifa = :tarifa"),
		@NamedQuery(name = "ServicioFactura.findByUdCobro", query = "SELECT s FROM ServicioFactura s WHERE s.udCobro = :udCobro"),
		@NamedQuery(name = "ServicioFactura.findByCodigo", query = "SELECT s FROM ServicioFactura s WHERE s.codigo = :codigo") })
public class ServicioFactura implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "descripcion")
	private String descripcion;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation

	@Basic(optional = false)
	@NotNull
	@Column(name = "cantidad")
	private BigDecimal cantidad;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "unidad")
	private String unidad;

	@Basic(optional = false)
	@NotNull
	@Column(name = "costo")
	private BigDecimal costo;

	@Column(name = "tarifa")
	private BigDecimal tarifa;

	@Size(max = 10)
	@Column(name = "ud_cobro")
	private String udCobro;

	@Size(max = 20)
	@Column(name = "codigo")
	private String codigo;

	@JoinColumn(name = "factura", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private Factura factura;

	@JoinColumn(name = "tipo_cobro", referencedColumnName = "id")
	@ManyToOne
	private TipoCobro tipoCobro;

	@Column(name = "cd_unidad")
	private String cdUnidad;

	public ServicioFactura() {
	}

	public ServicioFactura(Integer id) {
		this.id = id;
	}

	public ServicioFactura(Integer id, String descripcion, BigDecimal cantidad, String unidad, BigDecimal costo) {
		this.id = id;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.unidad = unidad;
		this.costo = costo;
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

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public TipoCobro getTipoCobro() {
		return tipoCobro;
	}

	public void setTipoCobro(TipoCobro tipoCobro) {
		this.tipoCobro = tipoCobro;
	}

	public BigDecimal getTarifa() {
		return tarifa;
	}

	public void setTarifa(BigDecimal tarifa) {
		this.tarifa = tarifa;
	}

	public String getUdCobro() {
		return udCobro;
	}

	public void setUdCobro(String udCobro) {
		this.udCobro = udCobro;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public String getCdUnidad() {
		return cdUnidad;
	}

	public void setCdUnidad(String cdUnidad) {
		this.cdUnidad = cdUnidad;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ServicioFactura)) {
			return false;
		}
		ServicioFactura other = (ServicioFactura) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.ServicioFactura[ id=" + id + " ]";
	}

}
