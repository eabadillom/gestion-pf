package mx.com.ferbo.pagos.businesslogic;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.pagos.dao.MedioPagoDAO;

@Named
@RequestScoped
public class MedioPagoBL {

	private static final Logger log = LogManager.getLogger(MedioPago.class);

	@Inject
	private MedioPagoDAO medioPagoDAO;

	public List<MedioPago> obtenerMediosPago() throws SystemException {
		log.info("Inicia proceso para obtener todos los medios de pago");
		Date fecha = new Date();
		try {
			return medioPagoDAO.buscarVigentes(fecha);
		} catch (SystemException ex) {
			log.error("Error al obtener los medios de pago vigentes hasta la fecha: " + fecha, ex);
			throw ex;
		}
	}

}
