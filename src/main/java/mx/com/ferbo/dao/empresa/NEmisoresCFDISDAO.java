package mx.com.ferbo.dao.empresa;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ApplicationScoped
public class NEmisoresCFDISDAO extends EmpresaBaseDAO<NEmisoresCFDIS> {

    private static final Logger log = LogManager.getLogger(NEmisoresCFDISDAO.class);

    public NEmisoresCFDISDAO() {
        super(NEmisoresCFDIS.class);
    }

    @Override
    protected Class<NEmisoresCFDIS> getEntityClass() {
        return NEmisoresCFDIS.class;
    }

}
