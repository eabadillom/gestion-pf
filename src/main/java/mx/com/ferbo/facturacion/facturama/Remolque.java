package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Remolque<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Remolque<br>
 * @author esteban
 *
 */
public class Remolque {
    
    /**SubTipoRem<br>
     * Required<br>
     */
    @SerializedName(value = "SubTipoRem")
    private String subTipoRem = null;
    
    /**Placa<br>
     * Required<br>
     * Matching regular expression pattern: [^(?!.*\s)-]{6,7}<br>
     */
    @SerializedName(value = "Placa")
    private String placa = null;

    public String getSubTipoRem() {
        return subTipoRem;
    }

    public void setSubTipoRem(String subTipoRem) {
        this.subTipoRem = subTipoRem;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return "{\"subTipoRem\":\"" + subTipoRem + "\", \"placa\":\"" + placa + "\"}";
    }
}
