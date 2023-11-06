package mx.com.ferbo.facturacion.facturama;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class ThirdPartyCustomsInformation {
    /**Atributo requerido para expresar el número del documento aduanero que ampara la importación del bien.<br>
     * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ThirdPartyCustomsInformation<br>
     * Required<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "Number")
    private String number = null;
    
    /**Atributo requerido para expresar la fecha de expedición del documento aduanero que ampara la importación del bien.<br>
     * Required<br>
     */
    @SerializedName(value = "Date")
    private Date date = null;
    
    /**Atributo opcional para precisar la aduana por la que se efectuó la importación del bien.<br>
     * Min length: 1<br>
     */
    @SerializedName(value = "Customs")
    private String customs = null;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustoms() {
        return customs;
    }

    public void setCustoms(String customs) {
        this.customs = customs;
    }

    @Override
    public String toString() {
        return "{\"number\":\"" + number + "\", \"date\":\"" + date + "\", \"customs\":\"" + customs + "\"}";
    }
}
