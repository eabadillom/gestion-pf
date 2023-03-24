package mx.com.ferbo.facturacion.facturama;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**Recipient<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Recipient<br>
 * @author esteban
 *
 */
public class Recipient {
    
    /**Matching regular expression pattern: [^|]{1,300}<br>
     * 
     */
    @SerializedName(value = "Name")
    private String name = null;
    
    /**NumRegIdTrib<br>
     * Max length: 40<br>
     * Min length: 4<br>
     */
    @SerializedName(value = "NumRegIdTrib")
    private String numRegIdTrib = null;
    
    /**Addresses<br>
     * Required<br>
     */
    @SerializedName(value = "Addresses")
    private List<CEAddress> addresses = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumRegIdTrib() {
        return numRegIdTrib;
    }

    public void setNumRegIdTrib(String numRegIdTrib) {
        this.numRegIdTrib = numRegIdTrib;
    }

    public List<CEAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<CEAddress> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\", \"numRegIdTrib\":\"" + numRegIdTrib + "\", \"addresses\":\"" + addresses
                + "\"}";
    }
}
