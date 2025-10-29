package mx.com.ferbo.business.clientes;

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
import mx.com.ferbo.dao.n.TipoMailDAO;
import mx.com.ferbo.dao.n.TipoTelefonoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.TipoTelefono;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class ContactoBL {

    private static final Logger log = LogManager.getLogger(ContactoBL.class);

    @Inject
    private MedioCntDAO medioCntDAO;

    @Inject
    private ClienteContactoDAO clienteContactoDAO;

    @Inject
    private ContactoDAO contactoDAO;

    @Inject
    private TipoTelefonoDAO tipoTelefonoDAO;

    @Inject
    private TipoMailDAO tipoMailDAO;

    public ClienteContacto nuevoContacto() {
        ClienteContacto clienteContacto = new ClienteContacto();
        Contacto contacto = new Contacto();
        contacto.setMedioCntList(new ArrayList<>());
        clienteContacto.setIdContacto(contacto);
        clienteContacto.setFhAlta(new Date());
        return clienteContacto;
    }

    public MedioCnt nuevoMedio() {
        MedioCnt medio = new MedioCnt();
        return medio;
    }

    public List<ClienteContacto> obtenerListaContactos(Cliente cliente) throws InventarioException {

        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío.");

        List<ClienteContacto> lista = clienteContactoDAO.obtenerPorClienteId(cliente);

        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }

    public List<TipoMail> obtenerTiposMail() {
        List<TipoMail> lista = tipoMailDAO.buscarTodos();

        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }

    public List<TipoTelefono> obtenerTiposTelefono() {
        List<TipoTelefono> lista = tipoTelefonoDAO.buscarTodos();

        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
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

        if (clienteContactos == null) {
            clienteContactos = new ArrayList<>();
            cliente.setClienteContactoList(clienteContactos);
        }

        final List<ClienteContacto> lista = clienteContactos;

        int index = IntStream.range(0, lista.size()).filter(i -> lista.get(i).equals(clienteContacto)).findFirst()
                .orElse(-1);

        if (index >= 0) {
            clienteContactos.set(index, clienteContacto);
        } else {
            clienteContactos.add(clienteContacto);
        }
    }

    public void agregarOActualizarMedioContacto(ClienteContacto clienteContacto, MedioCnt medioCnt)
            throws InventarioException {

        FacesUtils.requireNonNullWithReturn(clienteContacto, "El contacto del cliente no puede ser vacío.");
        FacesUtils.requireNonNullWithReturn(medioCnt, "El medio de contacto no puede ser vacío.");

        List<MedioCnt> medioCnts = clienteContacto.getIdContacto().getMedioCntList();
        if (medioCnts == null) {
            medioCnts = new ArrayList<>();
        }

        final List<MedioCnt> lista = medioCnts;

        int index = IntStream.range(0, lista.size()).filter(i -> lista.get(i).equals(medioCnt)).findFirst().orElse(-1);

        if (index >= 0) {
            medioCnts.set(index, medioCnt);
        } else {
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

    public void seleccionarMedioContacto(MedioCnt medioCnt) throws InventarioException {

        switch (medioCnt.getTpMedio()) {

            case "m":
                medioCnt.setIdMail(new Mail());
                break;

            case "t":
                medioCnt.setIdTelefono(new Telefono());
                break;

            default:
                throw new InventarioException("Tipo de medio de contacto no valido");
        }
    }

}
