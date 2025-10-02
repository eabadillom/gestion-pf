package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.component.sidebar.Sidebar;

import mx.com.ferbo.business.clientes.ServiciosBL;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class ClientesBean implements Serializable {

    private static final Logger log = LogManager.getLogger(ClientesBean.class);

    @Inject
    private Sidebar sidebar;

    // Objetos de clientes
    private Cliente clienteSelected;
    private List<Cliente> lstCliente;
    private ClienteDAO clienteDAO;

    // Objetos para Servicios
    @Inject
    private ServiciosBL serviciosBL;

    private PrecioServicio precioServicioSelected;
    private List<PrecioServicio> lstPrecioServicios;
    private Servicio servicioSelected;
    private List<Servicio> lstServicio;
    private UnidadDeManejo unidadDeManejoSelected;
    private List<UnidadDeManejo> lstUnidadManejo;

    // Ejemplo de estrucutura de una funcion

    /*
     * public tipo numbreFuncion(Parametros si lleva){
     * try {
     * addMessage(FacesMessage.SEVERITY_INFO, "Servicio", "Servicio Eliminado");
     * } catch (IneventatioException ex) {
     * addMessage(FacesMessage.SEVERITY_WARN, "Servicio", ex.getMessage());
     * } catch (exception){
     * addMessage(FacesMessage.SEVERITY_ERROR, "Servicio",
     * "Contacte con el admistrador del sistema.");
     * } finally {
     * PrimeFaces.current().ajax().update("form:messages",
     * "form:tabView:dt-servicios");
     * }
     * }
     * 
     * private void addMessage(FacesMessage.Severity severity, String title, String
     * msg) {
     * FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,
     * title, msg));
     * }
     * 
     */

    // Metodos exclusivos de servicios
    public void nuevoPrecioServicio(){
        this.precioServicioSelected = new PrecioServicio();
        this.precioServicioSelected.setCliente(this.clienteSelected);
        this.precioServicioSelected.setPrecio(BigDecimal.ZERO);
        this.precioServicioSelected.setServicio(new Servicio());
        this.precioServicioSelected.setUnidad(new UnidadDeManejo());
        this.precioServicioSelected.setAvisoCve(new Aviso());
    }

    public void copiarPrecioServicio(PrecioServicio precioServicio) {
        this.precioServicioSelected = precioServicio;
    }

    public void OperarServicios(String operacion) {
        String mensaje = null;
        try {
            switch (operacion) {
                case "agregaprecioservicio":
                    serviciosBL.agregarOActulizarPrecioServicio(this.clienteSelected, this.precioServicioSelected);
                    mensaje = "Servicio agregado";
                    break;
                case "eliminarprecioservicio":
                    serviciosBL.eliminarPrecioServicio(this.clienteSelected, this.precioServicioSelected);
                    mensaje = "Servicio eliminado";
                    break;
            }
            addMessage(FacesMessage.SEVERITY_INFO, "Servicio", mensaje);
        } catch (InventarioException ex) {
            log.warn(ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Servicio", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Servicio",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form:tabView:dt-servicios");
        }
    }

    private void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,
                title, msg));
    }

    // Getters y Setter para sidebar
    public Sidebar getSidebar() {
        return sidebar;
    }

    public void setSidebar(Sidebar sidebar) {
        this.sidebar = sidebar;
    }

    // Getter y Setter para Cliente
    public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public void setClienteSelected(Cliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    public List<Cliente> getLstCliente() {
        return lstCliente;
    }

    public void setLstCliente(List<Cliente> lstCliente) {
        this.lstCliente = lstCliente;
    }

    public ClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    public void setClienteDAO(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    // Metodos exclusivos para Precio servicios
    public PrecioServicio getPrecioServicioSelected() {
        return precioServicioSelected;
    }

    public void setPrecioServicioSelected(PrecioServicio precioServicioSelected) {
        this.precioServicioSelected = precioServicioSelected;
    }

    public List<PrecioServicio> getLstPrecioServicios() {
        return lstPrecioServicios;
    }

    public void setLstPrecioServicios(List<PrecioServicio> lstPrecioServicios) {
        this.lstPrecioServicios = lstPrecioServicios;
    }

    public Servicio getServicioSelected() {
        return servicioSelected;
    }

    public void setServicioSelected(Servicio servicioSelected) {
        this.servicioSelected = servicioSelected;
    }

    public List<Servicio> getLstServicio() {
        return lstServicio;
    }

    public void setLstServicio(List<Servicio> lstServicio) {
        this.lstServicio = lstServicio;
    }

    public UnidadDeManejo getUnidadDeManejoSelected() {
        return unidadDeManejoSelected;
    }

    public void setUnidadDeManejoSelected(UnidadDeManejo unidadDeManejoSelected) {
        this.unidadDeManejoSelected = unidadDeManejoSelected;
    }

    public List<UnidadDeManejo> getLstUnidadManejo() {
        return lstUnidadManejo;
    }

    public void setLstUnidadManejo(List<UnidadDeManejo> lstUnidadManejo) {
        this.lstUnidadManejo = lstUnidadManejo;
    }

}