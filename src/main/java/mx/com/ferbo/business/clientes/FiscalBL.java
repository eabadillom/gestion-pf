package mx.com.ferbo.business.clientes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.MedioPagoDAO;
import mx.com.ferbo.dao.n.MetodoPagoDAO;
import mx.com.ferbo.dao.n.RegimenFiscalDAO;
import mx.com.ferbo.dao.n.UsoCfdiDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.ClienteUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class FiscalBL {

	private static Logger log = LogManager.getLogger(FiscalBL.class);

	@Inject
	private MedioPagoDAO medioPagoDAO;

	@Inject
	private MetodoPagoDAO metodoPagoDAO;

	@Inject
	private RegimenFiscalDAO regimenFiscalDAO;

	@Inject
	private UsoCfdiDAO usoCfdiDAO;

	public List<UsoCfdi> obtenerUsoCfdis(Cliente cliente) throws InventarioException {

		requireNonNull(cliente, "El cliente no puede estar vacío");

		requireNonNull(cliente.getTipoPersona(), "El tipo de persona del cliente esta vacío");

		return usoCfdiDAO.buscarUsoCfdiPorTipoPersona(cliente.getTipoPersona());

	}

	public List<RegimenFiscal> obtenerRegimenesFiscales(Cliente cliente) throws InventarioException {
		requireNonNull(cliente, "El cliente no puede estar vacío");

		requireNonNull(cliente.getTipoPersona(), "El tipo de persona del cliente esta vacío");

		return regimenFiscalDAO.buscarPorTipoPersona(cliente.getTipoPersona());
	}

	public void validarInfoFiscal(Cliente cliente) throws InventarioException {

		log.info("Validando información del cliente: " + cliente);

		if (cliente.getRegimenFiscal() == null) {
			cliente.setRegimenFiscal(new RegimenFiscal());
		}

		if (cliente.getUsoCfdi() == null) {
			cliente.setUsoCfdi(new UsoCfdi());
		}

		if (cliente.getMetodoPago() == null) {
			cliente.setMetodoPago(new MetodoPago());
		}

		if (cliente.getFormaPago() == null) {
			cliente.setFormaPago(new String());
		}

	}

	public void validarCodigoUnico(Cliente cliente1, Cliente cliente2) throws InventarioException {
		if (cliente2 != null) {
			if (cliente1 == null) {
				throw new InventarioException("El cliente1 está vacío");
			}

			if (cliente1.equals(cliente2)) {

				throw new InventarioException("El código único " + cliente1.getCodUnico()
						+ " ya está registrado para el cliente " + cliente1.getNombre());
			}
		}
	}

	public void validarRFC(Cliente cliente) throws InventarioException {
		if (ClienteUtil.validarRFC(cliente.getTipoPersona(), cliente.getCteRfc()) == false) {
			throw new InventarioException("El RFC es incorrecto");
		}

		String codigoUnico = cliente.getCteRfc();
		if ("F".equalsIgnoreCase(cliente.getTipoPersona())) {
			codigoUnico = codigoUnico.substring(0, 4);
		} else if ("M".equalsIgnoreCase(cliente.getTipoPersona())) {
			codigoUnico = codigoUnico.substring(0, 3);
		}

		if (cliente.getCteCve() == null &&
				(ClienteUtil.RFC_GENERICO_NACIONAL.equalsIgnoreCase(cliente.getCteRfc()) == false
						|| ClienteUtil.RFC_GENERICO_EXTRANJERO
								.equalsIgnoreCase(cliente.getCteRfc()) == false)) {
			cliente.setCodUnico(codigoUnico);
		}
	}

	public List<MedioPago> obtenerMediosPago() {
		List<MedioPago> lista =   medioPagoDAO.buscarVigentes(new Date());
	
		if (lista == null){
			return new ArrayList<>();
		}

		return lista;
	}

	public List<MetodoPago> obtenerMetodosPago() {

		List<MetodoPago> lista = metodoPagoDAO.buscarVigentes(new Date()); 

		if (lista == null) {
			return new ArrayList<>();
		}

		return lista;
	}

	private <T> T requireNonNull(T obj, String mensaje) throws InventarioException {
		if (obj == null) {
			throw new InventarioException(mensaje);
		}
		return obj;
	}

}
