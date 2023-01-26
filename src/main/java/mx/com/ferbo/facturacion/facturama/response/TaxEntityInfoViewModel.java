package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

import mx.com.ferbo.facturacion.facturama.Address;

/**TaxEntityInfoViewModel<br>
 * Cuerpo del Resultado de una consulta de producto<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=TaxEntityInfoViewModel<br>
 * @author esteban
 *
 */
public class TaxEntityInfoViewModel {
    
    /**nombre del régimen en el que tributa el contribuyente
     */
    @SerializedName(value = "FiscalRegime")
    private String fiscalRegime = null;
    
    /**nombre comercial<br>
     */
    @SerializedName(value = "ComercialName")
    private String comercialName = null;
    
    /**Clave del Registro Federal de Contribuyentes correspondiente al contribuyente<br>
     */
    @SerializedName(value = "Rfc")
    private String rfc = null;
    
    /**Nodo para enlistar los detalles del nombre del contribuyente<br>
     */
    @SerializedName(value = "TaxName")
    private String taxName = null;
    
    /**Correo electronico del contributente<br>
     */
    @SerializedName(value = "Email")
    private String email = null;
    
    /**Correo electronico opcional del contributente<br>
     */
    @SerializedName(value = "OptionalEmail")
    private String optionalEmail = null;
    
    /**Telefono del contribuyente<br>
     */
    @SerializedName(value = "Phone")
    private String Phone = null;
    
    /**Nodo para enlistar los detalles de la dirección del contribuyente<br>
     */
    @SerializedName(value = "TaxAddress")
    private Address taxAddress = null;
    
    /**Nodo para enlistar los detalles de una dirección de expedicion<br>
     */
    @SerializedName(value = "IssuedIn")
    private Address issuedIn = null;
    
    /**Nodo para enlistar los certificados<br>
     */
    @SerializedName(value = "Csd")
    private CsdBindingModel csd = null;
    
    @SerializedName(value = "PasswordSat")
    private String passwordSat = null;
    
    @SerializedName(value = "UrlLogo")
    private String urlLogo = null;

    public String getFiscalRegime() {
        return fiscalRegime;
    }

    public void setFiscalRegime(String fiscalRegime) {
        this.fiscalRegime = fiscalRegime;
    }

    public String getComercialName() {
        return comercialName;
    }

    public void setComercialName(String comercialName) {
        this.comercialName = comercialName;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOptionalEmail() {
        return optionalEmail;
    }

    public void setOptionalEmail(String optionalEmail) {
        this.optionalEmail = optionalEmail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Address getTaxAddress() {
        return taxAddress;
    }

    public void setTaxAddress(Address taxAddress) {
        this.taxAddress = taxAddress;
    }

    public Address getIssuedIn() {
        return issuedIn;
    }

    public void setIssuedIn(Address issuedIn) {
        this.issuedIn = issuedIn;
    }

    public CsdBindingModel getCsd() {
        return csd;
    }

    public void setCsd(CsdBindingModel csd) {
        this.csd = csd;
    }

    public String getPasswordSat() {
        return passwordSat;
    }

    public void setPasswordSat(String passwordSat) {
        this.passwordSat = passwordSat;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    @Override
    public String toString() {
        return "{\"fiscalRegime\":\"" + fiscalRegime + "\", \"comercialName\":\"" + comercialName + "\", \"rfc\":\""
                + rfc + "\", \"taxName\":\"" + taxName + "\", \"email\":\"" + email + "\", \"optionalEmail\":\""
                + optionalEmail + "\", \"Phone\":\"" + Phone + "\", \"taxAddress\":\"" + taxAddress
                + "\", \"issuedIn\":\"" + issuedIn + "\", \"csd\":\"" + csd + "\", \"passwordSat\":\"" + passwordSat
                + "\", \"urlLogo\":\"" + urlLogo + "\"}";
    }

}
