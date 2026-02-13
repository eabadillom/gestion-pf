package mx.com.ferbo.controller.egresos;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

@Named
@ApplicationScoped
public class EgresoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LogManager.getLogger(EgresoBean.class);
    
    public EgresoBean(){
    }
    
    @PostConstruct
    public void init(){
    }
    
    public void actualizaciones() {
        PrimeFaces.current().ajax().update("formConsulta:messages");
    }

}
