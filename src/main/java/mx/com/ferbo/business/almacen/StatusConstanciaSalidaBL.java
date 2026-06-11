package mx.com.ferbo.business.almacen;

import javax.inject.Inject;

import mx.com.ferbo.dao.n.StatusConstanciaSalidaDAO;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.util.InventarioException;

public class StatusConstanciaSalidaBL {
	
	@Inject
	private StatusConstanciaSalidaDAO statusDAO;
	
	private StatusConstanciaSalida nueva;
	private StatusConstanciaSalida cancelada;
	
	public StatusConstanciaSalida nueva() throws InventarioException {
		if(nueva == null)
			nueva = statusDAO.buscarPorId(1)
			.orElseThrow(() -> new InventarioException("Status no encontrado."));
		return nueva;
	}
	
	public StatusConstanciaSalida cancelada() throws InventarioException {
		if(cancelada == null)
			cancelada = statusDAO.buscarPorId(2)
			.orElseThrow(() -> new InventarioException("Status no encontrado."));
		return cancelada;
	}

}
