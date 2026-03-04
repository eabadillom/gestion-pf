package mx.com.ferbo.modulos.empresa.bussines;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.modulos.empresa.dao.EmisorCFDIDAO;
import mx.com.ferbo.modulos.empresa.model.EmisorCFDI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class EmisorCFDIBL extends EmpresaBaseBL <EmisorCFDI> {

    private static final Logger log = LogManager.getLogger(EmisorCFDIBL.class);

    @Inject
    private EmisorCFDIDAO dao;

    @PostConstruct
    public void init() {
        super.setDao(dao);
    }
}
