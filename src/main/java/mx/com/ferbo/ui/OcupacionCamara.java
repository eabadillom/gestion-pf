package mx.com.ferbo.ui;

import java.math.BigDecimal;

public class OcupacionCamara 
{
    private String cte_nombre;
    private String planta_ds;
    private BigDecimal tarima;//agregar posiciones que permite la camara  y posiciones disponibles

    public OcupacionCamara() {

    }

    public String getCte_nombre() {
        return cte_nombre;
    }

    public void setCte_nombre(String cte_nombre) {
        this.cte_nombre = cte_nombre;
    }

    public String getPlanta_ds() {
        return planta_ds;
    }

    public void setPlanta_ds(String planta_ds) {
        this.planta_ds = planta_ds;
    }

    public BigDecimal getTarima() {
        return tarima;
    }

    public void setTarima(BigDecimal tarima) {
        this.tarima = tarima;
    }
        
}
