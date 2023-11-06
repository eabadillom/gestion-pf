package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Compensation<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Compensation<br>
 * @author esteban
 *
 */
public class Compensation {
    
    
    /**PositiveBalance<br>
     * Required<br>
     */
    @SerializedName(value = "PositiveBalance")
    private BigDecimal positiveBalance = null;
    
    /**Year<br>
     * Required<br>
     */
    @SerializedName(value = "Year")
    private Integer year = null;
    
    /**RemainingPositiveBalance<br>
     * Required<br>
     */
    @SerializedName(value = "RemainingPositiveBalance")
    private BigDecimal remainingPositiveBalance = null;

    public BigDecimal getPositiveBalance() {
        return positiveBalance;
    }

    public void setPositiveBalance(BigDecimal positiveBalance) {
        this.positiveBalance = positiveBalance;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getRemainingPositiveBalance() {
        return remainingPositiveBalance;
    }

    public void setRemainingPositiveBalance(BigDecimal remainingPositiveBalance) {
        this.remainingPositiveBalance = remainingPositiveBalance;
    }

    @Override
    public String toString() {
        return "{\"positiveBalance\":\"" + positiveBalance + "\", \"year\":\"" + year
                + "\", \"remainingPositiveBalance\":\"" + remainingPositiveBalance + "\"}";
    }
}
