package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**Employee<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Employee<br>
 * @author esteban
 *
 */
public class Employee {
    
    /**Outsourcing
     */
    @SerializedName(value = "Outsourcing")
    private PayrollOutsourcing outsourcing = null;
    
    /**Curp<br>
     * Required<br>
     * Matching regular expression pattern: ^[A-Z][AEIOUX][A-Z]{2}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[MH]([ABCMTZ]S|[BCJMOT]C|[CNPST]L|[GNQ]T|[GQS]R|C[MH]|[MY]N|[DH]G|NE|VZ|DF|SP)[BCDFGHJ-NP-TV-Z]{3}[0-9A-Z][0-9]$<br>
     */
    @SerializedName(value = "Curp")
    private String curp = null;
    
    /**SocialSecurityNumber<br>
     * Matching regular expression pattern: [0-9]{1,15}<br>
     */
    @SerializedName(value = "SocialSecurityNumber")
    private String socialSecurityNumber = null;
    
    /**StartDateLaborRelations
     */
    @SerializedName(value = "StartDateLaborRelations")
    private Date startDateLaborRelations = null;
    
    /**ContractType<br>
     * Required<brS
     */
    @SerializedName(value = "ContractType")
    private String ContractType = null;
    
    @SerializedName(value = "Unionized")
    private Boolean unionized = null;
    
    @SerializedName(value = "TypeOfJourney")
    private String typeOfJourney = null;
    
    /**RegimeType<br>
     * Required<br>
     */
    @SerializedName(value = "RegimeType")
    private String regimeType = null;
    
    /**EmployeeNumber<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,15}
     */
    @SerializedName(value = "EmployeeNumber")
    private String employeeNumber = null;
    
    /**Department<br>
     * Matching regular expression pattern: [^|]{1,100}<br>
     */
    @SerializedName(value = "Department")
    private String department = null;
    
    /**Position<br>
     * Matching regular expression pattern: [^|]{1,100}<br>
     */
    @SerializedName(value = "Position")
    private String position = null;
    
    /**PositionRisk
     * 
     */
    @SerializedName(value = "PositionRisk")
    private String positionRisk = null;
    
    /**FrequencyPayment<br>
     * Required<br>
     */
    @SerializedName(value = "FrequencyPayment")
    private String frequencyPayment = null;
    
    /**Bank
     * 
     */
    @SerializedName(value = "Bank")
    private String bank = null;
    
    /**BankAccount<br>
     * Matching regular expression pattern: [0-9]{10,18}<br>
     */
    @SerializedName(value = "BankAccount")
    private String bankAccount = null;
    
    @SerializedName(value = "BaseSalary")
    private BigDecimal baseSalary = null;
    
    @SerializedName(value = "DailySalary")
    private BigDecimal dailySalary = null;
    
    /**FederalEntityKey<br>
     * Required<br>
     */
    @SerializedName(value = "FederalEntityKey")
    private String federalEntityKey = null;

    public PayrollOutsourcing getOutsourcing() {
        return outsourcing;
    }

    public void setOutsourcing(PayrollOutsourcing outsourcing) {
        this.outsourcing = outsourcing;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public Date getStartDateLaborRelations() {
        return startDateLaborRelations;
    }

    public void setStartDateLaborRelations(Date startDateLaborRelations) {
        this.startDateLaborRelations = startDateLaborRelations;
    }

    public String getContractType() {
        return ContractType;
    }

    public void setContractType(String contractType) {
        ContractType = contractType;
    }

    public Boolean getUnionized() {
        return unionized;
    }

    public void setUnionized(Boolean unionized) {
        this.unionized = unionized;
    }

    public String getTypeOfJourney() {
        return typeOfJourney;
    }

    public void setTypeOfJourney(String typeOfJourney) {
        this.typeOfJourney = typeOfJourney;
    }

    public String getRegimeType() {
        return regimeType;
    }

    public void setRegimeType(String regimeType) {
        this.regimeType = regimeType;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositionRisk() {
        return positionRisk;
    }

    public void setPositionRisk(String positionRisk) {
        this.positionRisk = positionRisk;
    }

    public String getFrequencyPayment() {
        return frequencyPayment;
    }

    public void setFrequencyPayment(String frequencyPayment) {
        this.frequencyPayment = frequencyPayment;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getDailySalary() {
        return dailySalary;
    }

    public void setDailySalary(BigDecimal dailySalary) {
        this.dailySalary = dailySalary;
    }

    public String getFederalEntityKey() {
        return federalEntityKey;
    }

    public void setFederalEntityKey(String federalEntityKey) {
        this.federalEntityKey = federalEntityKey;
    }

    @Override
    public String toString() {
        return "{\"outsourcing\":\"" + outsourcing + "\", \"curp\":\"" + curp + "\", \"socialSecurityNumber\":\""
                + socialSecurityNumber + "\", \"startDateLaborRelations\":\"" + startDateLaborRelations
                + "\", \"ContractType\":\"" + ContractType + "\", \"unionized\":\"" + unionized
                + "\", \"typeOfJourney\":\"" + typeOfJourney + "\", \"regimeType\":\"" + regimeType
                + "\", \"employeeNumber\":\"" + employeeNumber + "\", \"department\":\"" + department
                + "\", \"position\":\"" + position + "\", \"positionRisk\":\"" + positionRisk
                + "\", \"frequencyPayment\":\"" + frequencyPayment + "\", \"bank\":\"" + bank + "\", \"bankAccount\":\""
                + bankAccount + "\", \"baseSalary\":\"" + baseSalary + "\", \"dailySalary\":\"" + dailySalary
                + "\", \"federalEntityKey\":\"" + federalEntityKey + "\"}";
    }
}
