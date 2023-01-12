package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Complemento del CFDI<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Complement<br>
 * @author esteban
 *
 */
public class Complement {
    /**Complemento de Pago<br>
     */
    @SerializedName(value = "Payments")
    private PaymentBindingModel Payments = null;
    
    /**Complemento Detallista<br>
     * 
     */
    @SerializedName(value = "Donation")
    private Donat Donation = null;
    
    /**Complemento de Comercio Exterior<br>
     */
    @SerializedName(value = "ForeignTrade")
    private ForeignTrade ForeignTrade = null;
    
    /**Complemento de Nómina
     */
    @SerializedName(value = "Payroll")
    private Payroll Payroll = null;
    
    /**Complemento de Leyendas Fiscales<br>
     */
    @SerializedName(value = "TaxLegends")
    private TaxLegends TaxLegends = null;
    
    /**Complemento de carta porte 2.0
     * 
     */
    @SerializedName(value = "CartaPorte20")
    private ComplementoCartaPorte20 CartaPorte20 = null;
}
