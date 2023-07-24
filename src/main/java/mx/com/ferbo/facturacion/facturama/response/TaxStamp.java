package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

public class TaxStamp {
    
    @SerializedName(value = "Uuid")
    private String uuid;
    
    @SerializedName(value = "Date")
    private String date;
    
    @SerializedName(value = "CfdiSign")
    private String cfdiSign;
    
    @SerializedName(value = "SatCertNumber")
    private String satCertNumber;
    
    @SerializedName(value = "SatSign")
    private String satSign;
    
    @SerializedName(value = "RfcProvCertif")
    private String rfcProvCertif;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCfdiSign() {
        return cfdiSign;
    }

    public void setCfdiSign(String cfdiSign) {
        this.cfdiSign = cfdiSign;
    }

    public String getSatCertNumber() {
        return satCertNumber;
    }

    public void setSatCertNumber(String satCertNumber) {
        this.satCertNumber = satCertNumber;
    }

    public String getSatSign() {
        return satSign;
    }

    public void setSatSign(String satSign) {
        this.satSign = satSign;
    }

    public String getRfcProvCertif() {
        return rfcProvCertif;
    }

    public void setRfcProvCertif(String rfcProvCertif) {
        this.rfcProvCertif = rfcProvCertif;
    }

    @Override
    public String toString() {
        return "{\"uuid\":\"" + uuid + "\", \"date\":\"" + date + "\", \"cfdiSign\":\"" + cfdiSign
                + "\", \"satCertNumber\":\"" + satCertNumber + "\", \"satSign\":\"" + satSign
                + "\", \"rfcProvCertif\":\"" + rfcProvCertif + "\"}";
    }
}
