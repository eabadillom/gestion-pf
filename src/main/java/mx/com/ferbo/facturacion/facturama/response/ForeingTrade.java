package mx.com.ferbo.facturacion.facturama.response;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import mx.com.ferbo.facturacion.facturama.Commodity;
import mx.com.ferbo.facturacion.facturama.Issuer;
import mx.com.ferbo.facturacion.facturama.Owner;
import mx.com.ferbo.facturacion.facturama.ReceiverF;
import mx.com.ferbo.facturacion.facturama.Recipient;

public class ForeingTrade {
    
    @SerializedName(value = "Issuer")
    private Issuer issuer;
    
    @SerializedName(value = "Receiver")
    private ReceiverF receiver;
    
    @SerializedName(value = "Owner")
    private List<Owner> owner;
    
    @SerializedName(value = "Recipient")
    private List<Recipient> recipient;
    
    @SerializedName(value = "ReasonForTransfer")
    private String reasonForTransfer;
    
    @SerializedName(value = "operationType")
    private String OperationType;
    
    @SerializedName(value = "Comodity")
    private List<Commodity> comodity;
    
    @SerializedName(value = "RequestCode")
    private String requestCode;
    
    @SerializedName(value = "Incoterm")
    private String incoterm;
    
    @SerializedName(value = "Subdivision")
    private Boolean subdivision;
    
    @SerializedName(value = "ExchangeRateUSD")
    private BigDecimal exchangeRateUSD;
    
    @SerializedName(value = "TotalUSD")
    private BigDecimal totalUSD;
    
    @SerializedName(value = "OriginCertificate")
    private Boolean originCertificate;
    
    @SerializedName(value = "OriginCertificateNumber")
    private String originCertificateNumber;
    
    @SerializedName(value = "ReliableExporterNumber")
    private String reliableExporterNumber;
    
    @SerializedName(value = "Observations")
    private String observations;

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public ReceiverF getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverF receiver) {
        this.receiver = receiver;
    }

    public List<Owner> getOwner() {
        return owner;
    }

    public void setOwner(List<Owner> owner) {
        this.owner = owner;
    }

    public List<Recipient> getRecipient() {
        return recipient;
    }

    public void setRecipient(List<Recipient> recipient) {
        this.recipient = recipient;
    }

    public String getReasonForTransfer() {
        return reasonForTransfer;
    }

    public void setReasonForTransfer(String reasonForTransfer) {
        this.reasonForTransfer = reasonForTransfer;
    }

    public String getOperationType() {
        return OperationType;
    }

    public void setOperationType(String operationType) {
        OperationType = operationType;
    }

    public List<Commodity> getComodity() {
        return comodity;
    }

    public void setComodity(List<Commodity> comodity) {
        this.comodity = comodity;
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
        return originCertificateNumber;
    }

    public void setOriginCertificateNumber(String originCertificateNumber) {
        this.originCertificateNumber = originCertificateNumber;
    }

    public String getReliableExporterNumber() {
        return reliableExporterNumber;
    }

    public void setReliableExporterNumber(String reliableExporterNumber) {
        this.reliableExporterNumber = reliableExporterNumber;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return "{\"issuer\":\"" + issuer + "\", \"receiver\":\"" + receiver + "\", \"owner\":\"" + owner
                + "\", \"recipient\":\"" + recipient + "\", \"reasonForTransfer\":\"" + reasonForTransfer
                + "\", \"OperationType\":\"" + OperationType + "\", \"comodity\":\"" + comodity
                + "\", \"requestCode\":\"" + requestCode + "\", \"incoterm\":\"" + incoterm + "\", \"subdivision\":\""
                + subdivision + "\", \"exchangeRateUSD\":\"" + exchangeRateUSD + "\", \"totalUSD\":\"" + totalUSD
                + "\", \"originCertificate\":\"" + originCertificate + "\", \"originCertificateNumber\":\""
                + originCertificateNumber + "\", \"reliableExporterNumber\":\"" + reliableExporterNumber
                + "\", \"observations\":\"" + observations + "\"}";
    }
}
