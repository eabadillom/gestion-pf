package mx.com.ferbo.business.constancias;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.almacen.StatusConstanciaSalidaBL;
import mx.com.ferbo.dao.n.ConstanciaSalidaDAO;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ConstanciaSalidaBL {
	private static Logger log = LogManager.getLogger(ConstanciaSalidaBL.class);

	@Inject
	private ConstanciaSalidaDAO constanciaSalidaDAO;

	@Inject
	private StatusConstanciaSalidaBL statusBO;

	public ConstanciaSalida create() throws InventarioException {
		ConstanciaSalida constancia = null;
		
		constancia = new ConstanciaSalida.Builder()
				.detalleConstanciaSalidaList(new ArrayList<DetalleConstanciaSalida>())
				.constanciaSalidaServiciosList(new ArrayList<ConstanciaSalidaServicios>())
				.status(statusBO.nueva())
				.build();
		
		return constancia;
	}

	public String buscarPorFolioSalida(String folioSalida) {
		return constanciaSalidaDAO.buscarPorNumero(folioSalida);
	}

	public void guardar(ConstanciaSalida constanciaSalida) throws InventarioException {
		FacesUtils.requireNonNullWithReturn(constanciaSalida, "Debe proporcionar una constancia de salida");

		if (constanciaSalida.getStatus().getId() == 2) {
			throw new InventarioException("La constancia de salida ya está cancelada");
		}

		constanciaSalidaDAO.guardar(constanciaSalida);
	}

	public StatusConstanciaSalida statusNueva() {
		StatusConstanciaSalida status = null;
		try {
			status = statusBO.nueva();
		} catch (InventarioException e) {
			log.error("Status no encontrado.");
			status = null;
		}
		
		return status;
	}

}
