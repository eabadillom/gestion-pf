package mx.com.ferbo.business.clientes;

import java.util.List;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.UnidadManejoDAO;
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
public class ServiciosClienteBL {

    private UnidadManejoDAO unidadManejoDAO;
    private ServicioDAO servicioDAO;
    private PrecioServicioDAO precioServicioDAO;

    public ServiciosClienteBL() {
        this.unidadManejoDAO = new UnidadManejoDAO();
        this.servicioDAO = new ServicioDAO();
        this.precioServicioDAO = new PrecioServicioDAO();
    }

    public PrecioServicio crearNuevoPrecioServicio(Cliente cliente) throws Exception {
        if(cliente == null){
            throw new Exception("Error inesperado"); 
        }
        PrecioServicio ps = new PrecioServicio();
        ps.setCliente(cliente);
        ps.setServicio(new Servicio());
        ps.setUnidad(new UnidadDeManejo());
        return ps;
    }

    public void guardarOActualizar(PrecioServicio ps) throws InventarioException {
        ps.setAvisoCve(new Aviso(1)); // Esto es parte del negocio

        if (ps.getId() == null) {
            if (precioServicioDAO.guardar(ps) != null) {
                throw new InventarioException("No se guardo el servicio");
            }
        } else {
            if (precioServicioDAO.actualizar(ps) != null) {
                throw new InventarioException("No se actualizo el servicio");
            }
        }
    }

    public void eliminarPrecioServicio(PrecioServicio ps) throws InventarioException {
        if (precioServicioDAO.eliminar(ps) != null) {
            throw new InventarioException("No se elimino el servicio");
        }
    }

    public List<UnidadDeManejo> obtenerUnidadesDeManejo() {
        return unidadManejoDAO.buscarTodos();
    }

    public List<Servicio> obtenerServicios() {
        return servicioDAO.buscarTodos();
    }

    public List<PrecioServicio> buscarPreciosPorCliente(Cliente cliente) throws InventarioException {
        if (cliente == null) {
            throw new InventarioException("El cliente esta vacío.");
        }
        List<PrecioServicio> precios = precioServicioDAO.buscarPorCliente(cliente.getCteCve(), false);
        if (precios.isEmpty()){
            throw new InventarioException("El cliente no tiene precios definidos.");
        }
        return precios ;
    }

}
