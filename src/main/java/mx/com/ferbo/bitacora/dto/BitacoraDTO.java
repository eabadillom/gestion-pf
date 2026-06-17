package mx.com.ferbo.bitacora.dto;

import java.util.Date;

public class BitacoraDTO {

    private String documento;
    private Integer idUsuario;
    private String nombreUsuario;
    private String apellido1Usuario;
    private String apellido2Usuario;
    private String nombrePantalla;
    private String tipoPantalla;
    private Date momento;

    public BitacoraDTO() {
    }

    public BitacoraDTO(String documento,
            Integer idUsuario, String nombreUsuario, String apellido1Usuario, String apellido2Usuario, 
            String nombrePantalla,
            String tipoPantalla,
            Date momento) {

        this.documento = documento;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellido1Usuario = apellido1Usuario;
        this.apellido2Usuario = apellido2Usuario;
        this.nombrePantalla = nombrePantalla;
        this.tipoPantalla = tipoPantalla;
        this.momento = momento; 

    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellido1Usuario() {
        return apellido1Usuario;
    }

    public void setApellido1Usuario(String apellido1Usuario) {
        this.apellido1Usuario = apellido1Usuario;
    }

    public String getApellido2Usuario() {
        return apellido2Usuario;
    }

    public void setApellido2Usuario(String apellido2Usuario) {
        this.apellido2Usuario = apellido2Usuario;
    }

    public String getNombrePantalla() {
        return nombrePantalla;
    }

    public void setNombrePantalla(String nombrePantalla) {
        this.nombrePantalla = nombrePantalla;
    }

    public String getTipoPantalla() {
        return tipoPantalla;
    }

    public void setTipoPantalla(String tipoPantalla) {
        this.tipoPantalla = tipoPantalla;
    }

    public Date getMomento() {
        return momento;
    }

    public void setMomento(Date momento) {
        this.momento = momento;
    }

}
