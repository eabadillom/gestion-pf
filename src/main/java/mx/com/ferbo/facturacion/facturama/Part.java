package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Nodo opcional para expresar las partes o componentes que integran la totalidad del concepto expresado en el CFDI.<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Part<br>
 * @author esteban
 */
public class Part {
    
    /**Atributo requerido para precisar la cantidad de bienes o servicios del tipo particular definido por la presente parte<br>
     * Required<br>
     */
    @SerializedName(value = "Quantity")
    private BigDecimal quantity = null;
    
    /**Atributo opcional para precisar la unidad de medida aplicable para la cantidad expresada en la parte.<br>
     */
    @SerializedName(value = "Unit")
    private String unit = null;
    
    /**Atributo opcional para expresar el número de serie del bien o identificador del servicio amparado por la presente parte.<br>
     */
    @SerializedName(value = "IdentificationNumber")
    private String identificationNumber = null;
    
    /**Atributo requerido para precisar la descripción del bien o servicio cubierto por la presente parte.<br>
     * Required<br>
     */
    @SerializedName(value = "Description")
    private String description = null;
    
    /**Atributo opcional para precisar el valor o precio unitario del bien o servicio cubierto por la presente parte.<br>
     * 
     */
    @SerializedName(value = "UnitPrce")
    private BigDecimal unitPrce = null;
    
    /**Atributo opcional para precisar el importe total de los bienes o servicios de la presente parte.Debe ser equivalente al resultado de multiplicar la cantidad por el valor unitario expresado en la parte.<br>
     */
    @SerializedName(value = "Amount")
    private BigDecimal amount = null;
    
    /**Nodo opcional para introducir la información aduanera aplicable cuando se trate de partes o componentes importados vendidos de primera mano.<br>
     */
    @SerializedName(value = "CustomsInformation")
    private ThirdPartyCustomsInformation customsInformation = null;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrce() {
        return unitPrce;
    }

    public void setUnitPrce(BigDecimal unitPrce) {
        this.unitPrce = unitPrce;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ThirdPartyCustomsInformation getCustomsInformation() {
        return customsInformation;
    }

    public void setCustomsInformation(ThirdPartyCustomsInformation customsInformation) {
        this.customsInformation = customsInformation;
    }

    @Override
    public String toString() {
        return "{\"quantity\":\"" + quantity + "\", \"unit\":\"" + unit + "\", \"identificationNumber\":\""
                + identificationNumber + "\", \"description\":\"" + description + "\", \"unitPrce\":\"" + unitPrce
                + "\", \"amount\":\"" + amount + "\", \"customsInformation\":\"" + customsInformation + "\"}";
    }
}
