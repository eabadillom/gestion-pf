package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

/**Obtiene el archivo de la factura en una sucesión de caracteres base64 en el formato deseado<br>
 * https://www.api.facturama.com.mx/Docs/Api/GET-api-Cfdi-format-type-id<br>
 * @author esteban
 */
public class FileViewModel {
    
    /**Codificación del archivo obtenido
     */
    @SerializedName(value = "ContentEncoding")
    private String contentEncoding = null;
    
    /**Tipo de archivo (pdf, html, xml)
     */
    @SerializedName(value = "ContentType")
    private String contentType = null;
    
    /**Tamaño en bytes
     */
    @SerializedName(value = "ContentLength")
    private Integer contentLength = null;
    
    /**Contenido del archivo
     */
    @SerializedName(value = "Content")
    private String content = null;

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{\"contentEncoding\":\"" + contentEncoding + "\", \"contentType\":\"" + contentType
                + "\", \"contentLength\":\"" + contentLength + "\", \"content\":\"" + content + "\"}";
    }
}
