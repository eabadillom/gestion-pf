package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**PayrollOutsourcing<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=PayrollOutsourcing<br>
 * @author esteban
 *
 */
public class PayrollOutsourcing {
    
    /**RfcContractor
     */
    @SerializedName(value = "RfcContractor")
    private String rfcContractor = null;
    
    /**PercentageTime<br>
     * Range: inclusive between 0,001 and 100
     */
    @SerializedName(value = "PercentageTime")
    private String percentageTime = null;

    public String getRfcContractor() {
        return rfcContractor;
    }

    public void setRfcContractor(String rfcContractor) {
        this.rfcContractor = rfcContractor;
    }

    public String getPercentageTime() {
        return percentageTime;
    }

    public void setPercentageTime(String percentageTime) {
        this.percentageTime = percentageTime;
    }

    @Override
    public String toString() {
        return "{\"rfcContractor\":\"" + rfcContractor + "\", \"percentageTime\":\"" + percentageTime + "\"}";
    }
}
