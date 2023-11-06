package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Crea una nueva sucursal<br>
 * https://www.api.facturama.com.mx/Docs/Api/POST-api-BranchOffice<br>
 * @author esteban
 *
 */
public class BranchOffice {
    
    /**Nombre de la sucursal (requerido)<br>
     *Required<br>
     *String length: inclusive between 1 and 50<br>
     */
    @SerializedName(value = "Name")
    private String name = null;
    
    /**Descripción (requerido)<br>
     * Required<br>
     * String length: inclusive between 1 and 100<br>
     */
    @SerializedName(value = "Description")
    private String description = null;
    
    /**Nodo para enlistar los de detalles de la dirección fiscal (requerido)
     * Required
     */
    @SerializedName(value = "Address")
    private TaxAddress address = null;

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

    public TaxAddress getAddress() {
        return address;
    }

    public void setAddress(TaxAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\", \"description\":\"" + description + "\", \"address\":\"" + address + "\"}";
    }
}
