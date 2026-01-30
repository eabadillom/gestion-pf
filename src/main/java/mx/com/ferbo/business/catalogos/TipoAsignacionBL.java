package mx.com.ferbo.business.catalogos;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.TipoAsignacionDAO;
import mx.com.ferbo.model.n.catalogos.TipoAsignacion;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class TipoAsignacionBL extends BaseCatalogosBL<TipoAsignacion> {

    private static final Logger log = LogManager.getLogger(TipoAsignacionBL.class);

    @Inject
    private TipoAsignacionDAO dao;

    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(TipoAsignacion model) throws InventarioException {
        // Metodo vacío porque no hay más validaciones
    }
}
