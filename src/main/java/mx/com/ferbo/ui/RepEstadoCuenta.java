package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RepEstadoCuenta {
	@Id
	private Date fecha;
	private BigDecimal ventas;
	private BigDecimal pagos;
	private BigDecimal saldoInicial;
	private String emisor;

	public RepEstadoCuenta() {

	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getVentas() {
		return ventas;
	}

	public void setVentas(BigDecimal ventas) {
		this.ventas = ventas;
	}

	public BigDecimal getPagos() {
		return pagos;
	}

	public void setPagos(BigDecimal pagos) {
		this.pagos = pagos;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	@Override
	public String toString() {
		return "RepEstadoCuenta [fecha=" + fecha + ", ventas=" + ventas + ", pagos=" + pagos + ", saldoInicial="
				+ saldoInicial + ", emisor=" + emisor + "]";
	}

}
