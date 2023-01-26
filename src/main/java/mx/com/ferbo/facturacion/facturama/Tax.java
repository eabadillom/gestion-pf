package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

public class Tax {
    
    /**Monto total del impuesto<br>
     * Required<br>
     * Data type: Currency<br>
     * Matching regular expression pattern: ^\d+(?:\.\d{1,2})?$<br>
     */
    @SerializedName(value = "Total")
    private BigDecimal total = null;
    
    /**Nombre (IVA|ISR|IEPS|IVA RET|IVA Exento)<br>
     * Required<br>
     * Data type: Text<br>
     */
    @SerializedName(value = "Name")
    private String name = null;
    
    /**Monto base al que se aplica el impuesto<br>
     * Data type: Currency<br>
     * Range: inclusive between 1E-06 and 1,79769313486232E+308<br>
     */
    @SerializedName(value = "Base")
    private BigDecimal base = null;
    
    /**Porcentaje de impuesto<br>
     * Required<br>
     * Data type: Currency<br>
     * Matching regular expression pattern: ^\d+(?:\.\d{1,6})?$<br>
     */
    @SerializedName(value = "Rate")
    private BigDecimal rate = null;
    
    /**Especifica si es una retención<br>
     */
    @SerializedName(value = "IsRetention")
    private Boolean isRetention = null;
    
    /**Especifica si es el impuesto es Cuota, si no se toma como Tasa<br>
     */
    @SerializedName(value = "IsQuota")
    private Boolean isQuota = null;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Boolean getIsRetention() {
        return isRetention;
    }

    public void setIsRetention(Boolean isRetention) {
        this.isRetention = isRetention;
    }

    public Boolean getIsQuota() {
        return isQuota;
    }

    public void setIsQuota(Boolean isQuota) {
        this.isQuota = isQuota;
    }

    @Override
    public String toString() {
        return "{\"total\":\"" + total + "\", \"name\":\"" + name + "\", \"base\":\"" + base + "\", \"rate\":\"" + rate
                + "\", \"isRetention\":\"" + isRetention + "\", \"isQuota\":\"" + isQuota + "\"}";
    }
}
