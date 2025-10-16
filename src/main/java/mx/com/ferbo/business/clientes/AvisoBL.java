package mx.com.ferbo.business.clientes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.AvisoDAO;
import mx.com.ferbo.dao.n.CategoriaDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.PrecioServicioDAO;
import mx.com.ferbo.dao.n.UdCobroDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.UdCobro;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class AvisoBL {

    private static Logger log = LogManager.getLogger(AvisoBL.class);

    @Inject
    private CategoriaDAO categoriaDAO;

    @Inject
    private UdCobroDAO udCobroDAO;

    @Inject
    private PlantaDAO plantaDAO;

    @Inject
    private AvisoDAO avisoDAO;

    @Inject
    private PrecioServicioDAO precioServicioDAO;

    public Aviso nueAviso(){
        Aviso aviso =  new Aviso();
        aviso.setPlantaCve(new Planta());
        aviso.setAvisoValSeg(BigDecimal.ZERO);
        aviso.setCategoriaCve(new Categoria());
        aviso.setPrecioServicioList(new ArrayList<>());
        return aviso;
    }

    public List<Categoria> obtenerCategorias() {
        List<Categoria> list = categoriaDAO.buscarTodos();

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

    public List<UdCobro> obtenerUnidadesCobro() {
        List<UdCobro> list = udCobroDAO.buscarTodos();

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

    public List<Planta> obtenerPlantas() {
        List<Planta> list = plantaDAO.buscarTodos();

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

    public void agregarAviso(Cliente cliente, Aviso aviso) throws InventarioException {

        requireNonNull(cliente, "El cliente no puede ser vacío");
        requireNonNull(aviso, "El aviso no puede ser vacío");

        List<Aviso> avisos = cliente.getAvisoList();

        if (avisos == null) {
            avisos = new ArrayList<>();
            cliente.setAvisoList(avisos);
        }

        avisos.add(aviso);
    }

    public void agregarServicioAviso(Aviso aviso, PrecioServicio ps) throws InventarioException {

        requireNonNull(aviso, "El aviso no puede ser vacío");
        requireNonNull(aviso, "El servicio no puede ser vacío");

        List<PrecioServicio> precioServicios = aviso.getPrecioServicioList();

        if (precioServicios == null) {
            precioServicios = new ArrayList<>();
            aviso.setPrecioServicioList(precioServicios);
        }

        precioServicios.add(ps);
    }

    public void eliminaAviso(Cliente cliente, Aviso aviso) throws InventarioException {
        requireNonNull(cliente, "El cliente no puede ser vacío");
        requireNonNull(aviso, "El aviso no puede ser vacío");

        List<Aviso> avisos = cliente.getAvisoList();
        List<PrecioServicio> precioServicios = aviso.getPrecioServicioList();

        if (precioServicios != null) {
            for (PrecioServicio precio : new ArrayList<>(precioServicios)) { // evitar ConcurrentModificationException
                eliminaServicioAviso(aviso, precio);
            }
        }

        avisos.remove(aviso);
        avisoDAO.eliminar(aviso);
    }

    public void eliminaServicioAviso(Aviso aviso, PrecioServicio ps) throws InventarioException {
        requireNonNull(aviso, "El aviso no puede ser vacío");
        requireNonNull(ps, "El servicio no puede ser vacío");

        List<PrecioServicio> precioServicios = aviso.getPrecioServicioList();
        requireNonNull(precioServicios, "El aviso no tiene servicios para eliminar");

        precioServicios.remove(ps);

        if (ps.getId() != null) {
            precioServicioDAO.eliminar(ps);
        }
    }

    private <T> T requireNonNull(T obj, String mensaje) throws InventarioException {
        if (obj == null) {
            throw new InventarioException(mensaje);
        }
        return obj;
    }
}
