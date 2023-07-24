package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Issuer<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Issuer<br>
 * @author esteban
 *
 */
public class Issuer {
    /**Address
     */
    @SerializedName(value = "Address")
    private AddressEmisor address = null;
    
    /**Curp
     */
    @SerializedName(value = "Curp")
    private String curp = null;
    
    @SerializedName(value = "TaxName")
    private String taxName = null;
    
    @SerializedName(value = "Email")
    private String email;
    
    @SerializedName(value = "Phone")
    private String phone;
    
    @SerializedName(value = "TaxAddress")
    private Address taxAddress;
    
    @SerializedName(value = "IssuedIn")
    private Address issuedIn;  
    
    @SerializedName(value = "UrlLogo")
    private String urlLogo;
}
