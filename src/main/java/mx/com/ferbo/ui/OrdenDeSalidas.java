package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OrdenDeSalidas {
	@Id
	private String folioSalida;
	private String status;
	private Date fechaSalida;
	private Time horaSalida;
	private Integer partidaCve;
	private Integer cantidad;
	private BigDecimal peso;
	private String codigo;
	private String lote;
	private Date fechaCaducidad;
	private String SAP;
	private String pedimento;
	private String temperatura;
	private String unidadManejo;
	private String codigoProducto;
	private String nombreProducto;
	private String nombrePlanta;
	private String nombreCamara;
	public OrdenDeSalidas() {
		
		
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
		return SAP;
	}
	public void setSAP(String sAP) {
		SAP = sAP;
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
	@Override
	public String toString() {
		return "OrdenDeSalidas [folioSalida=" + folioSalida + ", status=" + status + ", fechaSalida=" + fechaSalida
				+ ", horaSalida=" + horaSalida + ", partidaCve=" + partidaCve + ", cantidad=" + cantidad + ", peso="
				+ peso + ", codigo=" + codigo + ", lote=" + lote + ", fechaCaducidad=" + fechaCaducidad + ", SAP=" + SAP
				+ ", pedimento=" + pedimento + ", temperatura=" + temperatura + ", unidadManejo=" + unidadManejo
				+ ", codigoProducto=" + codigoProducto + ", nombreProducto=" + nombreProducto + ", nombrePlanta="
				+ nombrePlanta + ", nombreCamara=" + nombreCamara + "]";
	}

	
	
	
}
