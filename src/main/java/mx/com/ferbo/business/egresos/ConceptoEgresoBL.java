package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.egresos.ConceptoEgresoDAO;
import mx.com.ferbo.model.n.catalogos.CategoriaEgreso;
import mx.com.ferbo.model.n.egresos.ConceptoEgreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ConceptoEgresoBL {

    private static final Logger log = LogManager.getLogger(ConceptoEgresoBL.class);

    @Inject
    private ConceptoEgresoDAO conceptoEgresoDAO;

    public ConceptoEgreso validarConceptoEgreso(ConceptoEgreso conceptoEgreso) throws InventarioException {
        if (conceptoEgreso == null) {
            throw new InventarioException("El concepto del egreso no puede ser vacío.");
        }

        if (conceptoEgreso.getNombre().equalsIgnoreCase("")) {
            throw new InventarioException("El concepto del egreso no tiene asociado ningun nombre.");
        }

        if (conceptoEgreso.getActivo() == null) {
            throw new InventarioException("No se sabe si el concepto del egreso esta activo o inactivo.");
        }

        if (conceptoEgreso.getCodigoSAT().equalsIgnoreCase("")) {
            throw new InventarioException("El concepto del egreso no tiene asociada ninguna clave del SAT.");
        }

        if (conceptoEgreso.getEsActivoFijo() == null) {
            throw new InventarioException("No se sabe si el concepto del egreso es activo fijo o no.");
        }

        if (conceptoEgreso.getTieneIVA() == null) {
            throw new InventarioException("No se sabe si el concepto del egreso tiene o no IVA.");
        }

        if (Boolean.TRUE.equals(conceptoEgreso.getTieneIVA()) && (conceptoEgreso.getPorcentajeIVA() == null
                || conceptoEgreso.getPorcentajeIVA().compareTo(BigDecimal.ZERO) == 0)) {
            throw new InventarioException("El porcentaje de IVA no puede ser vacío o cero.");
        }

        if (conceptoEgreso.getTieneIEPS() == null) {
            throw new InventarioException("No se sabe si el concepto del egreso tiene o no IEPS.");
        }

        if (Boolean.TRUE.equals(conceptoEgreso.getTieneIEPS()) && (conceptoEgreso.getPorcentajeIEPS() == null
                || conceptoEgreso.getPorcentajeIEPS().compareTo(BigDecimal.ZERO) == 0)) {
            throw new InventarioException("El porcentaje de IEPS no puede ser vacío o cero.");
        }

        if (conceptoEgreso.getCategoriaEgreso() == null) {
            throw new InventarioException("El concepto de egreso no esta asociada a ninguna categoría.");
        }

        return conceptoEgreso;
    }

    public List<ConceptoEgreso> obtenerConceptos(boolean activo) throws InventarioException {
        try {
            return conceptoEgresoDAO.buscarTodosActivos(activo);
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
            throw new InventarioException(ex.getMessage());
        }
    }

    public List<ConceptoEgreso> obtenerConceptosPorCategoria(CategoriaEgreso categoriaEgreso)
            throws InventarioException {
        try {
            return conceptoEgresoDAO.buscarTodosPorCategoriaEgreso(categoriaEgreso.getId());
        } catch (DAOException ex) {
            log.warn("No se pudo obtener algun concepto de egreso relacionado con la categoria {}. {}",
                    categoriaEgreso.getNombre(), ex);
            throw new InventarioException("No se pudo obtener algun concepto de egreso relacionado con la categoria "
                    + categoriaEgreso.getNombre());
        }
    }

    public List<ConceptoEgreso> obtenerConceptosPorCategoriaYActivo(CategoriaEgreso categoriaEgreso, boolean activo)
            throws InventarioException {
        try {
            return conceptoEgresoDAO.buscarTodoPorCategoriaEgresoYActivo(categoriaEgreso.getId(), activo);
        } catch (DAOException ex) {
            String estado = (activo) ? "activo" : "no activo";
            log.warn("No se pudo obtener algun concepto de egreso relacion con la categoria {} y en estado {}. {}",
                    categoriaEgreso.getNombre(), estado, ex);
            throw new InventarioException("No se pudo obtener algun concepto de egreso relacion con la categoria "
                    + categoriaEgreso.getNombre() + " y en estado " + estado);
        }
    }

    public String agregarOActualizar(ConceptoEgreso conceptoEgreso) throws InventarioException {
        validarConceptoEgreso(conceptoEgreso);
        String mensaje = "El concepto de egreso " + conceptoEgreso.getNombre();

        if (conceptoEgreso.getId() == null) {
            conceptoEgresoDAO.guardar(conceptoEgreso);
            mensaje += " se agrego correctamente";
        } else {
            conceptoEgresoDAO.actualizar(conceptoEgreso);
            mensaje += " se actualizo correctamente";
        }
        return mensaje;
    }
}
