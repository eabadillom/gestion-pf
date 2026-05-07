package mx.com.ferbo.egresos.business;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.validation.ObjectValidator;
import com.ferbo.tools.validation.ObjectValidatorBuilder;

import mx.com.ferbo.egresos.dao.EgresoDAO;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class EgresoBL {

    private static final Logger log = LogManager.getLogger(EgresoBL.class);

    @Inject
    private EgresoDAO dao;

    // =====================================================
    // 1. CREAR EGRESO
    // =====================================================
    public void crearEgreso(Egreso egreso) throws InventarioException {

        validarNuevoEgreso(egreso);

        egreso.setStatus(obtenerStatusInicial());

        try {
            dao.guardar(egreso);
        } catch (InventarioException ex) {
            throw ex;
        }

    }

    // =====================================================
    // 2. ACTUALIZAR EGRESO
    // =====================================================
    public void actualizarEgreso(Egreso egreso) throws InventarioException {

        Egreso actual = obtenerPorId(egreso);

        validarActualizacion(actual);

        try {
            dao.actualizar(actual);
        } catch (InventarioException ex) {
            throw ex;
        }
    }

    // =====================================================
    // 3. OBTENER POR ID
    // =====================================================
    public Egreso obtenerPorId(Egreso egreso) {
        return dao.buscarPorId(egreso.getId())
                .orElseThrow(() -> new SystemException("Egreso no encontrado con id: " + egreso.getId()));
    }

    // =====================================================
    // 4. LISTAR TODOS
    // =====================================================
    public List<Egreso> listarTodos() {
        try {
            return dao.buscarTodos();
        } catch (SystemException ex) {
            throw ex;
        }
    }

    // =====================================================
    // 5. BUSCAR CON FILTROS
    // =====================================================
    public List<Egreso> buscarPorFiltros(
            LocalDateTime inicio,
            LocalDateTime fin,
            CategoriaEgreso categoria,
            StatusEgreso status,
            String concepto) {
        try {
            return dao.buscarPorFiltros(inicio, fin, categoria, status, concepto);
        } catch (SystemException ex) {
            throw ex;
        }
    }

    public void asignarStatusEgreso(Egreso egreso, StatusEgreso status) {
        ObjectValidator.notNull(egreso, "El egreso");
        ObjectValidator.notNull(status, "El status");

        egreso.setStatus(status);
    }

    
    // =====================================================
    // 7. PROCESAR EGRESO
    // =====================================================
    public void procesarEgreso(Egreso egreso) throws InventarioException {

        validarProcesamiento(egreso);

        try {
            dao.actualizar(egreso);
        } catch (InventarioException ex) {
            throw ex;
        }
    }

    // =====================================================
    // VALIDACIONES DE NEGOCIO
    // =====================================================

    private void validarNuevoEgreso(Egreso e) {

        new ObjectValidatorBuilder<>("egreso", e)
                .validateObject()
                .texto("concepto", Egreso::getConcepto)
                .validateNested("categoria", Egreso::getCategoria, cat -> cat.validateObject()
                        .texto("nombre", CategoriaEgreso::getNombre))
                .validateOrThrow();
    }

    private void validarActualizacion(Egreso actual) {

        if (esProcesado(actual)) {
            throw new SystemException("No se puede modificar un egreso procesado");
        }

        if (esCancelado(actual)) {
            throw new SystemException("No se puede modificar un egreso cancelado");
        }
    }

    private void validarCancelacion(Egreso e) {

        if (esCancelado(e)) {
            throw new SystemException("El egreso ya está cancelado");
        }

        if (esProcesado(e)) {
            throw new SystemException("No se puede cancelar un egreso procesado");
        }
    }

    private void validarProcesamiento(Egreso e) {

        if (esCancelado(e)) {
            throw new SystemException("No se puede procesar un egreso cancelado");
        }

        if (esProcesado(e)) {
            throw new SystemException("El egreso ya está procesado");
        }
    }

    // =====================================================
    // REGLAS DE ESTADO
    // =====================================================

    private boolean esProcesado(Egreso e) {
        return "PRO".equals(e.getStatus().getClave());
    }

    private boolean esCancelado(Egreso e) {
        return "CAN".equals(e.getStatus().getClave());
    }

    // =====================================================
    // ESTADOS AUXILIARES
    // =====================================================

    private StatusEgreso obtenerStatusInicial() {
        StatusEgreso s = new StatusEgreso();
        s.setClave("PEN");
        return s;
    }

    private StatusEgreso obtenerStatusCancelado() {
        StatusEgreso s = new StatusEgreso();
        s.setClave("CAN");
        return s;
    }

    private StatusEgreso obtenerStatusProcesado() {
        StatusEgreso s = new StatusEgreso();
        s.setClave("PRO");
        return s;
    }
}