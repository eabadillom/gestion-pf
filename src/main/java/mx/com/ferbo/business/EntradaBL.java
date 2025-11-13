package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.ConstanciaDeDepositoDAO;
import mx.com.ferbo.dao.EstadoConstanciaDAO;
import mx.com.ferbo.dao.EstadoInventarioDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.dao.TipoMovimientoDAO;
import mx.com.ferbo.dao.UnidadDeProductoDAO;
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
	
	public static synchronized void actualizarFolio(Cliente cliente, Planta planta)
	throws InventarioException {
		String resultado = null;
		SerieConstanciaDAO serieDAO = null;
		SerieConstancia serie = null;
		
		serieDAO = new SerieConstanciaDAO();
		
		if(cliente == null)
			throw new InventarioException("Debe proporcionar un cliente");
		
		if(planta == null)
			throw new InventarioException("Debe proporcionar una planta");
		
		log.info("Actualizando Serie-Constancia...");
		serie = serieDAO.buscarPorClienteTipoSerieAndPlanta(cliente.getCteCve(), "I", planta.getPlantaCve())
				.orElseThrow(() -> new InventarioException("No es posible generar el folio de la constancia de depósito."));
		
		Integer consecutivo = serie.getNuSerie() + 1;
		
		serie.setNuSerie(consecutivo);
		log.info("Consecutivo: {}", consecutivo);
		
		resultado = serieDAO.actualizar(serie);
		
		if(resultado != null)
			throw new InventarioException("Ocurrió un problema con la actualización del folio.");
		
		log.info("La serie de la constancia de depósito se actualizó correctamente.");
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
        
        public static synchronized void agregarPartidaCompleta(ConstanciaDeDeposito constancia, BigDecimal numTarimas, Partida partida)
	throws InventarioException, CloneNotSupportedException {
                Partida p = null;
                UnidadDeProductoDAO udpDAO = null;
                
                if(numTarimas == null)
			throw new InventarioException("Debe indicar el número de tarimas.");
                
                if(numTarimas.compareTo(BigDecimal.ZERO) <= 0)
                        throw new InventarioException("El número de tarimas indicado es incorrecto");
                
		if(constancia == null)
			throw new InventarioException("No se ha indicado la constancia de depósito.");
		
		if(partida == null)
			throw new InventarioException("No se ha indicado la partida.");
                
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
                
                unidadDeProducto = partida.getUnidadDeProductoCve();
                
                p = partida.clone();
                
                p.setNoTarimas(numTarimas);
                p.setUnidadDeCobro(unidadDeProducto.getUnidadDeManejoCve());
                constancia.getPartidaList().add(p);
	}
        
        public static synchronized void editarPartidaCompleta(ConstanciaDeDeposito constancia, Partida partida) 
        throws InventarioException{
                
                if(constancia == null)
			throw new InventarioException("No se ha indicado la constancia de depósito.");
                
                if(partida == null)
			throw new InventarioException("Debe indicar una partida.");
                
                for(int i = 0; i < constancia.getPartidaList().size(); i++){
                    if (constancia.getPartidaList().get(i).equals(partida)) {
                        constancia.getPartidaList().set(i, partida);
                        break;
                    }
                }
        }
        
        public static synchronized void eliminarPartidaCompleta(ConstanciaDeDeposito constancia, Partida partida) 
        throws InventarioException{
                
                if(constancia == null)
			throw new InventarioException("No se ha indicado la constancia de depósito.");
                
                if(partida == null)
			throw new InventarioException("Debe indicar una partida.");
                
                log.info("Producto eliminado: {}", partida);
                constancia.getPartidaList().remove(partida);
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
			
			p.setTarima(t);
			p.setUnidadDeCobro(partida.getUnidadDeProductoCve().getUnidadDeManejoCve());
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
		partida.setTarima(tarima);
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
        
        public static synchronized Integer totalPartidasEliminadas(List<Tarima> tarimas, Tarima tarima)
        throws InventarioException {
                Integer totalEliminadas = 0;
                
                if(tarima == null)
			throw new InventarioException("Debe indicar una tarima.");
		
		if(tarimas == null)
			throw new InventarioException("Debe indicar una lista de tarimas");
		
		if(tarimas.size() <= 0)
			throw new InventarioException("La lista de tarimas está vacía.");
		
		for(Partida partida : tarima.getPartidas()) {
			totalEliminadas = totalEliminadas + 1;
		}
                
                return totalEliminadas;
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
        
        public static synchronized void eliminarPartidaEnListaTarimas(List<Tarima> tarimas, Partida partida)
        throws InventarioException {
            if(tarimas == null)
                        throw new InventarioException("Debe indicar una lista de tarimas");
            
            if(tarimas.size() <= 0)
			throw new InventarioException("La lista de tarimas está vacía.");
            
            if(partida == null)
			throw new InventarioException("Debe indicar una partida.");
            
            for (Tarima t : tarimas) {
                List<Partida> partidas = t.getPartidas();
                if (partidas == null || partidas.isEmpty()) continue;
                
                Iterator<Partida> it = partidas.iterator();
                while (it.hasNext()) {
                    Partida p = it.next();
                    if (p.equals(partida)) {
                        it.remove();
                        return;
                    }
                }
            }
        }
	
	public static synchronized void agregarServicio(ConstanciaDeDeposito constancia, PrecioServicio ps, BigDecimal cantidad)
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
		
		Optional<ConstanciaDepositoDetalle> isServicio = constancia.getConstanciaDepositoDetalleList().stream()
			.filter(item -> item.getServicioCve().equals(ps.getServicio()))
			.findFirst();
		
		if(isServicio.isPresent())
			throw new InventarioException("El servicio ya se encuentra registrado en la constancia");
		
		constancia.getConstanciaDepositoDetalleList().add(servicio);
	}
	
	public static synchronized void agregarServicio(ConstanciaDeDeposito constancia, Servicio servicio, BigDecimal cantidad)
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
		
		constancia.getConstanciaDepositoDetalleList().add(constanciaServicio);
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
		
		constanciaDAO = new ConstanciaDeDepositoDAO();
		log.info("Verificando si ya existe la constancia de deposito {}", constancia.getFolioCliente());
		existeConstancia = constanciaDAO.buscarPorFolioCliente(constancia.getFolioCliente());
		
		if(existeConstancia != null)
			throw new InventarioException("La constancia de depósito ya se encuentra registrada.");
		
		log.info("La constancia de depósito {} se encuentra disponible, procediendo con el guardado...");
		udpDAO = new UnidadDeProductoDAO();
		
		for(Partida partida : constancia.getPartidaList()) {
                        Integer idProducto = partida.getUnidadDeProductoCve().getProductoCve().getProductoCve();
			Integer idUnidad = partida.getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve();
			
			unidadDeProducto = udpDAO.buscarPorProductoUnidad(idProducto, idUnidad);
			
			if(unidadDeProducto == null) {
				unidadDeProducto = partida.getUnidadDeProductoCve();
				udpDAO.guardar(unidadDeProducto);
			}
			
			partida.setUnidadDeProductoCve(unidadDeProducto);
			partida.setFolio(constancia);
			partida.setCantidadDeCobro(partida.getPesoTotal());
			partida.setUnidadDeCobro(partida.getUnidadDeProductoCve().getUnidadDeManejoCve());
			partida.getDetallePartidaList().forEach(detallePartida -> {
				detallePartida.setCantidadUManejo(partida.getCantidadTotal());
				detallePartida.setCantidadUMedida(partida.getPesoTotal());
			});
		}
		
		for(ConstanciaDepositoDetalle cDetalle : constancia.getConstanciaDepositoDetalleList()) {
			cDetalle.setFolio(constancia);
		}
		
		respuesta = constanciaDAO.guardar(constancia);
		
		if(respuesta != null)
    		throw new InventarioException("Ocurrió un problema para guardar la constancia de depósito.");
		
		log.info("La constancia de depósito {} se guardó correctamente.", constancia.getFolioCliente());
	}
}
