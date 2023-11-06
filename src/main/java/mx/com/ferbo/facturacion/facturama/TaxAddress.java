package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Detalles de la Dirección Fiscal<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=TaxAddress<br>
 * @author esteban
 *
 */
public class TaxAddress {
    
    /**Calle<br>
     * Required<br>
     * String length: inclusive between 1 and 100<br>
     */
    @SerializedName(value = "Street")
    private String street = null;
    
    /**Numero Exterior (Requerido)<br>
     * String length: inclusive between 1 and 30<br>
     */
    @SerializedName(value = "ExteriorNumber")
    private String exteriorNumber = null;
    
    /**Numero Interior<br>
     * String length: inclusive between 0 and 30<br>
     */
    @SerializedName(value = "InteriorNumber")
    private String interiorNumber = null;
    
    /**Colonia<br>
     * Required<br>
     * String length: inclusive between 1 and 80<br>
     */
    @SerializedName(value = "Neighborhood")
    private String neighborhood = null;
    
    /**Código Postal<br>
     * Required<br>
     * Data type: PostalCode<br>
     * String length: inclusive between 5 and 5<br>
     */
    @SerializedName(value = "ZipCode")
    private String zipCode = null;
    
    /**Localidad (En facturama web a veces utilizado como ciudad)<br>
     * String length: inclusive between 0 and 80<br>
     */
    @SerializedName(value = "Locality")
    private String locality = null;
    
    /**Municipio<br>
     * Required<br>
     * String length: inclusive between 1 and 100<br>
     */
    @SerializedName(value = "Municipality")
    private String municipality = null;
    
    /**Estado<br>
     * Required<br>
     * String length: inclusive between 1 and 50<br>
     */
    @SerializedName(value = "State")
    private String State = null;
    
    @SerializedName(value = "Country")
    private String country = null;

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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    @Override
    public String toString() {
        return "{\"street\":\"" + street + "\", \"exteriorNumber\":\"" + exteriorNumber + "\", \"interiorNumber\":\""
                + interiorNumber + "\", \"neighborhood\":\"" + neighborhood + "\", \"zipCode\":\"" + zipCode
                + "\", \"locality\":\"" + locality + "\", \"municipality\":\"" + municipality + "\", \"State\":\""
                + State + "\"}";
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
