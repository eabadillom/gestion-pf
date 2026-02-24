
package mx.com.ferbo.dao.egresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.model.egresos.CargoEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class CargoEgresoDAO extends EgresoBaseDAO <CargoEgreso>{
    
    private static final Logger log = LogManager.getLogger(CargoEgresoDAO.class);
    
    public CargoEgresoDAO(){
        super(CargoEgreso.class);
    }

    @Override
    protected Class getEntityClass() {
        return CargoEgreso.class;
    }
    
    
}
