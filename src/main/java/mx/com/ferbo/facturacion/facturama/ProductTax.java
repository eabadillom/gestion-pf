package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

public class ProductTax {
    @SerializedName(value="Name")
    private String name = null;
    
    @SerializedName(value="Rate")
    private BigDecimal rate = null;
    
    @SerializedName(value="IsRetention")
    private Boolean isRetention = null;
    
    @SerializedName(value="IsFederalTax")
    private Boolean isFederalTax = null;
    
    @SerializedName(value="IsQuota")
    private Boolean isQuota = null;
    
    @SerializedName(value="Total")
    private BigDecimal total = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getIsFederalTax() {
        return isFederalTax;
    }

    public void setIsFederalTax(Boolean isFederalTax) {
        this.isFederalTax = isFederalTax;
    }

    public Boolean getIsQuota() {
        return isQuota;
    }

    public void setIsQuota(Boolean isQuota) {
        this.isQuota = isQuota;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\", \"rate\":\"" + rate + "\", \"isRetention\":\"" + isRetention
                + "\", \"isFederalTax\":\"" + isFederalTax + "\", \"isQuota\":\"" + isQuota + "\", \"total\":\"" + total
                + "\"}";
    }
}
