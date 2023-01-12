package mx.com.ferbo.facturacion.facturama.response;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**CfdiInfoModel<br>
 * Cuerpo del resultado de consulta de un CFDI<br>
 * @see mx.com.ferbo.facturacion.facturama.CFDIInfo
 * @author esteban
 *
 */
public class CfdiInfoModel {
    
    /**Identifiacador unico del cfdi<br>
     */
    @SerializedName(value = "Id")
    private String id = null;
    
    /**Tipo del efecto del comprobante fiscal para el contribuyente emisor: ingreso, egreso ó traslado<br>
     */
    @SerializedName(value = "CfdiType")
    private String cfdiType = null;
    
    /**Tipo de comprobante segun catalogo del SAT<br>
     * 
     */
    @SerializedName(value = "Type")
    private String type = null;
    
    /**Numero de la serie en el control interno del contribuyente<br>
     */
    @SerializedName(value = "Serie")
    private String serie = null;
    
    /**Numero de la folio en el control interno del contribuyente<br>
     * 
     */
    @SerializedName(value = "Folio")
    private String folio = null;
    
    /**Fecha y hora de expedición del comprobante fiscal<br>
     * 
     */
    @SerializedName(value = "Date")
    private String date = null;
    
    /**No del Certificado
     */
    @SerializedName(value = "CertNumber")
    private String certNumber = null;
    
    /**Forma de pago
     */
    @SerializedName(value = "PaymentTerms")
    private String paymentTerms = null;
    
    /**Condiciones comerciales aplicables para el pago del comprobante fiscal digital a través de Internet<br>
     * 
     */
    @SerializedName(value = "PaymentConditions")
    private String paymentConditions = null;
    
    /**Método de pago de los bienes o servicios amparados por el comprobante. Se entiende como método de pago leyendas tales como: cheque, tarjeta de crédito o debito, depósito en cuenta, etc<br>
     * 
     */
    @SerializedName(value = "PaymentMethod")
    private String PaymentMethod = null;
    
    /**Incorpora al menos los cuatro últimos digitos del número de cuenta con la que se realizó el pago<br>
     */
    @SerializedName(value = "PaymentAccountNumber")
    private String paymentAccountNumber = null;
    
    /**Nombre del banco donde se realizo el pago<br>
     * 
     */
    @SerializedName(value = "PaymentBankName")
    private String paymentBankName = null;
    
    /**Lugar de expedición del comprobante.<br>
     */
    @SerializedName(value = "ExpeditionPlace")
    private String expeditionPlace = null;
    
    /**Tipo de cambio conforme a la moneda usada
     */
    @SerializedName(value = "ExchangeRate")
    private BigDecimal exchangeRate = null;
    
    /**Moneda utilizada para expresar los montos<br>
     */
    @SerializedName(value = "Currency")
    private String currency = null;
    
    /**Representa la suma de los importes antes de descuentos e impuestos<br>
     */
    @SerializedName(value = "Subtotal")
    private BigDecimal subtotal = null;
    
    /**Representa el importe total de los descuentos aplicables antes de impuestos<br>
     */
    @SerializedName(value = "Discount")
    private BigDecimal discount = null;
    
    /**Representar la suma del subtotal, menos los descuentos aplicables, más los impuestos trasladados, menos los impuestos retenidos<br>
     */
    @SerializedName(value = "Total")
    private BigDecimal total = null;
    
    /**Observaciones no fiscales de la factura<br>
     */
    @SerializedName(value = "Observations")
    private String observations = null;
    
    /**Observaciones no fiscales de la factura<br>
     */
    @SerializedName(value = "OrderNumber")
    private String orderNumber = null;
    
    /**Nodo que contiene el detalle del emisor
     */
    @SerializedName(value = "Issuer")
    private TaxEntityInfoViewModel Issuer = null;
    
    /**Nodo que contiene el detalle del receptor
     */
    @SerializedName(value = "Receiver")
    private ReceiverViewModel receiver = null;
    
    /**Nodo que contiene el detalle de los conceptos
     */
    @SerializedName(value = "Items")
    private List<ItemInfoModel> items = null;
    
    /**Nodo que contiene el detalle de los impuestos
     */
    @SerializedName(value ="Taxes")
    private List<TaxInfoModel> taxes = null;
    
    /**Nodo que contiene complementos de extensión definidos por el SAT<br>
     */
    @SerializedName(value = "Complement")
    private ComplementModel complement = null;
    
    @SerializedName(value = "Complements")
    private Complements complements = null;
    
    /**Estatus de la factura
     */
    @SerializedName(value = "Status")
    private String status = null;
    
    /**Cadena original<br>
     */
    @SerializedName(value = "OriginalString")
    private String originalString = null;   
    
    /**Determina si esta factura se puede pagar<br>
     */
    @SerializedName(value = "IsPayableBy")
    private String isPayableBy = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCfdiType() {
        return cfdiType;
    }

    public void setCfdiType(String cfdiType) {
        this.cfdiType = cfdiType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getPaymentConditions() {
        return paymentConditions;
    }

    public void setPaymentConditions(String paymentConditions) {
        this.paymentConditions = paymentConditions;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getPaymentAccountNumber() {
        return paymentAccountNumber;
    }

    public void setPaymentAccountNumber(String paymentAccountNumber) {
        this.paymentAccountNumber = paymentAccountNumber;
    }

    public String getPaymentBankName() {
        return paymentBankName;
    }

    public void setPaymentBankName(String paymentBankName) {
        this.paymentBankName = paymentBankName;
    }

    public String getExpeditionPlace() {
        return expeditionPlace;
    }

    public void setExpeditionPlace(String expeditionPlace) {
        this.expeditionPlace = expeditionPlace;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public TaxEntityInfoViewModel getIssuer() {
        return Issuer;
    }

    public void setIssuer(TaxEntityInfoViewModel issuer) {
        Issuer = issuer;
    }

    public ReceiverViewModel getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverViewModel receiver) {
        this.receiver = receiver;
    }

    public List<ItemInfoModel> getItems() {
        return items;
    }

    public void setItems(List<ItemInfoModel> items) {
        this.items = items;
    }

    public List<TaxInfoModel> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<TaxInfoModel> taxes) {
        this.taxes = taxes;
    }

    public ComplementModel getComplement() {
        return complement;
    }

    public void setComplement(ComplementModel complement) {
        this.complement = complement;
    }

    public Complements getComplements() {
        return complements;
    }

    public void setComplements(Complements complements) {
        this.complements = complements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }

    public String getIsPayableBy() {
        return isPayableBy;
    }

    public void setIsPayableBy(String isPayableBy) {
        this.isPayableBy = isPayableBy;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"cfdiType\":\"" + cfdiType + "\", \"type\":\"" + type + "\", \"serie\":\""
                + serie + "\", \"folio\":\"" + folio + "\", \"date\":\"" + date + "\", \"certNumber\":\"" + certNumber
                + "\", \"paymentTerms\":\"" + paymentTerms + "\", \"paymentConditions\":\"" + paymentConditions
                + "\", \"PaymentMethod\":\"" + PaymentMethod + "\", \"paymentAccountNumber\":\"" + paymentAccountNumber
                + "\", \"paymentBankName\":\"" + paymentBankName + "\", \"expeditionPlace\":\"" + expeditionPlace
                + "\", \"exchangeRate\":\"" + exchangeRate + "\", \"currency\":\"" + currency + "\", \"subtotal\":\""
                + subtotal + "\", \"discount\":\"" + discount + "\", \"total\":\"" + total + "\", \"observations\":\""
                + observations + "\", \"orderNumber\":\"" + orderNumber + "\", \"Issuer\":\"" + Issuer
                + "\", \"receiver\":\"" + receiver + "\", \"items\":\"" + items + "\", \"taxes\":\"" + taxes
                + "\", \"complement\":\"" + complement + "\", \"complements\":\"" + complements + "\", \"status\":\""
                + status + "\", \"originalString\":\"" + originalString + "\", \"isPayableBy\":\"" + isPayableBy
                + "\"}";
    }
}
