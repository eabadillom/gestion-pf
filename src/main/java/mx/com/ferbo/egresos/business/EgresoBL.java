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

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ValidationException;
import com.ferbo.tools.validation.Notification;
import com.ferbo.tools.validation.ObjectValidator;
import com.ferbo.tools.validation.ObjectValidatorBuilder;
import com.ferbo.tools.validation.TextValidator;

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

    public Egreso nuevoOExistente(Long id) {
        try {
            return dao.buscarPorId(id).orElseThrow(
                    () -> new SystemException("El egreso con identificador: " + id + ",  no se ha encontrado."));
        } catch (SystemException ex) {
            log.warn(ex);
            return new Egreso();
        }
    }

    public void crearOActualizarEgreso(Egreso egreso) {
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

    public void asignarStatusEgreso(Egreso egreso, StatusEgreso status) {
        ObjectValidator.notNull(egreso, "El egreso");
        ObjectValidator.notNull(status, "El status");

        egreso.setStatus(status);
    }

    public List<Egreso> listarTodos() {
        try {
            return dao.buscarTodos();
        } catch (SystemException ex) {
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

        if ("PRO".equalsIgnoreCase(egreso.getStatus().getClave())) {

            BigDecimal total = egreso.getMonto();

            Notification notification = new Notification();

            TextValidator textValidator = new TextValidator(150);

            try {
                textValidator.validate(egreso.getReferencia(), notification);
            } catch (ValidationException ex) {
                log.warn("Hubo una error en la referenficía, solamente se cambia el mensaje para el usuario.");
                throw new ValidationException(
                        "El egreso cambio a status 'Procesado' por lo tanto debe tener un una referencía.");
            }

            if (total == null) {
                throw new ValidationException(
                        "El egreso cambio a status 'Procesado' por lo tanto debe tener el monto pagado.");
            }

            if (total.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("El monto del egreso no puede ser negativo.");
            }
        }

    }

    public void procesarEgreso(Egreso egreso) throws ValidationException, SystemException {

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
