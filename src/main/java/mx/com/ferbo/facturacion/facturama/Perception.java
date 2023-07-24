package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Perception<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Perception<br>
 * @author esteban
 *
 */
public class Perception {
    
    @SerializedName(value = "ActionsOrTitles")
    private ActionsOrTitles actionsOrTitles = null;
    
    @SerializedName(value = "ExtraHours")
    private ExtraHour extraHours = null;
    
    /**PerceptionType
     */
    @SerializedName(value = "PerceptionType")
    private String perceptionType = null;
    
    /**Code<br>
     * Matching regular expression pattern: [^|]{3,15}<br>
     */
    @SerializedName(value = "Code")
    private String code = null;
    
    /**Description<br>
     * Matching regular expression pattern: [^|]{1,100}<br>
     */
    @SerializedName(value = "Description")
    private String description = null;
    
    /**TaxedAmount<br>
     * Required<br>
     */
    @SerializedName(value = "TaxedAmount")
    private BigDecimal taxedAmount = null;
    
    /**ExemptAmount<br>
     * Required<br>
     */
    @SerializedName(value = "ExemptAmount")
    private BigDecimal exemptAmount = null;

    public ActionsOrTitles getActionsOrTitles() {
        return actionsOrTitles;
    }

    public void setActionsOrTitles(ActionsOrTitles actionsOrTitles) {
        this.actionsOrTitles = actionsOrTitles;
    }

    public ExtraHour getExtraHours() {
        return extraHours;
    }

    public void setExtraHours(ExtraHour extraHours) {
        this.extraHours = extraHours;
    }

    public String getPerceptionType() {
        return perceptionType;
    }

    public void setPerceptionType(String perceptionType) {
        this.perceptionType = perceptionType;
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

    public BigDecimal getTaxedAmount() {
        return taxedAmount;
    }

    public void setTaxedAmount(BigDecimal taxedAmount) {
        this.taxedAmount = taxedAmount;
    }

    public BigDecimal getExemptAmount() {
        return exemptAmount;
    }

    public void setExemptAmount(BigDecimal exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    @Override
    public String toString() {
        return "{\"actionsOrTitles\":\"" + actionsOrTitles + "\", \"extraHours\":\"" + extraHours
                + "\", \"perceptionType\":\"" + perceptionType + "\", \"code\":\"" + code + "\", \"description\":\""
                + description + "\", \"taxedAmount\":\"" + taxedAmount + "\", \"exemptAmount\":\"" + exemptAmount
                + "\"}";
    }
}
