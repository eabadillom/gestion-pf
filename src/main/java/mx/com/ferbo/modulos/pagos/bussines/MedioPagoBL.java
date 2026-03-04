package mx.com.ferbo.modulos.pagos.bussines;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.modulos.pagos.dao.MedioPagoDAO;
import mx.com.ferbo.util.DAOException;

@Named
@RequestScoped
public class MedioPagoBL extends PagoBaseBL <MedioPago> {

	private static final Logger log = LogManager.getLogger(MedioPagoBL.class);

	@Inject
	private MedioPagoDAO dao;

	public MedioPagoBL() {
		setDao(dao);
	}

	@Override
	protected List<MedioPago> buscarVigentes(Date fecha) throws DAOException {
		return dao.buscarVigentes(fecha);
	}

	@Override
	protected String getNombreSingularEntidad() {
		return "medio de pago";
	}

	@Override
	protected String getNombrePluralEntidad() {
		return "medios de pago";
	}

	

}
