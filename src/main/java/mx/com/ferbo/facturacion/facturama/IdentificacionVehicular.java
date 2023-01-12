package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**IdentificacionVehicular<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=IdentificacionVehicular<br>
 * @author esteban
 *
 */
public class IdentificacionVehicular {
    
    /**ConfigVehicular<br>
     * Required<br>
     */
    @SerializedName(value = "ConfigVehicular")
    private String configVehicular = null;
    
    /**PlacaVM<br>
     * Required<br>
     * Matching regular expression pattern: [^(?!.*\s)-]{5,7}<br>
     */
    @SerializedName(value = "PlacaVM")
    private String placaVM = null;
    
    /**AnioModeloVM<br>
     * Required<br>
     * Matching regular expression pattern: (19[0-9]{2}|20[0-9]{2})<br>
     */
    @SerializedName(value = "AnioModeloVM")
    private String anioModeloVM = null;

    public String getConfigVehicular() {
        return configVehicular;
    }

    public void setConfigVehicular(String configVehicular) {
        this.configVehicular = configVehicular;
    }

    public String getPlacaVM() {
        return placaVM;
    }

    public void setPlacaVM(String placaVM) {
        this.placaVM = placaVM;
    }

    public String getAnioModeloVM() {
        return anioModeloVM;
    }

    public void setAnioModeloVM(String anioModeloVM) {
        this.anioModeloVM = anioModeloVM;
    }

    @Override
    public String toString() {
        return "{\"configVehicular\":\"" + configVehicular + "\", \"placaVM\":\"" + placaVM + "\", \"anioModeloVM\":\""
                + anioModeloVM + "\"}";
    }
}
