package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

public class PartesTransporte {
    
    /**Required
     */
    @SerializedName(value = "ParteTransporte")
    private String parteTransporte = null;

    public String getParteTransporte() {
        return parteTransporte;
    }

    public void setParteTransporte(String parteTransporte) {
        this.parteTransporte = parteTransporte;
    }

    @Override
    public String toString() {
        return "{\"parteTransporte\":\"" + parteTransporte + "\"}";
    }
}
