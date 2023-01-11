package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Commodity<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Commodity<br>
 * @author esteban
 *
 */
public class Commodity {
    
    /**SpecificDescriptions
     */
    @SerializedName(value = "SpecificDescriptions")
    private SpecificDescriptions specificDescriptions = null;
    
    /**IdentificationNumber<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,100}<br>
     */
    @SerializedName(value = "IdentificationNumber")
    private String identificationNumber = null;
    
    /**TariffFraction
     */
    @SerializedName(value = "TariffFraction")
    private String tariffFraction = null;
    
    /**CustomsQuantity<br>
     * Matching regular expression pattern: ^[0-9]{1,14}(.([0-9]{1,3}))?$<br>
     */
    @SerializedName(value = "CustomsQuantity")
    private BigDecimal customsQuantity = null;
    
    /**CustomsUnit
     */
    @SerializedName(value = "CustomsUnit")
    private String customsUnit = null;
    
    /**CustomsUnitValue
     * Matching regular expression pattern: ^[0-9]{1,16}(.([0-9]{1,2}))?$
     */
    @SerializedName(value = "CustomsUnitValue")
    private BigDecimal customsUnitValue = null;
    
    /**ValueInDolar<br>
     * Required<br>
     * Matching regular expression pattern: ^[0-9]{1,16}(.([0-9]{1,2}))?$<br>
     */
    @SerializedName(value = "ValueInDolar")
    private BigDecimal valueInDolar = null;

    public SpecificDescriptions getSpecificDescriptions() {
        return specificDescriptions;
    }

    public void setSpecificDescriptions(SpecificDescriptions specificDescriptions) {
        this.specificDescriptions = specificDescriptions;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getTariffFraction() {
        return tariffFraction;
    }

    public void setTariffFraction(String tariffFraction) {
        this.tariffFraction = tariffFraction;
    }

    public BigDecimal getCustomsQuantity() {
        return customsQuantity;
    }

    public void setCustomsQuantity(BigDecimal customsQuantity) {
        this.customsQuantity = customsQuantity;
    }

    public String getCustomsUnit() {
        return customsUnit;
    }

    public void setCustomsUnit(String customsUnit) {
        this.customsUnit = customsUnit;
    }

    public BigDecimal getCustomsUnitValue() {
        return customsUnitValue;
    }

    public void setCustomsUnitValue(BigDecimal customsUnitValue) {
        this.customsUnitValue = customsUnitValue;
    }

    public BigDecimal getValueInDolar() {
        return valueInDolar;
    }

    public void setValueInDolar(BigDecimal valueInDolar) {
        this.valueInDolar = valueInDolar;
    }

    @Override
    public String toString() {
        return "{\"specificDescriptions\":\"" + specificDescriptions + "\", \"identificationNumber\":\""
                + identificationNumber + "\", \"tariffFraction\":\"" + tariffFraction + "\", \"customsQuantity\":\""
                + customsQuantity + "\", \"customsUnit\":\"" + customsUnit + "\", \"customsUnitValue\":\""
                + customsUnitValue + "\", \"valueInDolar\":\"" + valueInDolar + "\"}";
    }
}
