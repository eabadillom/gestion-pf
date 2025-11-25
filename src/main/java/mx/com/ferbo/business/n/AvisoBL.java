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
        Aviso aviso = new Aviso();
        aviso.setPlantaCve(new Planta());
        aviso.setAvisoValSeg(BigDecimal.ZERO);
        aviso.setAvisoFecha(new Date());
        aviso.setCategoriaCve(new Categoria());
        aviso.setPrecioServicioList(new ArrayList<>());
        return aviso;
    }

    public PrecioServicio nuevoServicioAviso(Aviso aviso) {
        PrecioServicio pServicio = new PrecioServicio();
        pServicio.setAvisoCve(aviso);
        pServicio.setServicio(new Servicio());
        pServicio.setUnidad(new UnidadDeManejo());
        pServicio.setPrecio(BigDecimal.ZERO);
        return pServicio;
    }

    public List<Aviso> obtnerAvisoPorCliente(Cliente cliente) throws InventarioException {
        try {
            return avisoDAO.buscarPorCliente(cliente.getCteCve());
        } catch (DAOException ex) {
            throw new InventarioException("Error al obtener los avisos del cliente: " + cliente.getNombre());
        }
    }

    public void agregarAviso(Cliente cliente, Aviso aviso, List<Aviso> avisos) throws InventarioException {

        log.info("Se verifica información de cliente y aviso");
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");

        if (avisos == null || avisos.isEmpty()) {
            avisos = new ArrayList<>();
        }

        final List<Aviso> lista = avisos;

        log.info("Se busca el aviso dentro de la lista de avisos del cliente");
        int index = IntStream.range(0, lista.size()).filter(i -> lista.get(i).equals(aviso)).findFirst().orElse(-1);

        if (index >= 0) {
            log.info("Se actualizo temporalmente el aviso del cliente");
            avisos.set(index, aviso);
            avisoDAO.actualizar(aviso);

        } else {
            log.info("Se agrego temporalmente el nuevo aviso a la lista de avisos del cliente");
            aviso.setCteCve(cliente);
            avisos.add(aviso);
            avisoDAO.guardar(aviso);
        }
    }

    public void agregarServicioAviso(
            Aviso aviso,
            Cliente cliente,
            List<PrecioServicio> serviciosDisponibles,
            List<PrecioServicio> serviciosPorAgregar,
            List<PrecioServicio> serviciosAviso) throws InventarioException {

        log.info("Se verifica información de aviso y precioservicio");
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
    }

    public void eliminaAviso(Cliente cliente, Aviso aviso, List<Aviso> avisos)
            throws InventarioException, DAOException {

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
    }

    public void eliminarServicioAviso(
            List<PrecioServicio> precioServicios, PrecioServicio precioServicio) throws InventarioException {

        if (precioServicios == null || precioServicios.isEmpty()) {
            throw new InventarioException("La lista de precios de servicio esta vacía");
        }
        FacesUtils.requireNonNull(precioServicio, "El servicio a eliminar no puede ser vacío");

        precioServicios.remove(precioServicio);

        precioServicio.setAvisoCve(null);

        if (precioServicio.getId() != null) {
            precioServicioDAO.eliminar(precioServicio);
        }

    }
}
