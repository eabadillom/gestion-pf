package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Nodo opcional para expresar las partes o componentes que integran la totalidad del concepto expresado en el CFDI.<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=KitPart<br>
 * @author esteban
 *
 */
public class KitPart {
    
    /**Atributo requerido para precisar la cantidad de bienes o servicios del tipo particular definido por la presente parte<br>
     * Required<br>
     */
    @SerializedName(value = "Quantity")
    private BigDecimal quantity = null;
    
    /**Atributo opcional para precisar la unidad de medida aplicable para la cantidad expresada en la parte.<br>
     */
    @SerializedName(value = "UnitCode")
    private String unitCode = null;
    
    /**Atributo requerido para expresar la clave del producto o del servicio amparado por la presente parte. Es requerido y deben utilizar las claves del catálogo de productos y servicios, cuando los conceptos que registren por sus actividades correspondan con dichos conceptos.<br>
     * Required<br>
     * String length: inclusive between 0 and 8<br>
     */
    @SerializedName(value = "ProductCode")
    private String productCode = null;
    
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
     * Data type: Currency<br>
     *  Matching regular expression pattern: ^\d+(?:\.\d{1,2})?$<br>
     */
    @SerializedName(value = "UnitPrice")
    private BigDecimal unitPrice = null;
    
    /**Atributo opcional para precisar el importe total de los bienes o servicios de la presente parte.Debe ser equivalente al resultado de multiplicar la cantidad por el valor unitario expresado en la parte.<br>
     */
    @SerializedName(value = "Amount")
    private BigDecimal amount = null;
    
    @SerializedName(value = "CustomsInformation")
    private ThirdPartyCustomsInformation customsInformation = null;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
        return "{\"quantity\":\"" + quantity + "\", \"unitCode\":\"" + unitCode + "\", \"productCode\":\"" + productCode
                + "\", \"identificationNumber\":\"" + identificationNumber + "\", \"description\":\"" + description
                + "\", \"unitPrice\":\"" + unitPrice + "\", \"amount\":\"" + amount + "\", \"customsInformation\":\""
                + customsInformation + "\"}";
    }
}
