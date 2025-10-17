package mx.com.ferbo.business.clientes;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
        clienteContacto.setIdContacto(new Contacto());
        clienteContacto.setFhAlta(new Date());
        clienteContacto.setStHabilitado(true);
        clienteContacto.setStUsuario("A"); // Asumimos valor por defecto
        return clienteContacto;
    }

    public void agregarContacto(Cliente cliente, ClienteContacto clienteContacto)
            throws InventarioException {

        FacesUtils.requireNonNullWithReturn(cliente, "El cliente no puede ser vacío.");
        FacesUtils.requireNonNullWithReturn(clienteContacto, "El contacto del cliente no puede ser vacío.");

        List<ClienteContacto> clienteContactos = cliente.getClienteContactoList();

        if (clienteContactos == null) {
            clienteContactos = new ArrayList<>();
            cliente.setClienteContactoList(clienteContactos);
        }

        Optional<ClienteContacto> existente = clienteContactos.stream()
                .filter(cc -> FacesUtils.normalizar(cc.getIdContacto().getNbNombre())
                        .equals(FacesUtils.normalizar(clienteContacto.getIdContacto().getNbNombre()))
                        && FacesUtils.normalizar(cc.getIdContacto().getNbApellido1())
                                .equals(FacesUtils.normalizar(clienteContacto.getIdContacto().getNbApellido1()))
                        && FacesUtils.normalizar(cc.getIdContacto().getNbApellido2())
                                .equals(FacesUtils.normalizar(clienteContacto.getIdContacto().getNbApellido2())))
                .findFirst();
        if (existente.isPresent()) {
            int index = clienteContactos.indexOf(existente.get());
            clienteContactos.set(index, clienteContacto);
        } else {
            clienteContactos.add(clienteContacto);
        }
    }

    public void agregarMedioContacto(ClienteContacto clienteContacto, MedioCnt medioCnt) throws InventarioException {

        FacesUtils.requireNonNullWithReturn(clienteContacto, "El contacto del cliente no puede ser vacío.");
        FacesUtils.requireNonNullWithReturn(medioCnt, "El medio de contacto no puede ser vacío.");

        Contacto contacto = clienteContacto.getIdContacto();
        if (contacto == null) {
            contacto = new Contacto(); // Si estás creando nuevo
            clienteContacto.setIdContacto(contacto);
        }

        List<MedioCnt> medioCnts = contacto.getMedioCntList();
        if (medioCnts == null) {
            medioCnts = new ArrayList<>();
            contacto.setMedioCntList(medioCnts);
        }

        medioCnts.add(medioCnt);
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
            List<MedioCnt> medioCnts = new ArrayList<>(clienteContacto.getIdContacto().getMedioCntList());
            for (MedioCnt medioCnt : medioCnts) {
                eliminarMedioContacto(clienteContacto, medioCnt);
            }

            contactoDAO.eliminar(clienteContacto.getIdContacto());
            clienteContactoDAO.eliminar(clienteContacto);
            cliente.getClienteContactoList().remove(clienteContacto);
        }
    }

    public void seleccionarMedioContacto(MedioCnt medioCnt) throws InventarioException {
        if ("t".equalsIgnoreCase(medioCnt.getTpMedio())) {
            Telefono telefono = new Telefono();
            medioCnt.setIdTelefono(telefono);
            medioCnt.setIdMail(null);
            log.info("Agregando tipo de medio Telefono.");
        } else if ("m".equalsIgnoreCase(medioCnt.getTpMedio())) {
            Optional<TipoMail> tipo = tipoMailDAO.buscarPorId(1);
            Mail mail = new Mail();
            mail.setStPrincipal(true);
            mail.setTpMail(tipo.orElseThrow(() -> new InventarioException("TipoMail con ID 1 no encontrado")));
            medioCnt.setIdMail(mail);
            medioCnt.setIdTelefono(null);
            log.info("Agregando tipo de medio Mail.");
        }
    }
    
}
