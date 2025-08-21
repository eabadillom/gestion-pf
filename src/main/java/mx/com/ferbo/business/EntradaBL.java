package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.dao.TipoMovimientoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.DetallePartidaPK;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.Tarima;
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
	
	public static synchronized Partida crearPartida(Camara camara)
	throws InventarioException {
		Partida partida = null;
		DetallePartida detallePartida = null;
		TipoMovimiento tipoMovimiento = null;
		TipoMovimientoDAO tipoMovimientoDAO = null;
		EstadoInventario estadoInventario = null;
		EstadoInventarioDAO estadoInventarioDAO = null;
		
		if(camara == null)
			throw new InventarioException("No se ha indicado la cámara destino.");
		
		partida = new Partida();
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
		
		return partida;
	}
	
	public static synchronized UnidadDeProducto buscarUnidadProducto(UnidadDeProducto unidad)
	throws InventarioException {
		UnidadDeProductoDAO udpDAO = null;
		UnidadDeProducto udp = null;
		
		if(unidad == null)
			throw new InventarioException("No se ha indicado el producto o unidad de manejo.");
		
		if(unidad.getProductoCve() == null)
			throw new InventarioException("No se ha indicado el producto.");
		
		if(unidad.getUnidadDeManejoCve() == null)
			throw new InventarioException("No se ha indicado la unidad de manejo.");
		
		udpDAO = new UnidadDeProductoDAO();
		udp = udpDAO.buscarPorProductoUnidad(unidad.getProductoCve().getProductoCve(), unidad.getUnidadDeManejoCve().getUnidadDeManejoCve());
		
		if(udp == null) {
			log.info("La unidad de producto indicada no existe, se registrará una nueva: id producto = {}, id unidad = {}",
					unidad.getProductoCve().getProductoCve(), unidad.getUnidadDeManejoCve().getUnidadDeManejoCve());
			udpDAO.guardar(unidad);
			udp = unidad;
		}		
		
		return udp;
	}
	
	public static synchronized void agregarPartida(ConstanciaDeDeposito constancia, Partida partida)
	throws InventarioException {
		
		if(constancia == null)
			throw new InventarioException("No se ha indicado la constancia de depósito.");
		
		if(partida == null)
			throw new InventarioException("No se ha indicado la partida");
		
		constancia.getPartidaList().add(partida);
		partida.setFolio(constancia);
		
	}
	
	public static synchronized List<Tarima> crearTarimas(ConstanciaDeDeposito constancia, Integer numeroTarimas, Partida partida, List<Tarima> tarimas)
	throws InventarioException, CloneNotSupportedException {
		Tarima t = null;
		Partida p = null;
		UnidadDeProductoDAO udpDAO = null;
		
		if(numeroTarimas == null)
			throw new InventarioException("Debe indicar el número de tarimas.");
		
		if(numeroTarimas <= 0)
			throw new InventarioException("El número de tarimas indicado es incorrecto");
		
		if(partida == null)
			throw new InventarioException("No se indicó una partida");
		
		if(tarimas == null)
			tarimas = new ArrayList<Tarima>();
		
		Integer idProducto = partida.getUnidadDeProductoCve().getProductoCve().getProductoCve();
		Integer idUnidadManejo = partida.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve();
		
		udpDAO = new UnidadDeProductoDAO();
		
		UnidadDeProducto unidadDeProducto = udpDAO.buscarPorProductoUnidad(idProducto, idUnidadManejo);
		
		if(unidadDeProducto == null) {
			log.info("Agregando unidad de producto: {}");
			unidadDeProducto = partida.getUnidadDeProductoCve();
			udpDAO.guardar(unidadDeProducto);
			partida.setUnidadDeProductoCve(unidadDeProducto);
		}
		
		log.info("UnidaDeProducto: {}", partida.getUnidadDeProductoCve());
		
		for(int i = 0; i < numeroTarimas; i++) {
			
			p = partida.clone();
			
			t = new Tarima();
			t.setPartidas(new ArrayList<Partida>());
			t.getPartidas().add(p);
			constancia.getPartidaList().add(p);
			
			tarimas.add(t);
		}
		
		EntradaBL.nombrarTarimas(constancia.getFolioCliente(), tarimas);
		
		return tarimas;
	}
	
	public static synchronized void agregarATarima(ConstanciaDeDeposito constancia, Tarima tarima, Partida partida)
	throws InventarioException {
		if(constancia == null)
			throw new InventarioException("Debe indicar una entrada.");
		
		if(tarima == null)
			throw new InventarioException("Debe indicar una tarima.");
		
		if(partida == null)
			throw new InventarioException("Debe indicar una partida.");
		
		constancia.getPartidaList().add(partida);
		partida.setFolio(constancia);
		tarima.getPartidas().add(partida);
	}
	
	public static synchronized void nombrarTarimas(String folioCliente, List<Tarima> tarimas) {
		String nombreTarima = null;
		for(Tarima tarima : tarimas) {
			Integer index = tarimas.indexOf(tarima);
			nombreTarima = String.format("%s-%s", folioCliente, (index + 1));
			tarima.setNombre(nombreTarima);
		}
	}
	
	public static synchronized void eliminarTarima(ConstanciaDeDeposito constancia, List<Tarima> tarimas, Tarima tarima)
	throws InventarioException {
		
		if(constancia == null)
			throw new InventarioException("Debe indicar una entrada.");
		
		if(tarima == null)
			throw new InventarioException("Debe indicar una tarima.");
		
		if(tarimas == null)
			throw new InventarioException("Debe indicar una lista de tarimas");
		
		if(tarimas.size() <= 0)
			throw new InventarioException("La lista de tarimas está vacía.");
		
		for(Partida partida : tarima.getPartidas()) {
			constancia.getPartidaList().remove(partida);
		}
		
		tarimas.remove(tarima);
		
		EntradaBL.nombrarTarimas(constancia.getFolioCliente(), tarimas);
		
	}
	
	public static synchronized void eliminarProducto(ConstanciaDeDeposito constancia, Tarima tarima, Partida partida)
	throws InventarioException {
		if(constancia == null)
			throw new InventarioException("Debe indicar una entrdad.");
		
		if(tarima == null)
			throw new InventarioException("Debe indicar una tarima.");
		
		if(partida == null)
			throw new InventarioException("Debe indicar una partida.");
		
		partida.setFolio(null);
		constancia.getPartidaList().remove(partida);
		tarima.getPartidas().remove(partida);
	}
	
	public static void agregarServicio(ConstanciaDeDeposito constancia, PrecioServicio ps, BigDecimal cantidad)
	throws InventarioException {
		ConstanciaDepositoDetalle servicio = null;
		
		if(constancia == null) 
			throw new InventarioException("Debe proporcionar una constancia de depósito.");
		
		if(ps == null)
			throw new InventarioException("Debe indicar un servicio.");
		
		if(cantidad == null)
			throw new InventarioException("Debe indicar una cantidad.");
		
		if(cantidad.compareTo(BigDecimal.ZERO) <= 0)
			throw new InventarioException("La cantidad indicada es incorrecta.");
		
		servicio = new ConstanciaDepositoDetalle();
		servicio.setFolio(constancia);
		servicio.setServicioCantidad(cantidad);
		servicio.setServicioCve(ps.getServicio());
		
		constancia.getConstanciaDepositoDetalleList().add(servicio);
	}
	
	public static void agregarServicio(ConstanciaDeDeposito constancia, Servicio servicio, BigDecimal cantidad)
	throws InventarioException {
		ConstanciaDepositoDetalle constanciaServicio = null;
		
		if(constancia == null)
			throw new InventarioException("Debe proporcionar una constancia de depósito.");
		
		if(servicio == null)
			throw new InventarioException("Debe proporcionar un servicio.");
		
		if(cantidad == null)
			throw new InventarioException("Debe proporcionar una cantidad");
		
		if(cantidad.compareTo(BigDecimal.ZERO) <= 0)
			throw new InventarioException("La cantidad indicada es incorrecta.");
		
		constanciaServicio = new ConstanciaDepositoDetalle();
		constanciaServicio.setFolio(constancia);
		constanciaServicio.setServicioCantidad(cantidad);
		constanciaServicio.setServicioCve(servicio);
	}
	
	public static synchronized void eliminarServicio(ConstanciaDeDeposito constancia, ConstanciaDepositoDetalle servicio)
	throws InventarioException {
		if(constancia == null)
			throw new InventarioException("Debe proporcionar una constancia de depósito.");
		
		if(servicio == null)
			throw new InventarioException("Debe proporcionar un servicio.");
		
		servicio.setFolio(null);
		constancia.getConstanciaDepositoDetalleList().remove(servicio);
	}
	
	public static synchronized void guardar(ConstanciaDeDeposito constancia)
	throws InventarioException {
		String respuesta = null;
		
		ConstanciaDeDepositoDAO constanciaDAO = null;
		ConstanciaDeDeposito    existeConstancia = null;
		UnidadDeProductoDAO udpDAO = null;
		UnidadDeProducto unidadDeProducto = null;
		
		if(constancia == null)
			throw new InventarioException("Debe proporcionar una constancia de depósito.");
		
		if(constancia.getAvisoCve() == null)
			throw new InventarioException("Debe indicar un aviso.");
		
		if(constancia.getPartidaList() == null)
			throw new InventarioException("Debe proporcionar uno o más productos.");
		
		if(constancia.getPartidaList().size() <= 0)
			throw new InventarioException("Debe proporcionar uno o más productos.");
		
		if(constancia.getConstanciaDepositoDetalleList() == null)
			throw new InventarioException("Debe proporcionar uno o más servicios.");
		
		if(constancia.getConstanciaDepositoDetalleList().size() <= 0)
			throw new InventarioException("Debe proporcionar uno o más servicios.");
		
		constanciaDAO = new ConstanciaDeDepositoDAO();
		
		existeConstancia = constanciaDAO.buscarPorFolioCliente(constancia.getFolioCliente());
		
		if(existeConstancia != null)
			throw new InventarioException("La constancia de depósito ya se encuentra registrada.");
		
		udpDAO = new UnidadDeProductoDAO();
		
		for(Partida item : constancia.getPartidaList()) {
			unidadDeProducto = udpDAO
					.buscarPorProductoUnidad(item.getUnidadDeProductoCve().getProductoCve().getProductoCve(), item.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve());
			
			if(unidadDeProducto == null)
				udpDAO.guardar(item.getUnidadDeProductoCve());
			else
				item.setUnidadDeProductoCve(unidadDeProducto);
			item.setUnidadDeCobro(item.getUnidadDeProductoCve().getUnidadDeManejoCve());
		}
		
		respuesta = constanciaDAO.guardar(constancia);
		
		if(respuesta != null)
    		throw new InventarioException("Ocurrió un problema para guardar la constancia de depósito.");
	}
}
