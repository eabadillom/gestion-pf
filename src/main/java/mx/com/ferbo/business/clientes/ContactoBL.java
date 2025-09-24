package mx.com.ferbo.business.clientes;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.SecurityUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class ContactoBL implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(ContactoBL.class);
    
    public static ClienteContacto nuevoContacto(List<ClienteContacto> listClienteContacto) 
    {
        ClienteContacto clienteContacto = new ClienteContacto();
        clienteContacto.setFhAlta(new Date());
        clienteContacto.setStHabilitado(true);
        clienteContacto.setStUsuario("A");
        Contacto contacto = new Contacto();
        contacto.setClienteContactoList(listClienteContacto);
        clienteContacto.setIdContacto(contacto);
        
        return clienteContacto;
    }
    
    public static void validateNewPassword(String newPassword, String confirmPassword) throws InventarioException
    {
        if (newPassword == null) {
            throw new InventarioException("Debe indicar una contraseña nueva.");
        }

        if (confirmPassword == null) {
            throw new InventarioException("Debe confirmar su contraseña nueva.");
        }

        if (newPassword.equals(confirmPassword) == false) {
            throw new InventarioException("Su nueva contraseña no coincide en los dos campos.");
        }
    }
    
    public static ClienteContacto changePassword(ClienteContacto clienteContacto, String newPassword, String confirmPassword) throws InventarioException
    {
        SecurityUtil util = new SecurityUtil();
        ClienteContacto auxClienteContacto = clienteContacto;
        
        if (newPassword == null || "".equalsIgnoreCase(newPassword.trim())) {
            throw new InventarioException("Debe indicar su nueva contraseña");
        }

        if (confirmPassword == null || "".equalsIgnoreCase(confirmPassword.trim())) {
            throw new InventarioException("Debe confirmar su nueva contraseña");
        }

        util.checkPassword(newPassword);
        
        String newPasswordSHA512 = util.getSHA512(newPassword);
        
        auxClienteContacto.setNbPassword(newPasswordSHA512);
        auxClienteContacto.setStUsuario("A");
        
        return auxClienteContacto;
    }
    
}
