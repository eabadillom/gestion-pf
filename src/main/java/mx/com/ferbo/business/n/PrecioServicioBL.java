package mx.com.ferbo.business.n;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public void validarEstructura(PrecioServicio precioServicio) {

        log.info("Inicia proceso que verifica la estructura del precio de servicio");

        if (precioServicio.getId() == null) {
            if (precioServicio.getServicio().getServicioCve() == null) {
                throw new ValidationException("El servicio del cliente no puede ser vacío");
            }

            if (precioServicio.getUnidad().getUnidadDeManejoCve() == null) {
                throw new ValidationException("La unidad de manejo para el servicio no puede ser vacío");
            }
        }

        if (precioServicio.getPrecio() == null || precioServicio.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio del servicio tiene que ser mayor a cero");
        }

    }

    public boolean sonPreciosEquivalentes(PrecioServicio actual,
            PrecioServicio nuevo) {

        boolean resultado = true;

        resultado = sonPreciosEquivalentesSinConsiderarAviso(actual, nuevo);

        Integer idAvisoActual = (actual.getAvisoCve() == null)
                ? null
                : actual.getAvisoCve().getAvisoCve();

        Integer idAvisoNuevo = (nuevo.getAvisoCve() == null)
                ? null
                : nuevo.getAvisoCve().getAvisoCve();

        if (!Objects.equals(idAvisoActual, idAvisoNuevo)) {
            resultado = false;
            return resultado;
        }

        return resultado;
    }

    public boolean sonPreciosEquivalentesSinConsiderarAviso(PrecioServicio actual,
            PrecioServicio nuevo) {
        boolean resultado = true;

        Integer idServicioActual = actual.getServicio().getServicioCve();
        Integer idServicioNuevo = nuevo.getServicio().getServicioCve();

        if (idServicioActual.compareTo(idServicioNuevo) != 0) {
            resultado = false;
            return resultado;
        }

        Integer idUnidadManejoActual = actual.getUnidad().getUnidadDeManejoCve();
        Integer idUnidadManejoNuevo = nuevo.getUnidad().getUnidadDeManejoCve();

        if (idUnidadManejoActual.compareTo(idUnidadManejoNuevo) != 0) {
            resultado = false;
            return resultado;
        }

        return resultado;
    }

    public long contarCuantoHayEquivalentes(List<PrecioServicio> lista, PrecioServicio precioServicio) {

        log.info("Inicia proceso para contar cuantas veces puede existir el precio servicio");

        long total = 0;

        for (int i = 0; i < lista.size(); i++) {
            if (sonPreciosEquivalentes(lista.get(i), precioServicio)) {
                total++;
            }
        }
        return total;
    }

    public void validarUnicidad(List<PrecioServicio> lista, PrecioServicio precioServicio) {

        log.info("Inicia proceso para verificar unicidad del precio de servicio");

        long cantidad = contarCuantoHayEquivalentes(lista, precioServicio);

        if (cantidad >= 1) {
            throw new BusinessException(
                    "Existe más de un precio servicio con las mismas características");
        }

    }

    public int buscarIndicePrecioServicio(List<PrecioServicio> lista,
            PrecioServicio precioServicio) {

        log.info("Inicia proceso para obtener la posición del precio de servicio");

        int index = -1;

        for (int i = 0; i < lista.size(); i++) {
            boolean sonEquivalentes = false;
            sonEquivalentes = sonPreciosEquivalentes(lista.get(i), precioServicio);
            if (sonEquivalentes) {
                index = i;
            }
        }
        return index;
    }

    public PrecioServicio clonar(PrecioServicio original) {
        PrecioServicio clon = new PrecioServicio();

        copiarValores(original, clon);

        return clon;
    }

    public void copiarValores(PrecioServicio origen, PrecioServicio destino) {
        destino.setServicio(origen.getServicio());
        destino.setUnidad(origen.getUnidad());
        destino.setPrecio(origen.getPrecio());
        destino.setAvisoCve(origen.getAvisoCve());
    }

    public void agregarOActualizar(Cliente cliente, PrecioServicio precioServicio)
            throws ValidationException {

        log.info("Inicia proceso de actualización o agregado del precio de servicio del cliente", cliente.getNombre());

        int index = buscarIndicePrecioServicio(cliente.getPrecioServicioList(), precioServicio);

        if (index >= 0) {
            cliente.getPrecioServicioList().set(index, precioServicio);
            log.info("Se ha actualizado el precio del servicio de forma correcta al cliente {}", cliente.getNombre());
        } else {
            precioServicio.setCliente(cliente);
            cliente.getPrecioServicioList().add(precioServicio);
            log.info("Se ha agregado el precio del servicio de forma correcta al cliente {}", cliente.getNombre());
        }
    }

    private void quitarDeLista(List<PrecioServicio> lista, PrecioServicio precioServicio) {

        if (precioServicio.getId() != null) {
            lista.remove(precioServicio);
        } else {

            int index = buscarIndicePrecioServicio(lista, precioServicio);
            lista.remove(index);
        }
    }

    public void eliminar(Cliente cliente, PrecioServicio precioServicio)
            throws InventarioException {
        log.info("Inicia el proceso de eliminacion del precio de servicio del cliente");

        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser vacío");
        }

        if (precioServicio == null) {
            throw new BusinessException("El precio de servicio no puede ser vacío");
        }

        quitarDeLista(cliente.getPrecioServicioList(), precioServicio);

        precioServicio.setCliente(null);

        log.info("Finaliza el proceso de eliminacion del precio de servicio del cliente {}", cliente.getNombre());
    }

    public List<PrecioServicio> obtenerDisponiblesParaAviso(List<PrecioServicio> preciosSinAviso,
            List<PrecioServicio> preciosConAviso) {

        log.info("Incia proceso para filtra los precios disponible pertenecientes al cliente pero que no tienen aviso");
        List<PrecioServicio> disponibles = new ArrayList<>();

        for (PrecioServicio sinAviso : preciosSinAviso) {

            boolean existe = preciosConAviso.stream()
                    .anyMatch(ps -> sonPreciosEquivalentesSinConsiderarAviso(sinAviso, ps));

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

        log.info("Inicia proceso para filtrar la lista " + ((aviso == null) ? "sin aviso" : "con aviso"));

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

        PrecioServicio copia = clonar(origen);
        copia.setAvisoCve(aviso);

        return copia;
    }

    public void asociarAAviso(List<PrecioServicio> disponiblesParaAviso, List<PrecioServicio> porAgregarAAviso,
            List<PrecioServicio> conAviso, Cliente cliente,
            Aviso aviso) {

        log.info("Inicia proceo que asocia al aviso los precio servicios seleccionados");

        for (PrecioServicio origen : porAgregarAAviso) {
            PrecioServicio copiaConAviso = crearParaAviso(origen, aviso);
            agregarOActualizar(cliente, copiaConAviso);
            quitarDeLista(disponiblesParaAviso, origen);
            conAviso.add(copiaConAviso);
        }
        porAgregarAAviso.clear();
        porAgregarAAviso = new ArrayList<>();

    }

    public boolean verificarParaDisponiblesParaAviso(List<PrecioServicio> sinAviso, PrecioServicio precioServicio) {

        log.info("Inicia proceso para verificar si el precio servicio aun esta en la lista original");

        return sinAviso.stream()
                .anyMatch(ps -> sonPreciosEquivalentes(ps, precioServicio));
    }

    public void eliminarDeAviso(List<PrecioServicio> disponiblesParaAviso, List<PrecioServicio> conAviso,
            Cliente cliente, PrecioServicio precioServicio)
            throws InventarioException, ValidationException, BusinessException {

        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser vacío");
        }

        if (precioServicio == null) {
            throw new BusinessException("El precio de servicio no puede ser vacío");
        }

        eliminar(cliente, precioServicio);

        precioServicio.setAvisoCve(null);

        List<PrecioServicio> sinAviso = filtrarPorAvisoOSinAviso(cliente, null);

        if (verificarParaDisponiblesParaAviso(sinAviso, precioServicio)) {
            disponiblesParaAviso.add(precioServicio);
        }

        quitarDeLista(conAviso, precioServicio);

    }

}
