package mx.com.ferbo.dao.n;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class ClienteDAO extends BaseDAO<Cliente, Integer> {

    private static Logger log = LogManager.getLogger(ClienteDAO.class);

    public ClienteDAO() {
        super(Cliente.class);
    }

    public List<Cliente> buscarTodos() {
        List<Cliente> clientes = null;
        EntityManager em = null;
        try {
            em = super.getEntityManager();
            clientes = em.createNamedQuery("Cliente.findAll", Cliente.class).getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de clientes...", ex);
        } finally {
            super.close(em);
        }
        return clientes;
    }

    public Cliente obtenerPorId(Integer cteCve, Boolean isFullInfo) throws InventarioException, DAOException {

        Cliente cliente = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            cliente = em.createNamedQuery("Cliente.findByCteCve", Cliente.class)
                    .setParameter("cteCve", cteCve).getSingleResult();

            if (isFullInfo) {

                if (cliente.getCandadoSalida() != null) {
                    cliente.getCandadoSalida().getId();
                }

                List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();
                List<PrecioServicio> clientePrecioServicios = cliente.getPrecioServicioList();
                List<Aviso> clienteAvisos = cliente.getAvisoList();
                List<ClienteDomicilios> clienteDomicilios = cliente.getClienteDomiciliosList();
                log.info(cliente.getRegimenFiscal().getCd_regimen());
                log.info(cliente.getRegimenFiscal().getNb_regimen());
                log.info(cliente.getUsoCfdi().getUsoCfdi());
                log.info(cliente.getMetodoPago().getNbMetodoPago());
                log.info(cliente.getMetodoPago().getCdMetodoPago());
                
                for(Aviso a : clienteAvisos) {
                	log.debug("Aviso: {}", a.getAvisoCve());
                	a.getPrecioServicioList().forEach(ps -> {
                		log.debug("Precio servicio: {}", ps.getId());
                	});
                }
                
                for(PrecioServicio ps : clientePrecioServicios) {
                	log.debug("Precio servicio: {}", ps.getId());
                }
                
                if (!clienteContactoList.isEmpty()) {
                    for (ClienteContacto clienteContacto : clienteContactoList) {

                        Contacto contacto = clienteContacto.getIdContacto();

                        List<MedioCnt> medioCntList = contacto.getMedioCntList();

                        for (MedioCnt medioContacto : medioCntList) {

                            Mail idMail = medioContacto.getIdMail();
                            Telefono idTelefono = medioContacto.getIdTelefono();

                            if (idMail != null) {
                                idMail.getTpMail().getNbTipo();
                            }

                            if (idTelefono != null) {
                                idTelefono.getTpTelefono().getNbTelefono();
                            }

                        }

                    }
                }

                if (!clienteDomicilios.isEmpty()) {
                    for (ClienteDomicilios clienteDomicilio : clienteDomicilios) {
                        log.info(clienteDomicilio.getDomicilios());
                    }
                }
            }

            return cliente;
        } catch (Exception ex) {
            log.error("No se encontro ningun cliente con ese identifiador " + cteCve, ex);
            throw new DAOException("No se encontro ningun cliente con el identifiador " + cteCve);
        } finally {
            super.close(em);
        }
    }

    public Cliente buscarPorCodigoUnico(String codigoUnico) throws DAOException {
        Cliente model = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            model = em.createNamedQuery("Cliente.findByCodUnico", this.modelClass)
                    .setParameter("codUnico", codigoUnico)
                    .getSingleResult();
        } catch (NoResultException ex) {
            log.info("No se encontró cliente con código único: {}", codigoUnico);
            return null;
        } catch (Exception ex) {
            log.warn("Problema para obtener el cliente por codigo unico: {}", ex.getMessage());
            throw new DAOException("Hubo un problema al buscar el cliente");
        } finally {
            super.close(em);
        }

        return model;
    }
    
    public Cliente buscarPorNombre(String nombreCte) throws DAOException {
        Cliente model = null;
        EntityManager em = null;
        
        try {
            em = super.getEntityManager();
            
            model = em.createNamedQuery("Cliente.findByCteNombre", this.modelClass)
                    .setParameter("cteNombre", nombreCte)
                    .getSingleResult();
        } catch (NoResultException ex) {
            log.info("No se encontró cliente con el nombre: {}", nombreCte);
            return null;
        } catch (Exception ex) {
            log.warn("Problema para obtener el cliente por el nombre: {}", ex.getMessage());
            throw new DAOException("Hubo un problema al buscar el cliente");
        } finally {
            super.close(em);
        }
        
        return model;
    }
    
}
