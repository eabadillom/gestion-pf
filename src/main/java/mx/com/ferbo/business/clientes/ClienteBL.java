package mx.com.ferbo.business.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.util.ClienteUtil;
import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class ClienteBL implements Serializable
{
    private static final long serialVersionUID = 8438449261015571241L;
    private static Logger log = LogManager.getLogger(ClienteBL.class);
    
    public static Cliente nuevoCliente() 
    {
        Cliente clienteSelected;
        clienteSelected = new Cliente();
        clienteSelected.setHabilitado(true);
        clienteSelected.setClienteContactoList(new ArrayList<>());
        
        return clienteSelected;
    }
    
    public static Cliente cargaInfoCliente() 
    {
        return null;
    }
    
    public static void validarCodigoUnico(Cliente cliente) throws InventarioException 
    {
        ClienteDAO clienteDAO = new ClienteDAO();
        
        if(cliente == null)
            return;

        if(cliente.getCodUnico() == null)
                return;

        Cliente auxcliente = clienteDAO.buscarPorCodigoUnico(cliente.getCodUnico());


        if(auxcliente == null)
                return;

        if(auxcliente.equals(cliente))
                return;

        String mensaje = String.format("El código único %s ya está registrado para el cliente %s",
                        cliente.getCodUnico(), auxcliente.getNombre());
        throw new InventarioException(mensaje);
    }
    
    public static void validarRFC(Cliente cliente) throws InventarioException
    {
        String codigoUnico = null;
        
        if (ClienteUtil.validarRFC(cliente.getTipoPersona(), cliente.getCteRfc()) == false) {
            throw new InventarioException("El RFC es incorrecto");
        }

        codigoUnico = cliente.getCteRfc();
        if ("F".equalsIgnoreCase(cliente.getTipoPersona())) {
            codigoUnico = codigoUnico.substring(0, 4);
        } else if ("M".equalsIgnoreCase(cliente.getTipoPersona())) {
            codigoUnico = codigoUnico.substring(0, 3);
        }

        if (cliente.getCteCve() == null
                && (ClienteUtil.RFC_GENERICO_NACIONAL.equalsIgnoreCase(cliente.getCteRfc()) == false
                || ClienteUtil.RFC_GENERICO_EXTRANJERO.equalsIgnoreCase(cliente.getCteRfc()) == false)) {
            cliente.setCodUnico(codigoUnico);
        }
    }
    
}
