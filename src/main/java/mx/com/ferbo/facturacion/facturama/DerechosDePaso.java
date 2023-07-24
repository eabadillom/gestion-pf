package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**DerechosDePaso<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=DerechosDePaso<br>
 * @author esteban
 *
 */
public class DerechosDePaso {
    
    /**Required
     */
    @SerializedName(value = "TipoDerechoDePaso")
    private String tipoDerechoDePaso = null;
    
    /**Required
     */
    @SerializedName(value = "KilometrajePagado")
    private String kilometrajePagado = null;

    public String getTipoDerechoDePaso() {
        return tipoDerechoDePaso;
    }

    public void setTipoDerechoDePaso(String tipoDerechoDePaso) {
        this.tipoDerechoDePaso = tipoDerechoDePaso;
    }

    public String getKilometrajePagado() {
        return kilometrajePagado;
    }

    public void setKilometrajePagado(String kilometrajePagado) {
        this.kilometrajePagado = kilometrajePagado;
    }

    @Override
    public String toString() {
        return "{\"tipoDerechoDePaso\":\"" + tipoDerechoDePaso + "\", \"kilometrajePagado\":\"" + kilometrajePagado
                + "\"}";
    }
}
