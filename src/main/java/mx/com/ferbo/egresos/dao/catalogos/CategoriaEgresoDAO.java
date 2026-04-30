package mx.com.ferbo.egresos.dao.catalogos;

import java.util.List;

import mx.com.ferbo.config.persistencia.JpaDAO;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;

public class CategoriaEgresoDAO extends JpaDAO<CategoriaEgreso, Long> {

    public CategoriaEgresoDAO() {
        super(CategoriaEgreso.class);
    }

    public CategoriaEgreso buscarPorClave(String clave) {
        return em.createNamedQuery("CategoriaEgreso.findByClave", CategoriaEgreso.class)
                .setParameter("clave", clave).getSingleResult();
    }

    public List<CategoriaEgreso> buscarActivosOInactivos(Boolean activo) {
        return em.createNamedQuery("CategoriaEgreso.findActivosOInactivos", CategoriaEgreso.class)
                .setParameter("activo", activo).getResultList();
    }

}
