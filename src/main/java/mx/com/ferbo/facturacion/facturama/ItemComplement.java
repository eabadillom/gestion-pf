package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Complementos de Item<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=ItemComplement<br>
 * @author esteban
 *
 */
public class ItemComplement {
    /**Complemento de Instituciones educativas<br>
     */
    @SerializedName(value = "EducationalInstitution")
    private EducationalInstitution cducationalInstitution = null;
    
    @SerializedName(value = "ThirdPartyAccount")
    private ThirdPartyAccount thirdPartyAccount = null;

    public EducationalInstitution getCducationalInstitution() {
        return cducationalInstitution;
    }

    public void setCducationalInstitution(EducationalInstitution cducationalInstitution) {
        this.cducationalInstitution = cducationalInstitution;
    }

    public ThirdPartyAccount getThirdPartyAccount() {
        return thirdPartyAccount;
    }

    public void setThirdPartyAccount(ThirdPartyAccount thirdPartyAccount) {
        this.thirdPartyAccount = thirdPartyAccount;
    }

    @Override
    public String toString() {
        return "{\"cducationalInstitution\":\"" + cducationalInstitution + "\", \"thirdPartyAccount\":\""
                + thirdPartyAccount + "\"}";
    }
}
