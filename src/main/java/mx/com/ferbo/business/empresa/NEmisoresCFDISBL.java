package mx.com.ferbo.business.empresa;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.empresa.NEmisoresCFDISDAO;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class NEmisoresCFDISBL extends BaseEmpresaBL <NEmisoresCFDIS> {

    private static final Logger log = LogManager.getLogger(NEmisoresCFDISBL.class);

    @Inject
    private NEmisoresCFDISDAO dao;

    @PostConstruct
    public void init() {
        super.setDao(dao);
    }
}
