package mx.com.ferbo.empresa.business;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.empresa.dao.EmisorCfdiDAO;
import mx.com.ferbo.model.EmisoresCFDIS;

@Named
@RequestScoped
public class EmisorCdfiBL {

    private static final Logger log = LogManager.getLogger(EmisorCdfiBL.class);

    @Inject
    private EmisorCfdiDAO emisorDAO;

    public List<EmisoresCFDIS> obtenerTodos() {
        try {
            return emisorDAO.obtenerTodos();
        } catch (SystemException ex) {
            throw ex;
        }
    }
}
