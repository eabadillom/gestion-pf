package mx.com.ferbo.business.n;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.model.Planta;

@Named
@RequestScoped
public class PlantaBL {

    private static final Logger log = LogManager.getLogger(PlantaBL.class);

    @Inject
    private PlantaDAO plantaDAO;
    
    public List<Planta> obtenerPlantas(Boolean isFullInfo) {
        List<Planta> list = plantaDAO.buscarTodos(isFullInfo);

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

}
