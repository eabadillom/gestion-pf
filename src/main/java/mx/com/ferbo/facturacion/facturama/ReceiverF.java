package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**ReceiverBindingModel<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ReceiverBindingModel<br>
 * @author esteban
 *
 */
public class ReceiverF {
    /**Address
     */
    @SerializedName(value = "Address")
    private CEAddress Address = null;

    public CEAddress getAddress() {
        return Address;
    }

    public void setAddress(CEAddress address) {
        Address = address;
    }

    @Override
    public String toString() {
        return "{\"Address\":\"" + Address + "\"}";
    }
}
