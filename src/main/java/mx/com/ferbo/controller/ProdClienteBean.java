package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CategoriaDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ProductoClienteDAO;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ProductoPorCliente;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class ProdClienteBean implements Serializable {

	private static final long serialVersionUID = -626048119540963939L;
	private static Logger log = LogManager.getLogger(ProdClienteBean.class);

	private List<Cliente> lstClientes;
	private Cliente clienteSelected;
	private ClienteDAO clienteDAO;

	private List<Producto> listProducto;
	private Producto productoSelected;
	private ProductoDAO productoDAO;

	private List<ProductoPorCliente> lstProductosClienteFiltered;
	private List<ProductoPorCliente> lstProductosCliente;
	private ProductoPorCliente ppcSelected;
	private ProductoClienteDAO productoPorClienteDAO;
	private List<Categoria> categoriaList;
	private Categoria categoria;
	private CategoriaDAO categoriaDAO;

	private FacesContext faceContext;
	private HttpServletRequest request;
	private HttpSession session;

	public ProdClienteBean() {
		clienteDAO = new ClienteDAO();
		productoPorClienteDAO = new ProductoClienteDAO();
		productoDAO = new ProductoDAO();
		lstProductosClienteFiltered = new ArrayList<>();
		lstProductosCliente = new ArrayList<>();
		categoriaDAO = new CategoriaDAO();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		faceContext = FacesContext.getCurrentInstance();
		request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		setSession(request.getSession(false));

		lstClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		lstClientes = clienteDAO.buscarTodos();
		categoriaList = categoriaDAO.buscarTodos();
		try {
			setCategoria(categoriaList.stream().filter(c -> c.getCategoriaCve() == 1).collect(Collectors.toList())
					.get(0));
		} catch (Exception e) {
		}

	}

	public void filtraListado() {
		lstProductosClienteFiltered.clear();
		lstProductosClienteFiltered = productoPorClienteDAO.buscarPorCliente(clienteSelected.getCteCve(), true);

		if (listProducto == null)
			listProducto = new ArrayList<Producto>();

		if (listProducto.isEmpty() == false)
			listProducto.clear();

		for (ProductoPorCliente ppc : lstProductosClienteFiltered) {
			listProducto.add(ppc.getProductoCve());
		}

		log.debug("Productos por cliente filtrados: {}", lstProductosClienteFiltered);
	}

	public void nuevoProductoCliente() {

		productoSelected = new Producto();

		ppcSelected = new ProductoPorCliente();
		ppcSelected.setCteCve(clienteSelected);
		ppcSelected.setProductoCve(productoSelected);
	}

	public void cargaProductoCliente() {
		log.info("Cargando el producto {} para modificación.", ppcSelected.getProductoCve().getProductoDs());
		this.productoSelected = ppcSelected.getProductoCve();
	}

	public void guardaProductoCliente() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Producto";

		String resultado = null;

		try {
			ppcSelected.setProductoCve(productoSelected);

			if (productoSelected.getProductoPorClienteList() == null)
				productoSelected.setProductoPorClienteList(new ArrayList<>());

			productoSelected.getProductoPorClienteList().add(ppcSelected);

			if (ppcSelected.getProdXCteCve() == null)
				resultado = productoDAO.guardar(productoSelected);
			else
				resultado = productoDAO.actualizar(productoSelected);

			if (resultado != null) {
				throw new InventarioException("Problema al guardar el producto del cliente.");
			}

			filtraListado();
			ppcSelected = new ProductoPorCliente();

			mensaje = "El producto se registró correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('productoClienteDialog').hide()");
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para actualizar el usuario...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-productosCliente");
		}
	}

	/**
	 * Método para actualizar objeto tipo ProductoCliente
	 */
	public void actualizaProductoCliente() {

		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Producto";

		try {

			ppcSelected.setCteCve(clienteSelected);
			ppcSelected.setProductoCve(productoSelected);

			if (productoDAO.actualizar(productoSelected) != null)
				throw new InventarioException("Problema al actualizar el producto del cliente.");

			mensaje = "El producto se actualizó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('productoClienteDialog').hide()");
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para actualizar el usuario...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-productosCliente");
		}

	}

	/**
	 * Método para eliminar objeto tipo PrecioServicio
	 */

	public void eliminarProductoCliente() {
		if (productoPorClienteDAO.eliminar(ppcSelected) == null) {
			lstProductosClienteFiltered.remove(this.ppcSelected);
			lstProductosCliente.remove(ppcSelected);
			ppcSelected = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Producto Eliminado"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-productosCliente");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el Producto"));
			PrimeFaces.current().ajax().update("form:messages");
		}

	}

	/**
	 * Getters y Setters
	 */
	public List<Cliente> getLstClientes() {
		return lstClientes;
	}

	public void setLstClientes(List<Cliente> lstClientes) {
		this.lstClientes = lstClientes;
	}

	public Cliente getClienteSelected() {
		return clienteSelected;
	}

	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}

	public ClienteDAO getClienteDAO() {
		return clienteDAO;
	}

	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public List<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(List<Producto> listProducto) {
		this.listProducto = listProducto;
	}

	public Producto getProductoSelected() {
		return productoSelected;
	}

	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	public List<ProductoPorCliente> getLstProductosClienteFiltered() {
		return lstProductosClienteFiltered;
	}

	public void setLstProductosClienteFiltered(List<ProductoPorCliente> lstProductosClienteFiltered) {
		this.lstProductosClienteFiltered = lstProductosClienteFiltered;
	}

	public List<ProductoPorCliente> getLstProductosCliente() {
		return lstProductosCliente;
	}

	public void setLstProductosCliente(List<ProductoPorCliente> lstProductosCliente) {
		this.lstProductosCliente = lstProductosCliente;
	}

	public ProductoPorCliente getPpcSelected() {
		return ppcSelected;
	}

	public void setPpcSelected(ProductoPorCliente prodClienteSelected) {
		this.ppcSelected = prodClienteSelected;
	}

	public ProductoClienteDAO getProductoPorClienteDAO() {
		return productoPorClienteDAO;
	}

	public void setProductoPorClienteDAO(ProductoClienteDAO productoPorClienteDAO) {
		this.productoPorClienteDAO = productoPorClienteDAO;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
}