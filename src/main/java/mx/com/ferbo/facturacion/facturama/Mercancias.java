package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Mercancias<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Mercancias<br>
 * @author esteban
 *
 */
public class Mercancias {
    
    @SerializedName(value = "PesoBrutoTotal")
    private BigDecimal pesoBrutoTotal = null;
    
    /**UnidadPeso<br>
     * Required<br>
     */
    @SerializedName(value = "UnidadPeso")
    private String unidadPeso = null;
    
    @SerializedName(value = "PesoNetoTotal")
    private String pesoNetoTotal = null;
    
    @SerializedName(value = "NumTotalMercancias")
    private Integer numTotalMercancias = null;
    
    @SerializedName(value = "CargoPorTasacion")
    private BigDecimal CargoPorTasacion = null;
    
    /**Mercancia<br>
     * Required<br>
     */
    @SerializedName(value = "Mercancia")
    private Mercancia Mercancia = null;
    
    @SerializedName(value = "Autotransporte")
    private Autotransporte autotransporte = null;
    
    @SerializedName(value = "TransporteMaritimo")
    private TransporteMaritimo transporteMaritimo = null;
    
    @SerializedName(value = "TransporteAereo")
    private TransporteAereo transporteAereo = null;
    
    @SerializedName(value = "TransporteFerroviario")
    private TransporteFerroviario transporteFerroviario = null;

    public BigDecimal getPesoBrutoTotal() {
        return pesoBrutoTotal;
    }

    public void setPesoBrutoTotal(BigDecimal pesoBrutoTotal) {
        this.pesoBrutoTotal = pesoBrutoTotal;
    }

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    public String getPesoNetoTotal() {
        return pesoNetoTotal;
    }

    public void setPesoNetoTotal(String pesoNetoTotal) {
        this.pesoNetoTotal = pesoNetoTotal;
    }

    public Integer getNumTotalMercancias() {
        return numTotalMercancias;
    }

    public void setNumTotalMercancias(Integer numTotalMercancias) {
        this.numTotalMercancias = numTotalMercancias;
    }

    public BigDecimal getCargoPorTasacion() {
        return CargoPorTasacion;
    }

    public void setCargoPorTasacion(BigDecimal cargoPorTasacion) {
        CargoPorTasacion = cargoPorTasacion;
    }

    public Mercancia getMercancia() {
        return Mercancia;
    }

    public void setMercancia(Mercancia mercancia) {
        Mercancia = mercancia;
    }

    public Autotransporte getAutotransporte() {
        return autotransporte;
    }

    public void setAutotransporte(Autotransporte autotransporte) {
        this.autotransporte = autotransporte;
    }

    public TransporteMaritimo getTransporteMaritimo() {
        return transporteMaritimo;
    }

    public void setTransporteMaritimo(TransporteMaritimo transporteMaritimo) {
        this.transporteMaritimo = transporteMaritimo;
    }

    public TransporteAereo getTransporteAereo() {
        return transporteAereo;
    }

    public void setTransporteAereo(TransporteAereo transporteAereo) {
        this.transporteAereo = transporteAereo;
    }

    public TransporteFerroviario getTransporteFerroviario() {
        return transporteFerroviario;
    }

    public void setTransporteFerroviario(TransporteFerroviario transporteFerroviario) {
        this.transporteFerroviario = transporteFerroviario;
    }

    @Override
    public String toString() {
        return "{\"pesoBrutoTotal\":\"" + pesoBrutoTotal + "\", \"unidadPeso\":\"" + unidadPeso
                + "\", \"pesoNetoTotal\":\"" + pesoNetoTotal + "\", \"numTotalMercancias\":\"" + numTotalMercancias
                + "\", \"CargoPorTasacion\":\"" + CargoPorTasacion + "\", \"Mercancia\":\"" + Mercancia
                + "\", \"autotransporte\":\"" + autotransporte + "\", \"transporteMaritimo\":\"" + transporteMaritimo
                + "\", \"transporteAereo\":\"" + transporteAereo + "\", \"transporteFerroviario\":\""
                + transporteFerroviario + "\"}";
    }
}
