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

import mx.com.ferbo.dao.n.PrecioServicioDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public List<PrecioServicio> obtenerPrecioServiciosPorCliente(Cliente cliente) throws InventarioException {
        requireNonNull(cliente, "El cliente no puede estar vacío");

        Integer id = cliente.getCteCve();

        try {
            return precioServicioDAO.buscarServiciosPorCliente(cliente);
        } catch (DAOException e) {
            log.info(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PrecioServicio> buscarPorCriterios(PrecioServicio e) throws DAOException {
		if(e.getCliente().getCteCve() == null)
			return null;
		if(e.getServicio()!=null) {
			return precioServicioDAO.buscarPorClienteServicio(e);
		}
		if(e.getAvisoCve()!=null) {
			return precioServicioDAO.buscarPorClienteAviso(e);
		}
		return precioServicioDAO.buscarPorCliente(e);
	}
    

    public void agregarOActualizarPrecioServicio(Cliente cliente, PrecioServicio precioServicio)
            throws InventarioException {

        requireNonNull(cliente, "El cliente no puede ser vacío");
        requireNonNull(precioServicio, "El precio de servicio no puede ser vacío");

        List<PrecioServicio> preciosServicios = cliente.getPrecioServicioList();
        if (preciosServicios == null) {
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
            precioServicio.setCliente(cliente);
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
    
    public List<PrecioServicio> filtrarPrecioServicios(Aviso avisoSelected, Cliente clienteSelected){
        List<PrecioServicio> lista = avisoSelected.getPrecioServicioList()
            .stream()
            .filter(p -> p.getAvisoCve() != null && p.getAvisoCve().equals(avisoSelected))
            .filter(p -> p.getCliente() != null && p.getCliente().getCteCve().equals(clienteSelected.getCteCve()))
            .collect(Collectors.toList());
        
        return lista;
    }

    private <T> T requireNonNull(T obj, String mensaje) throws InventarioException {
        if (obj == null) {
            throw new InventarioException(mensaje);
        }
        return obj;
    }

}
