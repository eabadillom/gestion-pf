package mx.com.ferbo.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;

public class ClienteBL extends Thread {
	private static Logger log = LogManager.getLogger(ClienteBL.class);
	
	private Usuario usuario = null;
	private List<Cliente> clientesTodos = null;
	private List<Cliente> clientesActivos = null;
	private ClienteDAO clienteDAO = null;
	private ParametroDAO parametroDAO = null;
	private Parametro parametro = null;
	private HttpSession session = null;
	
	public ClienteBL(Usuario usuario, List<Cliente> clientesTodos, List<Cliente> clientesActivos, HttpSession session) {
		this.usuario = usuario;
		this.clientesTodos = clientesTodos;
		this.clientesActivos = clientesActivos;
		this.clienteDAO = new ClienteDAO();
		this.parametroDAO = new ParametroDAO();
		this.session = session;
	}
	
	@Override
	public void run() {
		this.clientesTodos = clienteDAO.buscarTodos();
		log.info("Total de clientes cargados en memoria: {}", clientesTodos.size());
		parametro = this.parametroDAO.buscarPorNombre("CFGPXC");
		
		if(parametro == null) {
			this.doNoConfig();
		} else {
			this.doConfig();
		}
		log.info("Total de clientes activos: {}", clientesActivos.size());
		this.session.setAttribute("clientesTodosList", this.clientesTodos);
		this.session.setAttribute("clientesActivosList", this.clientesActivos);
	}
	
	private void doNoConfig() {
		this.clientesActivos = clientesTodos
				.stream().filter( c -> c.getHabilitado() == true )
				.collect(Collectors.toList());
		
		if(this.clientesActivos == null)
			this.clientesActivos = new ArrayList<Cliente>();
	}
	
	private void doConfig() {
		String configuracion = null;
		String valores[] = null;
		String sIdClienteRestringido = null;
		String sIdPlantaRestringida = null;
		Integer idPlantaRestringida = null;
		
		Cliente cliente = null;
		
		try {
			if(clientesActivos == null)
				clientesActivos = new ArrayList<Cliente>();
			
			configuracion = parametro.getValor();
			if(configuracion == null)
				throw new InventarioException("El valor de configuración de restricción clientes por planta es incorrecto: " + configuracion);
			
			valores = configuracion.split("\\|");
			sIdPlantaRestringida = valores[0];
			sIdClienteRestringido = valores[1];
			
			idPlantaRestringida = Integer.parseInt(sIdPlantaRestringida);
			final Integer idClienteRestringido = Integer.parseInt(sIdClienteRestringido);
			
			if(idPlantaRestringida == this.usuario.getIdPlanta()) {
				cliente = this.clientesTodos.stream()
						.filter(c -> c.getCteCve().equals(idClienteRestringido))
						.collect(Collectors.toList()).get(0);
				
				clientesActivos.add(cliente);
				log.info("Aplicando regla para restricción de clientes (cliente unico): {}", cliente.getNombre());
			} else {
				this.doNoConfig();
			}
			
			log.info("Planta del usuario {}: {}", usuario.getUsuario(), usuario.getIdPlanta());
		} catch(Exception ex) {
			log.error("Problema con la configuración de restricción de clientes por planta...", ex);
		}
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
	
	public static synchronized List<PrecioServicio> getServicios(Cliente cliente, Aviso aviso) {
		List<PrecioServicio> precios = null;
		PrecioServicioDAO psDAO = null;
		
		try {
			psDAO = new PrecioServicioDAO();
			precios = psDAO.buscarPorAviso(aviso, cliente);
		} catch(Exception ex) {
			log.error("Problema para obtener los servicios del aviso {}...\n{}", aviso, ex);
			precios = new ArrayList<PrecioServicio>();
		}
		
		return precios;
	}
	
	public static synchronized Boolean isServicio(List<PrecioServicio> preciosServicio, Integer idServicio) {
		Boolean isServicio = false;
		PrecioServicio ps = null;
		
		try {
			if(preciosServicio == null)
				throw new InventarioException("Debe proporcionar una lista de servicios.");
			
			if(idServicio == null)
				throw new InventarioException("Debe proporcionar un id de servicio.");
			
			ps = preciosServicio.stream()
					.filter(item -> item.getServicio().getServicioCve().equals(idServicio))
					.findFirst()
					.orElseThrow(() -> new InventarioException("Servicio no encontrado: " + idServicio));
			
			log.info("Servicio encontrado: {}", ps.getServicio().getServicioDs());
			isServicio = true;
			
		} catch(Exception ex) {
			log.warn(ex.getMessage());
			isServicio = Boolean.FALSE;
		}
		
		return isServicio;
	}
}
