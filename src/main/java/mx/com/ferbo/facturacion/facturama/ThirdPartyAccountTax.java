package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Impuestos aplicables al complemento terceros<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ThirdPartyAccountTax<br>
 * @author esteban
 *
 */
public class ThirdPartyAccountTax {
    /**Nombre (IVA|ISR|IEPS|IVA RET)<br>
     * Required<br>
     * Matching regular expression pattern: IVA ?RET|ISR|IEPS|IVA<br>
     */
    @SerializedName(value = "Name")
    private String name = null;
    
    /**Obligatorio cuando se trata de IVA o IEPS<br>
     */
    @SerializedName(value = "Rate")
    private BigDecimal Rate = null;
    
    /**Monto total del impuesto<br>
     * Required<br>
     */
    @SerializedName(value = "Amount")
    private BigDecimal amount = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return Rate;
    }

    public void setRate(BigDecimal rate) {
        Rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\", \"Rate\":\"" + Rate + "\", \"amount\":\"" + amount + "\"}";
    }
}
