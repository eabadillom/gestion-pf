package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Deduction<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Deduction<br>
 * @author esteban
 *
 */
public class Deduction {
    
    /**DeduccionType<br>
     * Required<br>
     */
    @SerializedName(value = "DeduccionType")
    private String deduccionType = null;
    
    /**Code<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{3,15}<br>
     */
    @SerializedName(value = "Code")
    private String code = null;
    
    /**Description<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,100}<br>
     */
    @SerializedName(value = "Description")
    private String description = null;
    
    /**Amount<br>
     * Required<br>
     */
    @SerializedName(value = "Amount")
    private BigDecimal amount = null;

    public String getDeduccionType() {
        return deduccionType;
    }

    public void setDeduccionType(String deduccionType) {
        this.deduccionType = deduccionType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{\"deduccionType\":\"" + deduccionType + "\", \"code\":\"" + code + "\", \"description\":\""
                + description + "\", \"amount\":\"" + amount + "\"}";
    }
}
