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
import com.ferbo.tools.exception.ValidationException;
import com.ferbo.tools.validation.ObjectValidator;
import com.ferbo.tools.validation.ObjectValidatorBuilder;

import mx.com.ferbo.egresos.dao.EgresoDAO;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class EgresoBL {

    private static final Logger log = LogManager.getLogger(EgresoBL.class);

    @Inject
    private EgresoDAO dao;

    // =====================================================
    // 1. OBTENER POR ID
    // =====================================================
    public Egreso nuevoOExistente(Long id) {
        try {
            return dao.buscarPorId(id).orElseThrow(
                    () -> new SystemException("El egreso con identificador: " + id + ",  no se ha encontrado."));
        } catch (SystemException ex) {
            log.warn(ex);
            return new Egreso();
        }
    }

    // =====================================================
    // 2. PERSISTIR EGRESO
    // =====================================================
    private void crearOActualizarEgreso(Egreso egreso) {
        String estado = "";
        try {
            Long id = egreso.getId();
            if (id == null) {
                dao.guardar(egreso);
                estado = "guardar";
            } else {
                dao.actualizar(egreso);
                estado = "actualizar";
            }
        } catch (InventarioException ex) {
            log.warn(ex);
            throw new SystemException("Hubo un problema al momento de " + estado + " el egreso.");
        }
    }

    // =====================================================
    // 3. CAMBIAR STATUS EGRESO
    // =====================================================
    public void asignarStatusEgreso(Egreso egreso, StatusEgreso status) {
        ObjectValidator.notNull(egreso, "El egreso");
        ObjectValidator.notNull(status, "El status");

        egreso.setStatus(status);
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

    // =====================================================
    // 6. VALIDACIONES DE NEGOCIO
    // =====================================================
    private void validarEgresoNuevo(Egreso egreso) {

        new ObjectValidatorBuilder<>("egreso", egreso)
                .validateObject()
                .texto("concepto", Egreso::getConcepto)
                .texto("referencia", Egreso::getReferencia)
                .validateNested("categoria", Egreso::getCategoria, cat -> cat.validateObject()
                        .texto("nombre", CategoriaEgreso::getNombre))
                .validateNested("emisor", Egreso::getEmisor, emi -> emi.validateObject()
                        .texto("nombre", EmisoresCFDIS::getNb_emisor))
                .validateNested("forma pago", Egreso::getFormaPago, fpago -> fpago.validateObject()
                        .texto("nombre", MedioPago::getFormaPago))
                .validateNested("metodo pago", Egreso::getMetodoPago, mpago -> mpago.validateObject()
                        .texto("nombre", MetodoPago::getNbMetodoPago))
                .validateOrThrow();
            
        BigDecimal total = egreso.getMonto();

        if (total == null || BigDecimal.ZERO.compareTo(total) < 0) {
            throw new ValidationException("El monto no del egreso no puede ser vacío, ni negativo.");
        }

        LocalDateTime fechaEgreso = egreso.getFecha();
        
        LocalDateTime hoy = LocalDateTime.now();

        if (fechaEgreso == null || fechaEgreso.isAfter(hoy)) {
            throw new ValidationException("La fecha de pago del egreso no puede ser mayor a al día de hoy.");
        }
    }

    // =====================================================
    // 7. PROCESAR EGRESO
    // =====================================================
    public void procesarEgreso(Egreso egreso) throws ValidationException, SystemException {

        validarEgresoNuevo(egreso);

        crearOActualizarEgreso(egreso);

    }

}