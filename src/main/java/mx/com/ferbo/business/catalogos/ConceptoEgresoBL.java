package mx.com.ferbo.business.catalogos;

import java.math.BigDecimal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.catalogos.ConceptoEgresoDAO;
import mx.com.ferbo.model.catalogos.ConceptoEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ConceptoEgresoBL {

    private static final Logger log = LogManager.getLogger(ConceptoEgresoBL.class);

    @Inject
    private ConceptoEgresoDAO dao;

    public void validarEspecifico(ConceptoEgreso model)
            throws InventarioException {

        // validarTotalPlaneado(model);
        validarImpuestos(
                model.getTieneIVA(),
                model.getPorcentajeIVA(),
                model.getTieneIEPS(),
                model.getPorcentajeIEPS());
        //validarDeducibilidad(model);
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
}
