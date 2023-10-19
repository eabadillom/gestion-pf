package mx.com.ferbo.ui;

import java.math.BigDecimal;

import mx.com.ferbo.model.Pago;

public class PagoUI {
	
	private BigDecimal saldo = null;
	private Pago pago = null;
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public Pago getPago() {
		return pago;
	}
	public void setPago(Pago pago) {
		this.pago = pago;
	}
}
