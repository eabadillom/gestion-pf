package mx.com.ferbo.dao.n.catalogos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.n.catalogos.TipoCargo;

@Named
@ApplicationScoped
public class TipoCargoDAO extends BaseDAO<TipoCargo, Integer>{

    private static final Logger log = LogManager.getLogger(TipoCargoDAO.class);

    public TipoCargoDAO(){
        super(TipoCargo.class);
    }
}
