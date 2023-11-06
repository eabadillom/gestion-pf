package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**AddressEmisor<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=AddressEmisor<br>
 * @author esteban
 *
 */
public class AddressEmisor {
    
    /**Street<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,100}<br>
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
    private String interiorNumber = null;
    
    /**Neighborhood
     */
    @SerializedName(value = "Neighborhood")
    private String neighborhood = null;
    
    /**Reference
     */
    @SerializedName(value = "Reference")
    private String reference = null;
    
    /**ZipCode<br>
     * Matching regular expression pattern: ^[0-9]{5}$<br>
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
        return interiorNumber;
    }

    public void setInteriorNumber(String interiorNumber) {
        this.interiorNumber = interiorNumber;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "{\"street\":\"" + street + "\", \"exteriorNumber\":\"" + exteriorNumber + "\", \"interiorNumber\":\""
                + interiorNumber + "\", \"neighborhood\":\"" + neighborhood + "\", \"reference\":\"" + reference
                + "\", \"zipCode\":\"" + zipCode + "\"}";
    }
}
