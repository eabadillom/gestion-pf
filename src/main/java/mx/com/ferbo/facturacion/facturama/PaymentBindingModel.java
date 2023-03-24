package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**PaymentBindingModel<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=PaymentBindingModel<br>
 * @author esteban
 *
 */
public class PaymentBindingModel {
    
    /**Atributo condicional para integrar el sello digital que se asocie al pago. La entidad que emite el comprobante de pago, ingresa una cadena original y el sello digital en una sección de dicho comprobante, este sello digital es el que se debe registrar en este campo. Debe ser expresado como una cadena de texto en formato base 64.<br>
     */
    @SerializedName(value = "SignPayment")
    private String signPayment = null;
    
    /**Atributo condicional que sirve para incorporar el certificado que ampara al pago, como una cadena de texto en formato base 64.<br>
     */
    @SerializedName(value = "CertPayment")
    private String CertPayment = null;
    
    /**Atributo condicional para expresar la cadena original del comprobante de pago generado por la entidad emisora de la cuenta beneficiaria.<br>
     */
    @SerializedName(value = "OriginalString")
    private String originalString = null;
    
    /**Atributo condicional para identificar la clave del tipo de cadena de pago que genera la entidad receptora del pago.<br>
     * Matching regular expression pattern: ^01$<br>
     */
    @SerializedName(value = "StringTypePayment")
    private String stringTypePayment = null;
    
    @SerializedName(value = "RelatedDocuments")
    private List<RelatedDocument> RelatedDocuments = null;
    
    
    @SerializedName(value = "Taxes")
    private List<TaxBindingModel> taxes = null;
    
    /**Atributo requerido para expresar la fecha y hora en la que el beneficiario recibe el pago. Se expresa en la forma aaaa-mm-ddThh:mm:ss, de acuerdo con la especificación ISO 8601.En caso de no contar con la hora se debe registrar 12:00:00.<br>
     * Data type: DateTime<br>
     * Matching regular expression pattern: ^([\+-]?\d{4}(?!\d{2}\b))((-?)((0[1-9]|1[0-2])(\3([12]\d|0[1-9]|3[01]))?|W([0-4]\d|5[0-2])(-?[1-7])?|(00[1-9]|0[1-9]\d|[12]\d{2}|3([0-5]\d|6[1-6])))([T\s]((([01]\d|2[0-3])((:?)[0-5]\d)?|24\:?00)([\.,]\d+(?!:))?)?(\17[0-5]\d([\.,]\d+)?)?([zZ]|([\+-])([01]\d|2[0-3]):?([0-5]\d)?)?)?)?$<br>
     */
    @SerializedName(value = "Date")
    private Date date = null;
    
    /**Atributo requerido para expresar la clave de la forma en que se realiza el pago.<br>
     * Required<br>
     */
    @SerializedName(value = "PaymentForm")
    private String paymentForm = null;
    
    /**Atributo requerido para identificar la clave de la moneda utilizada para realizar el pago. Conforme con la especificación ISO 4217.<br>
     * 
     */
    @SerializedName(value = "Currency")
    private String currency = null;
    
    /**Atributo condicional para expresar el tipo de cambio de la moneda a la fecha en que se realizó el pago. El valor debe reflejar el número de pesos mexicanos que equivalen a una unidad de la divisa señalada en el atributo MonedaP. Es requerido cuando el atributo MonedaP es diferente a MXN.<br>
     */
    @SerializedName(value = "ExchangeRate")
    private BigDecimal exchangeRate = null;
    
    /**Atributo requerido para expresar el importe del pago.<br>
     * Required<br>
     */
    @SerializedName(value = "Amount")
    private BigDecimal amount = null;
    
    /**Atributo condicional para expresar el número de cheque, número de autorización, número de referencia, clave de rastreo en caso de ser SPEI, línea de captura o algún número de referencia análogo que identifique la operación que ampara el pago efectuado<br>
     */
    @SerializedName(value = "OperationNumber")
    private String OperationNumber = null;
    
    /**Atributo condicional para expresar la clave RFC de la entidad emisora de la cuenta origen, es decir, la operadora, el banco, la institución financiera, emisor de monedero electrónico, etc., en caso de ser extranjero colocar XEXX010101000, considerar las reglas de obligatoriedad publicadas en la página del SAT para éste atributo de acuerdo con el catálogo Formas de pago.<br>
     * Matching regular expression pattern: XEXX010101000|[A-Z&amp;Ñ]{3}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]<br>
     */
    @SerializedName(value = "RfcIssuerPayerAccount")
    private String rfcIssuerPayerAccount = null;
    
    /**Atributo condicional para expresar el nombre del banco ordenante, es requerido en caso de ser extranjero. Considerar las reglas de obligatoriedad publicadas en la página del SAT para éste atributo de acuerdo con el catálogo Formas de pago.<br>
     */
    @SerializedName(value = "ForeignAccountNamePayer")
    private String foreignAccountNamePayer = null;
    
    /**Atributo condicional para incorporar el número de la cuenta con la que se realizó el pago. Considerar las reglas de obligatoriedad publicadas en la página del SAT para éste atributo de acuerdo con el catálogo Formas de pago<br>
     */
    @SerializedName(value = "PayerAccount")
    private String payerAccount = null;
    
    /**Atributo condicional para expresar la clave RFC de la entidad operadora de la cuenta destino, es decir, la operadora, el banco, la institución financiera, emisor de monedero electrónico, etc. Considerar las reglas de obligatoriedad publicadas en la página del SAT para éste atributo de acuerdo con el catálogo Formas de pago.<br>
     * Matching regular expression pattern: [A-Z&Ñ]{3}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]<br>
     */
    @SerializedName(value = "RfcReceiverBeneficiaryAccount")
    private String RfcReceiverBeneficiaryAccount = null;
    
    /**Atributo condicional para incorporar el número de cuenta en donde se recibió el pago. Considerar las reglas de obligatoriedad publicadas en la página del SAT para éste atributo de acuerdo con el catálogo Formas de pago.<br>
     */
    @SerializedName(value = "BeneficiaryAccount")
    private String beneficiaryAccount = null;
    
    /**ExpectedPaid
     * 
     */
    @SerializedName(value = "ExpectedPaid")
    private BigDecimal ExpectedPaid = null;

    public String getSignPayment() {
        return signPayment;
    }

    public void setSignPayment(String signPayment) {
        this.signPayment = signPayment;
    }

    public String getCertPayment() {
        return CertPayment;
    }

    public void setCertPayment(String certPayment) {
        CertPayment = certPayment;
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }

    public String getStringTypePayment() {
        return stringTypePayment;
    }

    public void setStringTypePayment(String stringTypePayment) {
        this.stringTypePayment = stringTypePayment;
    }

    public List<RelatedDocument> getRelatedDocuments() {
        return RelatedDocuments;
    }

    public void setRelatedDocuments(List<RelatedDocument> relatedDocuments) {
        RelatedDocuments = relatedDocuments;
    }

    public List<TaxBindingModel> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<TaxBindingModel> taxes) {
        this.taxes = taxes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOperationNumber() {
        return OperationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        OperationNumber = operationNumber;
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

    public BigDecimal getExpectedPaid() {
        return ExpectedPaid;
    }

    public void setExpectedPaid(BigDecimal expectedPaid) {
        ExpectedPaid = expectedPaid;
    }

    @Override
    public String toString() {
        return "{\"signPayment\":\"" + signPayment + "\", \"CertPayment\":\"" + CertPayment
                + "\", \"originalString\":\"" + originalString + "\", \"stringTypePayment\":\"" + stringTypePayment
                + "\", \"RelatedDocuments\":\"" + RelatedDocuments + "\", \"taxes\":\"" + taxes + "\", \"date\":\""
                + date + "\", \"paymentForm\":\"" + paymentForm + "\", \"currency\":\"" + currency
                + "\", \"exchangeRate\":\"" + exchangeRate + "\", \"amount\":\"" + amount + "\", \"OperationNumber\":\""
                + OperationNumber + "\", \"rfcIssuerPayerAccount\":\"" + rfcIssuerPayerAccount
                + "\", \"foreignAccountNamePayer\":\"" + foreignAccountNamePayer + "\", \"payerAccount\":\""
                + payerAccount + "\", \"RfcReceiverBeneficiaryAccount\":\"" + RfcReceiverBeneficiaryAccount
                + "\", \"beneficiaryAccount\":\"" + beneficiaryAccount + "\", \"ExpectedPaid\":\"" + ExpectedPaid
                + "\"}";
    }
}
