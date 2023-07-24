package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Tipo definido para expresar domicilios o direcciones<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ThirdPartyAddress<br>
 * @author esteban
 *
 */
public class ThirdPartyAddress {
    /**Este atributo requerido sirve para precisar la avenida, calle, camino o carretera donde se da la ubicación.<br>
     * Required<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "Street")
    private String street = null;
    
    /**Este atributo opcional sirve para expresar el número particular en donde se da la ubicación sobre una calle dada.<br>
     */
    @SerializedName(value = "ExteriorNumber")
    private String exteriorNumber = null;
    
    /**Este atributo opcional sirve para expresar información adicional para especificar la ubicación cuando calle y número exterior(noExterior) no resulten suficientes para determinar la ubicación de forma precisa.<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "InteriorNumber")
    private String interiorNumber = null;
    
    /**Este atributo opcional sirve para precisar la colonia en donde se da la ubicación cuando se desea ser más específico en casos de ubicaciones urbanas.<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "Neighborhood")
    private String neighborhood = null;
    
    /**Atributo opcional que sirve para precisar la ciudad o población donde se da la ubicación.<br>
     *Min length: 1<br> 
     */
    @SerializedName(value = "Locality")
    private String locality = null;
    
    /**Atributo opcional para expresar una referencia de ubicación adicional.<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "Reference")
    private String reference = null;
    
    /**Atributo requerido que sirve para precisar el municipio o delegación(en el caso del Distrito Federal) en donde se da la ubicación.<br>
     * Required<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "Municipality")
    private String municipality = null;
    
    /**Atributo requerido que sirve para asentar el estado o entidad federativa en donde se da la ubicación.<br>
     * Required<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "State")
    private String state = null;
    
    /**Atributo requerido que sirve para asentar el pais en donde se da la ubicación.<br>
     * Required<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "Country")
    private String country = null;
    
    /**Atributo requerido que sirve para asentar el código postal en donde se da la ubicación.<br>
     * Data type: PostalCode<br>
     * String length: inclusive between 5 and 5<br>
     */
    @SerializedName(value = "PostalCode")
    private String postalCode = null;
    
    /**Atributo requerido que sirve para asentar el código postal en donde se da la ubicación.<br>
     * Data type: PostalCode<br>
     * String length: inclusive between 5 and 5<br>
     */
    @SerializedName(value = "ZipCode")
    private String zipCode = null;
}
