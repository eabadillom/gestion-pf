package mx.com.ferbo.modulos.pagos.bussines;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.modulos.pagos.dao.MetodoPagoDAO;
import mx.com.ferbo.util.DAOException;

@Named
@RequestScoped
public class MetodoPagoBL extends PagoBaseBL <MetodoPago>{

    private static final Logger log = LogManager.getLogger(MetodoPagoBL.class);

    @Inject
    private MetodoPagoDAO dao;

    public MetodoPagoBL (){
        setDao(dao);
    }

    @Override
    protected List<MetodoPago> buscarVigentes(Date fecha) throws DAOException {
        return dao.buscarVigentes(fecha);
    }

    @Override
    protected String getNombreSingularEntidad() {
        return "metodo de pago";
    }

    @Override
    protected String getNombrePluralEntidad() {
        return "metodos de pago";
    }

    
}
