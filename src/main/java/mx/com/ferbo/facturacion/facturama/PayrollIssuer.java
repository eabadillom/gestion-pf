package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**PayrollIssuer<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=PayrollIssuer<br>
 * @author esteban
 *
 */
public class PayrollIssuer {
    
    /**EntitySNCF
     */
    @SerializedName(value = "EntitySNCF")
    private EntitySNCF EntitySNCF = null;
    
    /**CURP
     * Matching regular expression pattern: ^[A-Z][AEIOUX][A-Z]{2}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[MH]([ABCMTZ]S|[BCJMOT]C|[CNPST]L|[GNQ]T|[GQS]R|C[MH]|[MY]N|[DH]G|NE|VZ|DF|SP)[BCDFGHJ-NP-TV-Z]{3}[0-9A-Z][0-9]$
     * Min length: 18
     */
    @SerializedName(value = "Curp")
    private String curp = null;
    
    /**EmployerRegistration<br>
     * Matching regular expression pattern: ^[^| ]{1,20}$<br>
     */
    @SerializedName(value = "EmployerRegistration")
    private String employerRegistration = null;
    
    /**FromEmployerRfc<br>
     * Matching regular expression pattern: ^[A-Z&Ñ]{3,4}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]$<br>
     */
    @SerializedName(value = "FromEmployerRfc")
    private String fromEmployerRfc = null;

    public EntitySNCF getEntitySNCF() {
        return EntitySNCF;
    }

    public void setEntitySNCF(EntitySNCF entitySNCF) {
        EntitySNCF = entitySNCF;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getEmployerRegistration() {
        return employerRegistration;
    }

    public void setEmployerRegistration(String employerRegistration) {
        this.employerRegistration = employerRegistration;
    }

    public String getFromEmployerRfc() {
        return fromEmployerRfc;
    }

    public void setFromEmployerRfc(String fromEmployerRfc) {
        this.fromEmployerRfc = fromEmployerRfc;
    }

    @Override
    public String toString() {
        return "{\"EntitySNCF\":\"" + EntitySNCF + "\", \"curp\":\"" + curp + "\", \"employerRegistration\":\""
                + employerRegistration + "\", \"fromEmployerRfc\":\"" + fromEmployerRfc + "\"}";
    }
}
