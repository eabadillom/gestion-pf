package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Contenedor<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Contenedor<br>
 * @author esteban
 *
 */
public class Contenedor {
    
    /**Required
     * 
     */
    @SerializedName(value = "MatriculaContenedor")
    private String matriculaContenedor = null;
    
    /**Required
     * 
     */
    @SerializedName(value = "TipoContenedor")
    private String tipoContenedor = null;
    
    @SerializedName(value = "NumPrecinto")
    private String numPrecinto = null;

    public String getMatriculaContenedor() {
        return matriculaContenedor;
    }

    public void setMatriculaContenedor(String matriculaContenedor) {
        this.matriculaContenedor = matriculaContenedor;
    }

    public String getTipoContenedor() {
        return tipoContenedor;
    }

    public void setTipoContenedor(String tipoContenedor) {
        this.tipoContenedor = tipoContenedor;
    }

    public String getNumPrecinto() {
        return numPrecinto;
    }

    public void setNumPrecinto(String numPrecinto) {
        this.numPrecinto = numPrecinto;
    }

    @Override
    public String toString() {
        return "{\"matriculaContenedor\":\"" + matriculaContenedor + "\", \"tipoContenedor\":\"" + tipoContenedor
                + "\", \"numPrecinto\":\"" + numPrecinto + "\"}";
    }
}
