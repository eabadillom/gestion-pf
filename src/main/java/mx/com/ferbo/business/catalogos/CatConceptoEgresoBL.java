package mx.com.ferbo.business.catalogos;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.catalogos.CatConceptoEgresoDAO;
import mx.com.ferbo.model.n.catalogos.CatConceptoEgreso;
import mx.com.ferbo.model.n.catalogos.CategoriaEgreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class CatConceptoEgresoBL extends BaseCatalogosBL<CatConceptoEgreso> {

    private static final Logger log = LogManager.getLogger(CatConceptoEgresoBL.class);

    @Inject
    private CatConceptoEgresoDAO dao;

    @PostConstruct
    public void init() {
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(CatConceptoEgreso model) throws InventarioException {
        
        validarCodigoSat(model);
        validarCategoria(model);
        validarFlagsFiscales(model);
        validarImpuestos(
                model.getTieneIVA(),
                model.getPorcentajeIVA(),
                model.getTieneIEPS(),
                model.getPorcentajeIEPS());
    }

    private void validarCodigoSat(CatConceptoEgreso model)
            throws InventarioException {

        if (model.getCodigoSAT() == null || model.getCodigoSAT().trim().isEmpty()) {
            throw new InventarioException("El concepto del catálogo no tiene código SAT.");
        }

        if (model.getCodigoSAT().length() != 8) {
            throw new InventarioException("El código SAT debe tener 8 dígitos.");
        }
    }

    private void validarCategoria(CatConceptoEgreso model)
            throws InventarioException {

        if (model.getCategoriaEgreso() == null) {
            throw new InventarioException("El concepto del catálogo no tiene categoría.");
        }
    }

    private void validarFlagsFiscales(CatConceptoEgreso model)
            throws InventarioException {

        if (model.getEsActivoFijo() == null) {
            throw new InventarioException("No se puede determinar si es activo fijo.");
        }

        if (model.getRequiereCFDI() == null) {
            throw new InventarioException("No se puede determinar si requiere CFDI.");
        }

        if (model.getEsDeducible() == null) {
            throw new InventarioException("No se puede determinar si es deducible.");
        }
    }

    private void validarImpuestos(
            Boolean tieneIva, BigDecimal pcIva,
            Boolean tieneIeps, BigDecimal pcIeps) throws InventarioException {

        if (tieneIva == null) {
            throw new InventarioException("No se puede determinar si aplica IVA.");
        }

        if (Boolean.TRUE.equals(tieneIva)) {
            if (pcIva == null) {
                throw new InventarioException("Aplica IVA pero no tiene porcentaje.");
            }
            if (pcIva.compareTo(BigDecimal.ZERO) < 0 ||
                    pcIva.compareTo(new BigDecimal("100")) > 0) {
                throw new InventarioException("El porcentaje de IVA debe estar entre 0 y 100.");
            }
        }

        if (tieneIeps == null) {
            throw new InventarioException("No se puede determinar si aplica IEPS.");
        }

        if (Boolean.TRUE.equals(tieneIeps)) {
            if (pcIeps == null) {
                throw new InventarioException("Aplica IEPS pero no tiene porcentaje.");
            }
            if (pcIeps.compareTo(BigDecimal.ZERO) < 0 ||
                    pcIeps.compareTo(new BigDecimal("100")) > 0) {
                throw new InventarioException("El porcentaje de IEPS debe estar entre 0 y 100.");
            }
        }
    }

    public List<CatConceptoEgreso> obtenerPorCategoria(CategoriaEgreso categoria) throws InventarioException {
        try {
            return dao.buscarPorCategoriaEgreso(categoria.getId());
        } catch (DAOException ex) {
            log.warn("Error al obtener conceptos asociados con la categoria {}. {}", categoria.getNombre(), ex);
            throw new InventarioException(
                    "Hubo un problema al obtener los conceptos asociados con la categoria " + categoria.getNombre());
        }
    }

    public List<CatConceptoEgreso> obtenerPorCategoriaYVigencia(CategoriaEgreso categoria, Boolean vigencia)
            throws InventarioException {
        try {
            return dao.buscarPorCategoriaEgresoYEstado(categoria.getId(), vigencia);
        } catch (DAOException ex) {
            String estado = vigencia ? "vigentes" : "no vigentes";
            log.warn("Error al obtener conceptos asociados con la categoria {} y {}. {}", categoria.getNombre(), estado,
                    ex);
            throw new InventarioException("Hubo un problema al obtener los conceptos asociados con la categoria "
                    + categoria.getNombre() + " y " + estado);
        }
    }
    
    public void verificarExistenciaHijos(CategoriaEgreso categoria) throws InventarioException {
        try {
            List<CatConceptoEgreso> conceptos = dao.buscarPorCategoriaEgresoYEstado(categoria.getId(), Boolean.TRUE);
            if (!conceptos.isEmpty()) {
                throw new DAOException("No se puede cancelar la categoria de egreso por tener conceptos vigentes.");
            }
        } catch (DAOException ex) {
            throw new InventarioException(ex.getMessage());
        }
    }
}
