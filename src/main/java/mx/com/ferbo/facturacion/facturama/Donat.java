package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Nodo opcional para incluir la información requerida por el Servicio de Administración Tributaria a las organizaciones civiles o fideicomisos autorizados para recibir donativos, que permite hacer deducibles los Comprobantes Fiscales Digitales (CFD) y Comprobantes Fiscales Digitales a través de Internet (CFDI) a los donantes.<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Donat<br>
 * @author esteban
 *
 */
public class Donat {
    
    /**Atributo requerido para expresar el número del oficio en que se haya informado a la organización civil o fideicomiso, la procedencia de la autorización para recibir donativos deducibles, o su renovación correspondiente otorgada por el Servicio de Administración Tributaria.<br>
     * Required<br>
     */
    @SerializedName(value = "AuthorizationNumber")
    private String AuthorizationNumber = null;
    
    /**Atributo requerido para expresar la fecha del oficio en que se haya informado a la organización civil o fideicomiso, la procedencia de la autorización para recibir donativos deducibles, o su renovación correspondiente otorgada por el Servicio de Administración Tributaria.<br>
     * Required<br>
     */
    @SerializedName(value = "AuthorizationDate")
    private String AuthorizationDate = null;
    
    /**Atributo requerido para señalar de manera expresa que el comprobante que se expide se deriva de un donativo.<br>
     * Required<br>
     */
    @SerializedName(value = "Legend")
    private String legend = null;

    public String getAuthorizationNumber() {
        return AuthorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        AuthorizationNumber = authorizationNumber;
    }

    public String getAuthorizationDate() {
        return AuthorizationDate;
    }

    public void setAuthorizationDate(String authorizationDate) {
        AuthorizationDate = authorizationDate;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    @Override
    public String toString() {
        return "{\"AuthorizationNumber\":\"" + AuthorizationNumber + "\", \"AuthorizationDate\":\"" + AuthorizationDate
                + "\", \"legend\":\"" + legend + "\"}";
    }
}
