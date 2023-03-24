package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

/**ComplementModel<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ComplementModel<br>
 * @author esteban
 *
 */
public class ComplementModel {
    
    @SerializedName(value = "TaxStamp")
    private TaxStamp taxStamp;

    public TaxStamp getTaxStamp() {
        return taxStamp;
    }

    public void setTaxStamp(TaxStamp taxStamp) {
        this.taxStamp = taxStamp;
    }

    @Override
    public String toString() {
        return "{\"taxStamp\":\"" + taxStamp + "\"}";
    }
}
