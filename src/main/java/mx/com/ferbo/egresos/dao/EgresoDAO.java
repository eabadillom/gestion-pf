package mx.com.ferbo.egresos.dao;

import java.time.LocalDateTime;
import java.util.List;

import mx.com.ferbo.config.persistencia.JpaDAO;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;

public class EgresoDAO extends JpaDAO<Egreso, Long> {

    public EgresoDAO() {
        super(Egreso.class);
    }

    public List<Egreso> buscarTodos() {
        return em.createNamedQuery("Egreso.findAll", Egreso.class).getResultList();
    }

    public List<Egreso> buscarPorRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        return em.createNamedQuery("Egreso.findByFechaBetween", Egreso.class).setParameter("inicio", inicio)
                .setParameter("fin", fin).getResultList();

    }

    public List<Egreso> buscarPorCategoria(CategoriaEgreso categoria) {
        return em.createNamedQuery("Egreso.findByCategoria", Egreso.class).setParameter("categoria", categoria)
                .getResultList();
    }

    public List<Egreso> buscarPorStatus(StatusEgreso status) {
        return em.createNamedQuery("Egreso.findByStatus", Egreso.class).setParameter("status", status)
                .getResultList();
    }

    public List<Egreso> buscarPorConcepto(String concepto) {
        return em.createNamedQuery("Egreso.searchByConcepto", Egreso.class).setParameter("concepto", concepto)
                .getResultList();
    }

    public List<Egreso> buscarPorFiltros(LocalDateTime inicio, LocalDateTime fin, CategoriaEgreso categoria, StatusEgreso status,
            String concepto) {
        return em.createNamedQuery("Egreso.findWithFilters", Egreso.class).setParameter("inicio", inicio).setParameter("fin", fin)
                .setParameter("categoria", categoria).setParameter("status", status)
                .setParameter("concepto", concepto).getResultList();
    }
}
