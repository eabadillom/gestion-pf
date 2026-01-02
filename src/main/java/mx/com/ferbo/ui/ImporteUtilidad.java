package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.util.Date;

public class ImporteUtilidad 
{
    private Date fecha;
    private String emiNombre;
    private BigDecimal pagos;
    private BigDecimal egresos;
    private BigDecimal utilidadPerdida;
    private BigDecimal izq;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEmiNombre() {
        return emiNombre;
    }

    public void setEmiNombre(String emiNombre) {
        this.emiNombre = emiNombre;
    }

    public BigDecimal getPagos() {
        return pagos;
    }

    public void setPagos(BigDecimal pagos) {
        this.pagos = pagos;
    }

    public BigDecimal getEgresos() {
        return egresos;
    }

    public void setEgresos(BigDecimal egresos) {
        this.egresos = egresos;
    }

    public BigDecimal getUtilidadPerdida() {
        return utilidadPerdida;
    }

    public void setUtilidadPerdida(BigDecimal utilidadPerdida) {
        this.utilidadPerdida = utilidadPerdida;
    }

    public BigDecimal getIzq() {
        return izq;
    }

    public void setIzq(BigDecimal izq) {
        this.izq = izq;
    }

}
