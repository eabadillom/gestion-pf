package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

import mx.com.ferbo.facturacion.facturama.Address;

/**ReceiverViewModel<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ReceiverViewModel<br>
 * @author esteban
 *
 */
public class ReceiverViewModel {
    
    @SerializedName(value = "Address")
    private Address address;
    
    @SerializedName(value = "Rfc")
    private String rfc;
    
    @SerializedName(value = "Name")
    private String name;
    
    @SerializedName(value = "Email")
    private String email;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{\"address\":\"" + address + "\", \"rfc\":\"" + rfc + "\", \"name\":\"" + name + "\", \"email\":\""
                + email + "\"}";
    }
    
    
}
