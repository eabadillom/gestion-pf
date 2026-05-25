package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OrdenDeSalidas implements Cloneable {
	@Id
	protected Integer id;
	protected String folioSalida;
	protected String status;
	protected Date fechaSalida;
	protected Time horaSalida;
	protected Integer partidaCve;
	protected Integer cantidad;
	protected BigDecimal peso;
	protected String codigo;
	protected String lote;
	protected Date fechaCaducidad;
	protected String sap;
	protected String pedimento;
	protected String temperatura;
	protected String unidadManejo;
	protected String codigoProducto;
	protected String nombreProducto;
	protected String nombrePlanta;
	protected String nombreCamara;
	protected Integer folioOrdenSalida;
	protected Integer productoClave;
	protected Integer unidadManejoCve;

	public OrdenDeSalidas() {
	}

	public OrdenDeSalidas(String folioSalida, String status, Date fechaSalida, Time horaSalida, Integer partidaCve,
			Integer cantidad, BigDecimal peso, String codigo, String lote, Date fechaCaducidad, String sap,
			String pedimento, String temperatura, String unidadManejo, String codigoProducto, String nombreProducto,
			String nombrePlanta, String nombreCamara, Integer folioOrdenSalida, Integer productoCve,
			Integer unidadManejoCve) {
		this.folioSalida = folioSalida;
		this.status = status;
		this.fechaSalida = fechaSalida;
		this.horaSalida = horaSalida;
		this.partidaCve = partidaCve;
		this.cantidad = cantidad;
		this.peso = peso;
		this.codigo = codigo;
		this.lote = lote;
		this.fechaCaducidad = fechaCaducidad;
		this.sap = sap;
		this.pedimento = pedimento;
		this.temperatura = temperatura;
		this.unidadManejo = unidadManejo;
		this.codigoProducto = codigoProducto;
		this.nombreProducto = nombreProducto;
		this.nombrePlanta = nombrePlanta;
		this.nombreCamara = nombreCamara;
		this.folioOrdenSalida = folioOrdenSalida;
		this.productoClave = productoCve;
		this.unidadManejoCve = unidadManejoCve;
	}

	public String getFolioSalida() {
		return folioSalida;
	}

	public void setFolioSalida(String folioSalida) {
		this.folioSalida = folioSalida;
	}

	public Time getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(Time horaSalida) {
		this.horaSalida = horaSalida;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Time getHoraSalia() {
		return horaSalida;
	}

	public void setHoraSalia(Time horaSalia) {
		this.horaSalida = horaSalia;
	}

	public Integer getPartidaCve() {
		return partidaCve;
	}

	public void setPartidaCve(Integer partidaCve) {
		this.partidaCve = partidaCve;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public String getSAP() {
		return sap;
	}

	public void setSAP(String sap) {
		this.sap = sap;
	}

	public String getPedimento() {
		return pedimento;
	}

	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public String getUnidadManejo() {
		return unidadManejo;
	}

	public void setUnidadManejo(String unidadManejo) {
		this.unidadManejo = unidadManejo;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public String getNombrePlanta() {
		return nombrePlanta;
	}

	public void setNombrePlanta(String nombrePlanta) {
		this.nombrePlanta = nombrePlanta;
	}

	public String getNombreCamara() {
		return nombreCamara;
	}

	public void setNombreCamara(String nombreCamara) {
		this.nombreCamara = nombreCamara;
	}

	public Integer getFolioOrdenSalida() {
		return folioOrdenSalida;
	}

	public void setFolioOrdenSalida(Integer folioOrdenSalida) {
		this.folioOrdenSalida = folioOrdenSalida;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSap() {
		return sap;
	}

	public void setSap(String sap) {
		this.sap = sap;
	}

	public Integer getProductoClave() {
		return productoClave;
	}

	public void setProductoClave(Integer productoClave) {
		this.productoClave = productoClave;
	}

	public Integer getUnidadManejoCve() {
		return unidadManejoCve;
	}

	public void setUnidadManejoCve(Integer unidadManejoCve) {
		this.unidadManejoCve = unidadManejoCve;
	}

	@Override
	public String toString() {
		return "OrdenDeSalidas [id=" + id + ", folioSalida=" + folioSalida + ", status=" + status + ", fechaSalida="
				+ fechaSalida + ", horaSalida=" + horaSalida + ", partidaCve=" + partidaCve + ", cantidad=" + cantidad
				+ ", peso=" + peso + ", codigo=" + codigo + ", lote=" + lote + ", fechaCaducidad=" + fechaCaducidad
				+ ", sap=" + sap + ", pedimento=" + pedimento + ", temperatura=" + temperatura + ", unidadManejo="
				+ unidadManejo + ", codigoProducto=" + codigoProducto + ", nombreProducto=" + nombreProducto
				+ ", nombrePlanta=" + nombrePlanta + ", nombreCamara=" + nombreCamara + ", folioOrdenSalida="
				+ folioOrdenSalida + ", productoClave=" + productoClave + ", unidadManejoCve=" + unidadManejoCve + "]";
	}

	@Override
	public OrdenDeSalidas clone() throws CloneNotSupportedException {
		return (OrdenDeSalidas) super.clone();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdenDeSalidas other = (OrdenDeSalidas) obj;
		return Objects.equals(id, other.id);
	}
}
