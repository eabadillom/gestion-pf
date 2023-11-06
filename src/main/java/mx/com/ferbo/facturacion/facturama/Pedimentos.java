package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Pedimentos<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Pedimentos<br>
 * @author esteban
 *
 */
public class Pedimentos {
    
    /**Pedimento<br>
     * Required<br>
     * Matching regular expression pattern: [0-9]{2} [0-9]{2} [0-9]{4} [0-9]{7}<br>
     */
    @SerializedName(value = "Pedimento")
    private String Pedimento = null;

    public String getPedimento() {
        return Pedimento;
    }

    public void setPedimento(String pedimento) {
        Pedimento = pedimento;
    }

    @Override
    public String toString() {
        return "{\"Pedimento\":\"" + Pedimento + "\"}";
    }
}
