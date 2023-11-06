package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**TransporteAereo<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=TransporteAereo<br>
 * @author esteban
 *
 */
public class TransporteAereo {
    
    /**Required<br>
     */
    @SerializedName(value = "PermSCT")
    private String permSCT = null;
    
    /**Required<br>
     * Matching regular expression pattern: [^|]{1,50}<br>
     */
    @SerializedName(value = "NumPermisoSCT")
    private String numPermisoSCT = null;
    
    /**Required<br>
     * Matching regular expression pattern: ([A-Z]|[0-9]|-|){5,10}<br>
     */
    @SerializedName(value = "MatriculaAeronave")
    private String matriculaAeronave = null;
    
    /**Matching regular expression pattern: [^|]{3,50}<br>
     */
    @SerializedName(value = "NombreAseg")
    private String nombreAseg = null;
    
    /**Matching regular expression pattern: [^|]{3,30}<br>
     */
    @SerializedName(value = "NumPolizaSeguro")
    private String numPolizaSeguro = null;
    
    /**Required <br>
     * Matching regular expression pattern: [^|]{12,15}<br>
     */
    @SerializedName(value = "NumeroGuia")
    private String numeroGuia = null;
    
    @SerializedName(value = "LugarContrato")
    private String lugarContrato = null;
    
    /**Required<br>
     */
    @SerializedName(value = "CodigoTransportista")
    private String codigoTransportista = null;
    
    @SerializedName(value = "NumRegIdTribTranspor")
    private String numRegIdTribTranspor = null;
    
    @SerializedName(value = "ResidenciaFiscalTranspor")
    private String residenciaFiscalTranspor = null;
    
    @SerializedName(value = "NombreTransportista")
    private String nombreTransportista = null; 
    
    @SerializedName(value = "RFCEmbarcador")
    private String rfcEmbarcador = null;
    
    @SerializedName(value = "NumRegIdTribEmbarc")
    private String numRegIdTribEmbarc = null;
    
    @SerializedName(value = "ResidenciaFiscalEmbarc")
    private String residenciaFiscalEmbarc = null;
    
    @SerializedName(value = "NombreEmbarcador")
    private String nombreEmbarcador = null;

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

    public String getMatriculaAeronave() {
        return matriculaAeronave;
    }

    public void setMatriculaAeronave(String matriculaAeronave) {
        this.matriculaAeronave = matriculaAeronave;
    }

    public String getNombreAseg() {
        return nombreAseg;
    }

    public void setNombreAseg(String nombreAseg) {
        this.nombreAseg = nombreAseg;
    }

    public String getNumPolizaSeguro() {
        return numPolizaSeguro;
    }

    public void setNumPolizaSeguro(String numPolizaSeguro) {
        this.numPolizaSeguro = numPolizaSeguro;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getLugarContrato() {
        return lugarContrato;
    }

    public void setLugarContrato(String lugarContrato) {
        this.lugarContrato = lugarContrato;
    }

    public String getCodigoTransportista() {
        return codigoTransportista;
    }

    public void setCodigoTransportista(String codigoTransportista) {
        this.codigoTransportista = codigoTransportista;
    }

    public String getNumRegIdTribTranspor() {
        return numRegIdTribTranspor;
    }

    public void setNumRegIdTribTranspor(String numRegIdTribTranspor) {
        this.numRegIdTribTranspor = numRegIdTribTranspor;
    }

    public String getResidenciaFiscalTranspor() {
        return residenciaFiscalTranspor;
    }

    public void setResidenciaFiscalTranspor(String residenciaFiscalTranspor) {
        this.residenciaFiscalTranspor = residenciaFiscalTranspor;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public String getRfcEmbarcador() {
        return rfcEmbarcador;
    }

    public void setRfcEmbarcador(String rfcEmbarcador) {
        this.rfcEmbarcador = rfcEmbarcador;
    }

    public String getNumRegIdTribEmbarc() {
        return numRegIdTribEmbarc;
    }

    public void setNumRegIdTribEmbarc(String numRegIdTribEmbarc) {
        this.numRegIdTribEmbarc = numRegIdTribEmbarc;
    }

    public String getResidenciaFiscalEmbarc() {
        return residenciaFiscalEmbarc;
    }

    public void setResidenciaFiscalEmbarc(String residenciaFiscalEmbarc) {
        this.residenciaFiscalEmbarc = residenciaFiscalEmbarc;
    }

    public String getNombreEmbarcador() {
        return nombreEmbarcador;
    }

    public void setNombreEmbarcador(String nombreEmbarcador) {
        this.nombreEmbarcador = nombreEmbarcador;
    }

    @Override
    public String toString() {
        return "{\"permSCT\":\"" + permSCT + "\", \"numPermisoSCT\":\"" + numPermisoSCT + "\", \"matriculaAeronave\":\""
                + matriculaAeronave + "\", \"nombreAseg\":\"" + nombreAseg + "\", \"numPolizaSeguro\":\""
                + numPolizaSeguro + "\", \"numeroGuia\":\"" + numeroGuia + "\", \"lugarContrato\":\"" + lugarContrato
                + "\", \"codigoTransportista\":\"" + codigoTransportista + "\", \"numRegIdTribTranspor\":\""
                + numRegIdTribTranspor + "\", \"residenciaFiscalTranspor\":\"" + residenciaFiscalTranspor
                + "\", \"nombreTransportista\":\"" + nombreTransportista + "\", \"rfcEmbarcador\":\"" + rfcEmbarcador
                + "\", \"numRegIdTribEmbarc\":\"" + numRegIdTribEmbarc + "\", \"residenciaFiscalEmbarc\":\""
                + residenciaFiscalEmbarc + "\", \"nombreEmbarcador\":\"" + nombreEmbarcador + "\"}";
    }
}
