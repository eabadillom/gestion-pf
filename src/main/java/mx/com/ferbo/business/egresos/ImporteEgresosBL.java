package mx.com.ferbo.business.egresos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.egresos.ImporteEgresoDAO;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.categresos.StatusEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ImporteEgresosBL extends EgresoBaseBL<ImporteEgreso, ConceptoEgreso, StatusEgreso> {

    private static final Logger log = LogManager.getLogger(ImporteEgresosBL.class);

    @Inject
    private ImporteEgresoDAO importeEgresoDAO;

    @Override
    protected String nombreHijo() {
        return "el importe de egreso";
    }

    @Override
    protected String nombreHijos() {
        return "los importe de egresos";
    }

    @Override
    protected String nombrePadre() {
        return "el concepto egreso";
    }

    @Override
    protected String nombreCatalogo() {
        return "el status";
    }

    @Override
    protected void validar(ImporteEgreso egreso) throws InventarioException {
        if (egreso == null) {
            throw new InventarioException("El egreso no puede ser vacío");
        }
        if (egreso.getSubTotal() == null || egreso.getSubTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InventarioException("El subtotal debe ser mayor que cero");
        }
        if (egreso.getConceptoEgreso() == null) {
            throw new InventarioException("Debe asignar un concepto al egreso");
        }
    }

    @Override
    protected void antesDeGuardar(ImporteEgreso importe, ConceptoEgreso concepto) throws InventarioException {

        if (importe.getConceptoEgreso() == null) {
            importe.setConceptoEgreso(concepto);
        }

        if (importe.getId() == null) {
            importe.setFechaAlta(new Date());
        }
    }

    @Override
    protected void antesDeCambiar(ImporteEgreso entity, StatusEgreso catalog) throws InventarioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ImporteEgreso obtenerPorId(Integer id) {
        try {
            return importeEgresoDAO.buscarPorId(id).orElseThrow(() -> new DAOException ("Hubo un problema al buscar un egreso con id: " + id));
        } catch (DAOException ex) {
            log.error("Hubo un problema al obtener algun egreso con id: {}. {}", id, ex);
            return new ImporteEgreso();
        }
    }
    
    public List<ImporteEgreso> obtenerPorFiltros(
            CatConceptoEgreso concepto,
            NEmisoresCFDIS razon,
            YearMonth mes)
            throws InventarioException, DAOException {

        try {

            LocalDate lInicio = mes.atDay(1);
            LocalDate lFin = mes.atEndOfMonth();

            Date inicio = DateUtil.toDate(lInicio);
            Date fin = DateUtil.toDate(lFin);

            Integer idConcepto = null;
            Integer idEmisor = null;

            if (concepto != null && concepto != null) {
                idConcepto = concepto.getId();
            }

            if (razon != null) {
                idEmisor = razon.getId();
            }

            return importeEgresoDAO.buscarPorFiltros(
                    inicio,
                    fin,
                    idConcepto,
                    idEmisor
            );

        } catch (DAOException ex) {

            log.warn("Error al obtener egresos: {}", ex.getMessage(), ex);

            String detalle = (concepto == null || concepto == null)
                    ? "del mes " + mes
                    : "con concepto " + concepto.getNombre();

            throw new InventarioException(
                    "Hubo un problema al obtener los egresos " + detalle
            );
        }
    }
}
