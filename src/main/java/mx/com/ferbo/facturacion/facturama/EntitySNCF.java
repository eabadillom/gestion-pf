package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**EntitySNCF<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=EntitySNCF<br>
 * @author esteban
 *
 */
public class EntitySNCF {
    
    /**OriginSource<br>
     * Required<br>
     * Matching regular expression pattern: IP|IM|IF<br>
     */
    @SerializedName(value = "OriginSource")
    private String originSource = null;
    
    /**AmountOriginSource<br>
     * Matching regular expression pattern: [0-9]{1,18}(.[0-9]{1,2})?<br>
     */
    @SerializedName(value = "AmountOriginSource")
    private BigDecimal amountOriginSource = null;

    public String getOriginSource() {
        return originSource;
    }

    public void setOriginSource(String originSource) {
        this.originSource = originSource;
    }

    public BigDecimal getAmountOriginSource() {
        return amountOriginSource;
    }

    public void setAmountOriginSource(BigDecimal amountOriginSource) {
        this.amountOriginSource = amountOriginSource;
    }
}
