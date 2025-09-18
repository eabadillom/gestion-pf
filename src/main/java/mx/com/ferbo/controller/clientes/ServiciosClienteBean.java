package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.business.clientes.ServiciosClienteBL;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.UnidadManejoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
import org.primefaces.PrimeFaces;

/**
 *
 * @author julio
 */
@Named("serviciosCliente")
@ViewScoped
public class ServiciosClienteBean implements Serializable {

    private static final long serialVersionUID = -5768146106301267486L;

    private List<Cliente> lstClientes;

    private List<UnidadDeManejo> lstUnidadManejo;
    private List<Servicio> lstServicio;
    private List<PrecioServicio> lstPrecioServicioFiltered;

    private Cliente clienteSelected;

    private PrecioServicio precioServicioSelected;

    private UnidadManejoDAO unidadManejoDAO;
    private ServicioDAO servicioDAO;
    private PrecioServicioDAO precioServicioDAO;

    private FacesContext faceContext;
    private HttpServletRequest request;

    public ServiciosClienteBean() {
        unidadManejoDAO = new UnidadManejoDAO();
        servicioDAO = new ServicioDAO();
        precioServicioDAO = new PrecioServicioDAO();
        lstPrecioServicioFiltered = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        // Ya no cargar nada aquí
        faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        lstClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        lstUnidadManejo = unidadManejoDAO.buscarTodos();
        lstServicio = servicioDAO.buscarTodos();
    }

    public void onTabServicioCliente() {
        if (lstClientes == null || lstClientes.isEmpty()) {
            faceContext = FacesContext.getCurrentInstance();
            request = (HttpServletRequest) faceContext.getExternalContext().getRequest();

            lstClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        }

        if (lstUnidadManejo == null || lstUnidadManejo.isEmpty()) {
            lstUnidadManejo = unidadManejoDAO.buscarTodos();
        }

        if (lstServicio == null || lstServicio.isEmpty()) {
            lstServicio = servicioDAO.buscarTodos();
        }
    }

    public void filtaListado() {
        if (clienteSelected != null) {
            lstPrecioServicioFiltered = ServiciosClienteBL.obtenerPorCliente(
                    clienteSelected.getCteCve(),
                    precioServicioDAO
            );
        }
    }

    public void nuevoServicioCliente() {
        if (clienteSelected != null) {
            precioServicioSelected = ServiciosClienteBL.crearNuevoServicio(clienteSelected);
        }
    }

    public void guardarPrecioServicio() {
        boolean exito = false;

        if (precioServicioSelected.getId() == null) {
            exito = ServiciosClienteBL.guardar(precioServicioSelected, precioServicioDAO);
            if (exito) {
                lstPrecioServicioFiltered.add(precioServicioSelected);
                addMessage("Servicio Agregado");
            } else {
                addError("Error", "Ocurrió un error al intentar guardar el Servicio");
            }
        } else {
            exito = ServiciosClienteBL.actualizar(precioServicioSelected, precioServicioDAO);
            if (exito) {
                addMessage("Servicio Actualizado");
            } else {
                addError("Error", "Ocurrió un error al intentar actualizar el Servicio");
            }
        }

        PrimeFaces.current().executeScript("PF('servicioClienteDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
    }

    public void eliminarPrecioServicio() {
        if (ServiciosClienteBL.eliminar(precioServicioSelected, precioServicioDAO)) {
            lstPrecioServicioFiltered.remove(precioServicioSelected);
            precioServicioSelected = null;
            addMessage("Servicio Eliminado");
            PrimeFaces.current().ajax().update("form:messages", "form:dt-servicios");
        } else {
            addError("Error", "Ocurrió un error al intentar eliminar el Servicio");
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    private void addMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
    }

    private void addError(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    // Getters y Setters
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

    public List<UnidadDeManejo> getLstUnidadManejo() {
        return lstUnidadManejo;
    }

    public void setLstUnidadManejo(List<UnidadDeManejo> lstUnidadManejo) {
        this.lstUnidadManejo = lstUnidadManejo;
    }

    public List<Servicio> getLstServicio() {
        return lstServicio;
    }

    public void setLstServicio(List<Servicio> lstServicio) {
        this.lstServicio = lstServicio;
    }

    public List<PrecioServicio> getLstPrecioServicioFiltered() {
        return lstPrecioServicioFiltered;
    }

    public void setLstPrecioServicioFiltered(List<PrecioServicio> lstPrecioServicioFiltered) {
        this.lstPrecioServicioFiltered = lstPrecioServicioFiltered;
    }

    public PrecioServicio getPrecioServicioSelected() {
        return precioServicioSelected;
    }

    public void setPrecioServicioSelected(PrecioServicio precioServicioSelected) {
        this.precioServicioSelected = precioServicioSelected;
    }
}
