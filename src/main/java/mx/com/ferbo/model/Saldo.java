package mx.com.ferbo.model;

import java.math.BigDecimal;

public class Saldo {
	
	private Cliente cliente = null;
	private BigDecimal saldo = null;
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}
