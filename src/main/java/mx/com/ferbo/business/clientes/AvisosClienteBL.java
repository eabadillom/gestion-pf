
package mx.com.ferbo.business.clientes;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CategoriaDAO;
import mx.com.ferbo.dao.CuotaMinimaDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.CuotaMinima;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.InventarioException;

/**
 *
 * @author julio
 */
public class AvisosClienteBL {

    private static final AvisoDAO avisoDAO = new AvisoDAO();
    private static final PrecioServicioDAO precioServicioDAO = new PrecioServicioDAO();
    private static final CuotaMinimaDAO cuotaMinimaDAO = new CuotaMinimaDAO();
    private static final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private static final PlantaDAO plantaDAO = new PlantaDAO();

    public static List<Aviso> obtenerAvisosPorCliente(Integer cteCve) {
        if (cteCve == null) {
            return Collections.emptyList();
        }
        return avisoDAO.buscarPorCliente(cteCve);
    }

    public static Aviso cargarAvisoCompleto(Integer avisoCve) {
        return avisoDAO.buscarPorId(avisoCve, true);
    }

    public static List<PrecioServicio> buscarServiciosDisponibles(Integer cteCve, Integer avisoCve) {
        return precioServicioDAO.buscarDisponibles(cteCve, avisoCve);
    }

    public static List<PrecioServicio> buscarServiciosPorCriterio(PrecioServicio criterio) {
        return precioServicioDAO.buscarPorCriterios(criterio);
    }

    public static CuotaMinima establecerCuotaMinima(CuotaMinima cuota, Cliente cliente, boolean tieneCuota) {
        cuota.setCliente(cliente);

        if (tieneCuota) {
            List<CuotaMinima> existentes = cuotaMinimaDAO.buscarPorCriterios(cuota);
            if (!existentes.isEmpty()) {
                cuotaMinimaDAO.actualizar(cuota);
            }
            cuotaMinimaDAO.guardar(cuota);
            return cuota;
        } else {
            cuotaMinimaDAO.eliminar(cuota);
            return new CuotaMinima();
        }
    }

    public static Optional<CuotaMinima> buscarCuotaMinima(CuotaMinima filtro, Cliente cliente) {
        filtro.setCliente(cliente);
        List<CuotaMinima> resultados = cuotaMinimaDAO.buscarPorCriterios(filtro);

        if (!resultados.isEmpty()) {
            return Optional.of(resultados.get(0));
        }

        return Optional.empty();
    }

    public static CuotaMinima cambiaCuotaMinima(
            CuotaMinima cuotaActual,
            Cliente cliente,
            boolean tieneCuota,
            boolean yaTieneCuotaEnBD
    ) {
        if (!yaTieneCuotaEnBD) {
            return establecerCuotaMinima(cuotaActual, cliente, tieneCuota);
        }
        return cuotaActual;
    }

    public static Aviso crearNuevoAviso(Cliente cliente, Integer categoriaCve) {
        Categoria categoria = categoriaDAO.buscarPorId(categoriaCve);

        Aviso aviso = new Aviso();
        aviso.setCteCve(cliente);
        aviso.setCategoriaCve(categoria);
        return aviso;
    }

    public static void guardarAviso(Aviso aviso, Integer categoriaCve, Integer plantaCve) {

        aviso.setAvisoFecha(new Date());

        Categoria categoria = categoriaDAO.buscarPorId(categoriaCve);
        aviso.setCategoriaCve(categoria);

        Planta planta = plantaDAO.buscarPorId(plantaCve);
        aviso.setPlantaCve(planta);

        avisoDAO.guardar(aviso);
    }

    public static void actualizarAviso(Aviso aviso, List<PrecioServicio> servicios, Integer plantaCve) {

        aviso.setPrecioServicioList(servicios);
        aviso.setAvisoFecha(new Date());

        Planta planta = plantaDAO.buscarPorId(plantaCve);
        aviso.setPlantaCve(planta);

        avisoDAO.actualizar(aviso);
    }

    public static String eliminarAviso(Aviso aviso) {

        int countCDD = avisoDAO.conteoConstanciaDeDeposito(aviso);
        int countPS = avisoDAO.conteoPrecioServicio(aviso);

        if (countCDD == 0 && countPS == 0) {
            avisoDAO.eliminar(aviso);
            return "OK";
        }

        return "NO_ELIMINADO";
    }

    public static List<PrecioServicio> eliminarServicioDeAviso(PrecioServicio ps, Aviso aviso, Cliente cliente) throws InventarioException {

        String eliminar = precioServicioDAO.eliminar(ps);
        if (eliminar != null) {
            throw new InventarioException("Problema para eliminar el servicio seleccionado.");
        }

        return precioServicioDAO.buscarPorAviso(aviso, cliente);
    }

    public static void eliminarListadoPrecioServicios(List<PrecioServicio> lista) {
        precioServicioDAO.eliminarListado(lista);
    }

    public static void agregarServiciosAAviso(
            List<PrecioServicio> serviciosSeleccionados,
            List<PrecioServicio> serviciosActuales,
            Aviso aviso,
            Cliente cliente
    ) throws InventarioException {

        for (PrecioServicio ps : serviciosSeleccionados) {
            PrecioServicio nuevo = new PrecioServicio();
            nuevo.setAvisoCve(aviso);
            nuevo.setCliente(cliente);
            nuevo.setPrecio(ps.getPrecio());
            nuevo.setServicio(ps.getServicio());
            nuevo.setUnidad(ps.getUnidad());

            // Verificar si ya existe en la lista actual
            boolean yaExiste = serviciosActuales.stream().anyMatch(s -> s.equals(nuevo));
            if (yaExiste) {
                continue;
            }

            if (precioServicioDAO.guardar(nuevo) != null) {
                throw new InventarioException("Problema para actualizar los servicios.");
            }
        }
    }
}
