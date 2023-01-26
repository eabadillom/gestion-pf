package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**ActionsOrTitles<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ActionsOrTitles<br>
 * @author esteban
 *
 */
public class ActionsOrTitles {
    
    /**MarketValue<br>
     * Required<br>
     */
    @SerializedName(value = "MarketValue")
    private BigDecimal MarketValue = null;
    
    /**PriceWhenGranting<br>
     * Required<br>
     */
    @SerializedName(value = "PriceWhenGranting")
    private BigDecimal PriceWhenGranting = null;

    public BigDecimal getMarketValue() {
        return MarketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        MarketValue = marketValue;
    }

    public BigDecimal getPriceWhenGranting() {
        return PriceWhenGranting;
    }

    public void setPriceWhenGranting(BigDecimal priceWhenGranting) {
        PriceWhenGranting = priceWhenGranting;
    }

    @Override
    public String toString() {
        return "{\"MarketValue\":\"" + MarketValue + "\", \"PriceWhenGranting\":\"" + PriceWhenGranting + "\"}";
    }
}
