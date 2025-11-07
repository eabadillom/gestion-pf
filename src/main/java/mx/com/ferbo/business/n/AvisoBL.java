package mx.com.ferbo.business.n;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public void agregarAviso(Cliente cliente, Aviso aviso) throws InventarioException {

        log.info("Se verifica información de cliente y aviso");
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");

        if (cliente.getAvisoList() == null) {
            cliente.setAvisoList(new ArrayList<>());
        }

        final List<Aviso> lista = cliente.getAvisoList();

        log.info("Se busca el aviso dentro de la lista de avisos del cliente");
        int index = IntStream.range(0, lista.size()).filter(i -> lista.get(i).equals(aviso)).findFirst().orElse(-1);

        if (index >= 0) {
            log.info("Se actualizo temporalmente el aviso del cliente");
            cliente.getAvisoList().set(index, aviso);
        } else {
            log.info("Se agrego temporalmente el nuevo aviso a la lista de avisos del cliente");
            cliente.getAvisoList().add(aviso);
        }
    }

    public void agregarServicioAviso(Aviso aviso, PrecioServicio precioServicio) throws InventarioException {

        log.info("Se verifica información de aviso y precioservicio");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");
        FacesUtils.requireNonNull(aviso, "El servicio no puede ser vacío");

        List<PrecioServicio> preciosServicios = aviso.getPrecioServicioList();

        if (preciosServicios == null) {
            preciosServicios = new ArrayList<>();
            aviso.setPrecioServicioList(preciosServicios);
        }

        final List<PrecioServicio> lista = preciosServicios;

        log.info("Se busca el aviso dentro de la lista de avisos del cliente");
        int index = IntStream.range(0, lista.size()).filter(i -> lista.get(i).equals(precioServicio)).findFirst()
                .orElse(-1);

        if (index >= 0) {
            preciosServicios.set(index, precioServicio);
        } else {
            preciosServicios.add(precioServicio);
        }
    }

    public void eliminaAviso(Cliente cliente, Aviso aviso) throws InventarioException {
        FacesUtils.requireNonNull(cliente, "El cliente no puede ser vacío");
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");

        List<Aviso> avisos = cliente.getAvisoList();
        List<PrecioServicio> preciosServicios = aviso.getPrecioServicioList();

        if (preciosServicios != null) {
            for (PrecioServicio precio : preciosServicios) {
                eliminaServicioAviso(aviso, precio);
            }
        }

        avisos.remove(aviso);

        if (aviso.getAvisoCve() != null) {
            avisoDAO.eliminar(aviso);
        }
    }

    public void eliminaServicioAviso(Aviso aviso, PrecioServicio ps) throws InventarioException {
        FacesUtils.requireNonNull(aviso, "El aviso no puede ser vacío");
        FacesUtils.requireNonNull(ps, "El servicio no puede ser vacío");

        List<PrecioServicio> precioServicios = aviso.getPrecioServicioList();
        FacesUtils.requireNonNull(precioServicios, "El aviso no tiene servicios para eliminar");

        precioServicios.remove(ps);

        if (ps.getId() != null) {
            precioServicioDAO.eliminar(ps);
        }
    }
}
