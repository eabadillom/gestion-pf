package mx.com.ferbo.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.dao.TipoMovimientoDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.TipoMovimiento;
import mx.com.ferbo.model.UnidadDeProducto;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;

public class EntradaBL {
	private static Logger log = LogManager.getLogger(EntradaBL.class);
	
	public static synchronized ConstanciaDeDeposito crear() {
		ConstanciaDeDeposito constancia = null;
		EstadoConstancia status = null;
		EstadoConstanciaDAO statusDAO = null;
		Date fecha = null;
		
		try {
			fecha = new Date();
			DateUtil.setTime(fecha, 0, 0, 0, 0);
			
			statusDAO = new EstadoConstanciaDAO();
			status = statusDAO.buscarPorId(1);
			
			constancia = new ConstanciaDeDeposito();
			constancia.setStatus(status);
			constancia.setPartidaList(new ArrayList<Partida>());
			constancia.setConstanciaDepositoDetalleList(new ArrayList<ConstanciaDepositoDetalle>());
			constancia.setFechaIngreso(fecha);
			
		} catch(Exception ex) {
			log.error("Problema para crear la constancia de depósito...", ex);
		}
		
		return constancia;
	}
	
	public static synchronized String crearFolio(ConstanciaDeDeposito constancia, Planta planta)
	throws InventarioException {
		String folio = null;
		
		SerieConstanciaDAO serieDAO = null;
		SerieConstancia serie = null;
		serieDAO = new SerieConstanciaDAO();
		
		if(constancia.getCteCve() == null)
			throw new InventarioException("No se ha proporcionado el cliente para la creación del folio");
		
		if(planta == null)
			throw new InventarioException("No se ha proporcionado la planta para la creación del folio");
		serie = serieDAO.buscarPorClienteTipoSerieAndPlanta(constancia.getCteCve().getCteCve(), "I", planta.getPlantaCve())
				.orElseThrow(() -> new InventarioException("No es posible generar el folio de la constancia de depósito."));
		
		folio = String.format("I%s%s%d",
				planta.getPlantaSufijo(), constancia.getCteCve().getCodUnico(), serie.getNuSerie());
		
		return folio;
	}
	
	public static synchronized Partida crearPartida(ConstanciaDeDeposito constancia, Camara camara)
	throws InventarioException {
		Partida partida = null;
		DetallePartida detallePartida = null;
		TipoMovimiento tipoMovimiento = null;
		TipoMovimientoDAO tipoMovimientoDAO = null;
		EstadoInventario estadoInventario = null;
		EstadoInventarioDAO estadoInventarioDAO = null;
		
		if(constancia == null)
			throw new InventarioException("Constancia de depósito vacía.");
		
		partida = new Partida();
		partida.setFolio(constancia);
		partida.setCamaraCve(camara);
		partida.setUnidadDeProductoCve(new UnidadDeProducto());
		partida.setDetallePartidaList(new ArrayList<DetallePartida>());
		
		tipoMovimientoDAO = new TipoMovimientoDAO();
		tipoMovimiento = tipoMovimientoDAO.buscarPorId(1);
		
		estadoInventarioDAO = new EstadoInventarioDAO();
		estadoInventario = estadoInventarioDAO.buscarPorId(1);
		
		detallePartida = new DetallePartida(new DetallePartidaPK(1, partida));
		detallePartida.setTipoMovCve(tipoMovimiento);
		detallePartida.setEdoInvCve(estadoInventario);
		
		partida.getDetallePartidaList().add(detallePartida);
		constancia.getPartidaList().add(partida);
		
		return partida;
	}
	
	public static synchronized List<Producto> productosPorCliente(Cliente cliente) {
		List<Producto> productos = null;
		List<ProductoPorCliente> productosPorCliente = null;
		ProductoClienteDAO ppcDAO = null;
		
		ppcDAO = new ProductoClienteDAO();
		productosPorCliente = ppcDAO.buscarPorCteCve(cliente.getCteCve());
		
		productos = productosPorCliente.stream()
				.map(ProductoPorCliente::getProductoCve)
				.collect(Collectors.toList())
				;
		
		return productos;
	}

}
