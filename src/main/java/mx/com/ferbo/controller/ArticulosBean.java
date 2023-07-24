package mx.com.ferbo.controller;

import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.com.ferbo.dao.ProductoDAO;
import mx.com.ferbo.model.Producto;

@Named
@ViewScoped
public class ArticulosBean implements Serializable {
	
	private static final long serialVersionUID = -9171234069697090700L;

	private List<Producto> productos;

	private Producto selectedProducto;

	private List<Producto> selectedProductos;

	private ProductoDAO productoDAO;

	public ArticulosBean() {
		productoDAO = new ProductoDAO();
		selectedProductos = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		productos = productoDAO.buscarTodos();
	}

	public void openNew() {
		this.selectedProducto = new Producto();
	}

	public void saveProduct() {
		if (this.selectedProducto.getProductoCve() == null) {
			if (productoDAO.guardar(selectedProducto) == null) {
				this.productos.add(this.selectedProducto);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Artículo Agregado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar guardar el Artículo"));
			}

		} else {
			if (productoDAO.actualizar(selectedProducto) == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Artículo Actualizado"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Ocurrió un error al intentar actualizar el Artículo"));
			}
		}

		PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-productos");
	}

	public void deleteProduct() {
		if (productoDAO.eliminar(selectedProducto) == null) {
			this.productos.remove(this.selectedProducto);
			this.selectedProducto = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Artículo Eliminado"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-productos");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar el Artículo"));
			PrimeFaces.current().ajax().update("form:messages");
		}

	}

	public String getDeleteButtonMessage() {
		if (hasSelectedProductos()) {
			int size = this.selectedProductos.size();
			return size > 1 ? size + " artículos seleccionados" : "1 artículo seleccionado";
		}

		return "Eliminar";
	}

	public boolean hasSelectedProductos() {
		return this.selectedProductos != null && !this.selectedProductos.isEmpty();
	}

	public void deleteSelectedProductos() {
		if (productoDAO.eliminarListado(productos) == null) {
			this.productos.removeAll(this.selectedProductos);
			this.selectedProductos = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Artículos Eliminados"));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-productos");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Ocurrió un error al intentar eliminar los Artículos"));
			PrimeFaces.current().ajax().update("form:messages");
		}
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public List<Producto> getSelectedProductos() {
		return selectedProductos;
	}

	public void setSelectedProductos(List<Producto> selectedProductos) {
		this.selectedProductos = selectedProductos;
	}

}
