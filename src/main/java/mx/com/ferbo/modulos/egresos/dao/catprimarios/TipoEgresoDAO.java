package mx.com.ferbo.modulos.egresos.dao.catprimarios;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.CatEgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catprimarios.TipoEgreso;

@Named
@ApplicationScoped
public class TipoEgresoDAO extends CatEgresoBaseDAO <TipoEgreso> {

    private static final Logger log = LogManager.getLogger(TipoEgresoDAO.class);

    public TipoEgresoDAO(){
        super(TipoEgreso.class);
    }

    @Override
    protected Class<TipoEgreso> getEntityClass() {
        return TipoEgreso.class;
    }

}
