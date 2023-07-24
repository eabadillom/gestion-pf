package mx.com.ferbo.facturacion.facturama.response;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**ItemInfoModel<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ItemInfoModel<br>
 * @author esteban
 *
 */
public class ItemInfoModel {
    @SerializedName(value = "Quantity")
    private BigDecimal quantity;
    
    @SerializedName(value = "Unit")
    private String unit;
    
    @SerializedName(value = "Description")
    private String description;
    
    @SerializedName(value = "UnitValue")
    private BigDecimal unitValue;
    
    @SerializedName(value = "Total")
    private BigDecimal total;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "{\"quantity\":\"" + quantity + "\", \"unit\":\"" + unit + "\", \"description\":\"" + description
                + "\", \"unitValue\":\"" + unitValue + "\", \"total\":\"" + total + "\"}";
    }
}
