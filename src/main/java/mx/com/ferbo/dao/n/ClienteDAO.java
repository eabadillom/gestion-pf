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
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.model.UnidadDeManejo;
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

    public Cliente obtenerPorId(Integer cteCve, Boolean isFullInfo) throws InventarioException {

        Cliente cliente = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            cliente = em.createNamedQuery("Cliente.findByCteCve", Cliente.class)
                    .setParameter("cteCve", cteCve).getSingleResult();

            if (isFullInfo) {

                if (cliente.getCandadoSalida() != null)
                    cliente.getCandadoSalida().getId();

                List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();
                List<PrecioServicio> clientePrecioServicios = cliente.getPrecioServicioList();
                List<Aviso> clienteAvisos = cliente.getAvisoList();
                log.info(cliente.getRegimenFiscal().getCd_regimen());
                log.info(cliente.getRegimenFiscal().getNb_regimen());
                log.info(cliente.getUsoCfdi().getUsoCfdi());
                log.info(cliente.getMetodoPago().getNbMetodoPago());
                log.info(cliente.getMetodoPago().getCdMetodoPago());

                if (!clienteContactoList.isEmpty()) {
                    for (ClienteContacto clienteContacto : clienteContactoList) {

                        Contacto contacto = clienteContacto.getIdContacto();

                        List<MedioCnt> medioCntList = contacto.getMedioCntList();

                        for (MedioCnt medioContacto : medioCntList) {

                            Mail idMail = medioContacto.getIdMail();
                            Telefono idTelefono = medioContacto.getIdTelefono();

                            if (idMail != null)
                                idMail.getTpMail().getNbTipo();

                            if (idTelefono != null)
                                idTelefono.getTpTelefono().getNbTelefono();

                        }

                    }
                }
                if (!clientePrecioServicios.isEmpty()) {
                    for (PrecioServicio precioServicio : clientePrecioServicios) {
                        Servicio servicio = precioServicio.getServicio();
                        if (servicio != null) {
                            precioServicio.getServicio().getServicioCve();
                        }

                        UnidadDeManejo unidadManejo = precioServicio.getUnidad();
                        if (unidadManejo != null) {
                            precioServicio.getUnidad().getUnidadDeManejoCve();
                        }
                    }
                }

                if (!clienteAvisos.isEmpty()) {
                    for (Aviso clienteAviso : clienteAvisos) {
                        if (clienteAviso.getAvisoCve() != null) {

                            Categoria categoria = clienteAviso.getCategoriaCve();
                            if (categoria != null) {
                                categoria.getCategoriaCve();
                            } else {
                                clienteAviso.setCategoriaCve(new Categoria());
                            }

                            Planta planta = clienteAviso.getPlantaCve();

                            if (planta != null) {
                                planta.getPlantaCve();
                            } else {
                                clienteAviso.setPlantaCve(new Planta());
                            }

                            List<PrecioServicio> precioServicios = clienteAviso.getPrecioServicioList();

                            if (!precioServicios.isEmpty()) {
                                for (PrecioServicio ps : precioServicios) {
                                    Servicio servicio = ps.getServicio();
                                    if (servicio != null){
                                        servicio.getServicioCve();
                                    } else {
                                        ps.setServicio(new Servicio());
                                    }

                                    UnidadDeManejo unidadDeManejo = ps.getUnidad();

                                    if (unidadDeManejo != null) {
                                        unidadDeManejo.getUnidadDeManejoCve();
                                    } else {
                                        ps.setUnidad(new UnidadDeManejo());
                                    }
                                    
                                }
                            }

                        }
                    }
                }
            }

            return cliente;

        } catch (NoResultException ex) {
            log.warn("No se encontro ningun cliente con ese identifiador " + cteCve, ex);
            throw new InventarioException("No se encontro ningun cliente con el identifiador " + cteCve);
        } catch (Exception ex) {
            log.error("No se encontro ningun cliente con ese identifiador " + cteCve, ex);
            throw new InventarioException("No se encontro ningun cliente con el identifiador " + cteCve);
        } finally {
            super.close(em);
        }
    }

    public Cliente buscarPorCodigoUnico(String codigoUnico) {
        Cliente model = null;
        EntityManager em = null;

        try {
            em = super.getEntityManager();
            model = em.createNamedQuery("Cliente.findByCodUnico", this.modelClass)
                    .setParameter("codUnico", codigoUnico)
                    .getSingleResult();
        } catch (Exception ex) {
            log.warn("Problema para obtener el cliente por codigo unico: {}", ex.getMessage());
        } finally {
            super.close(em);
        }

        return model;
    }
}