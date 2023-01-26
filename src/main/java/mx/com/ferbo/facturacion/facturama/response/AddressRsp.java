package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

public class AddressRsp {
    @SerializedName(value = "Street")
    private String street;

    @SerializedName(value = "ExteriorNumber")
    private String exteriorNumber;

    @SerializedName(value = "InteriorNumber")
    private String interiorNumber;

    @SerializedName(value = "Neighborhood")
    private String neighborhood;

    @SerializedName(value = "ZipCode")
    private String zipCode;

    @SerializedName(value = "Locality")
    private String locality;

    @SerializedName(value = "Municipality")
    private String municipality;

    @SerializedName(value = "State")
    private String state;

    @SerializedName(value = "Country")
    private String country;

    public String getStreet() {
        return street;
    }

    public String getExteriorNumber() {
        return exteriorNumber;
    }

    public String getInteriorNumber() {
        return interiorNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getLocality() {
        return locality;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public void setStreet(String Street) {
        this.street = Street;
    }

    public void setExteriorNumber(String ExteriorNumber) {
        this.exteriorNumber = ExteriorNumber;
    }

    public void setInteriorNumber(String InteriorNumber) {
        this.interiorNumber = InteriorNumber;
    }

    public void setNeighborhood(String Neighborhood) {
        this.neighborhood = Neighborhood;
    }

    public void setZipCode(String ZipCode) {
        this.zipCode = ZipCode;
    }

    public void setLocality(String Locality) {
        this.locality = Locality;
    }

    public void setMunicipality(String Municipality) {
        this.municipality = Municipality;
    }

    public void setState(String State) {
        this.state = State;
    }

    public void setCountry(String Country) {
        this.country = Country;
    }
}
