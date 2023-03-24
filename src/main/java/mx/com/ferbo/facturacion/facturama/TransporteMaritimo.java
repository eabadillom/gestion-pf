package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**TransporteMaritimo<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=TransporteMaritimo<br>
 * @author esteban
 *
 */
public class TransporteMaritimo {
    
    @SerializedName(value = "PermSCT")
    private String permSCT = null;
    
    @SerializedName(value = "NumPermisoSCT")
    private String numPermisoSCT = null;
    
    @SerializedName(value = "NombreAseg")
    private String nombreAseg = null;
    
    @SerializedName(value = "NumPolizaSeguro")
    private String numPolizaSeguro = null;
    
    @SerializedName(value = "TipoEmbarcacion")
    private String tipoEmbarcacion = null;
    
    @SerializedName(value = "Matricula")
    private String matricula = null;
    
    @SerializedName(value = "NumeroOMI")
    private String numeroOMI = null;
    
    @SerializedName(value = "AnioEmbarcacion")
    private Integer anioEmbarcacion = null;
    
    @SerializedName(value = "NombreEmbarc")
    private String nombreEmbarc = null;
    
    /**Required
     * 
     */
    @SerializedName(value = "NacionalidadEmbarc")
    private String nacionalidadEmbarc = null;
    
    /**Required
     * 
     */
    @SerializedName(value = "UnidadesDeArqBruto")
    private BigDecimal unidadesDeArqBruto = null;
    
    /**Required
     * 
     */
    @SerializedName(value = "TipoCarga")
    private String tipoCarga = null;
    
    /**Required
     * 
     */
    @SerializedName(value = "NumCertITC")
    private String numCertITC = null;
    
    @SerializedName(value = "Eslora")
    private BigDecimal eslora = null;
    
    @SerializedName(value = "Manga")
    private BigDecimal manga = null;
    
    @SerializedName(value = "Calado")
    private BigDecimal calado = null;
    
    @SerializedName(value = "LineaNaviera")
    private String lineaNaviera = null;
    
    /**Required
     * 
     */
    @SerializedName(value = "NombreAgenteNaviero")
    private String nombreAgenteNaviero = null;
    
    /**Required
     * 
     */
    @SerializedName(value = "NumAutorizacionNaviero")
    private String numAutorizacionNaviero = null;
    
    @SerializedName(value = "NumViaje")
    private String numViaje = null;
    
    @SerializedName(value = "NumConocEmbarc")
    private String numConocEmbarc = null;
    
    @SerializedName(value = "Contenedor")
    private Contenedor contenedor = null;

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

    public String getTipoEmbarcacion() {
        return tipoEmbarcacion;
    }

    public void setTipoEmbarcacion(String tipoEmbarcacion) {
        this.tipoEmbarcacion = tipoEmbarcacion;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNumeroOMI() {
        return numeroOMI;
    }

    public void setNumeroOMI(String numeroOMI) {
        this.numeroOMI = numeroOMI;
    }

    public Integer getAnioEmbarcacion() {
        return anioEmbarcacion;
    }

    public void setAnioEmbarcacion(Integer anioEmbarcacion) {
        this.anioEmbarcacion = anioEmbarcacion;
    }

    public String getNombreEmbarc() {
        return nombreEmbarc;
    }

    public void setNombreEmbarc(String nombreEmbarc) {
        this.nombreEmbarc = nombreEmbarc;
    }

    public String getNacionalidadEmbarc() {
        return nacionalidadEmbarc;
    }

    public void setNacionalidadEmbarc(String nacionalidadEmbarc) {
        this.nacionalidadEmbarc = nacionalidadEmbarc;
    }

    public BigDecimal getUnidadesDeArqBruto() {
        return unidadesDeArqBruto;
    }

    public void setUnidadesDeArqBruto(BigDecimal unidadesDeArqBruto) {
        this.unidadesDeArqBruto = unidadesDeArqBruto;
    }

    public String getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(String tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public String getNumCertITC() {
        return numCertITC;
    }

    public void setNumCertITC(String numCertITC) {
        this.numCertITC = numCertITC;
    }

    public BigDecimal getEslora() {
        return eslora;
    }

    public void setEslora(BigDecimal eslora) {
        this.eslora = eslora;
    }

    public BigDecimal getManga() {
        return manga;
    }

    public void setManga(BigDecimal manga) {
        this.manga = manga;
    }

    public BigDecimal getCalado() {
        return calado;
    }

    public void setCalado(BigDecimal calado) {
        this.calado = calado;
    }

    public String getLineaNaviera() {
        return lineaNaviera;
    }

    public void setLineaNaviera(String lineaNaviera) {
        this.lineaNaviera = lineaNaviera;
    }

    public String getNombreAgenteNaviero() {
        return nombreAgenteNaviero;
    }

    public void setNombreAgenteNaviero(String nombreAgenteNaviero) {
        this.nombreAgenteNaviero = nombreAgenteNaviero;
    }

    public String getNumAutorizacionNaviero() {
        return numAutorizacionNaviero;
    }

    public void setNumAutorizacionNaviero(String numAutorizacionNaviero) {
        this.numAutorizacionNaviero = numAutorizacionNaviero;
    }

    public String getNumViaje() {
        return numViaje;
    }

    public void setNumViaje(String numViaje) {
        this.numViaje = numViaje;
    }

    public String getNumConocEmbarc() {
        return numConocEmbarc;
    }

    public void setNumConocEmbarc(String numConocEmbarc) {
        this.numConocEmbarc = numConocEmbarc;
    }

    public Contenedor getContenedor() {
        return contenedor;
    }

    public void setContenedor(Contenedor contenedor) {
        this.contenedor = contenedor;
    }

    @Override
    public String toString() {
        return "{\"permSCT\":\"" + permSCT + "\", \"numPermisoSCT\":\"" + numPermisoSCT + "\", \"nombreAseg\":\""
                + nombreAseg + "\", \"numPolizaSeguro\":\"" + numPolizaSeguro + "\", \"tipoEmbarcacion\":\""
                + tipoEmbarcacion + "\", \"matricula\":\"" + matricula + "\", \"numeroOMI\":\"" + numeroOMI
                + "\", \"anioEmbarcacion\":\"" + anioEmbarcacion + "\", \"nombreEmbarc\":\"" + nombreEmbarc
                + "\", \"nacionalidadEmbarc\":\"" + nacionalidadEmbarc + "\", \"unidadesDeArqBruto\":\""
                + unidadesDeArqBruto + "\", \"tipoCarga\":\"" + tipoCarga + "\", \"numCertITC\":\"" + numCertITC
                + "\", \"eslora\":\"" + eslora + "\", \"manga\":\"" + manga + "\", \"calado\":\"" + calado
                + "\", \"lineaNaviera\":\"" + lineaNaviera + "\", \"nombreAgenteNaviero\":\"" + nombreAgenteNaviero
                + "\", \"numAutorizacionNaviero\":\"" + numAutorizacionNaviero + "\", \"numViaje\":\"" + numViaje
                + "\", \"numConocEmbarc\":\"" + numConocEmbarc + "\", \"contenedor\":\"" + contenedor + "\"}";
    }
}
