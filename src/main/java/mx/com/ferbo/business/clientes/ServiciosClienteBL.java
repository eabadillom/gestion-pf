package mx.com.ferbo.business.clientes;

import java.util.ArrayList;
import java.util.List;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;

/**
 *
 * @author julio
 */
public class ServiciosClienteBL {

    public static List<PrecioServicio> obtenerPorCliente(Integer clienteCve, PrecioServicioDAO dao) {
        if (clienteCve == null || dao == null) return new ArrayList<>();
        return dao.buscarPorCliente(clienteCve, false);
    }

    public static PrecioServicio crearNuevoServicio(Cliente cliente) {
        PrecioServicio nuevo = new PrecioServicio();
        nuevo.setCliente(cliente);
        nuevo.setServicio(new Servicio());
        nuevo.setUnidad(new UnidadDeManejo());
        return nuevo;
    }

    public static boolean guardar(PrecioServicio ps, PrecioServicioDAO dao) {
        if (ps == null || dao == null) return false;

        ps.setAvisoCve(new Aviso(1));
        return dao.guardar(ps) == null;
    }

    public static boolean actualizar(PrecioServicio ps, PrecioServicioDAO dao) {
        if (ps == null || dao == null) return false;

        return dao.actualizar(ps) == null;
    }

    public static boolean eliminar(PrecioServicio ps, PrecioServicioDAO dao) {
        if (ps == null || dao == null) return false;

        return dao.eliminar(ps) == null;
    }
}
