package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.InventarioException;

@Named
@ApplicationScoped
public class PrecioServicioDAO extends BaseDAO<PrecioServicio, Integer> {

    public PrecioServicioDAO() {
        super(PrecioServicio.class);
    }

    public List<PrecioServicio> buscarPorCliente(Integer idCliente) throws InventarioException {
        EntityManager em = super.getEntityManager();
        List<PrecioServicio> precios = new ArrayList<>();

        try {
            precios = em
                    .createNamedQuery("PrecioServicio.findByIdClienteConRelaciones", PrecioServicio.class)
                    .setParameter("idCliente", idCliente)
                    .getResultList();
        } catch (Exception ex) {
            throw new InventarioException("No existen datos relacionados con el identificador " + idCliente);
        } finally {
            super.close(em);
        }
        return precios;
    }

}
