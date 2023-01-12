package mx.com.ferbo.facturacion.facturama;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**ComplementoCartaPorte20<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ComplementoCartaPorte20<br>
 * @author esteban
 *
 */
public class ComplementoCartaPorte20 {
    
    /**TranspInternac<br>
     * Required<br>
     */
    @SerializedName(value = "TranspInternac")
    private String transpInternac = null;
    
    @SerializedName(value = "EntradaSalidaMerc")
    private String entradaSalidaMerc = null;
    
    @SerializedName(value = "PaisOrigenDestino")
    private String paisOrigenDestino = null;
    
    @SerializedName(value = "ViaEntradaSalida")
    private String viaEntradaSalida = null;
    
    @SerializedName(value = "TotalDistRec")
    private String totalDistRec = null; 
    
    @SerializedName(value = "Ubicaciones")
    private List<Ubicacion> ubicaciones = null;
    
    @SerializedName(value = "Mercancias")
    private Mercancias mercancias = null;
    
    /**Required<br>
     * 
     */
    @SerializedName(value = "FiguraTransporte")
    private TiposFigura figuraTransporte = null;

    public String getTranspInternac() {
        return transpInternac;
    }

    public void setTranspInternac(String transpInternac) {
        this.transpInternac = transpInternac;
    }

    public String getEntradaSalidaMerc() {
        return entradaSalidaMerc;
    }

    public void setEntradaSalidaMerc(String entradaSalidaMerc) {
        this.entradaSalidaMerc = entradaSalidaMerc;
    }

    public String getPaisOrigenDestino() {
        return paisOrigenDestino;
    }

    public void setPaisOrigenDestino(String paisOrigenDestino) {
        this.paisOrigenDestino = paisOrigenDestino;
    }

    public String getViaEntradaSalida() {
        return viaEntradaSalida;
    }

    public void setViaEntradaSalida(String viaEntradaSalida) {
        this.viaEntradaSalida = viaEntradaSalida;
    }

    public String getTotalDistRec() {
        return totalDistRec;
    }

    public void setTotalDistRec(String totalDistRec) {
        this.totalDistRec = totalDistRec;
    }

    public List<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<Ubicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public Mercancias getMercancias() {
        return mercancias;
    }

    public void setMercancias(Mercancias mercancias) {
        this.mercancias = mercancias;
    }

    public TiposFigura getFiguraTransporte() {
        return figuraTransporte;
    }

    public void setFiguraTransporte(TiposFigura figuraTransporte) {
        this.figuraTransporte = figuraTransporte;
    }

    @Override
    public String toString() {
        return "{\"transpInternac\":\"" + transpInternac + "\", \"entradaSalidaMerc\":\"" + entradaSalidaMerc
                + "\", \"paisOrigenDestino\":\"" + paisOrigenDestino + "\", \"viaEntradaSalida\":\"" + viaEntradaSalida
                + "\", \"totalDistRec\":\"" + totalDistRec + "\", \"ubicaciones\":\"" + ubicaciones
                + "\", \"mercancias\":\"" + mercancias + "\", \"figuraTransporte\":\"" + figuraTransporte + "\"}";
    }
}
