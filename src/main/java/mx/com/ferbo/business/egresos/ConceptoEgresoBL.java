
package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.egresos.ConceptoEgresoDAO;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.categresos.StatusEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class ConceptoEgresoBL extends EgresoBaseBL<ConceptoEgreso, CatConceptoEgreso, StatusEgreso> {

    private static final Logger log = LogManager.getLogger();
    
    @Inject
    private ConceptoEgresoDAO dao;
    
    public ConceptoEgresoBL(){
        setDao(dao);
    }
    
    @Override
    protected ConceptoEgreso nuevo(){
        return new ConceptoEgreso();
    }
    
    @Override
    protected String nombreHijo() {
        return "el concepto del egreso";
    }

    @Override
    protected String nombreHijos() {
        return "los concepto de egresos";
    }

    @Override
    protected String nombreCatalogo() {
        return "el status";
    }

    @Override
    protected void validar(ConceptoEgreso concepto) throws InventarioException {
        if (concepto == null) {
            throw new InventarioException("El concepto del egreso no puede ser vacío.");
        }
        
        if (concepto.getEsDeducible() == null) {
            throw new InventarioException("No se tiene la informacíon si el egreso es deducible o no.");
        }
        
        if (concepto.getRequiereCFDI() == null) {
            throw new InventarioException("No se tiene la información si el egreso es deducible por CFDI o no.");
        }
        
        if (concepto.getEsDeducible() && concepto.getRequiereCFDI() && (concepto.getCfdiUUID() == null || "".equalsIgnoreCase(concepto.getCfdiUUID()))) {
            throw new InventarioException("No se tiene la información de CFDI UUID.");
        }
        
        if (!concepto.getEsDeducible() && concepto.getRequiereCFDI() && (concepto.getCfdiUUID() == null || "".equalsIgnoreCase(concepto.getCfdiUUID()))) {
            throw new InventarioException("No se tiene la información de CFDI UUID.");
        }
        
        if (!concepto.getEsDeducible() && (concepto.getNoDeducible() == null || "".equalsIgnoreCase(concepto.getNoDeducible()))) {
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
        
        if (concepto.getFechaRegistro() == null) {
            throw new InventarioException("No se tiene la fecha de registro.");
        }
        
        if (concepto.getTotalConceptoEgreso() == null || concepto.getTotalConceptoEgreso().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("No se tiene un total esperado para el egreso.");
        }
    }

    @Override
    protected void antesDeGuardar(ConceptoEgreso concepto, CatConceptoEgreso catConcepto) throws InventarioException {
        if (concepto.getCatConcepto() == null) {
            concepto.setCatConcepto(catConcepto);
        }
        
        if (concepto.getId() == null && concepto.getFechaRegistro() == null) {
            concepto.setFechaRegistro(new Date());
        }
    }

    @Override
    protected void antesDeCambiar(ConceptoEgreso son, StatusEgreso catalog) throws InventarioException {
        // Método sin implementar porque no se reqioere en el proceso
    }
    
    public void extraerDeCatalogo(ConceptoEgreso concepto, CatConceptoEgreso catConcepto){
        
        concepto.setEsDeducible(catConcepto.getEsDeducible());
        concepto.setRequiereCFDI(catConcepto.getRequiereCFDI());
        concepto.setTieneIVA(catConcepto.getTieneIVA());
        concepto.setPorcentajeIVA(catConcepto.getPorcentajeIVA());
        concepto.setTieneIEPS(catConcepto.getTieneIEPS());
        concepto.setPorcentajeIEPS(catConcepto.getPorcentajeIEPS());
        concepto.setEsActivoFijo(catConcepto.getEsActivoFijo());
        
    }
}
