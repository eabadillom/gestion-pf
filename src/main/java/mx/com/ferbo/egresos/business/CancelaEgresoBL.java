package mx.com.ferbo.egresos.business;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ValidationException;
import com.ferbo.tools.validation.ObjectValidator;

import mx.com.ferbo.egresos.dao.CancelaEgresoDAO;
import mx.com.ferbo.egresos.model.CancelaEgreso;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class CancelaEgresoBL {

    private static final Logger log = LogManager.getLogger(CancelaEgresoBL.class);

    @Inject
    private CancelaEgresoDAO dao;

    public CancelaEgreso nuevoOExistente(CancelaEgreso cancelaEgreso) {
        return (cancelaEgreso == null) ? new CancelaEgreso() : cancelaEgreso;
    }

    public void guardarOActualizarCancelaEgreso(CancelaEgreso cancelaEgreso) throws SystemException {
        Long id = cancelaEgreso.getId();

        String estado = "";

        try {
            if (id == null) {
                estado = "guardar";
                dao.guardar(cancelaEgreso);
            } else {
                estado = "actualizar";
                dao.actualizar(cancelaEgreso);
            }
        } catch (InventarioException ex) {
            log.warn("Error al momento de {} cancela egreso. {}", estado, ex);
            throw new SystemException("Hubo un problema al momento de " + estado + " cancela egreso.");
        }
    }

    public void concatenarSiHayMotivoCancelacion(CancelaEgreso cancelaEgreso, String inicioLeyenda) {
        String motivoConcatenado = inicioLeyenda;
        motivoConcatenado = motivoConcatenado.concat(" ha cancelado el egreso porque: ");
        if (cancelaEgreso.getMotivo() == null || "".equalsIgnoreCase(cancelaEgreso.getMotivo())){
            throw new ValidationException("El motivo de cancelación del egreso no puede ser vacío.");
        }
        motivoConcatenado = motivoConcatenado.concat(cancelaEgreso.getMotivo());
        cancelaEgreso.setMotivo(motivoConcatenado);
    }

    public void asignarEgreso(CancelaEgreso cancelaEgreso, Egreso egreso) {
        ObjectValidator.notNull(egreso, "El egreso");
        ObjectValidator.notNull(cancelaEgreso, "El motivo cancelación");

        cancelaEgreso.setEgreso(egreso);
    }
}
