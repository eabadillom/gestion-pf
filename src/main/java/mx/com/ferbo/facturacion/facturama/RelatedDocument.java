package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Documentos Relacionados al Pago<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=RelatedDocument<br>
 * @author esteban
 *
 */
public class RelatedDocument {
    
    /**Required<br>
     * Matching regular expression pattern: [a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}<br>
     */
    @SerializedName(value = "Uuid")
    private String uuid = null;
    
    /**Serie<br>
     */
    @SerializedName(value = "Serie")
    private String serie = null;
    
    /**Folio<br>
     */
    @SerializedName(value = "Folio")
    private String folio = null;
    
    /**Currency<br>
     */
    @SerializedName(value = "Currency")
    private String currency = null;
    
    /**ExchangeRate<br>
     * Matching regular expression pattern: [0-9]{1,18}(.[0-9]{1,6})?<br>
     */
    @SerializedName(value = "ExchangeRate")
    private BigDecimal exchangeRate = null;
    
    /**PaymentMethod<br>
     * Required<br>
     * Matching regular expression pattern: PUE|PIP|PPD<br>
     */
    @SerializedName(value = "PaymentMethod")
    private String paymentMethod = null;
    
    /**PartialityNumber<br>
     * Matching regular expression pattern: [1-9][0-9]{0,2}<br>
     */
    @SerializedName(value = "PartialityNumber")
    private Integer partialityNumber = null;
    
    /**PreviousBalanceAmount<br>
     */
    @SerializedName(value = "PreviousBalanceAmount")
    private BigDecimal previousBalanceAmount = null;
    
    /**AmountPaid<br>
     */
    @SerializedName(value = "AmountPaid")
    private BigDecimal amountPaid = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getPartialityNumber() {
        return partialityNumber;
    }

    public void setPartialityNumber(Integer partialityNumber) {
        this.partialityNumber = partialityNumber;
    }

    public BigDecimal getPreviousBalanceAmount() {
        return previousBalanceAmount;
    }

    public void setPreviousBalanceAmount(BigDecimal previousBalanceAmount) {
        this.previousBalanceAmount = previousBalanceAmount;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    @Override
    public String toString() {
        return "{\"uuid\":\"" + uuid + "\", \"serie\":\"" + serie + "\", \"folio\":\"" + folio + "\", \"currency\":\""
                + currency + "\", \"exchangeRate\":\"" + exchangeRate + "\", \"paymentMethod\":\"" + paymentMethod
                + "\", \"partialityNumber\":\"" + partialityNumber + "\", \"previousBalanceAmount\":\""
                + previousBalanceAmount + "\", \"amountPaid\":\"" + amountPaid + "\"}";
    }
}
