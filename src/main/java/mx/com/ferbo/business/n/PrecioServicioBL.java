package mx.com.ferbo.business.n;

import java.util.ArrayList;
import java.util.List;
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
        precioServicio.setPrecio(null);
        precioServicio.setServicio(new Servicio());
        precioServicio.setUnidad(new UnidadDeManejo());
        precioServicio.setAvisoCve(null);
        return precioServicio;
    }

    public void agregarOActualizarPrecioServicio(Cliente cliente, PrecioServicio precioServicio)
            throws InventarioException {

        log.info("Inicia proceso de actualización o agregado del precio de servicio del cliente", cliente.getNombre());
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");

        final List<PrecioServicio> lista = cliente.getPrecioServicioList();
        int index = IntStream.range(0, lista.size())
                .filter(i -> lista.get(i).equals(precioServicio))
                .findFirst()
                .orElse(-1);

        if (index >= 0) {
            cliente.getPrecioServicioList().set(index, precioServicio);
            log.info("Se ha actualizado el precio del servicio de forma correcta al cliente {}", cliente.getNombre());
        } else {
            precioServicio.setAvisoCve(null);
            precioServicio.setCliente(cliente);
            cliente.getPrecioServicioList().add(precioServicio);
            log.info("Se ha agregado el precio del servicio de forma correcta al cliente {}", cliente.getNombre());
        }
    }

    public void eliminarPrecioServicio(Cliente cliente, PrecioServicio precioServicio) throws InventarioException {
        log.info("Inicia el proceso de eliminacion del precio de servicio del cliente");

        FacesUtils.requireNonNull(precioServicio, "El precio de servicio no puede ser vacío");

        cliente.getPrecioServicioList().remove(precioServicio);

        precioServicio.setCliente(null);
        
        log.info("Finaliza el proceso de eliminacion del precio de servicio del cliente {}", cliente.getNombre());
    }

    public List<PrecioServicio> obtenerPorClienteConOSinAviso(Cliente cliente, Aviso aviso) throws InventarioException {
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        try {
            List<PrecioServicio> lista;

            if (aviso != null) {
                lista = precioServicioDAO.buscarPorClienteConAviso(cliente.getCteCve(), aviso.getAvisoCve());
            } else {
                lista = precioServicioDAO.buscarPorClienteSinAviso(cliente.getCteCve());
            }

            if (lista == null) {
                return new ArrayList<>();
            }

            return lista;
        } catch (DAOException ex) {
            String tipo = (aviso != null) ? "con" : "sin";
            log.error("Error al obtener los precios servicios {} aviso del cliente {} de la base de datos", tipo,
                    cliente.getNombre());
            throw new InventarioException("Hubo un problema al obtener la lista de servicios con precios " + tipo
                    + " aviso del cliente " + cliente.getNombre());
        }
    }

    

    public List<PrecioServicio> obtenerDisponibles(List<PrecioServicio> preciosSinAviso,
            List<PrecioServicio> preciosConAviso) {

        log.info("Incia proceso para filtra los precios disponible pertenecientes al cliente pero que no tienen aviso");
        List<PrecioServicio> disponibles = new ArrayList<>();

        for (PrecioServicio sinAviso : preciosSinAviso) {

            boolean coincide = false;

            for (PrecioServicio conAviso : preciosConAviso) {

                boolean mismoServicio = sinAviso.getServicio().getServicioNombre()
                        .equalsIgnoreCase(conAviso.getServicio().getServicioNombre());

                boolean mismaUnidad = sinAviso.getUnidad().getUnidadDeManejoDs()
                        .equalsIgnoreCase(conAviso.getUnidad().getUnidadDeManejoDs());

                if (mismoServicio && mismaUnidad) {
                    log.info("Se encontro una conincidencia no, se aprega el servicio con su precio");
                    coincide = true;
                    break; // Ya no tiene caso seguir revisando
                }
            }

            if (!coincide) {
                disponibles.add(sinAviso);
            }
        }
        log.info("Finaliza proceso para filtra los precios disponible pertenecientes al cliente pero que no tienen aviso");
        return disponibles;
    }

}
