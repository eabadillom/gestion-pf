package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**Mercancia<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Mercancia<br>
 * @author esteban
 *
 */
public class Mercancia {
    
    /**BienesTransp<br>
     * Required<br>
     */
    @SerializedName(value = "BienesTransp")
    private String bienesTransp = null;
    
    /**ClaveSTCC<br>
     * Matching regular expression pattern: [0-9|]{6,7}<br>
     */
    @SerializedName(value = "ClaveSTCC")
    private String claveSTCC = null;
    
    /**Descripcion<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,1000}<br>
     */
    @SerializedName(value = "Descripcion")
    private String descripcion = null;
    
    /**Cantidad<br>
     * Required<br>
     */
    @SerializedName(value = "Cantidad")
    private BigDecimal cantidad = null;
    
    /**ClaveUnidad<br>
     * Required<br>
     */
    @SerializedName(value = "ClaveUnidad")
    private String claveUnidad = null;
    
    
    /**Unidad<br>
     * Matching regular expression pattern: [^|]{1,20}<br>
     */
    @SerializedName(value = "Unidad")
    private String unidad = null;
    
    /**Dimensiones<br>
     * Matching regular expression pattern: ([0-9]{1,3}[/]){2}([0-9]{1,3})(cm|plg)<br>
     */
    @SerializedName(value = "Dimensiones")
    private String dimensiones = null;
    
    @SerializedName(value = "MaterialPeligroso")
    private String materialPeligroso = null;
    
    @SerializedName(value = "CveMaterialPeligroso")
    private String cveMaterialPeligroso = null;
    
    @SerializedName(value = "Embalaje")
    private String embalaje = null;
    
    /**DescripEmbalaje
     * Matching regular expression pattern: [^|]{1,100}
     */
    @SerializedName(value = "DescripEmbalaje")
    private String descripEmbalaje = null;
    
    /**PesoEnKg<br>
     * Required<br>
     * Matching regular expression pattern: ^\d+.?\d{0,3}$ <br>
     */
    @SerializedName(value = "PesoEnKg")
    private BigDecimal pesoEnKg = null;
    
    @SerializedName(value = "ValorMercancia")
    private BigDecimal valorMercancia = null;
    
    @SerializedName(value = "Moneda")
    private String moneda = null;
    
    @SerializedName(value = "FraccionArancelaria")
    private String fraccionArancelaria = null;
    
    /**UUIDComercioExt
     * Matching regular expression pattern: [a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}
     */
    @SerializedName(value = "UUIDComercioExt")
    private String uuidComercioExt = null;
    
    @SerializedName(value = "Pedimentos")
    private List<Pedimentos> pedimentos = null;
    
    @SerializedName(value = "GuiasIdentificacion")
    private List<GuiasIdentificacion> guiasIdentificacion = null;
    
    @SerializedName(value = "CantidadTransporta")
    private List<CantidadTransporta> cantidadTransporta = null;
    
    @SerializedName(value = "DetalleMercancia")
    private DetalleMercancia detalleMercancia = null;

    public String getBienesTransp() {
        return bienesTransp;
    }

    public void setBienesTransp(String bienesTransp) {
        this.bienesTransp = bienesTransp;
    }

    public String getClaveSTCC() {
        return claveSTCC;
    }

    public void setClaveSTCC(String claveSTCC) {
        this.claveSTCC = claveSTCC;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getClaveUnidad() {
        return claveUnidad;
    }

    public void setClaveUnidad(String claveUnidad) {
        this.claveUnidad = claveUnidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public String getMaterialPeligroso() {
        return materialPeligroso;
    }

    public void setMaterialPeligroso(String materialPeligroso) {
        this.materialPeligroso = materialPeligroso;
    }

    public String getCveMaterialPeligroso() {
        return cveMaterialPeligroso;
    }

    public void setCveMaterialPeligroso(String cveMaterialPeligroso) {
        this.cveMaterialPeligroso = cveMaterialPeligroso;
    }

    public String getEmbalaje() {
        return embalaje;
    }

    public void setEmbalaje(String embalaje) {
        this.embalaje = embalaje;
    }

    public String getDescripEmbalaje() {
        return descripEmbalaje;
    }

    public void setDescripEmbalaje(String descripEmbalaje) {
        this.descripEmbalaje = descripEmbalaje;
    }

    public BigDecimal getPesoEnKg() {
        return pesoEnKg;
    }

    public void setPesoEnKg(BigDecimal pesoEnKg) {
        this.pesoEnKg = pesoEnKg;
    }

    public BigDecimal getValorMercancia() {
        return valorMercancia;
    }

    public void setValorMercancia(BigDecimal valorMercancia) {
        this.valorMercancia = valorMercancia;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getFraccionArancelaria() {
        return fraccionArancelaria;
    }

    public void setFraccionArancelaria(String fraccionArancelaria) {
        this.fraccionArancelaria = fraccionArancelaria;
    }

    public String getUuidComercioExt() {
        return uuidComercioExt;
    }

    public void setUuidComercioExt(String uuidComercioExt) {
        this.uuidComercioExt = uuidComercioExt;
    }

    public List<Pedimentos> getPedimentos() {
        return pedimentos;
    }

    public void setPedimentos(List<Pedimentos> pedimentos) {
        this.pedimentos = pedimentos;
    }

    public List<GuiasIdentificacion> getGuiasIdentificacion() {
        return guiasIdentificacion;
    }

    public void setGuiasIdentificacion(List<GuiasIdentificacion> guiasIdentificacion) {
        this.guiasIdentificacion = guiasIdentificacion;
    }

    public List<CantidadTransporta> getCantidadTransporta() {
        return cantidadTransporta;
    }

    public void setCantidadTransporta(List<CantidadTransporta> cantidadTransporta) {
        this.cantidadTransporta = cantidadTransporta;
    }

    public DetalleMercancia getDetalleMercancia() {
        return detalleMercancia;
    }

    public void setDetalleMercancia(DetalleMercancia detalleMercancia) {
        this.detalleMercancia = detalleMercancia;
    }

    @Override
    public String toString() {
        return "{\"bienesTransp\":\"" + bienesTransp + "\", \"claveSTCC\":\"" + claveSTCC + "\", \"descripcion\":\""
                + descripcion + "\", \"cantidad\":\"" + cantidad + "\", \"claveUnidad\":\"" + claveUnidad
                + "\", \"unidad\":\"" + unidad + "\", \"dimensiones\":\"" + dimensiones + "\", \"materialPeligroso\":\""
                + materialPeligroso + "\", \"cveMaterialPeligroso\":\"" + cveMaterialPeligroso + "\", \"embalaje\":\""
                + embalaje + "\", \"descripEmbalaje\":\"" + descripEmbalaje + "\", \"pesoEnKg\":\"" + pesoEnKg
                + "\", \"valorMercancia\":\"" + valorMercancia + "\", \"moneda\":\"" + moneda
                + "\", \"fraccionArancelaria\":\"" + fraccionArancelaria + "\", \"uuidComercioExt\":\""
                + uuidComercioExt + "\", \"pedimentos\":\"" + pedimentos + "\", \"guiasIdentificacion\":\""
                + guiasIdentificacion + "\", \"cantidadTransporta\":\"" + cantidadTransporta
                + "\", \"detalleMercancia\":\"" + detalleMercancia + "\"}";
    }
}
