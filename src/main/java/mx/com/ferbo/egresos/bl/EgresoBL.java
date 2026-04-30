package mx.com.ferbo.egresos.bl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.validation.ObjectValidatorBuilder;

import mx.com.ferbo.egresos.dao.EgresoDAO;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;

public class EgresoBL {

    private static final Logger log = LogManager.getLogger(EgresoBL.class);

    private final EgresoDAO dao;

    public EgresoBL(EgresoDAO dao) {
        this.dao = dao;
    }

    // =====================================================
    // 1. CREAR EGRESO
    // =====================================================
    public Egreso crearEgreso(Egreso egreso) {

        validarNuevoEgreso(egreso);

        egreso.setStatus(obtenerStatusInicial());

        return dao.save(egreso);
    }

    // =====================================================
    // 2. ACTUALIZAR EGRESO
    // =====================================================
    public Egreso actualizarEgreso(Egreso egreso) {

        Egreso actual = obtenerPorId(egreso.getId());

        validarActualizacion(actual);

        return dao.save(egreso);
    }

    // =====================================================
    // 3. OBTENER POR ID
    // =====================================================
    public Egreso obtenerPorId(Long id) {
        return dao.find(id)
                .orElseThrow(() -> new SystemException("Egreso no encontrado con id: " + id));
    }

    // =====================================================
    // 4. LISTAR TODOS
    // =====================================================
    public List<Egreso> listarTodos() {
        return dao.buscarTodos();
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

        return dao.buscarPorFiltros(inicio, fin, categoria, status, concepto);
    }

    // =====================================================
    // 6. CANCELAR EGRESO
    // =====================================================
    public void cancelarEgreso(Long id) {

        Egreso egreso = obtenerPorId(id);

        validarCancelacion(egreso);

        egreso.setStatus(obtenerStatusCancelado());

        dao.save(egreso);
    }

    // =====================================================
    // 7. PROCESAR EGRESO
    // =====================================================
    public void procesarEgreso(Long id) {

        Egreso egreso = obtenerPorId(id);

        validarProcesamiento(egreso);

        egreso.setStatus(obtenerStatusProcesado());

        dao.save(egreso);
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

        if (e.getMonto() == null || e.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new SystemException("El monto debe ser mayor a 0");
        }

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