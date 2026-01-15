package mx.com.ferbo.ui;

import java.math.BigDecimal;

/**
 *
 * @author alberto
 */
public class OcupacionPlanta 
{
    private String nbTarima;
    private BigDecimal tarima;
    private String camara;
    private String planta;

    public OcupacionPlanta() {
    }

    public String getNbTarima() {
        return nbTarima;
    }

    public void setNbTarima(String nbTarima) {
        this.nbTarima = nbTarima;
    }

    public BigDecimal getTarima() {
        return tarima;
    }

    public void setTarima(BigDecimal tarima) {
        this.tarima = tarima;
    }

    public String getCamara() {
        return camara;
    }

    public void setCamara(String camara) {
        this.camara = camara;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    @Override
    public String toString() {
        return "OcupacionPlanta[" + "nbTarima=" + nbTarima + ", tarima=" + tarima + ", camara=" + camara + ", planta=" + planta + ']';
    }
    
}
