/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "tmp_entradas_a_facturar")
@NamedQueries({ @NamedQuery(name = "TmpEntradasAFacturar.findAll", query = "SELECT t FROM TmpEntradasAFacturar t"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByTmpCve", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.tmpCve = :tmpCve"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByUsuario", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.usuario = :usuario"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByFolio", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.folio = :folio"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByFolioCliente", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.folioCliente = :folioCliente"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByIngreso", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.ingreso = :ingreso"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByVencimiento", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.vencimiento = :vencimiento"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByServicio", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.servicio = :servicio"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByCuota", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.cuota = :cuota"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByUnidad", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.unidad = :unidad"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByCantidad", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.cantidad = :cantidad"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByImporte", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.importe = :importe"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByIva", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.iva = :iva"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByTotal", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.total = :total"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByServicioCve", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.servicioCve = :servicioCve"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByProductoCve", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.productoCve = :productoCve"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByProducto", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.producto = :producto"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByCantidadSer", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.cantidadSer = :cantidadSer"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByPeso", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.peso = :peso"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByUnidadCobro", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.unidadCobro = :unidadCobro"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByValor", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.valor = :valor"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByPartidaCve", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.partidaCve = :partidaCve"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByPlazo", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.plazo = :plazo"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByPlantaCve", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.plantaCve = :plantaCve"),
		@NamedQuery(name = "TmpEntradasAFacturar.findByCamaraCve", query = "SELECT t FROM TmpEntradasAFacturar t WHERE t.camaraCve = :camaraCve") })
public class TmpEntradasAFacturar implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "tmp_cve")
	private Integer tmpCve;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 15)
	@Column(name = "usuario")
	private String usuario;
	@Basic(optional = false)
	@NotNull
	@Column(name = "folio")
	private int folio;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 8)
	@Column(name = "folio_cliente")
	private String folioCliente;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ingreso")
	@Temporal(TemporalType.DATE)
	private Date ingreso;
	@Basic(optional = false)
	@NotNull
	@Column(name = "vencimiento")
	@Temporal(TemporalType.DATE)
	private Date vencimiento;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 80)
	@Column(name = "servicio")
	private String servicio;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "cuota")
	private BigDecimal cuota;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "unidad")
	private String unidad;
	@Basic(optional = false)
	@NotNull
	@Column(name = "cantidad")
	private BigDecimal cantidad;
	@Basic(optional = false)
	@NotNull
	@Column(name = "importe")
	private BigDecimal importe;
	@Basic(optional = false)
	@NotNull
	@Column(name = "iva")
	private BigDecimal iva;
	@Basic(optional = false)
	@NotNull
	@Column(name = "total")
	private BigDecimal total;
	@Basic(optional = false)
	@NotNull
	@Column(name = "servicio_cve")
	private int servicioCve;
	@Column(name = "producto_cve")
	private Integer productoCve;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 80)
	@Column(name = "producto")
	private String producto;
	@Basic(optional = false)
	@NotNull
	@Column(name = "cantidad_ser")
	private BigDecimal cantidadSer;
	@Basic(optional = false)
	@NotNull
	@Column(name = "peso")
	private int peso;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "unidad_cobro")
	private String unidadCobro;
	@Column(name = "valor")
	private BigDecimal valor;
	@Column(name = "partida_cve")
	private Integer partidaCve;
	@Column(name = "plazo")
	private Integer plazo;
	@Column(name = "planta_cve")
	private Integer plantaCve;
	@Column(name = "camara_cve")
	private Integer camaraCve;

	public TmpEntradasAFacturar() {
	}

	public TmpEntradasAFacturar(Integer tmpCve) {
		this.tmpCve = tmpCve;
	}

	public TmpEntradasAFacturar(Integer tmpCve, String usuario, int folio, String folioCliente, Date ingreso,
			Date vencimiento, String servicio, BigDecimal cuota, String unidad, BigDecimal cantidad, BigDecimal importe,
			BigDecimal iva, BigDecimal total, int servicioCve, String producto, BigDecimal cantidadSer, int peso,
			String unidadCobro) {
		this.tmpCve = tmpCve;
		this.usuario = usuario;
		this.folio = folio;
		this.folioCliente = folioCliente;
		this.ingreso = ingreso;
		this.vencimiento = vencimiento;
		this.servicio = servicio;
		this.cuota = cuota;
		this.unidad = unidad;
		this.cantidad = cantidad;
		this.importe = importe;
		this.iva = iva;
		this.total = total;
		this.servicioCve = servicioCve;
		this.producto = producto;
		this.cantidadSer = cantidadSer;
		this.peso = peso;
		this.unidadCobro = unidadCobro;
	}

	public Integer getTmpCve() {
		return tmpCve;
	}

	public void setTmpCve(Integer tmpCve) {
		this.tmpCve = tmpCve;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio = folio;
	}

	public String getFolioCliente() {
		return folioCliente;
	}

	public void setFolioCliente(String folioCliente) {
		this.folioCliente = folioCliente;
	}

	public Date getIngreso() {
		return ingreso;
	}

	public void setIngreso(Date ingreso) {
		this.ingreso = ingreso;
	}

	public Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public BigDecimal getCuota() {
		return cuota;
	}

	public void setCuota(BigDecimal cuota) {
		this.cuota = cuota;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public int getServicioCve() {
		return servicioCve;
	}

	public void setServicioCve(int servicioCve) {
		this.servicioCve = servicioCve;
	}

	public Integer getProductoCve() {
		return productoCve;
	}

	public void setProductoCve(Integer productoCve) {
		this.productoCve = productoCve;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public BigDecimal getCantidadSer() {
		return cantidadSer;
	}

	public void setCantidadSer(BigDecimal cantidadSer) {
		this.cantidadSer = cantidadSer;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	public String getUnidadCobro() {
		return unidadCobro;
	}

	public void setUnidadCobro(String unidadCobro) {
		this.unidadCobro = unidadCobro;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getPartidaCve() {
		return partidaCve;
	}

	public void setPartidaCve(Integer partidaCve) {
		this.partidaCve = partidaCve;
	}

	public Integer getPlazo() {
		return plazo;
	}

	public void setPlazo(Integer plazo) {
		this.plazo = plazo;
	}

	public Integer getPlantaCve() {
		return plantaCve;
	}

	public void setPlantaCve(Integer plantaCve) {
		this.plantaCve = plantaCve;
	}

	public Integer getCamaraCve() {
		return camaraCve;
	}

	public void setCamaraCve(Integer camaraCve) {
		this.camaraCve = camaraCve;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tmpCve != null ? tmpCve.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TmpEntradasAFacturar)) {
			return false;
		}
		TmpEntradasAFacturar other = (TmpEntradasAFacturar) object;
		if ((this.tmpCve == null && other.tmpCve != null)
				|| (this.tmpCve != null && !this.tmpCve.equals(other.tmpCve))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "mx.com.ferbo.model.TmpEntradasAFacturar[ tmpCve=" + tmpCve + " ]";
	}

}
