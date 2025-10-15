package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class ClienteContactoDAO extends BaseDAO<ClienteContacto, Integer> {

    private static Logger log = LogManager.getLogger(ClienteContactoDAO.class);

    public ClienteContactoDAO() {
        super(ClienteContacto.class);
    }

    public List<ClienteContacto> obtenerPorClienteId(Cliente cliente) throws InventarioException {
    EntityManager em = getEntityManager();
    List<ClienteContacto> resultados = new ArrayList<>();

    try {
        Integer idCliente = cliente.getCteCve();
         resultados = em.createNamedQuery("ClienteContacto.findAllByIdCliente", ClienteContacto.class)
                       .setParameter("idCliente", idCliente)
                       .getResultList();
    } catch (Exception ex) {
        log.error("Error al obtener los contactos del cliente cliente: " + cliente.getAlias(), ex);
        throw new InventarioException("No se obtuvieron los contactos del cliente " + cliente.getAlias());
    } finally {
        super.close(em);
    }

    return resultados;
}

}
