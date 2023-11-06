package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**Items de la factura<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ItemFullBindingModel<br>
 * @author esteban
 *
 */
/**
 * @author esteban
 *
 */
public class ItemFullBindingModel {
    /**Atributo Opcional para relacionar el Identificador unico del producto<br>
     */
    @SerializedName(value = "IdProduct")
    private String idProduct = null;
    
    /**Clave del producto o servicio segun las claves del catalogo<br>
     * Required<br>
     * Matching regular expression pattern: [0-9]{8}<br>
     * String length: inclusive between 0 and 8<br>
     */
    @SerializedName(value = "ProductCode")
    private String productCode = null;
    
    /**Atributo opcional para expresar el número de serie del producto<br>
     * Data type: Text<br>
     * Matching regular expression pattern: [^|]{0,50}<br>
     * Min length: 1<br>
     * String length: inclusive between 0 and 50<br>
     */
    @SerializedName(value = "IdentificationNumber")
    private String identificationNumber = null;
    
    /**Descripción del concepto<br>
     * Required<br>
     * Data type: Text<br>
     * Matching regular expression pattern: [^|]{1,1000}<br>
     */
    @SerializedName(value = "Description")
    private String description = null;
    
    /**Atributo opcional para especificar el nombre de la unidad<br>
     * String length: inclusive between 1 and 20<br>
     */
    @SerializedName(value = "Unit")
    private String unit = null;
    
    /**Atributo requerido para precisar la unidad de medida aplicable al producto<br>
     * Required<br>
     * Data type: Text<br>
     */
    @SerializedName(value = "UnitCode")
    private String unitCode = null;
    
    /**Atributo requerido para precisar el valor o precio unitario del bien o servicio cubierto por el presente concepto.<br>
     * Required<br>
     * Data type: Currency<br>
     * Matching regular expression pattern: ^\d+(?:\.\d{1,2})?$<br>
     */
    @SerializedName(value = "UnitPrice")
    private BigDecimal unitPrice = null;
    
    /**Cantidad de productos ó servicios a comerciar<br>
     * Required<br>
     * Data type: Currency<br>
     * Matching regular expression pattern: ^\d+(?:\.\d{1,6})?$<br>
     */
    @SerializedName(value = "Quantity")
    private BigDecimal quantity = null;
    
    
    /**Subtotal<br>
     * Required<br>
     * Data type: Currency<br>
     * Matching regular expression pattern: ^\d+(?:\.\d{1,2})?$<br>
     */
    @SerializedName(value = "Subtotal")
    private BigDecimal subtotal = null;
    
    /**Descuento<br>
     * Data type: Currency<br>
     * Matching regular expression pattern: ^\d+(?:\.\d{1,6})?$<br>
     */
    @SerializedName(value = "Discount")
    private BigDecimal discount = null;
    
    @SerializedName(value = "TaxObject")
    private String taxObject = null;
    
    @SerializedName(value = "Taxes")
    private List<Tax> taxes = null;
    
    /**Atributo para precisar el número de la cuenta predial del inmueble cubierto por el presente concepto, o bien para incorporar los datos de identificación del certificado de participación inmobiliaria no amortizable, tratándose de arrendamiento.<br>
     * Data type: Text<br>
     * Matching regular expression pattern: [0-9]{1,150}<br>
     * String length: inclusive between 1 and 150<br>
     */
    @SerializedName(value = "CuentaPredial")
    private String cuentaPredial = null;
    
    /**Nodo opcional para listar el(los) número(s) de pedimento(s) que amparan la importación del bien, aplicable cuando se trate de ventas de primera mano de mercancías importadas o se trate de operaciones de comercio exterior con bienes o servicios. Patrón [0-9]{2} [0-9]{2} [0-9]{4} [0-9]{7}
     */
    @SerializedName(value = "NumerosPedimento")
    private String NumerosPedimento = null;
    
    @SerializedName(value = "Parts")
    private List<KitPart> parts = null;
    
    
    /**Total<br>
     * Required<br>
     * Data type: Currency<br>
     * Matching regular expression pattern: ^\d+(?:\.\d{1,2})?$<br>
     */
    @SerializedName(value = "Total")
    private BigDecimal total = null;
    
    /**Complementos de Item<br>
     */
    @SerializedName(value = "Complement")
    private ItemComplement complement = null;

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getTaxObject() {
        return taxObject;
    }

    public void setTaxObject(String taxObject) {
        this.taxObject = taxObject;
    }

    public List<Tax> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Tax> taxes) {
        this.taxes = taxes;
    }

    public String getCuentaPredial() {
        return cuentaPredial;
    }

    public void setCuentaPredial(String cuentaPredial) {
        this.cuentaPredial = cuentaPredial;
    }

    public String getNumerosPedimento() {
        return NumerosPedimento;
    }

    public void setNumerosPedimento(String numerosPedimento) {
        NumerosPedimento = numerosPedimento;
    }

    public List<KitPart> getParts() {
        return parts;
    }

    public void setParts(List<KitPart> parts) {
        this.parts = parts;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public ItemComplement getComplement() {
        return complement;
    }

    public void setComplement(ItemComplement complement) {
        this.complement = complement;
    }

    @Override
    public String toString() {
        return "{\"idProduct\":\"" + idProduct + "\", \"productCode\":\"" + productCode
                + "\", \"identificationNumber\":\"" + identificationNumber + "\", \"description\":\"" + description
                + "\", \"unit\":\"" + unit + "\", \"unitCode\":\"" + unitCode + "\", \"unitPrice\":\"" + unitPrice
                + "\", \"quantity\":\"" + quantity + "\", \"subtotal\":\"" + subtotal + "\", \"discount\":\"" + discount
                + "\", \"taxObject\":\"" + taxObject + "\", \"taxes\":\"" + taxes + "\", \"cuentaPredial\":\""
                + cuentaPredial + "\", \"NumerosPedimento\":\"" + NumerosPedimento + "\", \"parts\":\"" + parts
                + "\", \"total\":\"" + total + "\", \"complement\":\"" + complement + "\"}";
    }
}
