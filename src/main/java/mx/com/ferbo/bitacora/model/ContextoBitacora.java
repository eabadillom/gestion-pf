package mx.com.ferbo.bitacora.model;

import com.ferbo.tools.exception.ValidationException;
import com.ferbo.tools.validation.ObjectValidator;

import mx.com.ferbo.bitacora.enums.NombrePantalla;
import mx.com.ferbo.bitacora.enums.TipoPantalla;
import mx.com.ferbo.model.Usuario;

public class ContextoBitacora {

    private final String idSesion;

    private final Usuario usuario;

    private final NombrePantalla nombrePantalla;

    private final TipoPantalla tipoPantalla;

    private ContextoBitacora(String idSesion, Usuario usuario, NombrePantalla nombrePantalla,
            TipoPantalla tipoPantalla) {

        validacionDeDatos(idSesion, usuario, nombrePantalla, tipoPantalla);

        this.idSesion = idSesion;
        this.usuario = usuario;
        this.nombrePantalla = nombrePantalla;
        this.tipoPantalla = tipoPantalla;
    }

    private void validacionDeDatos(String idSesion, Usuario usuario, NombrePantalla nombrePantalla,
            TipoPantalla tipoPantalla) {
        if (idSesion == null || "".equalsIgnoreCase(idSesion)) {
            throw new ValidationException("El contexto de la bitacora necesita una sesión y no puede ser vacía");
        }

        ObjectValidator.notNull(usuario, "usuario");

        ObjectValidator.notNull(nombrePantalla, "nombre de pantalla");

        ObjectValidator.notNull(tipoPantalla, "tipo de pantalla");
    }

    public static ContextoBitacora of(
            String idSesion,
            Usuario usuario,
            NombrePantalla nombrePantalla,
            TipoPantalla tipoPantalla) {

        return new ContextoBitacora(
                idSesion,
                usuario,
                nombrePantalla,
                tipoPantalla);
    }

    public String getIdSesion() {
        return idSesion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public NombrePantalla getNombrePantalla() {
        return nombrePantalla;
    }

    public TipoPantalla getTipoPantalla() {
        return tipoPantalla;
    }

    @Override
    public String toString() {
        return "ContextBitacora{" +
                "idSesion='" + idSesion + '\'' +
                ", usuario=" + usuario.getNombre() + " " + usuario.getApellido1() + " " + usuario.getApellido2() +
                ", nombrePantalla=" + nombrePantalla +
                ", tipoPantalla=" + tipoPantalla +
                '}';
    }
}
