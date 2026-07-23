package mx.com.ferbo.egresos.business;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.BusinessException;
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

@Named
@RequestScoped
public class EgresoBL {

    private static final Logger log = LogManager.getLogger(EgresoBL.class);

    @Inject
    private EgresoDAO dao;

    public Egreso nuevoOExistente(Long id) {
        try {
            return dao.buscarPorId(id).orElseThrow(
                    () -> new SystemException("El egreso con identificador: " + id + ",  no se ha encontrado."));
        } catch (SystemException ex) {
            log.warn("Error al momento de obtener el egreso: {}", ex.getMessage(), ex);
            return new Egreso();
        }
    }

    public Egreso crearOActualizarEgreso(Egreso egreso, String inicioLeyenda) {
        String estado = "";
        try {
            Long id = egreso.getId();
            if (id == null) {
                estado = "guardar";
                return dao.guardarYObtener(egreso);
            } else {
                estado = "actualizar";
                return dao.actualizarYObtener(egreso);
            }
        } catch (SystemException ex) {
            log.warn("Error al momento de procesar el egreso: {}", ex.getMessage(), ex);
            throw new SystemException("Hubo un problema al momento de " + estado + " el egreso.");
        }
    }

    public List<Egreso> listarTodos() {
        try {
            return dao.buscarTodos();
        } catch (SystemException ex) {
            log.warn("Error de obtener todo el listado de egresos: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public List<Egreso> obtenerPorFiltros(
            YearMonth mes,
            CategoriaEgreso categoria,
            StatusEgreso status,
            String concepto, EmisoresCFDIS emisor) {
        try {
            LocalDateTime inicio = inicioMes(mes);
            LocalDateTime fin = finMes(mes);
            return dao.buscarPorFiltros(inicio, fin, categoria, status, concepto, emisor);
        } catch (SystemException ex) {
            log.warn("Error de obtener todo el listado de egresos por filtros: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public List<Egreso> obtenerPorCategoria(CategoriaEgreso categoria) {
        ObjectValidator.notNull(categoria, "categoria egreso");
        try {
            return dao.buscarPorCategoria(categoria);
        } catch (SystemException ex) {
            log.warn("Error al momento de obtener los egresos. {}", ex);
            throw ex;
        }
    }

    public List<Egreso> obtenerPorStatus(StatusEgreso status) {
        ObjectValidator.notNull(status, "status de egreso");
        try {
            return dao.buscarPorStatus(status);
        } catch (SystemException ex) {
            log.warn("Error al momento de obtener los egresos. {}", ex);
            throw ex;
        }
    }

    public void validarEgresoNuevo(Egreso egreso) {

        new ObjectValidatorBuilder<>("egreso", egreso)
                .validateObject()
                .texto("concepto", Egreso::getConcepto)
                .validateNested("categoria", Egreso::getCategoria, cat -> cat.validateObject()
                        .texto("nombre", CategoriaEgreso::getNombre))
                .validateNested("emisor", Egreso::getEmisor, emi -> emi.validateObject()
                        .texto("nombre", EmisoresCFDIS::getNb_emisor))
                .validateNested("forma pago", Egreso::getFormaPago, fpago -> fpago.validateObject()
                        .texto("nombre", MedioPago::getFormaPago))
                .validateNested("metodo pago", Egreso::getMetodoPago, mpago -> mpago.validateObject()
                        .texto("nombre", MetodoPago::getNbMetodoPago))
                .validateOrThrow();

        ObjectValidator.notNull(egreso.getFecha(), "fecha de pago");
    }

    public void validarEgresoProcesado(Egreso egreso) {

        BigDecimal total = egreso.getMonto();

        String referencia = egreso.getReferencia();

        if (referencia == null || "".equalsIgnoreCase(referencia)) {
            throw new ValidationException(
                    "El egreso cambio a status 'Procesado' por lo tanto debe tener una referencía.");
        }

        if (referencia.length() > 150) {
            throw new ValidationException("La referencía sobrepasa el limiete de 150 caracteres.");
        }

        if (total == null) {
            throw new ValidationException(
                    "El egreso cambio a status 'Procesado' por lo tanto debe tener el monto pagado.");
        }

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("El monto del egreso no puede ser negativo.");
        }

    }

    public boolean egresoEstaPersistido(Egreso egreso) {
        return (egreso.getId() == null); 
    }

    public boolean isProcesado(Egreso egreso) {
        return ("PRO".equalsIgnoreCase(egreso.getStatus().getClave()));
    }

    public boolean isCancelado(StatusEgreso statusEgreso) {
        return ("CAN".equalsIgnoreCase(statusEgreso.getClave()));
    }

    private void validarStatusCancelado(String claveStatusEgreso) {
        if ("PRO".equalsIgnoreCase(claveStatusEgreso)) {
            throw new BusinessException("El egreso ya se encuentra procesado y por lo tanto no se puede cancelar");
        }
    }

    private void validarStatusProcesado(String claveStatusEgreso) {
        if ("CAN".equalsIgnoreCase(claveStatusEgreso)) {
            throw new BusinessException("El egreso ya se encuentra cancelado y por lo tanto no puede procesar");
        }
    }

    private void validarStatusPendiente(String claveStatusEgreso) {
        if ("PRO".equalsIgnoreCase(claveStatusEgreso) || "CAN".equalsIgnoreCase(claveStatusEgreso)){
            throw new BusinessException("El egreso ya se encuentra con el estatus " + claveStatusEgreso + "por lo tanto ya no puede cambiar a pendiente");
        }
    }

    private void validarStatusPrimeraVez(StatusEgreso status) {
        String claveStatus = status.getClave();
        if ("CAN".equalsIgnoreCase(claveStatus)) {
            throw new BusinessException(
                    "No se puede asignar el status de cancelado si el egreso aún no está registrado en el sistema");
        }
    }

    private void validarStatusParaEgresoPersistido(Egreso egreso, StatusEgreso status) {
        String claveStatus = status.getClave();
        String claveStatusEgreso = egreso.getStatus().getClave();

        switch (claveStatus) {
            case "PRO":
                validarStatusProcesado(claveStatusEgreso);
                validarEgresoProcesado(egreso);
                break;

            case "CAN":
                validarStatusCancelado(claveStatusEgreso);
                break;

            case "PEN":
                validarStatusPendiente(claveStatusEgreso);
                break;

            default:
                throw new BusinessException("El status de egreso aún no se encuentra en el sistema");
        }
    }

    public void validarStatusExistenteONuevo(Egreso egreso, StatusEgreso status) {
        if (egresoEstaPersistido(egreso)) {
            validarStatusPrimeraVez(status);
            egreso.setStatus(status);
        } else {
            validarStatusParaEgresoPersistido(egreso, status);
        }
    }

    public void asigarStatusEgreso(Egreso egreso, StatusEgreso status) {
        egreso.setStatus(status);
    }

    public void validarEgresoYStatus(Egreso egreso, StatusEgreso status) {
        ObjectValidator.notNull(egreso, "egreso");
        ObjectValidator.notNull(status.getId(), "status de egreso");
    }

    private LocalDateTime inicioMes(YearMonth mes) {
        ObjectValidator.notNull(mes, "El mes");
        return mes.atDay(1).atStartOfDay();
    }

    private LocalDateTime finMes(YearMonth mes) {
        ObjectValidator.notNull(mes, "El mes");
        return mes.atEndOfMonth().atTime(23, 59, 59, 999_999_999);
    }

}
