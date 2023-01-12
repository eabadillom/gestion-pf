package mx.com.ferbo.facturacion.facturama.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import mx.com.ferbo.facturacion.facturama.RelatedDocument;
import mx.com.ferbo.facturacion.facturama.Tax;

public class Payment {
    @SerializedName(value = "RelatedDocuments")
    private List<RelatedDocument> relatedDocuments = null;;
    
    @SerializedName(value = "Impuestos")
    private List<Tax> impuestos = null;
    
    @SerializedName(value = "Date")
    private String date = null;
    
    @SerializedName(value = "PaymentForm")
    private String paymentForm = null;
    
    @SerializedName(value = "Currency")
    private String currency = null;
    
    @SerializedName(value = "ExchangeRate")
    private Double exchangeRate = null;
    
    @SerializedName(value = "Amount")
    private Double amount = null;
    
    @SerializedName(value = "OperationNumber")
    private String operationNumber = null;
    
    @SerializedName(value = "RfcIssuerPayerAccount")
    private String rfcIssuerPayerAccount = null;
    
    @SerializedName(value = "ForeignAccountNamePayer")
    private String foreignAccountNamePayer = null;
    
    @SerializedName(value = "PayerAccount")
    private String payerAccount = null;
    
    @SerializedName(value = "rfcReceiverBeneficiaryAccount")
    private String RfcReceiverBeneficiaryAccount = null;
    
    @SerializedName(value = "BeneficiaryAccount")
    private String beneficiaryAccount = null;

    public List<RelatedDocument> getRelatedDocuments() {
        return relatedDocuments;
    }

    public void setRelatedDocuments(List<RelatedDocument> relatedDocuments) {
        this.relatedDocuments = relatedDocuments;
    }

    public List<Tax> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<Tax> impuestos) {
        this.impuestos = impuestos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(String paymentForm) {
        this.paymentForm = paymentForm;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public String getRfcIssuerPayerAccount() {
        return rfcIssuerPayerAccount;
    }

    public void setRfcIssuerPayerAccount(String rfcIssuerPayerAccount) {
        this.rfcIssuerPayerAccount = rfcIssuerPayerAccount;
    }

    public String getForeignAccountNamePayer() {
        return foreignAccountNamePayer;
    }

    public void setForeignAccountNamePayer(String foreignAccountNamePayer) {
        this.foreignAccountNamePayer = foreignAccountNamePayer;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public String getRfcReceiverBeneficiaryAccount() {
        return RfcReceiverBeneficiaryAccount;
    }

    public void setRfcReceiverBeneficiaryAccount(String rfcReceiverBeneficiaryAccount) {
        RfcReceiverBeneficiaryAccount = rfcReceiverBeneficiaryAccount;
    }

    public String getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public void setBeneficiaryAccount(String beneficiaryAccount) {
        this.beneficiaryAccount = beneficiaryAccount;
    }

    @Override
    public String toString() {
        return "{\"relatedDocuments\":\"" + relatedDocuments + "\", \"impuestos\":\"" + impuestos + "\", \"date\":\""
                + date + "\", \"paymentForm\":\"" + paymentForm + "\", \"currency\":\"" + currency
                + "\", \"exchangeRate\":\"" + exchangeRate + "\", \"amount\":\"" + amount + "\", \"operationNumber\":\""
                + operationNumber + "\", \"rfcIssuerPayerAccount\":\"" + rfcIssuerPayerAccount
                + "\", \"foreignAccountNamePayer\":\"" + foreignAccountNamePayer + "\", \"payerAccount\":\""
                + payerAccount + "\", \"RfcReceiverBeneficiaryAccount\":\"" + RfcReceiverBeneficiaryAccount
                + "\", \"beneficiaryAccount\":\"" + beneficiaryAccount + "\"}";
    }
}
