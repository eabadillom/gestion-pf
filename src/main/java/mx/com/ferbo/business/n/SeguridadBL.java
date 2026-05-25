package mx.com.ferbo.business.n;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.SecurityUtil;

@Named
@RequestScoped
public class SeguridadBL {

    private static final Logger log = LogManager.getLogger(SeguridadBL.class);

    SecurityUtil util;

    public void generaPassword(ClienteContacto clienteContacto) {
        log.info("Inicia proceso el proceso de generar una contrasenia");
        util = new SecurityUtil();
        clienteContacto.setNbPassword(util.getRandomString());
    }

    public void validarContrasenia(String nuevaContrasenia, String contraseniaConfirmacion) throws InventarioException {
        log.info("Inicia proceso de validación de la contrasenia");
        if (nuevaContrasenia.equals("")) {
            throw new InventarioException("Debe indicar una contraseña nueva.");
        }

        if (contraseniaConfirmacion.equals("")) {
            throw new InventarioException("Debe confirmar su contraseña nueva.");
        }

        nuevaContrasenia = nuevaContrasenia.trim();
        contraseniaConfirmacion = contraseniaConfirmacion.trim();

        if (nuevaContrasenia.equals(contraseniaConfirmacion) == false) {
            throw new InventarioException("Su nueva contraseña no coincide en los dos campos.");
        }
        
        log.info("Finaliza proceso de validación de la contrasenia");
    }

    public String cambiarContrasenia(String nuevaContrasenia, String contraseniaConfirmacion)
            throws InventarioException {

        log.info("Inicia proceso para cambio de contrasenia");
        util = new SecurityUtil();

        String nuevaContrseniaSHA512 = null;

        validarContrasenia(nuevaContrasenia, contraseniaConfirmacion);

        util.checkPassword(nuevaContrasenia);

        nuevaContrseniaSHA512 = util.getSHA512(nuevaContrasenia);

        nuevaContrasenia = null;
        contraseniaConfirmacion = null;

        log.info("Finaliza proceso para cambio de contrasenia");
        return nuevaContrseniaSHA512;

    }
}
