package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**OtherPayment<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=OtherPayment<br>
 * @author esteban
 *
 */
public class OtherPayment {
    
    @SerializedName(value = "EmploymentSubsidy")
    private EmploymentSubsidy EmploymentSubsidy = null;
    
    @SerializedName(value = "Compensation")
    private Compensation Compensation = null;
    
    /**OtherPaymentType<br>
     * Required<br>
     */
    @SerializedName(value = "OtherPaymentType")
    private String otherPaymentType = null;
    
    /**Code<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,100}<br>
     */
    @SerializedName(value = "Code")
    private String Code = null;
    
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

    public EmploymentSubsidy getEmploymentSubsidy() {
        return EmploymentSubsidy;
    }

    public void setEmploymentSubsidy(EmploymentSubsidy employmentSubsidy) {
        EmploymentSubsidy = employmentSubsidy;
    }

    public Compensation getCompensation() {
        return Compensation;
    }

    public void setCompensation(Compensation compensation) {
        Compensation = compensation;
    }

    public String getOtherPaymentType() {
        return otherPaymentType;
    }

    public void setOtherPaymentType(String otherPaymentType) {
        this.otherPaymentType = otherPaymentType;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
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
        return "{\"EmploymentSubsidy\":\"" + EmploymentSubsidy + "\", \"Compensation\":\"" + Compensation
                + "\", \"otherPaymentType\":\"" + otherPaymentType + "\", \"Code\":\"" + Code + "\", \"description\":\""
                + description + "\", \"amount\":\"" + amount + "\"}";
    }
    
    
}
