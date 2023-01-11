package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**SpecificDescriptions<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=SpecificDescriptions<br>
 * @author esteban
 *
 */
public class SpecificDescriptions {
    
    /**Brand<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,35}<br>
     * String length: inclusive between 1 and 35<br>
     */
    @SerializedName(value = "Brand")
    private String brand = null;
    
    /**Model<br>
     * Matching regular expression pattern: [^|]{1,80}<br>
     * String length: inclusive between 1 and 80<br>
     */
    @SerializedName(value = "Model")
    private String model = null;
    
    /**SubModel<br>
     * Matching regular expression pattern: [^|]{1,50}<br>
     * String length: inclusive between 1 and 50<br>
     */
    @SerializedName(value = "SubModel")
    private String subModel = null;
    
    /**SerialNumber<br>
     * Matching regular expression pattern: [^|]{1,40}<br>
     * String length: inclusive between 1 and 40<br>
     */
    @SerializedName(value = "SerialNumber")
    private String serialNumber = null;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSubModel() {
        return subModel;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "{\"brand\":\"" + brand + "\", \"model\":\"" + model + "\", \"subModel\":\"" + subModel
                + "\", \"serialNumber\":\"" + serialNumber + "\"}";
    }
}
