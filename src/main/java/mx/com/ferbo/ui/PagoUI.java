package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.util.Objects;

import mx.com.ferbo.model.Pago;

public class PagoUI {

	private BigDecimal saldo = null;
	private BigDecimal saldoAnterior = null;
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

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	@Override
	public String toString() {
		return "{\"saldo\":\"" + saldo + "\", \"saldoAnterior\":\"" + saldoAnterior + "\", \"pago\":\"" + pago + "\"}";
	}

	@Override
	public int hashCode() {
		return Objects.hash(pago, saldo, saldoAnterior);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagoUI other = (PagoUI) obj;
		return Objects.equals(pago, other.pago) && Objects.equals(saldo, other.saldo)
				&& Objects.equals(saldoAnterior, other.saldoAnterior);
	}
}
