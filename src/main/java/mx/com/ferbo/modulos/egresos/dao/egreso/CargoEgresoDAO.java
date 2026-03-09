
package mx.com.ferbo.modulos.egresos.dao.egreso;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.egresos.dao.EgresoBaseDAO;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusCargoEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.CargoEgreso;

@Named
@ApplicationScoped
public class CargoEgresoDAO extends EgresoBaseDAO <CargoEgreso, StatusCargoEgreso>{
    
    private static final Logger log = LogManager.getLogger(CargoEgresoDAO.class);
    
    public CargoEgresoDAO(){
        super(CargoEgreso.class);
    }

    @Override
    protected Class<CargoEgreso> getEntityClass() {
        return CargoEgreso.class;
    }
    
    
}
