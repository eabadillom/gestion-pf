package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**Product<br>
 * Agrega un Producto o servicio.
 * @see <a href="https://www.api.facturama.com.mx/Docs/Api/POST-api-Product">Documentación de API Rest para Productos / Servicios de Facturama</a><br>
 * @see <a href="https://www.api.facturama.com.mx/api/Product">Endpoint para registro de productos</a><br>
 * @author esteban
 *
 */
public class Product {
    
    /**Unidad de medida aplicable al producto. La unidad debe corresponder con la descripción del concepto.
     *Required
     *Data type: Text
     *String length: inclusive between 1 and 20
     */
    @SerializedName(value="Unit")
    private String unit = null;
    
    
    /**Código de la unidad de medida según el catálogo del SAT (requerido) [vea el catálogo "Unidades"]
     */
    @SerializedName(value="UnitCode")
    private String unitCode = null;
    
    /**Número de parte identificador del producto. La clave de servicio, SKU o equivalente
     * Data type: Text
     * String length: inclusive between 0 and 50
     */
    @SerializedName(value="IdentificationNumber")
    private String identificationNumber = null;
    
    /**Nombre corto del producto
     * Required
     * Data type: Text
     * String length: inclusive between 2 and 50
     */
    @SerializedName(value="Name")
    private String name = null;
    
    /**Descripción del producto, o nombre ampliado
     * Required
     * Data type: Text
     */
    @SerializedName(value="Description")
    private String description = null;
    
    /**Valor o precio unitario del producto
     * Required
     * Data type: Currency
     */
    @SerializedName(value="Price")
    private BigDecimal price = null;
    
    /**Clave del Producto o servicio segun el catalogo del SAT (requerido) [vea el catálogo "Códigos de productos y servicios"]
     */
    @SerializedName(value="CodeProdServ")
    private String codeProdServ = null;
    
    /**Nombre del CodeProdServ
     */
    @SerializedName(value="CodeProdServName")
    private String codeProdServName = null;
    
    /**Atributo para precisar el número de la cuenta predial del inmueble cubierto por el presente concepto, o bien para incorporar los datos de identificación del certificado de participación inmobiliaria no amortizable, tratándose de arrendamiento.
     * Matching regular expression pattern: [0-9]{1,150}
     * String length: inclusive between 1 and 150
     */
    @SerializedName(value="CuentaPredial")
    private String cuentaPredial = null;
    
    /**Impuestos federales aplicables al producto (Nodo Opcional)
     */
    @SerializedName(value="Taxes")
    private List<ProductTax> taxes = null;

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

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCodeProdServ() {
        return codeProdServ;
    }

    public void setCodeProdServ(String codeProdServ) {
        this.codeProdServ = codeProdServ;
    }

    public String getCodeProdServName() {
        return codeProdServName;
    }

    public void setCodeProdServName(String codeProdServName) {
        this.codeProdServName = codeProdServName;
    }

    public String getCuentaPredial() {
        return cuentaPredial;
    }

    public void setCuentaPredial(String cuentaPredial) {
        this.cuentaPredial = cuentaPredial;
    }

    public List<ProductTax> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ProductTax> taxes) {
        this.taxes = taxes;
    }

    @Override
    public String toString() {
        return "{\"unit\":\"" + unit + "\", \"unitCode\":\"" + unitCode + "\", \"identificationNumber\":\""
                + identificationNumber + "\", \"name\":\"" + name + "\", \"description\":\"" + description
                + "\", \"price\":\"" + price + "\", \"codeProdServ\":\"" + codeProdServ + "\", \"codeProdServName\":\""
                + codeProdServName + "\", \"cuentaPredial\":\"" + cuentaPredial + "\", \"taxes\":\"" + taxes + "\"}";
    }
}
