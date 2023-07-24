package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Carro<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Carro<br>
 * @author esteban
 *
 */
public class Carro {
    
    /**Required
     */
    @SerializedName(value = "TipoCarro")
    private String tipoCarro = null;
    
    /**Required
     */
    @SerializedName(value = "MatriculaCarro")
    private String matriculaCarro = null;
    
    /**Required
     * 
     */
    @SerializedName(value = "GuiaCarro")
    private String guiaCarro = null;
    
    /**Required
     */
    @SerializedName(value = "ToneladasNetasCarro")
    private BigDecimal toneladasNetasCarro = null;
    
    @SerializedName(value = "Contenedor")
    private CarroContenedor contenedor = null;

    public String getTipoCarro() {
        return tipoCarro;
    }

    public void setTipoCarro(String tipoCarro) {
        this.tipoCarro = tipoCarro;
    }

    public String getMatriculaCarro() {
        return matriculaCarro;
    }

    public void setMatriculaCarro(String matriculaCarro) {
        this.matriculaCarro = matriculaCarro;
    }

    public String getGuiaCarro() {
        return guiaCarro;
    }

    public void setGuiaCarro(String guiaCarro) {
        this.guiaCarro = guiaCarro;
    }

    public BigDecimal getToneladasNetasCarro() {
        return toneladasNetasCarro;
    }

    public void setToneladasNetasCarro(BigDecimal toneladasNetasCarro) {
        this.toneladasNetasCarro = toneladasNetasCarro;
    }

    public CarroContenedor getContenedor() {
        return contenedor;
    }

    public void setContenedor(CarroContenedor contenedor) {
        this.contenedor = contenedor;
    }

    @Override
    public String toString() {
        return "{\"tipoCarro\":\"" + tipoCarro + "\", \"matriculaCarro\":\"" + matriculaCarro + "\", \"guiaCarro\":\""
                + guiaCarro + "\", \"toneladasNetasCarro\":\"" + toneladasNetasCarro + "\", \"contenedor\":\""
                + contenedor + "\"}";
    }
}
