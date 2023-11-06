package mx.com.ferbo.facturacion.facturama;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**PayrollDeductions<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=PayrollDeductions<br>
 * @author esteban
 *
 */
public class PayrollDeductions {
    
    @SerializedName(value = "Details")
    private List<Deduction> Details = null;

    public List<Deduction> getDetails() {
        return Details;
    }

    public void setDetails(List<Deduction> details) {
        Details = details;
    }

    @Override
    public String toString() {
        return "{\"Details\":\"" + Details + "\"}";
    }
}
