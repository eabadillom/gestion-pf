package mx.com.ferbo.business.clientes;

import java.util.List;
import java.util.stream.Collectors;

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

	public void cargaInfoCliente(Cliente cliente, List<UsoCfdi> lstUsoCfdi, List<RegimenFiscal> lstRegimenFiscal) {

		log.info("Cargando información del cliente: " + cliente);

		lstRegimenFiscal = regimenFiscalDAO.buscarPorTipoPersona(cliente.getTipoPersona());
		lstUsoCfdi = usoCfdiDAO.buscarUsoCfdiPorTipoPersona(cliente.getTipoPersona());


		if (cliente.getRegimenFiscal() != null) {
			throw new InventarioException("El cliente no tiene ningun regimen fiscal");
		}

		if (cliente.getUsoCfdi() != null) {
			throw new InventarioException("El cliente no tiene ningun uso de CFDI");
		}

		if (cliente.getMetodoPago() != null) {
			throw new InventarioException("El cliente no tiene ningun metodo de pago");
		}

		this.idMedioPagoSelected = null;
		if (this.clienteSelected.getFormaPago() != null) {

			final String fp = this.clienteSelected.getFormaPago();

			List<MedioPago> lst = this.lstMedioPago.stream().filter(c -> fp.equals(c.getFormaPago()))
					.collect(Collectors.toList());
			if (lst != null && lst.size() > 0) {
				this.idMedioPagoSelected = lst.get(0).getMpId();
			}
		}
	}

}
