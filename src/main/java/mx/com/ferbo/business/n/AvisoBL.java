package mx.com.ferbo.business.n;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.AvisoDAO;
import mx.com.ferbo.dao.n.PrecioServicioDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class AvisoBL {

    private static Logger log = LogManager.getLogger(AvisoBL.class);

    @Inject
    private AvisoDAO avisoDAO;

    @Inject
    private PrecioServicioDAO precioServicioDAO;

    public Aviso nuevoAviso() {
        log.info("Inicia el proceso para crear un nuevo aviso");
        Aviso aviso = new Aviso();
        aviso.setPlantaCve(new Planta());
        aviso.setAvisoValSeg(BigDecimal.ZERO);
        aviso.setAvisoFecha(new Date());
        aviso.setCategoriaCve(new Categoria());
        aviso.setPrecioServicioList(new ArrayList<>());
        log.info("Finaliza el proceso para crear un nuevo aviso");
        return aviso;
    }

    public List<Aviso> obtenerAvisosPorCliente(Cliente cliente) throws InventarioException {
        try {
            log.info("Inicia proceso para obtener los avisos del cliente: " + cliente.getNombre());
            return avisoDAO.buscarPorCliente(cliente.getCteCve());
        } catch (DAOException ex) {
            throw new InventarioException("Error al obtener los avisos del cliente: " + cliente.getNombre());
        }
    }

    public void agregarOActualizarAviso(Cliente cliente, Aviso aviso, List<Aviso> avisos) throws InventarioException {

        log.info("Inicia proceso para agregar o actualizar el aviso del cliente: " + cliente.getNombre());
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");

        if (avisos == null || avisos.isEmpty()) {
            avisos = new ArrayList<>();
        }

        final List<Aviso> lista = avisos;

        int index = IntStream.range(0, lista.size()).filter(i -> lista.get(i).equals(aviso)).findFirst().orElse(-1);

        if (index >= 0) {
            avisos.set(index, aviso);
            avisoDAO.actualizar(aviso);
            log.info("El aviso del cliente " + cliente.getNombre() + " se actualizo correctamente.");

        } else {
            aviso.setCteCve(cliente);
            avisos.add(aviso);
            avisoDAO.guardar(aviso);
            log.info("El aviso del cliente " + cliente.getNombre() + " se agrego correctamente.");
        }
        log.info("Finaliza proceso para agregar o actualizar el aviso del cliente: " + cliente.getNombre());
    }

    public void agregarServicioAviso(
            Aviso aviso,
            Cliente cliente,
            List<PrecioServicio> serviciosDisponibles,
            List<PrecioServicio> serviciosPorAgregar,
            List<PrecioServicio> serviciosAviso) throws InventarioException {

        log.info("Inicia proceso para agregar el servicio son su precio al aviso");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");

        if (serviciosPorAgregar == null || serviciosPorAgregar.isEmpty()) {
            return;
        }

        for (PrecioServicio ps : serviciosPorAgregar) {

            PrecioServicio nuevo = new PrecioServicio();
            nuevo.setAvisoCve(aviso);
            nuevo.setCliente(cliente);
            nuevo.setPrecio(ps.getPrecio());
            nuevo.setServicio(ps.getServicio());
            nuevo.setUnidad(ps.getUnidad());

            boolean existe = serviciosAviso.stream()
                    .anyMatch(p -> Objects.equals(p.getAvisoCve(), aviso) &&
                            Objects.equals(p.getServicio(), ps.getServicio()) &&
                            Objects.equals(p.getUnidad(), ps.getUnidad()));

            if (existe) {
                continue;
            }

            precioServicioDAO.guardar(nuevo);

            serviciosAviso.add(nuevo);
            serviciosDisponibles.remove(ps);
        }

        serviciosPorAgregar.clear();
        log.info("Finaliza proceso para agregar el servicio son su precio al aviso");
    }

    public void eliminaAviso(Cliente cliente, Aviso aviso, List<Aviso> avisos)
            throws InventarioException, DAOException {

        log.info("Inicia porceso para eliminar el aviso del cliente: " + cliente.getNombre());
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");

        Integer constancias = avisoDAO.conteoConstanciaDeDeposito(aviso);
        Integer servicios = avisoDAO.conteoPrecioServicio(aviso);

        if (constancias > 0) {
            throw new InventarioException(
                    "No se puede eliminar el aviso por tener " + constancias + " constancias de deposito");
        }

        if (servicios > 0) {
            throw new InventarioException(
                    "No se puede eliminar el aviso por tener " + servicios + " precios de servicios");
        }

        avisos.remove(aviso);

        aviso.setCteCve(null);

        if (aviso.getAvisoCve() != null) {
            avisoDAO.eliminar(aviso);
        }
        log.info("Finaliza porceso para eliminar el aviso del cliente: " + cliente.getNombre());
    }

    public void eliminarServicioAviso(
            List<PrecioServicio> precioServicios, PrecioServicio precioServicio, List<PrecioServicio> serviciosDisponibles) throws InventarioException {

        if (precioServicios == null || precioServicios.isEmpty()) {
            throw new InventarioException("La lista de precios de servicio esta vacía");
        }
        
        log.info("Inicia proceso para eliminar el servicio del aviso");
        
        FacesUtils.requireNonNull(precioServicio, "El servicio a eliminar no puede ser vacío");

        precioServicios.remove(precioServicio);
        serviciosDisponibles.add(precioServicio);

        precioServicio.setAvisoCve(null);

        if (precioServicio.getId() != null) {
            precioServicioDAO.eliminar(precioServicio);
        }
        log.info("Finaliza proceso para eliminar el servicio del aviso");

    }
}
