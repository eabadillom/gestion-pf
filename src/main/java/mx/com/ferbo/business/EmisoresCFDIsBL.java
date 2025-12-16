package mx.com.ferbo.business;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.EmisoresCFDISDAO;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class EmisoresCFDIsBL {

    private static final Logger log = LogManager.getLogger(EmisoresCFDIsBL.class);

    @Inject
    private EmisoresCFDISDAO emisoresCFDISDAO;

    public List<EmisoresCFDIS> obtenerEmisoresCFDIs() throws InventarioException {
        try {
            return emisoresCFDISDAO.findAll();
        } catch (Exception ex) {
            log.warn("Error al buscar todos los emisores de CFDIs. {}", ex);
            throw new InventarioException("Hubo un problema al buscar todos los emisores de CFDIs.");
        }
    }
}
