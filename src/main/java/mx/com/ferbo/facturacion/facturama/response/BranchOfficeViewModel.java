package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

import mx.com.ferbo.facturacion.facturama.Address;

/**Cuerpo del Resultado de una consulta de sucursal<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=BranchOfficeViewModel<br>
 * @author esteban
 *
 */
public class BranchOfficeViewModel {
    
    /**Identificador unico de la sucursal
     */
    @SerializedName(value = "Id")
    private String id = null;
    
    /**Nombre de la sucursal
     */
    @SerializedName(value = "Name")
    private String name = null;
    
    /**Descripcion de la sucursal
     */
    @SerializedName(value = "Description")
    private String description = null;
    
    /**Nodo que enlista los de detalles de la dirección de la sucursal  
     * 
     */
    @SerializedName(value = "Address")
    private Address address = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"description\":\"" + description
                + "\", \"address\":\"" + address + "\"}";
    }
}
