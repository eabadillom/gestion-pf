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
        PrecioServicio precioServicio = new PrecioServicio();
        precioServicio.setCliente(cliente);
        precioServicio.setPrecio(BigDecimal.ZERO);
        precioServicio.setServicio(new Servicio());
        precioServicio.setUnidad(new UnidadDeManejo());
        precioServicio.setAvisoCve(new Aviso());
        return precioServicio;
    }

    public List<PrecioServicio> obtenerPrecioServiciosPorCliente(Cliente cliente, Boolean isFullInfo) throws InventarioException {
        FacesUtils.requireNonNull(cliente, "El cliente no puede estar vacío");

        try {
            return precioServicioDAO.buscarPorCliente(cliente.getCteCve(), isFullInfo);
        } catch (DAOException ex) {
            log.info(ex.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PrecioServicio> buscarPorCriterios(PrecioServicio e) throws InventarioException {
        try {
            if (e.getCliente().getCteCve() == null)
                return null;

            if (e.getServicio() != null) {
                return precioServicioDAO.buscarPorClienteServicio(e);
            }

            if (e.getAvisoCve() != null) {
                return precioServicioDAO.buscarPorClienteAviso(e);
            }

            return precioServicioDAO.buscarPorCliente(e);
        } catch (DAOException ex) {
            log.info("Error al consultar los precios de servicios por cliente", ex);
            throw new InventarioException();
        }
    }
    
    public List<PrecioServicio> buscarServiciosDisponibles(Cliente cliente, Aviso aviso) throws InventarioException {
        
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");
        
        try {
            return precioServicioDAO.buscarDisponibles(cliente, aviso);
        } catch (DAOException ex) {
            log.error("Error al obtener los servicios relacionados con el aviso y cliente seleccionados");
            throw new InventarioException("Hubo un problema al obtener los los servicios relacionados con el aviso y cliente seleccionado");
        }
    }
    
    public void agregarOActualizarPrecioServicio(Cliente cliente, PrecioServicio precioServicio)
            throws InventarioException {

        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(precioServicio, "El precio de servicio no puede ser vacío");

        List<PrecioServicio> preciosServicios = cliente.getPrecioServicioList();
        if (preciosServicios == null || preciosServicios.isEmpty()) {
            preciosServicios = new ArrayList<>();
            cliente.setPrecioServicioList(preciosServicios);
        }

        final List<PrecioServicio> lista = preciosServicios;
        int index = IntStream.range(0, lista.size())
                .filter(i -> lista.get(i).equals(precioServicio))
                .findFirst()
                .orElse(-1);

        if (index >= 0) {
            preciosServicios.set(index, precioServicio);
        } else {
            precioServicio.setAvisoCve(new Aviso(1));
            precioServicio.setCliente(cliente);
            preciosServicios.add(precioServicio);
        }
    }

    public void eliminarPrecioServicio(Cliente cliente, PrecioServicio precioServicio) throws InventarioException {
        FacesUtils.requireNonNull(cliente, "El contacto del cliente no puede estar vacío.");
        FacesUtils.requireNonNull(precioServicio, "El precio de servicio no puede ser vacío");

        cliente.getPrecioServicioList().remove(precioServicio);

        if (precioServicio.getId() != null) {
            precioServicioDAO.eliminar(precioServicio);
        }
    }
    
    public List<PrecioServicio> filtrarPrecioServicios(Aviso avisoSelected, Cliente clienteSelected) {
        List<PrecioServicio> lista = avisoSelected.getPrecioServicioList()
                .stream()
                .filter(p -> p.getAvisoCve() != null && p.getAvisoCve().equals(avisoSelected))
                .filter(p -> p.getCliente() != null && p.getCliente().getCteCve().equals(clienteSelected.getCteCve()))
                .collect(Collectors.toList());
        
        return lista;
    }
    
}
