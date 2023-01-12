package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Address<br>
 * Detalles de la Dirección<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Address<br>
 * @author esteban
 *
 */
public class Address {
    /**Calle<br>
     * String length: inclusive between 1 and 100<br>
     */
    @SerializedName(value = "Street")
    private String street = null;
    
    /**Numero Exterior<br>
     * String length: inclusive between 1 and 30<br>
     */
    @SerializedName(value = "ExteriorNumber")
    private String exteriorNumber = null;
    
    /**Numero Interior
     * String length: inclusive between 0 and 30
     */
    @SerializedName(value = "InteriorNumber")
    private String interiorNumber = null;
    
    /**Colonia<br>
     * String length: inclusive between 1 and 80<br>
     */
    @SerializedName(value = "Neighborhood")
    private String neighborhood = null;
    
    /**Código Postal<br>
     * Data type: PostalCode<br>
     * String length: inclusive between 0 and 20<br>
     */
    @SerializedName(value = "ZipCode")
    private String zipCode = null;
    
    /**Localidad (En facturama web a veces utilizado como ciudad)<br>
     * String length: inclusive between 0 and 80<br>
     */
    @SerializedName(value = "Locality")
    private String locality = null;
    
    /**Municipio<br>
     * String length: inclusive between 1 and 100<br>
     */
    @SerializedName(value = "Municipality")
    private String municipality = null;
    
    /**Estado<br>
     */
    @SerializedName(value = "State")
    private String state = null;
    
    /**Pais
     * String length: inclusive between 1 and 50<br>
     */
    @SerializedName(value = "Country")
    private String country = null;
    
    /**Id de la Branch Office<br>
     */
    @SerializedName(value = "Id")
    private String id = null;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{\"street\":\"" + street + "\", \"exteriorNumber\":\"" + exteriorNumber + "\", \"interiorNumber\":\""
                + interiorNumber + "\", \"neighborhood\":\"" + neighborhood + "\", \"zipCode\":\"" + zipCode
                + "\", \"locality\":\"" + locality + "\", \"municipality\":\"" + municipality + "\", \"state\":\""
                + state + "\", \"country\":\"" + country + "\", \"id\":\"" + id + "\"}";
    }
}
