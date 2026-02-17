
package mx.com.ferbo.dao.egresos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.model.egresos.CargoEgreso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class CargoDAO extends EgresoBaseDAO{
    
    private static final Logger log = LogManager.getLogger(CargoDAO.class);
    
    public CargoDAO(){
        super(CargoEgreso.class);
    }

    @Override
    protected Class getEntityClass() {
        return CargoEgreso.class;
    }
    
    
}
