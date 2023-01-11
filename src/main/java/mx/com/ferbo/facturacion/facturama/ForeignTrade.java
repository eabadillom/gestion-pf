package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**ForeignTrade<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ForeignTrade<br>
 * @author esteban
 *
 */
public class ForeignTrade {
    
    /**Emisor del complemento<br>
     */
    @SerializedName(value = "Issuer")
    private Issuer Issuer = null;
    
    /**ReceiverBindingModel<br>
     */
    @SerializedName(value = "ReceiverBindingModel")
    private ReceiverF receiver = null;  
    
    /**Owner
     */
    @SerializedName(value = "Owner")
    private Owner owner = null;
    
    /**Recipient
     */
    @SerializedName(value = "Recipient")
    private Recipient Recipient = null;
    
    /**ReasonForTransfer
     */
    @SerializedName(value = "ReasonForTransfer")
    private String reasonForTransfer = null;
    
    /**OperationType
     */
    @SerializedName(value = "OperationType")
    private String operationType = null;
    
    @SerializedName(value = "Commodity")
    private Commodity Commodity = null;
    
    /**RequestCode
     */
    @SerializedName(value = "RequestCode")
    private String requestCode = null;
    
    /**Incoterm
     */
    @SerializedName(value = "Incoterm")
    private String incoterm = null;
    
    /**Subdivision
     */
    @SerializedName(value = "Subdivision")
    private Boolean subdivision = null;
    
    /**ExchangeRateUSD<br>
     * Matching regular expression pattern: [0-9]{1,18}(.[0-9]{1,6})?<br>
     */
    @SerializedName(value = "ExchangeRateUSD")
    private BigDecimal exchangeRateUSD = null;
    
    /**TotalUSD<br>
     * Matching regular expression pattern: ^\d+(?:\.\d{1,2})?$<br>
     */
    @SerializedName(value = "TotalUSD")
    private BigDecimal totalUSD = null;
    
    /**OriginCertificate
     */
    @SerializedName(value = "OriginCertificate")
    private Boolean originCertificate = null;
    
    /**OriginCertificateNumber<br>
     * Matching regular expression pattern: [^|]{6,40}<br>
     */
    @SerializedName(value = "OriginCertificateNumber")
    private String OriginCertificateNumber = null;
    
    /**ReliableExporterNumber<br>
     * Matching regular expression pattern: [^|]{1,50}
     */
    @SerializedName(value = "ReliableExporterNumber")
    private String ReliableExporterNumber = null;
    
    /**Observations<br>
     * Matching regular expression pattern: [^|]{1,300}<br>
     */
    @SerializedName(value = "Observations")
    private String observations = null;

    public Issuer getIssuer() {
        return Issuer;
    }

    public void setIssuer(Issuer issuer) {
        Issuer = issuer;
    }

    public ReceiverF getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverF receiver) {
        this.receiver = receiver;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Recipient getRecipient() {
        return Recipient;
    }

    public void setRecipient(Recipient recipient) {
        Recipient = recipient;
    }

    public String getReasonForTransfer() {
        return reasonForTransfer;
    }

    public void setReasonForTransfer(String reasonForTransfer) {
        this.reasonForTransfer = reasonForTransfer;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Commodity getCommodity() {
        return Commodity;
    }

    public void setCommodity(Commodity commodity) {
        Commodity = commodity;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getIncoterm() {
        return incoterm;
    }

    public void setIncoterm(String incoterm) {
        this.incoterm = incoterm;
    }

    public Boolean getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(Boolean subdivision) {
        this.subdivision = subdivision;
    }

    public BigDecimal getExchangeRateUSD() {
        return exchangeRateUSD;
    }

    public void setExchangeRateUSD(BigDecimal exchangeRateUSD) {
        this.exchangeRateUSD = exchangeRateUSD;
    }

    public BigDecimal getTotalUSD() {
        return totalUSD;
    }

    public void setTotalUSD(BigDecimal totalUSD) {
        this.totalUSD = totalUSD;
    }

    public Boolean getOriginCertificate() {
        return originCertificate;
    }

    public void setOriginCertificate(Boolean originCertificate) {
        this.originCertificate = originCertificate;
    }

    public String getOriginCertificateNumber() {
        return OriginCertificateNumber;
    }

    public void setOriginCertificateNumber(String originCertificateNumber) {
        OriginCertificateNumber = originCertificateNumber;
    }

    public String getReliableExporterNumber() {
        return ReliableExporterNumber;
    }

    public void setReliableExporterNumber(String reliableExporterNumber) {
        ReliableExporterNumber = reliableExporterNumber;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return "{\"Issuer\":\"" + Issuer + "\", \"receiver\":\"" + receiver + "\", \"owner\":\"" + owner
                + "\", \"Recipient\":\"" + Recipient + "\", \"reasonForTransfer\":\"" + reasonForTransfer
                + "\", \"operationType\":\"" + operationType + "\", \"Commodity\":\"" + Commodity
                + "\", \"requestCode\":\"" + requestCode + "\", \"incoterm\":\"" + incoterm + "\", \"subdivision\":\""
                + subdivision + "\", \"exchangeRateUSD\":\"" + exchangeRateUSD + "\", \"totalUSD\":\"" + totalUSD
                + "\", \"originCertificate\":\"" + originCertificate + "\", \"OriginCertificateNumber\":\""
                + OriginCertificateNumber + "\", \"ReliableExporterNumber\":\"" + ReliableExporterNumber
                + "\", \"observations\":\"" + observations + "\"}";
    }
}
