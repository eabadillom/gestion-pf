package mx.com.ferbo.business.n;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoMailDAO;
import mx.com.ferbo.dao.n.TipoTelefonoDAO;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.TipoTelefono;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class MedioContactoBL {

    private static final Logger log = LogManager.getLogger(MedioContactoBL.class);

    @Inject
    private TipoTelefonoDAO tipoTelefonoDAO;

    @Inject
    private TipoMailDAO tipoMailDAO;

    public List<TipoMail> obtenerTiposMail() throws InventarioException {

        try {
            return tipoMailDAO.buscarTodos();
        } catch (DAOException ex) {
            log.error("Error al obtener los tipos de email", ex);
            throw new InventarioException("Ocurrió un error al obtener los tipos de email", ex);
        }
    }

    public List<TipoTelefono> obtenerTiposTelefono() throws InventarioException {
        try {
            return tipoTelefonoDAO.buscarTodos();
        } catch (DAOException ex) {
            log.error("Error al obtener los tipos de teléfono", ex);
            throw new InventarioException("Ocurrió un error al obtener los tipos de teléfono", ex);
        }
    }

    public MedioCnt nuevoMedio() {
        MedioCnt medio = new MedioCnt();
        return medio;
    }

    public void seleccionarMedioContacto(MedioCnt medioCnt) throws InventarioException {

        switch (medioCnt.getTpMedio()) {

            case "m":
                medioCnt.setIdMail(new Mail());
                break;

            case "t":
                medioCnt.setIdTelefono(new Telefono());
                break;

            default:
                throw new InventarioException("Tipo de medio de contacto no valido");
        }
    }
}
