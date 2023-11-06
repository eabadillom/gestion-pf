package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Seguros<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Seguros<br>
 * @author esteban
 *
 */
public class Seguros {
    
    /**AseguraRespCivil<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{3,50}<br>
     */
    @SerializedName(value = "AseguraRespCivil")
    private String aseguraRespCivil = null;
    
    /**PolizaRespCivil<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{3,30}<br>
     */
    @SerializedName(value = "PolizaRespCivil")
    private String PolizaRespCivil = null;
    
    /**AseguraMedAmbiente<br>
     * Matching regular expression pattern: [^|]{3,50}<br>
     */
    @SerializedName(value = "AseguraMedAmbiente")
    private String aseguraMedAmbiente = null;
    
    /**PolizaMedAmbiente<br>
     * Matching regular expression pattern: [^|]{3,30}<br>
     */
    @SerializedName(value = "PolizaMedAmbiente")
    private String polizaMedAmbiente = null;
    
    /**AseguraCarga<br>
     * Matching regular expression pattern: [^|]{3,50}<br>
     */
    @SerializedName(value = "AseguraCarga")
    private String aseguraCarga = null;
    
    /**PolizaCarga<br>
     * Matching regular expression pattern: [^|]{3,30}<br>
     */
    @SerializedName(value = "PolizaCarga")
    private String polizaCarga = null;
    
    /**PrimaSeguro<br>
     */
    @SerializedName(value = "PrimaSeguro")
    private BigDecimal primaSeguro = null;

    public String getAseguraRespCivil() {
        return aseguraRespCivil;
    }

    public void setAseguraRespCivil(String aseguraRespCivil) {
        this.aseguraRespCivil = aseguraRespCivil;
    }

    public String getPolizaRespCivil() {
        return PolizaRespCivil;
    }

    public void setPolizaRespCivil(String polizaRespCivil) {
        PolizaRespCivil = polizaRespCivil;
    }

    public String getAseguraMedAmbiente() {
        return aseguraMedAmbiente;
    }

    public void setAseguraMedAmbiente(String aseguraMedAmbiente) {
        this.aseguraMedAmbiente = aseguraMedAmbiente;
    }

    public String getPolizaMedAmbiente() {
        return polizaMedAmbiente;
    }

    public void setPolizaMedAmbiente(String polizaMedAmbiente) {
        this.polizaMedAmbiente = polizaMedAmbiente;
    }

    public String getAseguraCarga() {
        return aseguraCarga;
    }

    public void setAseguraCarga(String aseguraCarga) {
        this.aseguraCarga = aseguraCarga;
    }

    public String getPolizaCarga() {
        return polizaCarga;
    }

    public void setPolizaCarga(String polizaCarga) {
        this.polizaCarga = polizaCarga;
    }

    public BigDecimal getPrimaSeguro() {
        return primaSeguro;
    }

    public void setPrimaSeguro(BigDecimal primaSeguro) {
        this.primaSeguro = primaSeguro;
    }

    @Override
    public String toString() {
        return "{\"aseguraRespCivil\":\"" + aseguraRespCivil + "\", \"PolizaRespCivil\":\"" + PolizaRespCivil
                + "\", \"aseguraMedAmbiente\":\"" + aseguraMedAmbiente + "\", \"polizaMedAmbiente\":\""
                + polizaMedAmbiente + "\", \"aseguraCarga\":\"" + aseguraCarga + "\", \"polizaCarga\":\"" + polizaCarga
                + "\", \"primaSeguro\":\"" + primaSeguro + "\"}";
    }
}
