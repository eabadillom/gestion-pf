package mx.com.ferbo.business.clientes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.dao.n.PrecioServicioDAO;
import mx.com.ferbo.dao.n.ServicioDAO;
import mx.com.ferbo.dao.n.UnidadManejoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author julio
 */
@Named
@RequestScoped
public class ServiciosBL {

    private static final Logger log = LogManager.getLogger(ServiciosBL.class);

    @Inject
    private UnidadManejoDAO unidadManejoDAO;

    @Inject
    private ServicioDAO servicioDAO;

    @Inject
    private PrecioServicioDAO precioServicioDAO;

    public List<PrecioServicio> obtenerPrecioServiciosPorCliente(Cliente cliente) throws InventarioException {
        requireNonNull(cliente, "El cliente no puede estar vacío");

        Integer id = cliente.getCteCve();

        try {
            return precioServicioDAO.buscarPorCliente(id);
        } catch (InventarioException ex) {
            log.info(ex.getMessage());
            return Collections.emptyList();
        } 
    }

    public List<UnidadDeManejo> obtenerUnidadesMenjo() {

        List<UnidadDeManejo> lista = unidadManejoDAO.buscarTodos();
        
        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }

    public List<Servicio> obtenerServicios() {
        List<Servicio> lista = servicioDAO.buscarTodos();

         if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }

    public void agregarOActulizarPrecioServicio(Cliente cliente, PrecioServicio precioServicio)
            throws InventarioException {

        requireNonNull(cliente, "El cliente no puede ser vacío");
        requireNonNull(precioServicio, "El precio de servicio no puede ser vacío");

        List<PrecioServicio> preciosServicios = cliente.getPrecioServicioList();

        if (preciosServicios == null) {
            preciosServicios = new ArrayList<>();
            cliente.setPrecioServicioList(preciosServicios);
        }

        Optional<PrecioServicio> existente = preciosServicios.stream()
                .filter(ps -> Objects.equals(ps.getId(), precioServicio.getId())).findFirst();

        if (existente.isPresent()) {
            int index = preciosServicios.indexOf(existente.get());
            preciosServicios.set(index, precioServicio);
        } else {
            preciosServicios.add(precioServicio);
        }

    }

    public void eliminarPrecioServicio(Cliente cliente, PrecioServicio precioServicio) throws InventarioException {
        requireNonNull(cliente, "El contacto del cliente no puede estar vacío.");
        requireNonNull(precioServicio, "El precio de servicio no puede ser vacío");

        cliente.getPrecioServicioList().remove(precioServicio);

        if (precioServicio.getId() != null) {
            precioServicioDAO.eliminar(precioServicio);
        }
    }

    private <T> T requireNonNull(T obj, String mensaje) throws InventarioException {
        if (obj == null) {
            throw new InventarioException(mensaje);
        }
        return obj;
    }

}