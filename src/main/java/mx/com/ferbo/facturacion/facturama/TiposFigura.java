package mx.com.ferbo.facturacion.facturama;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**TiposFigura<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=TiposFigura<br>
 * @author esteban
 *
 */
public class TiposFigura {
    
    @SerializedName(value = "PartesTransporte")
    private List<PartesTransporte> partesTransporte = null;
    
    /**Required
     */
    @SerializedName(value = "TipoFigura")
    private String tipoFigura = null;
    
    /**RFCFigura<br>
     * Matching regular expression pattern: [A-Z&Ñ]{3,4}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]<br>
     */
    @SerializedName(value = "RFCFigura")
    private String rfcFigura = null;
    
    /**NumLicencia<br>
     * Matching regular expression pattern: [^|(?!\s)]{6,16}<br>
     */
    @SerializedName(value = "NumLicencia")
    private String numLicencia = null;
    
    /**NombreFigura<br>
     * Matching regular expression pattern: [^|]{1,254}
     */
    @SerializedName(value = "NombreFigura")
    private String nombreFigura = null;
    
    /**NumRegIdTribFigura<br>
     * Matching regular expression pattern: [^|(?!\s)]{6,40}<br>
     */
    @SerializedName(value = "NumRegIdTribFigura")
    private String numRegIdTribFigura = null;
    
    @SerializedName(value = "ResidenciaFiscalFigura")
    private String residenciaFiscalFigura = null;
    
    @SerializedName(value = "Domicilio")
    private Domicilio domicilio = null;

    public List<PartesTransporte> getPartesTransporte() {
        return partesTransporte;
    }

    public void setPartesTransporte(List<PartesTransporte> partesTransporte) {
        this.partesTransporte = partesTransporte;
    }

    public String getTipoFigura() {
        return tipoFigura;
    }

    public void setTipoFigura(String tipoFigura) {
        this.tipoFigura = tipoFigura;
    }

    public String getRfcFigura() {
        return rfcFigura;
    }

    public void setRfcFigura(String rfcFigura) {
        this.rfcFigura = rfcFigura;
    }

    public String getNumLicencia() {
        return numLicencia;
    }

    public void setNumLicencia(String numLicencia) {
        this.numLicencia = numLicencia;
    }

    public String getNombreFigura() {
        return nombreFigura;
    }

    public void setNombreFigura(String nombreFigura) {
        this.nombreFigura = nombreFigura;
    }

    public String getNumRegIdTribFigura() {
        return numRegIdTribFigura;
    }

    public void setNumRegIdTribFigura(String numRegIdTribFigura) {
        this.numRegIdTribFigura = numRegIdTribFigura;
    }

    public String getResidenciaFiscalFigura() {
        return residenciaFiscalFigura;
    }

    public void setResidenciaFiscalFigura(String residenciaFiscalFigura) {
        this.residenciaFiscalFigura = residenciaFiscalFigura;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public String toString() {
        return "{\"partesTransporte\":\"" + partesTransporte + "\", \"tipoFigura\":\"" + tipoFigura
                + "\", \"rfcFigura\":\"" + rfcFigura + "\", \"numLicencia\":\"" + numLicencia
                + "\", \"nombreFigura\":\"" + nombreFigura + "\", \"numRegIdTribFigura\":\"" + numRegIdTribFigura
                + "\", \"residenciaFiscalFigura\":\"" + residenciaFiscalFigura + "\", \"domicilio\":\"" + domicilio
                + "\"}";
    }
}
