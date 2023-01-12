package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Complemento de Institución educativa Complemento concepto para la expedición de comprobantes fiscales por parte de Instituciones Educativas Privadas, para los efectos del artículo primero y cuarto del decreto por el que se otorga un estímulo fiscal a las personas físicas en relación con los pagos por servicios educativos<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=EducationalInstitution<br>
 * @author esteban
 */
public class EducationalInstitution {
    
    /**Nombre del alumno<br>
     */
    @SerializedName(value = "StudentsName")
    private String studentsName = null;
    
    /**Clave única de registro de población del alumno<br>
     * Required<br>
     * Matching regular expression pattern: [A-Z][A,E,I,O,U,X][A-Z]{2}[0-9]{2}[0-1][0-9][0-3][0-9][M,H][A-Z]{2}[B,C,D,F,G,H,J,K,L,M,N,Ñ,P,Q,R,S,T,V,W,X,Y,Z]{3}[0-9,A-Z][0-9]<br>
     */
    @SerializedName(value = "Curp")
    private String curp = null;
    
    /**Preescolar, Primaria, Secundaria, Profesional técnico, Bachillerato o su equivalente<br>
     * Required<br>
     * Matching regular expression pattern: Preescolar|Primaria|Secundaria|Profesional técnico|Bachillerato o su equivalente<br>
     */
    @SerializedName(value = "EducationLevel")
    private String educationLevel = null;
    
    /**Clave del centro de trabajo o el reconocimiento de validez oficial de esudios que tenga la instución educativa privada donde se realiza el pago<br>
     * Required<br>
     */
    @SerializedName(value = "AutRvoe")
    private String autRvoe = null;
    
    /**RFC de quien realiza el pago cuando sea diferente a quien recibe el servicio (opcional)<br>
     */
    @SerializedName(value = "PaymentRfc")
    private String paymentRfc = null;
}
