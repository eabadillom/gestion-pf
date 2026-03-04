package mx.com.ferbo.modulos.empresa.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.modulos.empresa.model.EmisorCFDI;

@Named
@ApplicationScoped
public class EmisorCFDIDAO extends EmpresaBaseDAO<EmisorCFDI> {

    private static final Logger log = LogManager.getLogger(EmisorCFDIDAO.class);

    public EmisorCFDIDAO() {
        super(EmisorCFDI.class);
    }

    @Override
    protected Class<EmisorCFDI> getEntityClass() {
        return EmisorCFDI.class;
    }

}
