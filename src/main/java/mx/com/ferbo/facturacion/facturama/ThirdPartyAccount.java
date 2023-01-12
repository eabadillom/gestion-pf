package mx.com.ferbo.facturacion.facturama;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**Complemento concepto para la emisión del Comprobante Fiscal Digital a través de Internet (CFDI) por orden y cuenta de terceros<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ThirdPartyAccount<br>
 * @author esteban
 */
public class ThirdPartyAccount {
    
    /**Tipo definido para expresar claves del Registro Federal de Contribuyentes<br>
     * Required<br>
     * Matching regular expression pattern: [A-Z&Ñ]{3,4}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]<br>
     * Max length: 13<br>
     * Min length: 12<br>
     */
    @SerializedName(value = "Rfc")
    private String rfc = null;
    
    /**Atributo opcional para el nombre o razón social del contribuyente emisor del comprobante.<br>
     */
    @SerializedName(value = "Name")
    private String name = null;
    
    /**La clave vigente del régimen fiscal del contribuyente receptor.<br>
     */
    @SerializedName(value = "FiscalRegime")
    private String fiscalRegime = null;
    
    /**Atributo requerido para registrar el código postal del domicilio fiscal del receptor del comprobante.<br>
     */
    @SerializedName(value = "TaxZipCode")
    private String taxZipCode = null;
    
    /**Nodo opcional para introducir la información aduanera aplicable cuando se trate de ventas de primera mano de mercancías importadas.
     */
    @SerializedName(value = "ThirdTaxInformation")
    private ThirdPartyAddress thirdTaxInformation = null;
    
    /**Nodo opcional para introducir la información aduanera aplicable cuando se trate de partes o componentes importados vendidos de primera mano.<br>
     */
    @SerializedName(value = "CustomsInformation")
    private ThirdPartyCustomsInformation customsInformation = null;
    
    /**Nodo opcional para expresar las partes o componentes que integran la totalidad del concepto expresado en el CFD o CFDI.<br>
     */
    @SerializedName(value = "Parts")
    private List<Part> parts = null;
    
    /**Opcional para asentar el número de cuenta predial con el que fue registrado el inmueble, en el sistema catastral de la entidad federativa de que trate.<br>
     */
    @SerializedName(value = "PropertyTaxNumber")
    private String propertyTaxNumber = null;
    
    @SerializedName(value = "Taxes")
    private List<ThirdPartyAccountTax> taxes = null;

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFiscalRegime() {
        return fiscalRegime;
    }

    public void setFiscalRegime(String fiscalRegime) {
        this.fiscalRegime = fiscalRegime;
    }

    public String getTaxZipCode() {
        return taxZipCode;
    }

    public void setTaxZipCode(String taxZipCode) {
        this.taxZipCode = taxZipCode;
    }

    public ThirdPartyAddress getThirdTaxInformation() {
        return thirdTaxInformation;
    }

    public void setThirdTaxInformation(ThirdPartyAddress thirdTaxInformation) {
        this.thirdTaxInformation = thirdTaxInformation;
    }

    public ThirdPartyCustomsInformation getCustomsInformation() {
        return customsInformation;
    }

    public void setCustomsInformation(ThirdPartyCustomsInformation customsInformation) {
        this.customsInformation = customsInformation;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public String getPropertyTaxNumber() {
        return propertyTaxNumber;
    }

    public void setPropertyTaxNumber(String propertyTaxNumber) {
        this.propertyTaxNumber = propertyTaxNumber;
    }

    public List<ThirdPartyAccountTax> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ThirdPartyAccountTax> taxes) {
        this.taxes = taxes;
    }

    @Override
    public String toString() {
        return "{\"rfc\":\"" + rfc + "\", \"name\":\"" + name + "\", \"fiscalRegime\":\"" + fiscalRegime
                + "\", \"taxZipCode\":\"" + taxZipCode + "\", \"thirdTaxInformation\":\"" + thirdTaxInformation
                + "\", \"customsInformation\":\"" + customsInformation + "\", \"parts\":\"" + parts
                + "\", \"propertyTaxNumber\":\"" + propertyTaxNumber + "\", \"taxes\":\"" + taxes + "\"}";
    }
}
