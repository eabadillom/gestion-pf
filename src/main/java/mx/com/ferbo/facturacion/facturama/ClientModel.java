package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

public class ClientModel {
    @SerializedName(value = "Id")
    private String id = null;
    
    @SerializedName(value = "Email")
    private String email = null;
    
    @SerializedName(value = "EmailOp1")
    private String emailOp1 = null;
    
    @SerializedName(value = "EmailOp2")
    private String emailOp2 = null;
    
    @SerializedName(value = "Address")
    private Address address = null;
    
    @SerializedName(value = "Rfc")
    private String rfc = null;
    
    @SerializedName(value = "Name")
    private String name = null;
    
    @SerializedName(value = "CfdiUse")
    private String cfdiUse = null;
    
    /** Clave del país de residencia (Aplica sólo para clientes extranjeros, ver catálogo de países)
     * 
     */
    @SerializedName(value = "TaxResidence")
    private String taxResidence = null;
    
    
    /**Número de registro de identidad fiscal (Solo para clientes extranjeros)
     * 
     */
    @SerializedName(value = "NumRegIdTrib")
    private String numRegIdTrib = null;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getEmailOp1() {
        return emailOp1;
    }


    public void setEmailOp1(String emailOp1) {
        this.emailOp1 = emailOp1;
    }


    public String getEmailOp2() {
        return emailOp2;
    }


    public void setEmailOp2(String emailOp2) {
        this.emailOp2 = emailOp2;
    }


    public Address getAddress() {
        return address;
    }


    public void setAddress(Address address) {
        this.address = address;
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


    public String getNumRegIdTrib() {
        return numRegIdTrib;
    }


    public void setNumRegIdTrib(String numRegIdTrib) {
        this.numRegIdTrib = numRegIdTrib;
    }


    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"email\":\"" + email + "\", \"emailOp1\":\"" + emailOp1 + "\", \"emailOp2\":\""
                + emailOp2 + "\", \"address\":\"" + address + "\", \"rfc\":\"" + rfc + "\", \"name\":\"" + name
                + "\", \"cfdiUse\":\"" + cfdiUse + "\", \"taxResidence\":\"" + taxResidence + "\", \"numRegIdTrib\":\""
                + numRegIdTrib + "\"}";
    }
}
