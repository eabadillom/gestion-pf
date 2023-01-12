package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Indemnification<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Indemnification<br>
 * @author esteban
 *
 */
public class Indemnification {
    
    @SerializedName(value = "TotalPaid")
    private BigDecimal totalPaid = null;
    
    @SerializedName(value = "YearsOfService")
    private BigDecimal yearsOfService = null;
    
    @SerializedName(value = "LastMonthlySalaryOrd")
    private BigDecimal lastMonthlySalaryOrd = null;
    
    /**AccumulatedIncome<br>
     * Required<br>
     */
    @SerializedName(value = "AccumulatedIncome")
    private BigDecimal accumulatedIncome = null;
    
    /**NonAccumulatedIncome<br>
     * Required<br>
     */
    @SerializedName(value = "NonAccumulatedIncome")
    private BigDecimal nonAccumulatedIncome = null;

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(BigDecimal yearsOfService) {
        this.yearsOfService = yearsOfService;
    }

    public BigDecimal getLastMonthlySalaryOrd() {
        return lastMonthlySalaryOrd;
    }

    public void setLastMonthlySalaryOrd(BigDecimal lastMonthlySalaryOrd) {
        this.lastMonthlySalaryOrd = lastMonthlySalaryOrd;
    }

    public BigDecimal getAccumulatedIncome() {
        return accumulatedIncome;
    }

    public void setAccumulatedIncome(BigDecimal accumulatedIncome) {
        this.accumulatedIncome = accumulatedIncome;
    }

    public BigDecimal getNonAccumulatedIncome() {
        return nonAccumulatedIncome;
    }

    public void setNonAccumulatedIncome(BigDecimal nonAccumulatedIncome) {
        this.nonAccumulatedIncome = nonAccumulatedIncome;
    }

    @Override
    public String toString() {
        return "{\"totalPaid\":\"" + totalPaid + "\", \"yearsOfService\":\"" + yearsOfService
                + "\", \"lastMonthlySalaryOrd\":\"" + lastMonthlySalaryOrd + "\", \"accumulatedIncome\":\""
                + accumulatedIncome + "\", \"nonAccumulatedIncome\":\"" + nonAccumulatedIncome + "\"}";
    }
}
