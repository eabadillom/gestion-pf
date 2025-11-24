package mx.com.ferbo.business.n;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.ClienteContactoDAO;
import mx.com.ferbo.dao.n.ContactoDAO;
import mx.com.ferbo.dao.n.MedioCntDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class ClienteContactoBL {

    private static final Logger log = LogManager.getLogger(ClienteContactoBL.class);

    @Inject
    private MedioCntDAO medioCntDAO;

    @Inject
    private ClienteContactoDAO clienteContactoDAO;

    @Inject
    private ContactoDAO contactoDAO;

    public ClienteContacto nuevoContacto() {
        ClienteContacto clienteContacto = new ClienteContacto();
        Contacto contacto = new Contacto();
        contacto.setMedioCntList(new ArrayList<>());
        clienteContacto.setIdContacto(contacto);
        clienteContacto.setFhAlta(new Date());
        return clienteContacto;
    }

    public List<ClienteContacto> obtenerListaContactos(Cliente cliente) throws InventarioException {

        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío.");
        try {
            return clienteContactoDAO.obtenerPorClienteId(cliente);
        } catch (DAOException ex) {
            log.error("Error al obtener los contactos del cliente: " + cliente.getNombre(), ex);
            throw new InventarioException("Ocurrió un error al obtener  los contactos del cliente: " + cliente.getNombre(), ex);
        }
    }

    public ClienteContacto nuevoClienteContacto() {
        ClienteContacto clienteContacto = new ClienteContacto();
        Contacto contacto = new Contacto();
        contacto.setMedioCntList(new ArrayList<>());
        clienteContacto.setIdContacto(contacto);
        clienteContacto.setFhAlta(new Date());
        clienteContacto.setStHabilitado(true);
        clienteContacto.setStUsuario("A");
        return clienteContacto;
    }

    public void agregarOActualizarContacto(Cliente cliente, ClienteContacto clienteContacto)
            throws InventarioException {

        FacesUtils.requireNonNullWithReturn(cliente, "El cliente no puede ser vacío.");
        FacesUtils.requireNonNullWithReturn(clienteContacto, "El contacto del cliente no puede ser vacío.");

        List<ClienteContacto> clienteContactos = cliente.getClienteContactoList();

        if (clienteContactos == null || clienteContactos.isEmpty()) {
            clienteContactos = new ArrayList<>();
            cliente.setClienteContactoList(clienteContactos);
        }

        final List<ClienteContacto> lista = clienteContactos;

        int index = IntStream.range(0, lista.size()).filter(i -> lista.get(i).equals(clienteContacto)).findFirst()
                .orElse(-1);

        if (index >= 0) {
            clienteContactos.set(index, clienteContacto);
        } else {
            clienteContacto.setIdCliente(cliente);
            clienteContactos.add(clienteContacto);
        }
    }

    public void agregarOActualizarMedioContacto(ClienteContacto clienteContacto, MedioCnt medioCnt)
            throws InventarioException {

        FacesUtils.requireNonNullWithReturn(clienteContacto, "El contacto del cliente no puede ser vacío.");
        FacesUtils.requireNonNullWithReturn(medioCnt, "El medio de contacto no puede ser vacío.");

        List<MedioCnt> medioCnts = clienteContacto.getIdContacto().getMedioCntList();
        if (medioCnts == null || medioCnts.isEmpty()) {
            medioCnts = new ArrayList<>();
            clienteContacto.getIdContacto().setMedioCntList(medioCnts);
        }

        final List<MedioCnt> lista = medioCnts;

        int index = IntStream.range(0, lista.size()).filter(i -> lista.get(i).equals(medioCnt)).findFirst().orElse(-1);

        if (index >= 0) {
            medioCnts.set(index, medioCnt);
        } else {
            medioCnt.setIdContacto(clienteContacto.getIdContacto());
            medioCnts.add(medioCnt);
        }
    }

    public void eliminarMedioContacto(ClienteContacto clienteContacto, MedioCnt medioCnt) throws InventarioException {
        FacesUtils.requireNonNullWithReturn(clienteContacto, "El contacto del cliente no puede estar vacío.");
        FacesUtils.requireNonNullWithReturn(medioCnt, "Debe proporcionar un medio de contacto para eliminar.");

        clienteContacto.getIdContacto().getMedioCntList().remove(medioCnt);

        if (medioCnt.getIdMedio() != null) {
            medioCntDAO.eliminar(medioCnt);
        }
    }

    public void eliminarContacto(Cliente cliente, ClienteContacto clienteContacto) throws InventarioException {
        FacesUtils.requireNonNullWithReturn(cliente, "El cliente no puede estar vacío.");
        FacesUtils.requireNonNullWithReturn(clienteContacto, "Debe proporcionar un ClienteContacto para eliminar.");

        if (clienteContacto.getId() == null) {
            cliente.getClienteContactoList().remove(clienteContacto);
        } else {

            List<MedioCnt> medioCnts = clienteContacto.getIdContacto().getMedioCntList();

            if (!medioCnts.isEmpty()) {
                do {
                    eliminarMedioContacto(clienteContacto, medioCnts.get(0));
                } while (!medioCnts.isEmpty());
            }

            cliente.getClienteContactoList().remove(clienteContacto);

            contactoDAO.eliminar(clienteContacto.getIdContacto());
            clienteContactoDAO.eliminar(clienteContacto);

        }
    }

}
