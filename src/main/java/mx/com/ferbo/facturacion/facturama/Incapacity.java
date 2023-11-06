package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Incapacity<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Incapacity<br>
 * @author esteban
 *
 */
public class Incapacity {
    
    /**Days<br>
     * Required<br>
     */
    @SerializedName(value = "Days")
    private Integer days = null;
    
    /**Type<br>
     * Required<br>
     */
    @SerializedName(value = "Type")
    private String type = null;
    
    /**Amount<br>
     */
    @SerializedName(value = "Amount")
    private BigDecimal amount = null;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{\"days\":\"" + days + "\", \"type\":\"" + type + "\", \"amount\":\"" + amount + "\"}";
    }
}
