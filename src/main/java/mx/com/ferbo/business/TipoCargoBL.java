package mx.com.ferbo.business;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoCargoDAO;
import mx.com.ferbo.model.TipoCargo;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoCargoBL {

    private static final Logger log = LogManager.getLogger(TipoCargoBL.class);

    @Inject
    private TipoCargoDAO tipoCargoDAO;

    public TipoCargo validarTipoCargo(TipoCargo tipoCargo) throws InventarioException {
        if (tipoCargo == null) {
            throw new InventarioException("El tipo de cargo no puede ser vacío.");
        }

        if ("".equalsIgnoreCase(tipoCargo.getNombre())){
            throw new InventarioException("El tipo de cargo no tiene ningun nombre asociado.");
        }

        if ("".equalsIgnoreCase(tipoCargo.getDescripcion())){
            throw new InventarioException("El tipo de cargo no tiene una descripción.");
        }

        if (tipoCargo.getActivo() == null) {
            throw new InventarioException("No se sabe si el tipo de cargo está activo o no.");
        }

        if (tipoCargo.getTieneIVA() == null) {
            throw new InventarioException("No se sabe si el tipo de cargo tiene o no IVA");
        }

        if (tipoCargo.getTieneIEPS() == null) {
            throw new InventarioException("No se sabe si el tipo de cargo tiene o no IEPS");
        }

        return tipoCargo;
    }

    public List<TipoCargo> obtenerTiposCargo(boolean activo) throws InventarioException {
        try {
            return tipoCargoDAO.buscarActivos(activo);
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            throw new InventarioException(ex.getMessage());
        }
    }

    public String agregarOActualizar(TipoCargo tipoCargo) throws InventarioException {
        validarTipoCargo(tipoCargo);
        String mensaje = "El tipo de cargo " + tipoCargo.getNombre();

        if (tipoCargo.getId() == null) {
            tipoCargoDAO.guardar(tipoCargo);
            mensaje += " se guardo correctamente";
        } else {
            tipoCargoDAO.actualizar(tipoCargo);
            mensaje += " se actualizo correctamente";
        }

        return mensaje;
    }
}
