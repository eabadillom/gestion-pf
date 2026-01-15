package mx.com.ferbo.ui;

import java.math.BigDecimal;

/**
 *
 * @author alberto
 */
public class PosicionesPlanta 
{
    private String planta;
    private String camara;
    private BigDecimal posiciones;

    public PosicionesPlanta() {
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public String getCamara() {
        return camara;
    }

    public void setCamara(String camara) {
        this.camara = camara;
    }

    public BigDecimal getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(BigDecimal posiciones) {
        this.posiciones = posiciones;
    }

    @Override
    public String toString() {
        return "PosicionesPlanta[" + "planta=" + planta + ", camara=" + camara + ", posiciones=" + posiciones + ']';
    }
    
}
