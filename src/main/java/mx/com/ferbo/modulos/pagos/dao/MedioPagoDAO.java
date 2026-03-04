package mx.com.ferbo.modulos.pagos.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.MedioPago;

@Named
@ApplicationScoped
public class MedioPagoDAO extends PagoBaseDAO <MedioPago> {

	private static Logger log = LogManager.getLogger(MedioPagoDAO.class);

	public MedioPagoDAO() {
		super(MedioPago.class);
	}

	@Override
	protected Class<MedioPago> getEntityClass() {
		return MedioPago.class;
	}

	
}
