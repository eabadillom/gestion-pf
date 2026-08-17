package mx.com.ferbo.ui;

import java.math.BigDecimal;

public class DatosPago 
{
    BigDecimal saldoAnterior;
    BigDecimal monto;
    BigDecimal saldoRestante;

    public DatosPago() {
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(BigDecimal saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    @Override
    public String toString() {
        return "DatosPago{" + "saldoAnterior=" + saldoAnterior + ", monto=" + monto + ", saldoRestante=" + saldoRestante + '}';
    }
    
}
