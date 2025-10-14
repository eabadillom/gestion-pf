package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Aviso;

@Named
@ApplicationScoped
public class AvisoDAO extends BaseDAO <Aviso, Integer> {

    private static Logger log = LogManager.getLogger(AvisoDAO.class);

    public AvisoDAO(){
        super(Aviso.class);
    }
}
