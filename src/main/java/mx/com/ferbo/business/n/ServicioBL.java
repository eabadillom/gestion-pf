package mx.com.ferbo.business.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.ServicioDAO;
import mx.com.ferbo.model.Servicio;

@Named
@RequestScoped
public class ServicioBL {

    private static final Logger log = LogManager.getLogger(ServicioBL.class);

    @Inject
    private ServicioDAO servicioDAO;
    
    public List<Servicio> obtenerServicios() {
        List<Servicio> lista = servicioDAO.buscarTodos();

        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }
}
