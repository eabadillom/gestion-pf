package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

/**Request de un CSD para ser almacenado en el servidor<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=CsdBindingModel<br>
 * @author esteban
 *
 */
public class CsdBindingModel {

    /**Certificado en base64<br>
     * Required<br>
     */
    @SerializedName(value = "Certificate")
    private String certificate = null;
    
    /**Llave privada en base64<br>
     * Required<br>
     */
    @SerializedName(value = "PrivateKey")
    private String privateKey = null;
    
    /**Contraseña de la Llave privada<br>
     * Required<br>
     */
    @SerializedName(value = "PrivateKeyPassword")
    private String privateKeyPassword = null;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }

    public void setPrivateKeyPassword(String privateKeyPassword) {
        this.privateKeyPassword = privateKeyPassword;
    }

    @Override
    public String toString() {
        return "{\"certificate\":\"" + certificate + "\", \"privateKey\":\"" + privateKey
                + "\", \"privateKeyPassword\":\"" + privateKeyPassword + "\"}";
    }
}
