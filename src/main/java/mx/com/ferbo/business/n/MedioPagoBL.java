package mx.com.ferbo.business.n;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.MedioPagoDAO;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class MedioPagoBL {

	private static final Logger log = LogManager.getLogger(MedioPago.class);

	@Inject
	private MedioPagoDAO medioPagoDAO;

	public List<MedioPago> obtenerMediosPago() throws InventarioException {
            log.info("Inicia proceso para obtener todos los medios de pago");
		Date fecha = new Date();
		try {

			return medioPagoDAO.buscarVigentes(fecha);
		} catch (DAOException ex) {
			log.error("Error al obtener los medios de pago vigentes hasta la fecha: " + fecha, ex);
			throw new InventarioException(
					"Ocurrio un problema al obtener los medios de pago vigentes hasta la fecha: " + fecha,
					ex);
		}
	}

}
