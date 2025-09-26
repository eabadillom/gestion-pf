package mx.com.ferbo.business.clientes;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.ClienteContactoDAO;
import mx.com.ferbo.dao.n.ContactoDAO;
import mx.com.ferbo.dao.n.MedioCntDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.MedioCnt;
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

	 public List<ClienteContacto> obtenerListaContactos(Cliente cliente) throws InventarioException {

		requireNonNull(cliente, "El cliente no puede ser vacío.");

        List<ClienteContacto> lista = clienteContactoDAO.obtenerPorClienteId(cliente);

        if( lista == null){
            return new ArrayList<>();
        }
		
        return lista;
    }

	public ClienteContacto nuevoClienteContacto() {
		ClienteContacto clienteContacto = new ClienteContacto();
		clienteContacto.setFhAlta(new Date());
		clienteContacto.setStHabilitado(true);
		clienteContacto.setStUsuario("A"); // Asumimos valor por defecto
		return clienteContacto;
	}

	public void agregarContacto(Cliente cliente, ClienteContacto clienteContacto, Contacto contacto)
			throws InventarioException {

		requireNonNull(cliente, "El cliente no puede ser vacío.");
		requireNonNull(contacto, "El contacto no puede ser vacío.");

		if (clienteContacto == null) {
			clienteContacto = nuevoClienteContacto();
		}

		clienteContacto.setIdContacto(contacto);
		clienteContacto.setIdCliente(cliente); // Si la relación es bidireccional

		List<ClienteContacto> clienteContactos = cliente.getClienteContactoList();
		if (clienteContactos == null) {
			clienteContactos = new ArrayList<>();
		}

		clienteContactos.add(clienteContacto);
		cliente.setClienteContactoList(clienteContactos);
	}

	public void agregarMedioContacto(Cliente cliente, ClienteContacto clienteContacto, Contacto contacto,
			MedioCnt medioCnt) throws InventarioException {

		requireNonNull(cliente, "El cliente no puede ser vacío.");
		requireNonNull(contacto, "El contacto no puede ser vacío.");
		requireNonNull(medioCnt, "El medio de contacto no puede ser vacío.");

		if (clienteContacto == null) {
			clienteContacto = nuevoClienteContacto();
		}

		List<ClienteContacto> clienteContactos = cliente.getClienteContactoList();
		if (clienteContactos == null) {
			clienteContactos = new ArrayList<>();
		}

		List<MedioCnt> medioCnts = contacto.getMedioCntList();
		if (medioCnts == null) {
			medioCnts = new ArrayList<>();
		}

		medioCnts.add(medioCnt);
		contacto.setMedioCntList(medioCnts);

		clienteContacto.setIdContacto(contacto);
		clienteContacto.setIdCliente(cliente); // si aplica

		clienteContactos.add(clienteContacto);
		cliente.setClienteContactoList(clienteContactos);
	}

	public void eliminarMedioContacto(MedioCnt medioCnt) throws InventarioException {
		requireNonNull(medioCnt, "Debe proporcionar un medio de contacto para eliminar.");
		medioCntDAO.eliminar(medioCnt);
	}

	public void eliminarContacto(ClienteContacto clienteContacto) throws InventarioException {
		requireNonNull(clienteContacto, "Debe proporcionar un ClienteContacto para eliminar.");

		Contacto contacto = clienteContacto.getIdContacto();
		if (contacto != null) {
			List<MedioCnt> medioCnts = contacto.getMedioCntList();
			if (medioCnts != null) {
				for (MedioCnt medioCnt : medioCnts) {
					eliminarMedioContacto(medioCnt);
				}
			}
			contactoDAO.eliminar(contacto);
		}
	}

	public void eliminarContactoCliente(Cliente cliente) throws InventarioException {
		requireNonNull(cliente, "Debe proporcionar un cliente para eliminar sus contactos.");

		List<ClienteContacto> clienteContactos = cliente.getClienteContactoList();
		if (clienteContactos != null) {
			for (ClienteContacto clienteContacto : clienteContactos) {
				eliminarContacto(clienteContacto);
				clienteContactoDAO.eliminar(clienteContacto);
			}
		}
	}

	private <T> T requireNonNull(T obj, String mensaje) throws InventarioException {
		if (obj == null) {
			throw new InventarioException(mensaje);
		}
		return obj;
	}
}
