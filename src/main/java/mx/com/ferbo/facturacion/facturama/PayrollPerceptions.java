package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**PayrollPerceptions<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=PayrollPerceptions<br>
 * @author esteban
 *
 */
public class PayrollPerceptions {
    
    @SerializedName(value = "Details")
    private Perception Details = null;
    
    @SerializedName(value = "Retirement")
    private Retirement retirement = null;
    
    @SerializedName(value = "Indemnification")
    private Indemnification Indemnification = null;

    public Perception getDetails() {
        return Details;
    }

    public void setDetails(Perception details) {
        Details = details;
    }

    public Retirement getRetirement() {
        return retirement;
    }

    public void setRetirement(Retirement retirement) {
        this.retirement = retirement;
    }

    public Indemnification getIndemnification() {
        return Indemnification;
    }

    public void setIndemnification(Indemnification indemnification) {
        Indemnification = indemnification;
    }

    @Override
    public String toString() {
        return "{\"Details\":\"" + Details + "\", \"retirement\":\"" + retirement + "\", \"Indemnification\":\""
                + Indemnification + "\"}";
    }
}
