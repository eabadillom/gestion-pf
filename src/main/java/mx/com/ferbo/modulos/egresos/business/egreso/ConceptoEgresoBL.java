package mx.com.ferbo.modulos.egresos.business.egreso;

import java.math.BigDecimal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.egresos.dao.egreso.ConceptoEgresoDAO;
import mx.com.ferbo.modulos.egresos.model.catprimarios.CatConceptoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ConceptoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.ValidationUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class ConceptoEgresoBL {

    private static final Logger log = LogManager.getLogger(ConceptoEgresoBL.class);

    @Inject
    private ConceptoEgresoDAO dao;

    public ConceptoEgresoBL() {
    }

    public ConceptoEgreso nuevo() {
        return new ConceptoEgreso();
    }

    private void validarConceptoEgreso(ConceptoEgreso concepto) throws InventarioException {

        ValidationUtils.requireNonNull(concepto, "El concepto del egreso no puede ser vacío.");

        ValidationUtils.requireNonNull(concepto.getEsDeducible(), "No se tiene la informacíon si el egreso es deducible o no.");

        ValidationUtils.requireNonNull(concepto.getRequiereCFDI(), "No se tiene la información si el egreso es deducible por CFDI o no.");

        ValidationUtils.requireNonNull(concepto, null);

        if (concepto.getEsDeducible() && concepto.getRequiereCFDI()
                && (concepto.getCfdiUUID() == null || "".equalsIgnoreCase(concepto.getCfdiUUID()))) {
            throw new InventarioException("No se tiene la información de CFDI UUID.");
        }

        if (!concepto.getEsDeducible() && concepto.getRequiereCFDI()
                && (concepto.getCfdiUUID() == null || "".equalsIgnoreCase(concepto.getCfdiUUID()))) {
            throw new InventarioException("No se tiene la información de CFDI UUID.");
        }

        if (!concepto.getEsDeducible()
                && (concepto.getNoDeducible() == null || "".equalsIgnoreCase(concepto.getNoDeducible()))) {
            throw new InventarioException("No se tiene la información de porque el egreso no es deducible.");
        }

        if (concepto.getTieneIVA() == null) {
            throw new InventarioException("No se tiene la información de si tiene o no IVA el egreso.");
        }

        if (concepto.getTieneIVA() && concepto.getPorcentajeIVA() == null) {
            throw new InventarioException("El egreso requiere IVA y no se tiene la información del porcentaje.");
        }

        if (concepto.getTieneIEPS() == null) {
            throw new InventarioException("No se tiene la información de si tiene o no IEPS el egreso.");
        }

        if (concepto.getTieneIEPS() && concepto.getPorcentajeIEPS() == null) {
            throw new InventarioException("El egreso requiere IEPS y no se tiene la información del porcentaje.");
        }

        if (concepto.getEsActivoFijo() == null) {
            throw new InventarioException("No se tiene la información si es activo fijo o no");
        }

        if (concepto.getTotalConceptoEgreso() == null
                || concepto.getTotalConceptoEgreso().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("No se tiene un total esperado para el egreso.");
        }
    }

    private void asignarAntesDeGuardar(ConceptoEgreso concepto, CatConceptoEgreso catConcepto) {

        if (concepto.getId() == null) {
            concepto.setCatConcepto(catConcepto);
        }

    }

    public ConceptoEgreso obtenerConceptoEgreso(ImporteEgreso egreso) {
        if (egreso.getConceptoEgreso().getId() == null) {
            return nuevo();
        } else {
            return egreso.getConceptoEgreso();
        }

    }

    public void guardarConceptoEgreso(CatConceptoEgreso catConcepto, ConceptoEgreso concepto) throws InventarioException{

        ValidationUtils.requireNonNull(catConcepto, "El concepto egreso del catalogo no puede ser vacío.");
        ValidationUtils.requireNonNull(concepto, "El concepto de egreso no puede ser vacío.");

        asignarAntesDeGuardar(concepto, catConcepto);

        validarConceptoEgreso(concepto);

        if (concepto.getId() == null){
            dao.guardar(concepto);
        }
        
    }

    public void extraerDeCatalogo(ConceptoEgreso concepto, CatConceptoEgreso catConcepto) throws InventarioException {

        ValidationUtils.requireNonNull(concepto, "El concepto del egreso no puede ser vacío.");
        ValidationUtils.requireNonNull(catConcepto, "El concepto del catalogo no puede ser vacío.");

        concepto.setEsDeducible(catConcepto.getEsDeducible());
        concepto.setRequiereCFDI(catConcepto.getRequiereCFDI());
        concepto.setTieneIVA(catConcepto.getTieneIVA());
        concepto.setPorcentajeIVA(catConcepto.getPorcentajeIVA());
        concepto.setTieneIEPS(catConcepto.getTieneIEPS());
        concepto.setPorcentajeIEPS(catConcepto.getPorcentajeIEPS());
        concepto.setEsActivoFijo(catConcepto.getEsActivoFijo());

    }

}
