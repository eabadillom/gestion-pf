package mx.com.ferbo.business.n;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.BusinessException;
import com.ferbo.tools.exception.ValidationException;

import mx.com.ferbo.dao.n.PrecioServicioDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
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

    public PrecioServicio nuevoPrecioServicio() {
        log.info("Incia proceso para crear un nuevo precio de servicio");
        PrecioServicio precioServicio = new PrecioServicio();
        precioServicio.setPrecio(null);
        precioServicio.setServicio(new Servicio());
        precioServicio.setUnidad(new UnidadDeManejo());
        precioServicio.setAvisoCve(null);
        return precioServicio;
    }

    private void validarPrevioServicio(PrecioServicio precioServicio) {

        if (precioServicio.getId() == null) {
            if (precioServicio.getServicio().getServicioCve() == null) {
                throw new ValidationException("El servicio del cliente no puede ser vacío");
            }

            if (precioServicio.getUnidad().getUnidadDeManejoCve() == null) {
                throw new ValidationException("La unidad de manejo para el servicio no puede ser vacío");
            }
        }

        if (precioServicio.getPrecio() == null || precioServicio.getPrecio().compareTo(BigDecimal.ZERO) <= 0){
            throw new BusinessException("El precio del servicio tiene que ser mayor a cero");
        }

    }

     public boolean sonPreciosEquivalentes(PrecioServicio actual,
            PrecioServicio nuevo,
            boolean compararAviso) {

        if (actual == null || nuevo == null) {
            return false;
        }

        boolean mismoServicio = Objects.equals(actual.getServicio(), nuevo.getServicio());
        boolean mismaUnidad = Objects.equals(actual.getUnidad(), nuevo.getUnidad());

        if (!mismoServicio || !mismaUnidad) {
            return false;
        }

        if (compararAviso) {
            return Objects.equals(actual.getAvisoCve(), nuevo.getAvisoCve());
        }

        return true;
    }

    public void agregarOActualizar(Cliente cliente, PrecioServicio precioServicio, String tipoOperacion, boolean compararAviso)
            throws ValidationException {

        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser vacío");
        }

        log.info("Inicia proceso de actualización o agregado del precio de servicio del cliente", cliente.getNombre());
        
        validarPrevioServicio(precioServicio);


        final List<PrecioServicio> lista = cliente.getPrecioServicioList();

        int index = IntStream.range(0, lista.size())
                .filter(i -> sonPreciosEquivalentes(lista.get(i), precioServicio, compararAviso))
                .findFirst()
                .orElse(-1);

        if (index >= 0 && "agregado".equalsIgnoreCase(tipoOperacion)) {
            throw new BusinessException("El precio servicio que se intenta agregar ya existe, favor de verificar");
        }

        if (index >= 0) {
            cliente.getPrecioServicioList().set(index, precioServicio);
            log.info("Se ha actualizado el precio del servicio de forma correcta al cliente {}", cliente.getNombre());
        } else {
            precioServicio.setCliente(cliente);
            cliente.getPrecioServicioList().add(precioServicio);
            log.info("Se ha agregado el precio del servicio de forma correcta al cliente {}", cliente.getNombre());
        }
    }

    public boolean verificarParaDisponiblesParaAviso(List<PrecioServicio> sinAviso, PrecioServicio precioServicio) {
        boolean resultado = false; 
        for (PrecioServicio ps : sinAviso) {
            resultado = sonPreciosEquivalentes(ps, precioServicio, false);
            if (resultado) {
                break;
            }
        }
        return resultado;
    }

    public void eliminar(Cliente cliente, PrecioServicio precioServicio) throws InventarioException {
        log.info("Inicia el proceso de eliminacion del precio de servicio del cliente");

        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser vacío");
        }

        if (precioServicio == null) {
            throw new BusinessException("El precio de servicio no puede ser vacío");
        }

        cliente.getPrecioServicioList().remove(precioServicio);

        precioServicio.setCliente(null);

        log.info("Finaliza el proceso de eliminacion del precio de servicio del cliente {}", cliente.getNombre());
    }

    public List<PrecioServicio> obtenerDisponiblesParaAviso(List<PrecioServicio> preciosSinAviso,
            List<PrecioServicio> preciosConAviso) {

        log.info("Incia proceso para filtra los precios disponible pertenecientes al cliente pero que no tienen aviso");
        List<PrecioServicio> disponibles = new ArrayList<>();

        for (PrecioServicio sinAviso : preciosSinAviso) {

            boolean existe = preciosConAviso.stream()
                    .anyMatch(ps -> sonPreciosEquivalentes(sinAviso, ps, false));

            if (!existe) {
                disponibles.add(sinAviso);
            }
        }
        log.info(
                "Finaliza proceso para filtra los precios disponible pertenecientes al cliente pero que no tienen aviso");
        return disponibles;
    }

    public List<PrecioServicio> buscarPorCliente(Integer cteCve, boolean isFullInfo) throws InventarioException {
        if (cteCve == null)
            throw new InventarioException("El cliente no puede ser vacío");

        return precioServicioDAO.buscarPorCliente(cteCve, isFullInfo);
    }

    public List<PrecioServicio> filtrarPorAvisoOSinAviso(Cliente cliente, Aviso aviso) {

        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser vacío");
        }

        if (cliente.getPrecioServicioList() == null || cliente.getPrecioServicioList().isEmpty()) {
            return new ArrayList<>();
        }

        return cliente.getPrecioServicioList()
                .stream()
                .filter(ps -> aviso == null
                        ? ps.getAvisoCve() == null
                        : Objects.equals(ps.getAvisoCve(), aviso))
                .collect(Collectors.toList());
    }

    private PrecioServicio crearParaAviso(
            PrecioServicio origen,
            Aviso aviso) {

        PrecioServicio copia = new PrecioServicio();
        copia.setUnidad(origen.getUnidad());
        copia.setServicio(origen.getServicio());
        copia.setPrecio(origen.getPrecio());
        copia.setAvisoCve(aviso);

        return copia;
    }

    public void asociarAAviso(List<PrecioServicio> disponiblesParaAviso, List<PrecioServicio> porAgregarAAviso, List<PrecioServicio> conAviso, Cliente cliente,
            Aviso aviso) {

        for (PrecioServicio origen : porAgregarAAviso) {
            PrecioServicio copiaConAviso = crearParaAviso(origen, aviso);
            agregarOActualizar(cliente, copiaConAviso, "agregado",true);
            disponiblesParaAviso.remove(origen);
            conAviso.add(copiaConAviso);
        }
        porAgregarAAviso.clear();
        porAgregarAAviso = new ArrayList<>();

    }

    public void eliminarDeAviso(List<PrecioServicio> disponiblesParaAviso, List<PrecioServicio> conAviso, Cliente cliente, PrecioServicio precioServicio)
            throws InventarioException, ValidationException, BusinessException{

        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser vacío");
        }

        if (precioServicio == null) {
            throw new BusinessException("El precio de servicio no puede ser vacío");
        }

        precioServicio.setAvisoCve(null);

        eliminar(cliente, precioServicio);

        List<PrecioServicio> sinAviso = filtrarPorAvisoOSinAviso(cliente, null);

        if (verificarParaDisponiblesParaAviso(sinAviso, precioServicio)) {
            disponiblesParaAviso.add(precioServicio);
        }

        conAviso.remove(precioServicio);

    }
}
