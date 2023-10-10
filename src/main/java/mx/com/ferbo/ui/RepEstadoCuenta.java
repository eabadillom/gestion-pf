package mx.com.ferbo.ui;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RepEstadoCuenta {
		@Id
		private Date fecha;
		private String ventas;
		private String pagos;
		private String saldoInicial;
		private String emisor;
		
		public RepEstadoCuenta() {
		
		}

		public Date getFecha() {
			return fecha;
		}

		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}

		public String getVentas() {
			return ventas;
		}

		public void setVentas(String ventas) {
			this.ventas = ventas;
		}

		public String getPagos() {
			return pagos;
		}

		public void setPagos(String pagos) {
			this.pagos = pagos;
		}

		public String getSaldoInicial() {
			return saldoInicial;
		}

		public void setSaldoInicial(String saldoInicial) {
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
