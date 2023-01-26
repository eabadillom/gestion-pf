package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**ExtraHour<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ExtraHour<br>
 * @author esteban
 *
 */
public class ExtraHour {
    
    /**Days<br>
     * Required<br>
     */
    @SerializedName(value = "Days")
    private Integer days = null;
    
    /**HoursType<br>
     * Required<br>
     */
    @SerializedName(value = "HoursType")
    private String hoursType = null;
    
    /**ExtraHours<br>
     * Required
     */
    @SerializedName(value = "ExtraHours")
    private Integer extraHours = null;
    
    @SerializedName(value = "PaidAmount")
    private BigDecimal paidAmount = null;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getHoursType() {
        return hoursType;
    }

    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    public Integer getExtraHours() {
        return extraHours;
    }

    public void setExtraHours(Integer extraHours) {
        this.extraHours = extraHours;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Override
    public String toString() {
        return "{\"days\":\"" + days + "\", \"hoursType\":\"" + hoursType + "\", \"extraHours\":\"" + extraHours
                + "\", \"paidAmount\":\"" + paidAmount + "\"}";
    }
}
