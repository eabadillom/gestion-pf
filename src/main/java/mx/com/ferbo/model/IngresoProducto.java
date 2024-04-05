package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ingreso_producto")
@NamedQueries({ @NamedQuery(name = "IngresoProducto.findByAll", query = "SELECT ip FROM IngresoProducto ip") })

public class IngresoProducto implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_ingreso_producto")
	private Integer idIngresoProducto;

	@Column(name = "cantidad")
	private Integer cantidad;

	@JoinColumn(name = "id_unidad_medida", referencedColumnName = "UNIDAD_DE_MANEJO_CVE")
	@ManyToOne
	private UnidadDeManejo unidadDeManejo;

	@Column(name = "peso")
	private BigDecimal peso;

	@JoinColumn(name = "id_planta", referencedColumnName = "PLANTA_CVE")
	@ManyToOne(fetch = FetchType.LAZY)
	private Planta planta;

	@Column(name = "no_tarimas")
	@NotNull
	private BigDecimal noTarimas;

	@Column(name = "lote")
	@Size(max = 20)
	private String lote;

	@Column(name = "pedimento")
	@Size(max = 13)
	private String pedimento;

	@Column(name = "contenedor")
	@Size(max = 20)
	private String contenedor;

	@Column(name = "fecha_Caducidad")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCaducidad;

	@Column(name = "otro")
	@Size(max = 12)
	private String otro;

	@JoinColumn(name = "id_ingreso", referencedColumnName = "id_ingreso")
	@ManyToOne(fetch = FetchType.LAZY)
	private Ingreso ingreso;

	@JoinColumn(name = "id_producto", referencedColumnName = "PRODUCTO_CVE")
	@ManyToOne
	private Producto producto;

	public IngresoProducto() {

	}

	public IngresoProducto clone() throws CloneNotSupportedException {
		return (IngresoProducto) super.clone();
	}

	public Integer getIdIngresoProducto() {
		return idIngresoProducto;
	}

	public void setIdIngresoProducto(Integer idIngresoProducto) {
		this.idIngresoProducto = idIngresoProducto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public UnidadDeManejo getUnidadDeManejo() {
		return unidadDeManejo;
	}

	public void setUnidadDeManejo(UnidadDeManejo unidadDeManejo) {
		this.unidadDeManejo = unidadDeManejo;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public BigDecimal getNoTarimas() {
		return noTarimas;
	}

	public void setNoTarimas(BigDecimal noTarimas) {
		this.noTarimas = noTarimas;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getPedimento() {
		return pedimento;
	}

	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}

	public String getContenedor() {
		return contenedor;
	}

	public void setContenedor(String contenedor) {
		this.contenedor = contenedor;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public String getOtro() {
		return otro;
	}

	public void setOtro(String otro) {
		this.otro = otro;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idIngresoProducto != null ? idIngresoProducto.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof IngresoProducto)) {
			return false;
		}
		IngresoProducto other = (IngresoProducto) object;
		if ((this.idIngresoProducto == null && other.idIngresoProducto != null)
				|| (this.idIngresoProducto != null && !this.idIngresoProducto.equals(other.idIngresoProducto))) {
			return false;
		}
		return true;
	}

}
