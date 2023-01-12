package mx.com.ferbo.facturacion.facturama.response;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**TaxInfoModel<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=TaxInfoModel<br>
 * @author esteban
 *
 */
public class TaxInfoModel {
    
    @SerializedName(value = "Total")
    private BigDecimal total;
    
    @SerializedName(value = "Name")
    private String name;
    
    @SerializedName(value = "Rate")
    private Double rate;
    
    @SerializedName(value = "Type")
    private String type;

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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{\"total\":\"" + total + "\", \"name\":\"" + name + "\", \"rate\":\"" + rate + "\", \"type\":\"" + type
                + "\"}";
    }

}
