package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**Payroll<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Payroll<br>
 * @author esteban
 *
 */
public class Payroll {
    /* Issuer
     * */
    @SerializedName(value = "Issuer")
    private PayrollIssuer Issuer = null;
    
    
    /**Employee<br>
     * Required<br>
     */
    @SerializedName(value = "Employee")
    private Employee employee = null;
    
    @SerializedName(value = "Perceptions")
    private PayrollPerceptions perceptions = null;
    
    @SerializedName(value = "Deductions")
    private PayrollDeductions deductions = null;
    
    @SerializedName(value = "OtherPayments")
    private List<OtherPayment> OtherPayments = null;
    
    @SerializedName(value = "Incapacities")
    private List<Incapacity> Incapacities = null;
    
    /**Type<br>
     * Required<br>
     */
    @SerializedName(value = "Type")
    private String type = null;
    
    /**PaymentDate<br>
     * Required<br>
     */
    @SerializedName(value = "PaymentDate")
    private Date paymentDate = null;
    
    /**InitialPaymentDate<br>
     * Required<br>
     */
    @SerializedName(value = "InitialPaymentDate")
    private Date initialPaymentDate = null;
    
    /**FinalPaymentDate<br>
     * Required<br>
     */
    @SerializedName(value = "FinalPaymentDate")
    private Date finalPaymentDate = null;
    
    /**DaysPaid<br>
     * Required<br>
     * Matching regular expression pattern: ^(([1-9][0-9]{0,4})|[0])(.[0-9]{1,3})?$<br>
     */
    @SerializedName(value = "DaysPaid")
    private BigDecimal daysPaid = null;

    public PayrollIssuer getIssuer() {
        return Issuer;
    }

    public void setIssuer(PayrollIssuer issuer) {
        Issuer = issuer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PayrollPerceptions getPerceptions() {
        return perceptions;
    }

    public void setPerceptions(PayrollPerceptions perceptions) {
        this.perceptions = perceptions;
    }

    public PayrollDeductions getDeductions() {
        return deductions;
    }

    public void setDeductions(PayrollDeductions deductions) {
        this.deductions = deductions;
    }

    public List<OtherPayment> getOtherPayments() {
        return OtherPayments;
    }

    public void setOtherPayments(List<OtherPayment> otherPayments) {
        OtherPayments = otherPayments;
    }

    public List<Incapacity> getIncapacities() {
        return Incapacities;
    }

    public void setIncapacities(List<Incapacity> incapacities) {
        Incapacities = incapacities;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getInitialPaymentDate() {
        return initialPaymentDate;
    }

    public void setInitialPaymentDate(Date initialPaymentDate) {
        this.initialPaymentDate = initialPaymentDate;
    }

    public Date getFinalPaymentDate() {
        return finalPaymentDate;
    }

    public void setFinalPaymentDate(Date finalPaymentDate) {
        this.finalPaymentDate = finalPaymentDate;
    }

    public BigDecimal getDaysPaid() {
        return daysPaid;
    }

    public void setDaysPaid(BigDecimal daysPaid) {
        this.daysPaid = daysPaid;
    }

    @Override
    public String toString() {
        return "{\"Issuer\":\"" + Issuer + "\", \"employee\":\"" + employee + "\", \"perceptions\":\"" + perceptions
                + "\", \"deductions\":\"" + deductions + "\", \"OtherPayments\":\"" + OtherPayments
                + "\", \"Incapacities\":\"" + Incapacities + "\", \"type\":\"" + type + "\", \"paymentDate\":\""
                + paymentDate + "\", \"initialPaymentDate\":\"" + initialPaymentDate + "\", \"finalPaymentDate\":\""
                + finalPaymentDate + "\", \"daysPaid\":\"" + daysPaid + "\"}";
    }
}
