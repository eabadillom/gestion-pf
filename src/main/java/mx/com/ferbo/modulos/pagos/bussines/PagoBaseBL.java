package mx.com.ferbo.modulos.pagos.bussines;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.pagos.dao.PagoBaseDAO;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

public abstract class PagoBaseBL<T> {

    protected PagoBaseDAO<T> dao;
    protected Logger log = LogManager.getLogger(this.getClass().getName());

    public PagoBaseBL(){}

    protected void setDao(PagoBaseDAO<T> dao) {
        this.dao = dao;
    }

    protected abstract List<T> buscarVigentes(Date fecha) throws DAOException;

    protected abstract String getNombreSingularEntidad();
    protected abstract String getNombrePluralEntidad();

    public List<T> obtenerVigentes() throws InventarioException {
        Date fecha = new Date();
        try {
            log.info("Inicia proceso para obtener todos los " + getNombreSingularEntidad());
            return buscarVigentes(fecha);
        } catch (DAOException ex) {
            log.error("Error al obtener los " + getNombreSingularEntidad() + " vigentes hasta la fecha: " + fecha, ex);
            throw new InventarioException(
                    "Ocurrió un problema al obtener los " + getNombreSingularEntidad() + " vigentes hasta la fecha: " + fecha,
                    ex);
        }
    }

}
