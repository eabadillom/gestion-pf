package mx.com.ferbo.business.complemento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.ComplementoPagoBL;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.n.ComplementoPagoDAO;
import mx.com.ferbo.dao.n.SerieComplementoPagoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.SerieComplementoPago;
import mx.com.ferbo.ui.DatosPago;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

public class ComplementoBL {
	private Logger log = LogManager.getLogger(ComplementoPagoBL.class);

	private PagoDAO pagoDAO;
	private ComplementoPagoDAO complementoPagoDAO;
	private SerieComplementoPagoDAO serieComplementoPagoDAO;

	public ComplementoBL() {
		this.pagoDAO = new PagoDAO();
		this.complementoPagoDAO = new ComplementoPagoDAO();
		this.serieComplementoPagoDAO = new SerieComplementoPagoDAO();
	}
	
	public ComplementoPago crear() {
		ComplementoPago complemento = new ComplementoPago();
		
		complemento.setEmisor(new EmisoresCFDIS());
		complemento.setReceptor(new Cliente());
		complemento.setListPagos(new ArrayList<Pago>());
		
		return complemento;
	}
	
	public ComplementoPago cargar(Integer id)
	throws InventarioException {
		ComplementoPago complemento = null;
		complemento = complementoPagoDAO.cargar(id).orElseThrow(() -> new InventarioException("No se encontró información del complemento de pago."));
		return complemento;
	}

	public List<ComplementoPago> buscarPor(Date fechaInicio, Date fechaFin) {
    	List<ComplementoPago> complementos = null;
    	
    	try {
			complementos = complementoPagoDAO.buscarPorPeriodoRegistro(fechaInicio, fechaFin);
		} catch (DAOException e) {
			complementos = new ArrayList<ComplementoPago>();
		}
    	
    	return complementos;
    }
	
	public String obtenerCP(Cliente cliente) {
		String cp = null;

		List<ClienteDomicilios> listDomiciliosCliente = cliente.getClienteDomiciliosList();
		for (ClienteDomicilios aux : listDomiciliosCliente) {
			if (aux.getDomicilios().getDomicilioTipoCve().getDomicilioTipoCve() == 1) {
				cp = aux.getDomicilios().getAsentamiento().getCp();
			}
		}

		return cp;
	}

	public DatosPago obtenerSaldos(Pago pago) {
		DatosPago datosPago = new DatosPago();
		Factura factura = pago.getFactura();
		BigDecimal saldoActual = factura.getTotal();
		BigDecimal montoAnterior = BigDecimal.ZERO;
		BigDecimal saldoRestante = BigDecimal.ZERO;
		List<Pago> listaPagosFactura = pagoDAO.buscaPorFactura(factura);
		for (Pago pagoFactura : listaPagosFactura) {
			montoAnterior = saldoActual;
			saldoRestante = montoAnterior.subtract(pagoFactura.getMonto());

			if (pagoFactura.equals(pago)) {
				datosPago.setSaldoAnterior(montoAnterior);
				datosPago.setMonto(pagoFactura.getMonto());
				datosPago.setSaldoRestante(saldoRestante);
				break;
			}
			saldoActual = saldoRestante;
		}

		return datosPago;
	}

	public ComplementoPago obtenerComplementoPorPago(Integer idComplementoPago)
			throws InventarioException, DAOException {
		if (idComplementoPago == null)
			throw new InventarioException("Debe seleccionar un emisor para el complemento de pago.");

		return complementoPagoDAO.buscarPorId(idComplementoPago)
				.orElseThrow(() -> new InventarioException("Comlemento de pago no entrontrado."));
	}

	public void guardarComplementoPago(ComplementoPago complemento)
			throws InventarioException {
		if(complemento == null)
			throw new InventarioException("La información del complemento de pago es incorrecta.");
		if(complemento.getFormaPago() == null || "".equalsIgnoreCase(complemento.getFormaPago()))
			throw new InventarioException("Debe indicar la forma de pago del complmeneto.");
		if(complemento.getEmisor() == null)
			throw new InventarioException("Debe indicar un emisor para el complemento.");
		if(complemento.getReceptor() == null)
			throw new InventarioException("Debe indicar un receptor para el complemento.");
		if(complemento.getSerie() == null || "".equalsIgnoreCase(complemento.getSerie().trim()))
			throw new InventarioException("Debe indicar la serie del complemento de pago");
		if(complemento.getNumero() == null || "".equalsIgnoreCase(complemento.getNumero().trim()))
			throw new InventarioException("Debe indicar el número de folio del complemento de pago");
		if(complemento.getRegistro() == null)
			throw new InventarioException("Debe indicar la fecha de registro del complemento de pago");
		
		complementoPagoDAO.guardar(complemento);
	}

	public ComplementoPago obtenerPorFolioSerie(String numero, String serie) throws DAOException {
		return complementoPagoDAO.buscarPorFolioSerie(numero, serie);
	}

	public SerieComplementoPago obtenerSeriePorEmisor(Integer idEmisor) throws InventarioException, DAOException {
		if (idEmisor == null)
			throw new InventarioException("Debe seleccionar un emisor para el complemento de pago.");

		return serieComplementoPagoDAO.buscarPorEmisor(idEmisor);
	}

	public List<SerieComplementoPago> obtenerSerieComplemento(Integer idEmisor)
			throws InventarioException, DAOException {
		if (idEmisor == null)
			throw new InventarioException("Debe seleccionar un emisor para el complemento de pago.");

		return serieComplementoPagoDAO.buscarSeriesPorEmisor(idEmisor);
	}

	public void actualizarSerieComplemento(SerieComplementoPago serieComplemento)
			throws InventarioException, CloneNotSupportedException {
		if (serieComplemento == null)
			throw new InventarioException("Debe seleccionar un emisor para el complemento de pago.");

		SerieComplementoPago serie = serieComplemento.clone();

		Integer numero = Integer.valueOf(serie.getNumero());
		numero = numero + 1;
		serie.setNumero(String.valueOf(numero));
		serieComplementoPagoDAO.actualizar(serie);
		log.info("Serie complemento de pago actualizado");
	}

}
