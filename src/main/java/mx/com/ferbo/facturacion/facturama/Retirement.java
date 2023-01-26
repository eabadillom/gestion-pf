package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**Retirement<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Retirement<br>
 * @author esteban
 *
 */
public class Retirement {
    
    @SerializedName(value = "TotalASinglePayment")
    private BigDecimal totalASinglePayment = null;
    
    @SerializedName(value = "TotalParciality")
    private BigDecimal totalParciality = null;
    
    @SerializedName(value = "DailyAmount")
    private BigDecimal dailyAmount = null;
    
    @SerializedName(value = "AccumulatedIncome")
    private BigDecimal accumulatedIncome = null;
    
    @SerializedName(value = "NonAccumulatedIncome")
    private BigDecimal nonAccumulatedIncome = null;

    public BigDecimal getTotalASinglePayment() {
        return totalASinglePayment;
    }

    public void setTotalASinglePayment(BigDecimal totalASinglePayment) {
        this.totalASinglePayment = totalASinglePayment;
    }

    public BigDecimal getTotalParciality() {
        return totalParciality;
    }

    public void setTotalParciality(BigDecimal totalParciality) {
        this.totalParciality = totalParciality;
    }

    public BigDecimal getDailyAmount() {
        return dailyAmount;
    }

    public void setDailyAmount(BigDecimal dailyAmount) {
        this.dailyAmount = dailyAmount;
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
        return "{\"totalASinglePayment\":\"" + totalASinglePayment + "\", \"totalParciality\":\"" + totalParciality
                + "\", \"dailyAmount\":\"" + dailyAmount + "\", \"accumulatedIncome\":\"" + accumulatedIncome
                + "\", \"nonAccumulatedIncome\":\"" + nonAccumulatedIncome + "\"}";
    }
    
    
}
