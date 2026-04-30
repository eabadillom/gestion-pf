package mx.com.ferbo.egresos.dao.catalogos;

import java.util.List;

import mx.com.ferbo.config.persistencia.JpaDAO;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;

public class StatusEgresoDAO extends JpaDAO<StatusEgreso, Long> {

    public StatusEgresoDAO() {
        super(StatusEgreso.class);
    }

    public StatusEgreso buscarPorClave(String clave) {
        return em.createNamedQuery("StatusEgreso.findByClave", StatusEgreso.class).setParameter("clave", clave)
                .getSingleResult();
    }

    public List<StatusEgreso> buscarActivosOInactivos(Boolean activo) {
        return em.createNamedQuery("StatusEgreso.findActivosOInactivos", StatusEgreso.class)
                .setParameter("activo", activo).getResultList();
    }
}
