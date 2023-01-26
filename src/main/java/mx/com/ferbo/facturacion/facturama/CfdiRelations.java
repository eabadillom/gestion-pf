package mx.com.ferbo.facturacion.facturama;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CfdiRelations {
    
    /**Required
     */
    @SerializedName(value = "Type")
    private String type = null;
    
    /**Required
     */
    @SerializedName(value = "Cfdis")
    private List<CfdiRelation> cfdis = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CfdiRelation> getCfdis() {
        return cfdis;
    }

    public void setCfdis(List<CfdiRelation> cfdis) {
        this.cfdis = cfdis;
    }

    @Override
    public String toString() {
        return "{\"type\":\"" + type + "\", \"cfdis\":\"" + cfdis + "\"}";
    }
}
