package mx.com.ferbo.bitacoraimp.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.bitacora.dao.BitacoraDAO;
import com.ferbo.bitacora.model.Bitacora;

import mx.com.ferbo.commons.dao.BaseDAO;

@Named
@ApplicationScoped
public class BitacoraDAOImp extends BaseDAO<Bitacora, Long> implements BitacoraDAO {

    private static final Logger log = LogManager.getLogger(BitacoraDAO.class);

    public BitacoraDAOImp() {
        super(Bitacora.class);
    }


    @Override
    public void aplicarRollBack(EntityManager em) {
        super.rollback(em);
    }

    @Override
    public void cerrarEnitytManager(EntityManager em) {
        super.close(em);
    }

    @Override
    public EntityManager obtenerEntityManager() {
        return super.getEntityManager();
    }

    @Override
    public void mostrarInfo(String mensaje) {
        log.info(mensaje);
    }

    @Override
    public void mostrarWaring(String mensaje, Exception ex) {
        log.warn("{}: {}", mensaje, ex.getMessage(), ex);
    }

    @Override
    public void mostrarError(String mensaje, Exception ex) {
        log.error("{}: {}", mensaje, ex.getMessage(), ex);
    }
    
}
