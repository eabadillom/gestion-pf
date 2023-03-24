package mx.com.ferbo.facturacion.facturama.response;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ProductRsp {
    @SerializedName(value="Id")
    private String id = null;
    
    @SerializedName(value="UnitCode")
    private String unitCode = null;
    
    @SerializedName(value="Unit")
    private String unit = null;
    
    @SerializedName(value="IdentificationNumber")
    private String identificationNumber = null;
    
    @SerializedName(value="Name")
    private String name = null;
    
    @SerializedName(value="Description")
    private String description = null;
    
    @SerializedName(value="Category")
    private String category = null;
    
    @SerializedName(value="Code")
    private String code = null;
    
    @SerializedName(value="Price")
    private BigDecimal price = null;
    
    @SerializedName(value="CodeProdServ")
    private String codeProdServ = null;
    
    @SerializedName(value="NameCodeProdServ")
    private String nameCodeProdServ = null;
    
    @SerializedName(value="CuentaPredial")
    private String cuentaPredial = null;
    
    @SerializedName(value="ObjetoImp")
    private String objetoImp = null;
    
    @SerializedName(value="Taxes")
    private List<ProductTaxRsp> taxes = null;

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"unitCode\":\"" + unitCode + "\", \"unit\":\"" + unit
                + "\", \"identificationNumber\":\"" + identificationNumber + "\", \"name\":\"" + name
                + "\", \"description\":\"" + description + "\", \"category\":\"" + category + "\", \"code\":\"" + code
                + "\", \"price\":\"" + price + "\", \"codeProdServ\":\"" + codeProdServ + "\", \"nameCodeProdServ\":\""
                + nameCodeProdServ + "\", \"objetoImp\":\"" + objetoImp
                + "\", \"taxes\":\"" + taxes + "\"}";
    }//"\", \"cuentaPredial\":\"" + cuentaPredial +

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCodeProdServ() {
        return codeProdServ;
    }

    public void setCodeProdServ(String codeProdServ) {
        this.codeProdServ = codeProdServ;
    }

    public String getNameCodeProdServ() {
        return nameCodeProdServ;
    }

    public void setNameCodeProdServ(String nameCodeProdServ) {
        this.nameCodeProdServ = nameCodeProdServ;
    }

    public String getCuentaPredial() {
        return cuentaPredial;
    }

    public void setCuentaPredial(String cuentaPredial) {
        this.cuentaPredial = cuentaPredial;
    }

    public String getObjetoImp() {
        return objetoImp;
    }

    public void setObjetoImp(String objetoImp) {
        this.objetoImp = objetoImp;
    }

    public List<ProductTaxRsp> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ProductTaxRsp> taxes) {
        this.taxes = taxes;
    }
}
