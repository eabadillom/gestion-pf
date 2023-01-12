package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Owner<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Owner<br>
 * @author esteban
 *
 */
public class Owner {
    
    /**NumRegIdTrib<br>
     * Required<br>
     */
    @SerializedName(value = "NumRegIdTrib")
    private String numRegIdTrib = null;
    
    /**TaxResidence<br>
     * Required<br>
     */
    @SerializedName(value = "TaxResidence")
    private String taxResidence = null;

    public String getNumRegIdTrib() {
        return numRegIdTrib;
    }

    public void setNumRegIdTrib(String numRegIdTrib) {
        this.numRegIdTrib = numRegIdTrib;
    }

    public String getTaxResidence() {
        return taxResidence;
    }

    public void setTaxResidence(String taxResidence) {
        this.taxResidence = taxResidence;
    }

    @Override
    public String toString() {
        return "{\"numRegIdTrib\":\"" + numRegIdTrib + "\", \"taxResidence\":\"" + taxResidence + "\"}";
    }
}
