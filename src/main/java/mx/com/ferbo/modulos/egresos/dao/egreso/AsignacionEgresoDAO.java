
package mx.com.ferbo.modulos.egresos.dao.egreso;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.EgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoAsignacionEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.AsignacionEgreso;

@Named
@ApplicationScoped
public class AsignacionEgresoDAO extends EgresoBaseDAO <AsignacionEgreso, TipoAsignacionEgreso> {
    
    private static final Logger log = LogManager.getLogger(AsignacionEgresoDAO.class);
    
    public AsignacionEgresoDAO(){
        super(AsignacionEgreso.class);
    }

    @Override
    protected Class<AsignacionEgreso> getEntityClass() {
        return AsignacionEgreso.class;
    }
}
