package mx.com.ferbo.dao.n;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.util.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class ClienteDomiciliosDAO extends BaseDAO<ClienteDomicilios, Integer> 
{
    Logger log = LogManager.getLogger(ClienteDomiciliosDAO.class);

    public ClienteDomiciliosDAO(Class<ClienteDomicilios> modelClass) {
        super(modelClass);
    }

    public ClienteDomiciliosDAO() {
        super(ClienteDomicilios.class);
    }

    public List<ClienteDomicilios> buscarTodos() {
        List<ClienteDomicilios> listado = null;
        EntityManager em = null;
        
        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("ClienteDomicilios.findAll", ClienteDomicilios.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de domicilios por cliente...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }
        
        return listado;
    }

    public List<ClienteDomicilios> buscarPorCriterios(ClienteDomicilios e) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ClienteDomicilios> buscarPorCliente(Integer idCliente) {
        List<ClienteDomicilios> lista = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            lista = em.createNamedQuery("ClienteDomicilios.findByCliente", ClienteDomicilios.class)
                .setParameter("cteCve", idCliente)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de domicilios por cliente...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return lista;
    }

    public List<ClienteDomicilios> buscarDomicilioFiscalPorCliente(Integer idCliente, boolean isFullInfo) {
        List<ClienteDomicilios> listado = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("ClienteDomicilios.findByClienteDomFiscal", ClienteDomicilios.class)
                .setParameter("cteCve", idCliente)
                .getResultList();

            if (isFullInfo == false) {
                return listado;
            }

            for (ClienteDomicilios cd : listado) {
                log.debug("Domicilio cve: {}", cd.getDomicilios().getDomCve());
                log.debug("PaisCve: {}", cd.getDomicilios().getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve());
            }

        } catch (Exception ex) {
            log.error("Problema para obtener el listado de domicilios por cliente...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

    public List<ClienteDomicilios> buscaPorCliente(Cliente c) {
        List<ClienteDomicilios> listado = null;
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            listado = em.createNamedQuery("ClienteDomicilios.findByCliente", ClienteDomicilios.class)
                .setParameter("cteCve", c.getCteCve())
                .getResultList();

            for (ClienteDomicilios cd : listado) {
                log.debug("Asentamiento: {}", cd.getDomicilios().getAsentamiento().toString());
                log.debug("Ciudad: {}", cd.getDomicilios().getAsentamiento().getAsentamientoHumanoPK().getCiudades().toString());
                log.debug("Municipios: {}", cd.getDomicilios().getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().toString());
                log.debug("Estado: {}", cd.getDomicilios().getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().toString());
                log.debug("Pais: {}", cd.getDomicilios().getAsentamiento().getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().toString());
            }

        } catch (Exception ex) {
            log.error("Problema para obtener el listado de domicilios por cliente...", ex);
        } finally {
            EntityManagerUtil.close(em);
        }

        return listado;
    }

}
