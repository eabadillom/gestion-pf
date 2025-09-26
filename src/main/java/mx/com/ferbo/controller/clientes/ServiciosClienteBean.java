package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.clientes.ServiciosBL;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.InventarioException;

/**
 *
 * @author julio
 */
@Named("serviciosCliente")
@ViewScoped
public class ServiciosClienteBean implements Serializable {

    private static final long serialVersionUID = -5768146106301267486L;

    private List<Cliente> lstClientes;
    private Cliente clienteSelected;
    private ClienteDAO clienteDAO;

    private List<UnidadDeManejo> lstUnidadManejo;
    private List<Servicio> lstServicio;
    private List<PrecioServicio> lstPrecioServicioFiltered;

    private PrecioServicio precioServicioSelected;

    private ServiciosBL serviciosClienteBL;

    public ServiciosClienteBean() {
        serviciosClienteBL = new ServiciosBL();
        FacesContext faceContext = FacesContext.getCurrentInstance();
        lstUnidadManejo = serviciosClienteBL.obtenerUnidadesDeManejo();
        lstServicio = serviciosClienteBL.obtenerServicios();
        lstPrecioServicioFiltered = new ArrayList<>();
        lstClientes = new ArrayList<Cliente>();
        clienteSelected = new Cliente();
        clienteDAO = new ClienteDAO();

    }

    @PostConstruct
    public void init() {
        lstClientes = clienteDAO.buscarTodos();
    }

    public void filtrarListadoServicios(Cliente cliente) throws Throwable {
        try {
            this.clienteSelected = cliente;
            this.lstPrecioServicioFiltered = serviciosClienteBL.buscarPreciosPorCliente(clienteSelected);
        } catch (InventarioException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void nuevoServicioCliente() {
        try {
            precioServicioSelected = serviciosClienteBL.crearNuevoPrecioServicio(clienteSelected);
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Servicio", "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void guardarPrecioServicio() {
        try {
            serviciosClienteBL.guardarOActualizar(precioServicioSelected);
            String exito;
            // Si fue nuevo, lo agregamos al listado filtrado
            if (precioServicioSelected.getId() == null) {
                lstPrecioServicioFiltered.add(precioServicioSelected);
                exito = "Servicio Agregado";
            } else {
                exito = "Servicio Actualizado";
            }

            PrimeFaces.current().executeScript("PF('servicioClienteDialog').hide()");
            addMessage(FacesMessage.SEVERITY_INFO, "Servicio", exito);
        } catch (InventarioException ex) {
            addMessage(FacesMessage.SEVERITY_WARN, "Servicio", ex.getMessage());
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Servicio", "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-servicios");
        }
    }

    public void eliminarPrecioServicio() {
        try {
            serviciosClienteBL.eliminarPrecioServicio(precioServicioSelected);
            lstPrecioServicioFiltered.remove(precioServicioSelected);
            precioServicioSelected = null;

            addMessage(FacesMessage.SEVERITY_INFO, "Servicio", "Servicio Eliminado");
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-servicios");

        } catch (InventarioException ex) {
            addMessage(FacesMessage.SEVERITY_WARN, "Servicio", ex.getMessage());
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Servicio", "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-servicios");
        }
    }

    // Métodos auxiliares para mensajes
    private void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, msg));
    }

    public List<Cliente> getLstClientes() {
        return lstClientes;
    }

    public void setLstClientes(List<Cliente> lstClientes) {
        this.lstClientes = lstClientes;
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

    public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public void setClienteSelected(Cliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    public PrecioServicio getPrecioServicioSelected() {
        return precioServicioSelected;
    }

    public void setPrecioServicioSelected(PrecioServicio precioServicioSelected) {
        this.precioServicioSelected = precioServicioSelected;
    }

    public ServiciosBL getServiciosClienteBL() {
        return serviciosClienteBL;
    }

    public void setServiciosClienteBL(ServiciosBL serviciosClienteBL) {
        this.serviciosClienteBL = serviciosClienteBL;
    }

}
