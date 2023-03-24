package mx.com.ferbo.facturacion.facturama;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**Autotransporte<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Autotransporte<br>
 * @author esteban
 *
 */
public class Autotransporte {
    
    /**PermSCT<br>
     * Required<br>
     */
    @SerializedName(value = "PermSCT")
    private String permSCT = null;
    
    /**NumPermisoSCT<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,50}<br>
     */
    @SerializedName(value = "NumPermisoSCT")
    private String numPermisoSCT = null;
    
    /**Seguros<br>
     * Required<br>
     */
    @SerializedName(value = "Seguros")
    private Seguros seguros = null;
    
    /**IdentificacionVehicular<br>
     * Required<br>
     */
    @SerializedName(value = "IdentificacionVehicular")
    private IdentificacionVehicular identificacionVehicular = null;
    
    @SerializedName(value = "Remolques")
    private List<Remolque> remolques = null;

    public String getPermSCT() {
        return permSCT;
    }

    public void setPermSCT(String permSCT) {
        this.permSCT = permSCT;
    }

    public String getNumPermisoSCT() {
        return numPermisoSCT;
    }

    public void setNumPermisoSCT(String numPermisoSCT) {
        this.numPermisoSCT = numPermisoSCT;
    }

    public Seguros getSeguros() {
        return seguros;
    }

    public void setSeguros(Seguros seguros) {
        this.seguros = seguros;
    }

    public IdentificacionVehicular getIdentificacionVehicular() {
        return identificacionVehicular;
    }

    public void setIdentificacionVehicular(IdentificacionVehicular identificacionVehicular) {
        this.identificacionVehicular = identificacionVehicular;
    }

    public List<Remolque> getRemolques() {
        return remolques;
    }

    public void setRemolques(List<Remolque> remolques) {
        this.remolques = remolques;
    }

    @Override
    public String toString() {
        return "{\"permSCT\":\"" + permSCT + "\", \"numPermisoSCT\":\"" + numPermisoSCT + "\", \"seguros\":\"" + seguros
                + "\", \"identificacionVehicular\":\"" + identificacionVehicular + "\", \"remolques\":\"" + remolques
                + "\"}";
    }
}
