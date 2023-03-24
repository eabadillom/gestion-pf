package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**Clase con los atributos necesarios para la facturación electrónica con Facturama.<br>
 * https://www.api.facturama.com.mx/docs/api/POST-2-cfdis<br>
 * <br>
 * Para realizar el registro del CFDI, se debe invocar mediante HTTP POST a la URL:<br>
 * https://www.api.facturama.com.mx/2/cfdis<br>
 * <br>
 * Como resultado, se obtiene un objeto de la clase CfdiInfoModel.<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=CfdiInfoModel<br>
 * @author esteban
 */
public class CFDIInfo {
    
    /**Atributo para especificar el nombre que se establecera en el pdf (default 1 = factura) [ Vea la documentación de "Nombres del CFDI" ]<br>
     * Data type: Text
     */
    @SerializedName(value = "NameId")
    private String nameId = null;
    
    /**Fecha de Emision (Opcional) del comprobante conforme a la norma ISO 8601<br>
     * Data type: DateTime <br>
     * Matching regular expression pattern: ^([\+-]?\d{4}(?!\d{2}\b))((-?)((0[1-9]|1[0-2])(\3([12]\d|0[1-9]|3[01]))?|W([0-4]\d|5[0-2])(-?[1-7])?|(00[1-9]|0[1-9]\d|[12]\d{2}|3([0-5]\d|6[1-6])))([T\s]((([01]\d|2[0-3])((:?)[0-5]\d)?|24\:?00)([\.,]\d+(?!:))?)?(\17[0-5]\d([\.,]\d+)?)?([zZ]|([\+-])([01]\d|2[0-3]):?([0-5]\d)?)?)?)?$<br>
     */
    @SerializedName(value = "Date")
    private Date date = null; //Fecha de expedición
    
    /**Referencia (Opcional) de la Serie existente en la Sucursal [ Vea la documentación de "Series" ]<br>
     * Matching regular expression pattern: [a-zA-Z0-9]{1,10}<br>
     * String length: inclusive between 0 and 10<br>
     */
    @SerializedName(value = "Serie")
    private String serie = null;
    
    /**Atributo opcional para incorporar al menos los cuatro últimos digitos del número de cuenta con la que se realizó el pago....<br>
     * Data type: Text<br>
     * Matching regular expression pattern: ^\d{1,4}?$<br>
     * String length: inclusive between 4 and 4<br>
     */
    @SerializedName(value = "PaymentAccountNumber")
    private String paymentAccountNumber = null;
    
    /**Tipo de cambio de la moneda en caso de ser diferente de MXN<br>
     * Data type: Text<br>
     * Matching regular expression pattern: [0-9]{1,18}(.[0-9]{1,6})?<br>
     * Range: inclusive between 1E-06 and 1,79769313486232E+308
     */
    @SerializedName(value = "CurrencyExchangeRate")
    private BigDecimal currencyExchangeRate = null;
    
    /**Atributo para expresar la moneda utilizada para expresar los montos en 3 caracteres según la especificación del estándar internacional ISO 4217<br>
     * Data type: Text<br>
     * String length: inclusive between 3 and 3<br>
     */
    @SerializedName(value = "Currency")
    private String currency = null;
    
    /**Lugar de Expedición (Codigo Postal desde donde se expide el comprobante)<br>
     * Required<br>
     * Matching regular expression pattern: [0-9]{5}<br>
     */
    @SerializedName(value = "ExpeditionPlace")
    private String expeditionPlace = null;
    
    /**(Opcional)
     * Data type: Text
     * Matching regular expression pattern: [^|]{1,100}
     */
    @SerializedName(value = "PaymentConditions")
    private String paymentConditions = null;
    
    /**Cfdis Relacionados, empleado para las notas de credito, etc.
     */
    @SerializedName(value = "Relations")
    private List<CfdiRelations> relations = null;
    
    /**Id de factura con Status invalid | unanswered (registro invalido sin timbre por alguna intermitencia durante las operaciones), si el paramento IdCfdi contiene valor se reutiliza el folio del registro encontrado en caso contrario se crea una nuevo.
     */
    @SerializedName(value = "IdCfdi")
    private String idCfdi = null;
    
    /**Folio de la factura, si el campo es nulo se asiganara automaticamente el consecutivo<br>
     * Matching regular expression pattern: [0-9]{1,10}?$<br>
s     */
    @SerializedName(value = "Folio")
    private String folio = null;

    
    /**Atributo requerido para expresar el efecto del comprobante fiscal para el contribuyente emisor: ingreso, egreso ó traslado<br>
     * Required<br>
     * Data type: Text<br>
     * Matching regular expression pattern: I|E|T|N|P<br>
     */
    @SerializedName(value = "CfdiType")
    private String cfdiType = null;
    
    /**Atributo obligatorio y de catálogo, para expresar la forma de pago de los bienes o servicios amparados por el comprobante. Se entiende como método de pago leyendas tales como: 01, 02, 03, 99<br>
     * Data type: Text<br>
     * Matching regular expression pattern:<br>
     * 01|02|03|04|05|06|08|12|13|14|15|17|23|24|25|26|27|28|29|30|31|99<br>
     */
    @SerializedName(value = "PaymentForm")
    private String paymentForm = null;
    
    /**Atributo obligatorio y de catálogo, para expresar el método de pago de los bienes o servicios amparados por el comprobante. Se entiende como método de pago leyendas tales como: PPD, PUE<br>
     * Matching regular expression pattern: PUE|PPD<br>
     */
    @SerializedName(value = "PaymentMethod")
    private String paymentMethod = null;
    
    /**ClientModel a quien se emitirá el CFDi, Atributo Requerido
     * Required
     */
    @SerializedName(value = "Receiver")
    private ReceiverBindingModel receiver = null;
    
    /**Nodo requerido para enlistar los conceptos cubiertos por el comprobante.<br>
     */
    @SerializedName(value = "Items")
    private List<ItemFullBindingModel> items = null;
    
    /**Complementos aplicables al cfdi 3.3<br>
     */
    @SerializedName(value = "Complemento")
    private Complement complemento = null;
    
    /**Descripcion no fiscal del pdf<br>
     */
    @SerializedName(value = "Observations")
    private String observations = null;
    
    /**Numero de Orden, propiedad no fiscal (opcional)<br>
     * Max length: 100<br>
     */
    @SerializedName(value = "OrderNumber")
    private String orderNumber = null;
    
    /**Nombre del banco, propiedad no fiscal (opcional)
     * Max length: 50
     */
    @SerializedName(value = "PaymentBankName")
    private String paymentBankName = null;
    
    /**Id de la cuenta de banco relacionado con la entidad fiscal
     * 
     */
    @SerializedName(value = "IdTaxEntityBankAccounts")
    private String idTaxEntityBankAccounts = null;

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getPaymentAccountNumber() {
        return paymentAccountNumber;
    }

    public void setPaymentAccountNumber(String paymentAccountNumber) {
        this.paymentAccountNumber = paymentAccountNumber;
    }

    public BigDecimal getCurrencyExchangeRate() {
        return currencyExchangeRate;
    }

    public void setCurrencyExchangeRate(BigDecimal currencyExchangeRate) {
        this.currencyExchangeRate = currencyExchangeRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpeditionPlace() {
        return expeditionPlace;
    }

    public void setExpeditionPlace(String expeditionPlace) {
        this.expeditionPlace = expeditionPlace;
    }

    public String getPaymentConditions() {
        return paymentConditions;
    }

    public void setPaymentConditions(String paymentConditions) {
        this.paymentConditions = paymentConditions;
    }

    public List<CfdiRelations> getRelations() {
        return relations;
    }

    public void setRelations(List<CfdiRelations> relations) {
        this.relations = relations;
    }

    public String getIdCfdi() {
        return idCfdi;
    }

    public void setIdCfdi(String idCfdi) {
        this.idCfdi = idCfdi;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getCfdiType() {
        return cfdiType;
    }

    public void setCfdiType(String cfdiType) {
        this.cfdiType = cfdiType;
    }

    public String getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(String paymentForm) {
        this.paymentForm = paymentForm;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ReceiverBindingModel getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverBindingModel receiver) {
        this.receiver = receiver;
    }

    public List<ItemFullBindingModel> getItems() {
        return items;
    }

    public void setItems(List<ItemFullBindingModel> items) {
        this.items = items;
    }

    public Complement getComplemento() {
        return complemento;
    }

    public void setComplemento(Complement complemento) {
        this.complemento = complemento;
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

    public String getPaymentBankName() {
        return paymentBankName;
    }

    public void setPaymentBankName(String paymentBankName) {
        this.paymentBankName = paymentBankName;
    }

    public String getIdTaxEntityBankAccounts() {
        return idTaxEntityBankAccounts;
    }

    public void setIdTaxEntityBankAccounts(String idTaxEntityBankAccounts) {
        this.idTaxEntityBankAccounts = idTaxEntityBankAccounts;
    }

    @Override
    public String toString() {
        return "{\"nameId\":\"" + nameId + "\", \"date\":\"" + date + "\", \"serie\":\"" + serie
                + "\", \"paymentAccountNumber\":\"" + paymentAccountNumber + "\", \"currencyExchangeRate\":\""
                + currencyExchangeRate + "\", \"currency\":\"" + currency + "\", \"expeditionPlace\":\""
                + expeditionPlace + "\", \"paymentConditions\":\"" + paymentConditions + "\", \"relations\":\""
                + relations + "\", \"idCfdi\":\"" + idCfdi + "\", \"folio\":\"" + folio + "\", \"cfdiType\":\""
                + cfdiType + "\", \"paymentForm\":\"" + paymentForm + "\", \"paymentMethod\":\"" + paymentMethod
                + "\", \"receiver\":\"" + receiver + "\", \"items\":\"" + items + "\", \"complemento\":\"" + complemento
                + "\", \"observations\":\"" + observations + "\", \"orderNumber\":\"" + orderNumber
                + "\", \"paymentBankName\":\"" + paymentBankName + "\", \"idTaxEntityBankAccounts\":\""
                + idTaxEntityBankAccounts + "\"}";
    }
}
