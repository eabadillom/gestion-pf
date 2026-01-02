package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.CategoriaEgreso;

@Named
@ApplicationScoped
public class CategoriaEgresoDAO extends BaseDAO<CategoriaEgreso, Integer> {

    private static final Logger log = LogManager.getLogger(CategoriaEgresoDAO.class);

    public CategoriaEgresoDAO(){
        super(CategoriaEgreso.class);
    }
}
