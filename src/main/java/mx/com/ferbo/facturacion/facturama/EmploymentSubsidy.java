package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**EmploymentSubsidy<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=EmploymentSubsidy<br>
 * @author esteban
 *
 */
public class EmploymentSubsidy {
    
    /**Amount<br>
     * Required<br>
     */
    @SerializedName(value = "Amount")
    private BigDecimal amount = null;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{\"amount\":\"" + amount + "\"}";
    }
}
