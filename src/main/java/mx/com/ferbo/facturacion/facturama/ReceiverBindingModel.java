package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Crea un nuevo cliente<br>
 * https://www.api.facturama.com.mx/Docs/Api/POST-api-ReceiverBindingModel<br>
 * @author esteban
 */
public class ReceiverBindingModel {
    
    /**Atributo para indicar el Id del ClientModel a relacionar<br>
     * 
     */
    @SerializedName(value = "Id")
    private String id = null;
    
    /**Atributo requerido para precisar la Clave del Registro Federal de Contribuyentes correspondiente al contribuyente<br>
     * Required<br>
     * Matching regular expression pattern: [A-Z&Ñ]{3,4}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]<br>
     */
    @SerializedName(value = "Rfc")
    private String rfc = null;
    
    /**Nombre del cliente Requerido<br>
     * Matching regular expression pattern: [^|]{1,300}<br>
     * String length: inclusive between 1 and 300<br>
     */
    @SerializedName(value = "Name")
    private String name = null; //Nombre del cliente
    
    /**Expresar la clave del uso que dará a esta factura el receptor del CFDI.<br>
     * Required<br>
     * Matching regular expression pattern: G01|G02|G03|I01|I02|I03|I04|I05|I06|I07|I08|D01|D02|D03|D04|D05|D06|D07|D08|D09|D10|P01<br>
     */
    @SerializedName(value = "CfdiUse")
    private String cfdiUse = null; //Uso del CFDI
    
    /**Atributo condicional para expresar la clave del pais cuando se trate de extranjero<br>
     */
    @SerializedName(value = "TaxResidence")
    private String taxResidence = null;
    
    /**Obligatorio cuando se trate de extranjero<br>
     */
    @SerializedName(value = "TaxRegistrationNumber")
    private String taxRegistrationNumber = null;
    
    
    /**Regimen Fiscal<br>
     */
    @SerializedName(value = "FiscalRegime")
    private String fiscalRegime = null;
    
    /**Codigo Postal del receptor.<br>
     */
    @SerializedName(value = "TaxZipCode")
    private String taxZipCode = null;
    
    @SerializedName(value = "Address")
    private Address address = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCfdiUse() {
        return cfdiUse;
    }

    public void setCfdiUse(String cfdiUse) {
        this.cfdiUse = cfdiUse;
    }

    public String getTaxResidence() {
        return taxResidence;
    }

    public void setTaxResidence(String taxResidence) {
        this.taxResidence = taxResidence;
    }

    public String getTaxRegistrationNumber() {
        return taxRegistrationNumber;
    }

    public void setTaxRegistrationNumber(String taxRegistrationNumber) {
        this.taxRegistrationNumber = taxRegistrationNumber;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"rfc\":\"" + rfc + "\", \"name\":\"" + name + "\", \"cfdiUse\":\"" + cfdiUse
                + "\", \"taxResidence\":\"" + taxResidence + "\", \"taxRegistrationNumber\":\"" + taxRegistrationNumber
                + "\", \"fiscalRegime\":\"" + fiscalRegime + "\", \"taxZipCode\":\"" + taxZipCode + "\", \"address\":\""
                + address + "\"}";
    }
}
