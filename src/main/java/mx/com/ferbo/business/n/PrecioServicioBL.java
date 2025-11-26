package mx.com.ferbo.business.n;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.PrecioServicioDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

/**
 *
 * @author julio
 */
@Named
@RequestScoped
public class PrecioServicioBL {

    private static Logger log = LogManager.getLogger(PrecioServicioBL.class);

    @Inject
    private PrecioServicioDAO precioServicioDAO;

    public PrecioServicio nuevoPrecioServicio(Cliente cliente) {
        log.info("Incia proceso para crear un nuevo precio de servicio");
        PrecioServicio precioServicio = new PrecioServicio();
        precioServicio.setCliente(cliente);
        precioServicio.setPrecio(BigDecimal.ZERO);
        precioServicio.setServicio(new Servicio());
        precioServicio.setUnidad(new UnidadDeManejo());
        precioServicio.setAvisoCve(new Aviso(1));
        log.info("Finaliza proceso para crear un nuevo precio de servicio");
        return precioServicio;
    }

    public List<PrecioServicio> obtenerPrecioServiciosPorCliente(Cliente cliente, Boolean isFullInfo) throws InventarioException {
        
        String tipo = (isFullInfo) ? "completa": "incompleta";
        
        log.info("Inicia proceso para obtener todos los precios de servicio con información {} del cliente {}", tipo, cliente.getNombre());
        FacesUtils.requireNonNull(cliente, "El cliente no puede estar vacío");

        try {
            return precioServicioDAO.buscarPorCliente(cliente.getCteCve(), isFullInfo);
        } catch (DAOException ex) {
            log.info(ex.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PrecioServicio> buscarPorCriterios(PrecioServicio e) throws InventarioException {
        log.info("Inicia proceso para obtener los precios de servicio por el critierio de: ");
        try {
            if (e.getCliente().getCteCve() == null)
                return null;

            if (e.getServicio() != null) {
                log.info("servicio");
                return precioServicioDAO.buscarPorClienteServicio(e);
            }

            if (e.getAvisoCve() != null) {
                log.info("aviso");
                return precioServicioDAO.buscarPorClienteAviso(e);
            }
            
            log.info("cliente");
            return precioServicioDAO.buscarPorCliente(e);
        } catch (DAOException ex) {
            log.info("Error al consultar los precios de servicios por cliente", ex);
            throw new InventarioException();
        }
    }
    
    public List<PrecioServicio> buscarServiciosDisponibles(Cliente cliente, Aviso aviso) throws InventarioException {
        
        log.info("Inicia proceso para obtener los precios de servicio disponibles");
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");
        
        try {
            return precioServicioDAO.buscarDisponibles(cliente, aviso);
        } catch (DAOException ex) {
            log.error("Error al obtener los servicios relacionados con el aviso y cliente seleccionados");
            throw new InventarioException("Hubo un problema al obtener los los servicios relacionados con el aviso y cliente seleccionado");
        }
    }
    
    public void agregarOActualizarPrecioServicio(Cliente cliente, PrecioServicio precioServicio, List<PrecioServicio> precioServicios)
            throws InventarioException {

        log.info("Inicia proceso de actualización o agregado del precio de servicio del cliente", cliente.getNombre());
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(precioServicio, "El precio de servicio no puede ser vacío");

        if (precioServicios == null || precioServicios.isEmpty()) {
            precioServicios = new ArrayList<>();
        }

        final List<PrecioServicio> lista = precioServicios;
        int index = IntStream.range(0, lista.size())
                .filter(i -> lista.get(i).equals(precioServicio))
                .findFirst()
                .orElse(-1);

        if (index >= 0) {
            precioServicios.set(index, precioServicio);
            precioServicioDAO.actualizar(precioServicio);
            log.info("Se ha actualizado el precio del servicio de forma correcta");
        } else {
            precioServicio.setAvisoCve(new Aviso(1));
            precioServicio.setCliente(cliente);
            precioServicios.add(precioServicio);
            precioServicioDAO.guardar(precioServicio);
            log.info("Se ha agregado el precio del servicio de forma correcta");
        }
    }

    public void eliminarPrecioServicio(List<PrecioServicio> precioServicios, PrecioServicio precioServicio) throws InventarioException {
        log.info("Inicia el proceso de eliminacion del precio de servicio del cliente");
        if (precioServicios.isEmpty()){
            throw new InventarioException("Los precios de servicio no puede estar vacia");
        }
        FacesUtils.requireNonNull(precioServicio, "El precio de servicio no puede ser vacío");

        precioServicios.remove(precioServicio);

        precioServicio.setCliente(null);

        if (precioServicio.getId() != null) {
            precioServicioDAO.eliminar(precioServicio);
        }
        log.info("Finaliza el proceso de eliminacion del precio de servicio del cliente");
    }
    
}
