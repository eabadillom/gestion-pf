package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

public class ClientModelRsp {

    @SerializedName(value = "Id")
    private String id;

    @SerializedName(value = "Address")
    AddressRsp address;

    @SerializedName(value = "Rfc")
    private String rfc;

    @SerializedName(value = "Name")
    private String name;

    @SerializedName(value = "Email")
    private String email;

    @SerializedName(value = "CfdiUse")
    private String cfdiUse;

    @SerializedName(value = "TaxResidence")
    private String taxResidence;

    @SerializedName(value = "NumRegIdTrib")
    private String numRegIdTrib;

    public String getId() {
        return id;
    }

    public AddressRsp getAddress() {
        return address;
    }

    public String getRfc() {
        return rfc;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCfdiUse() {
        return cfdiUse;
    }

    public String getTaxResidence() {
        return taxResidence;
    }

    public String getNumRegIdTrib() {
        return numRegIdTrib;
    }

    public void setId(String Id) {
        this.id = Id;
    }

    public void setAddress(AddressRsp AddressObject) {
        this.address = AddressObject;
    }

    public void setRfc(String Rfc) {
        this.rfc = Rfc;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public void setCfdiUse(String CfdiUse) {
        this.cfdiUse = CfdiUse;
    }

    public void setTaxResidence(String TaxResidence) {
        this.taxResidence = TaxResidence;
    }

    public void setNumRegIdTrib(String NumRegIdTrib) {
        this.numRegIdTrib = NumRegIdTrib;
    }
}
