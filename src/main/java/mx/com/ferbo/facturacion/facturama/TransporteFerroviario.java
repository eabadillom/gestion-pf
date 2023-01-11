package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**TransporteFerroviario<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=TransporteFerroviario<br>
 * @author esteban
 *
 */
public class TransporteFerroviario {
    
    /**Required<br>
     */
    @SerializedName(value = "TipoDeServicio")
    private String tipoDeServicio = null;
    
    @SerializedName(value = "NombreAseg")
    private String nombreAseg = null;
    
    @SerializedName(value = "NumPolizaSeguro")
    private String numPolizaSeguro = null;
    
    @SerializedName(value = "Concesionario")
    private String concesionario = null;
    
    @SerializedName(value = "DerechosDePaso")
    private DerechosDePaso derechosDePaso = null;
    
    /**Required
     */
    @SerializedName(value = "Carro")
    private Carro carro = null;

    public String getTipoDeServicio() {
        return tipoDeServicio;
    }

    public void setTipoDeServicio(String tipoDeServicio) {
        this.tipoDeServicio = tipoDeServicio;
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

    public String getConcesionario() {
        return concesionario;
    }

    public void setConcesionario(String concesionario) {
        this.concesionario = concesionario;
    }

    public DerechosDePaso getDerechosDePaso() {
        return derechosDePaso;
    }

    public void setDerechosDePaso(DerechosDePaso derechosDePaso) {
        this.derechosDePaso = derechosDePaso;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    @Override
    public String toString() {
        return "{\"tipoDeServicio\":\"" + tipoDeServicio + "\", \"nombreAseg\":\"" + nombreAseg
                + "\", \"numPolizaSeguro\":\"" + numPolizaSeguro + "\", \"concesionario\":\"" + concesionario
                + "\", \"derechosDePaso\":\"" + derechosDePaso + "\", \"carro\":\"" + carro + "\"}";
    }
}
