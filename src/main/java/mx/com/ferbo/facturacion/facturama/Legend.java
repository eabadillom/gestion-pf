package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Legend
 * Nodo opcional para incluir leyendas previstas en disposiciones fiscales, distintas a las contenidas en el estándar de Comprobante Fiscal Digital(CFD) o Comprobante Fiscal Digital a través de Internet(CFDI).<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Legend<br>
 * @author esteban
 *
 */
public class Legend {
    
    /**Atributo opcional para especificar la Ley, Resolución o Disposición fiscal que regula la leyenda, deberá expresarse en siglas de mayúsculas y sin puntuación(p.ej: ISR).<br>
     * Matching regular expression pattern: [^|]{0,1000}<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "TaxProvision")
    private String taxProvision = null;
    
    /**Atributo opcional para especificar el número de Artículo o en su caso Regla que regula la obligación de la leyenda.<br>
     * Matching regular expression pattern: [^|]{0,1000}<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "Norm")
    private String norm = null;
    
    /**Atributo requerido para especificar la leyenda fiscal<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{0,1000}<br>
     */
    @SerializedName(value = "Text")
    private String text = null;

    public String getTaxProvision() {
        return taxProvision;
    }

    public void setTaxProvision(String taxProvision) {
        this.taxProvision = taxProvision;
    }

    public String getNorm() {
        return norm;
    }

    public void setNorm(String norm) {
        this.norm = norm;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "{\"taxProvision\":\"" + taxProvision + "\", \"norm\":\"" + norm + "\", \"text\":\"" + text + "\"}";
    }
}
