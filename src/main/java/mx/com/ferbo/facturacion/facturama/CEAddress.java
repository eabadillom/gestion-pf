package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**CEAddress<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=CEAddress<br>
 * @author esteban
 *
 */
public class CEAddress {
    
    /**Street<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,55}<br>
     */
    @SerializedName(value = "Street")
    private String street = null;
    
    /**ExteriorNumber<br>
     * Matching regular expression pattern: [^|]{1,55}<br>
     */
    @SerializedName(value = "ExteriorNumber")
    private String exteriorNumber = null;
    
    /**InteriorNumber<br>
     * Matching regular expression pattern: [^|]{1,55}<br>
     */
    @SerializedName(value = "InteriorNumber")
    private String InteriorNumber = null;
    
    /**Neighborhood<br>
     * Matching regular expression pattern: [^|]{1,120}<br>
     */
    @SerializedName(value = "Neighborhood")
    private String neighborhood = null;
    
    @SerializedName(value = "Reference")
    private String reference = null;
    
    /**Locality
     */
    @SerializedName(value = "Locality")
    private String locality = null;
    
    /**Municipality<br>
     * Matching regular expression pattern: [^|]{1,120}<br>
     */
    @SerializedName(value = "Municipality")
    private String municipality = null;
    
    /**State
     * Matching regular expression pattern: [^|]{1,30}
     */
    @SerializedName(value = "State")
    private String state = null;
    
    /**Country<br>
     * Required<br>
     */
    @SerializedName(value = "Country")
    private String country = null;
    
    /**ZipCode<br>
     * Required<br>
     * Max length: 12<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "ZipCode")
    private String zipCode = null;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getExteriorNumber() {
        return exteriorNumber;
    }

    public void setExteriorNumber(String exteriorNumber) {
        this.exteriorNumber = exteriorNumber;
    }

    public String getInteriorNumber() {
        return InteriorNumber;
    }

    public void setInteriorNumber(String interiorNumber) {
        InteriorNumber = interiorNumber;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "{\"street\":\"" + street + "\", \"exteriorNumber\":\"" + exteriorNumber + "\", \"InteriorNumber\":\""
                + InteriorNumber + "\", \"neighborhood\":\"" + neighborhood + "\", \"reference\":\"" + reference
                + "\", \"locality\":\"" + locality + "\", \"municipality\":\"" + municipality + "\", \"state\":\""
                + state + "\", \"country\":\"" + country + "\", \"zipCode\":\"" + zipCode + "\"}";
    }
}
