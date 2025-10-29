package mx.com.ferbo.business.clientes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.ClienteDAO;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.InventarioException;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class ClienteBL {

    private static Logger log = LogManager.getLogger(ClienteBL.class);

    @Inject
    private ClienteDAO clienteDAO;

    public List<Cliente>  obtenerTodos(){
        List<Cliente> lista = clienteDAO.buscarTodos();
        
        if(lista == null){
            return new ArrayList<>();
        }

        return lista;
    }

    public Cliente obtenerTodoCliente(Integer id, Boolean isFullInfo) throws InventarioException{

        return clienteDAO.obtenerPorId(id, isFullInfo);

    }

    public Cliente obtenerPorCodigoUnico(String codigo){

        return clienteDAO.buscarPorCodigoUnico(codigo);
        
    }

    public Cliente nuevoCliente() {
        Cliente cliente = new Cliente();
        cliente = new Cliente();
        cliente.setMetodoPago(new MetodoPago());
        cliente.setRegimenFiscal(new RegimenFiscal());
        cliente.setUsoCfdi(new UsoCfdi());
        cliente.setCandadoSalida(new CandadoSalida());
        cliente.setClienteContactoList(new ArrayList());
        cliente.setAvisoList(new ArrayList());

        return cliente;
    }

    public String guardarOActualizar(Cliente cliente) throws InventarioException {
        String status = (cliente.getCteCve() != null) ? "actualizado" : "agregado";
        if (cliente.getCteCve() == null) {
            clienteDAO.guardar(cliente);
        } else {
            clienteDAO.actualizar(cliente);
        }
        return "Cliente " + status + " exitosamente";
    }

    public List<Cliente> filtrarPorEstatus(List<Cliente> original, Boolean estado) {
        if (original == null) {
            return Collections.emptyList();
        }

        return original.stream()
                .filter(cliente -> estado.equals(cliente.getHabilitado())).collect(Collectors.toList());
    }

}
