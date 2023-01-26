package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Clase que representa un CFDI relacionado a otro documento CFDI, a través de su UUID.
 * @author esteban
 */
public class CfdiRelation {
    
    /**Matching regular expression pattern: ^[a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}$ 
     */
    @SerializedName(value = "Uuid")
    private String uuid = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "{\"uuid\":\"" + uuid + "\"}";
    }
}
