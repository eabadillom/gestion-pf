package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.clientes.ContactoBL;
import mx.com.ferbo.controller.SideBarBean;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.TipoTelefono;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ViewScoped
public class ClientesBean implements Serializable {

    private static final Logger log = LogManager.getLogger(ClientesBean.class);

    // Side bar
    @Inject
    private SideBarBean sideBar;

    // Objetos de clientes
    private Cliente clienteSelected;
    private List<Cliente> lstClientes;
    private ClienteDAO clienteDAO;

    // Objetos para contactos
    @Inject
    ContactoBL contactoBL;

    private ClienteContacto clienteContactoSelected;
    private Contacto contactoSelected;
    private MedioCnt medioCntSelected;
    private Boolean editandoContacto;
    private List<TipoMail> lstTipoMail;
    private List<TipoTelefono> lstTipoTelefono;

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

     private void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,
                title, msg));
    }

    // Metodos exclusivos para contactos
    public void nuevoClienteContacto() {
        this.editandoContacto = null;
        this.editandoContacto = Boolean.TRUE;
        this.clienteContactoSelected = new ClienteContacto();
    }

    public void nuevoContacto() {
        this.contactoSelected = new Contacto();
    }

    public void nuevoMedioContacto() {
        this.medioCntSelected = new MedioCnt();
        this.medioCntSelected.setIdMail(new Mail());
        this.medioCntSelected.setIdTelefono(new Telefono());
    }

    public void copiarClienteContacto(ClienteContacto clienteContacto) {
            this.editandoContacto = null;
            this.editandoContacto = Boolean.FALSE;
            this.clienteContactoSelected = clienteContacto;
    }

    public void operarContactos(String operacion) {
        String mensaje = null;
        try {

            switch (operacion) {

                case "agregarcontacto":
                    contactoBL.agregarContacto(this.clienteSelected, this.clienteContactoSelected);
                    mensaje = "Contacto Agregado";
                    break;

                case "agregarmedio":
                    contactoBL.agregarMedioContacto(this.clienteContactoSelected, this.medioCntSelected);
                    mensaje = "Medio de contacto Agregado";
                    break;

                case "eliminarmedio":
                    contactoBL.eliminarMedioContacto(this.clienteContactoSelected, this.medioCntSelected);
                    mensaje = "Medio de contacto eliminado";
                    break;

                case "eliminarcontacto":
                    contactoBL.eliminarContacto(this.clienteSelected, this.clienteContactoSelected);
                    mensaje = "Contacto eliminado";
                    break;

                default:
                    throw new InventarioException("Sin operación valida para contactos");
            }
            addMessage(FacesMessage.SEVERITY_INFO, "Contacto", mensaje);
        } catch (InventarioException ex) {
            log.warn(ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Contacto", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Contacto", "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update();
        }
    }

    public void seleccionarMedioContacto() {
        try {
            contactoBL.seleccionarMedioContacto(this.medioCntSelected);
        } catch (InventarioException ex) {
            log.warn(ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Contacto", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Contacto", "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update();
        }
    }

    // Getters y setters para sidebar
    public SideBarBean getSideBar() {
        return sideBar;
    }

    public void setSideBar(SideBarBean sideBar) {
        this.sideBar = sideBar;
    }

    // Getters y Setters para Cliente
     public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public void setClienteSelected(Cliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    public List<Cliente> getLstClientes() {
        return lstClientes;
    }

    public void setLstClientes(List<Cliente> lstClientes) {
        this.lstClientes = lstClientes;
    }

    public ClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    public void setClienteDAO(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    // Getters y Setters para Contactos
    public ClienteContacto getClienteContactoSelected() {
        return clienteContactoSelected;
    }

    public void setClienteContactoSelected(ClienteContacto clienteContactoSelected) {
        this.clienteContactoSelected = clienteContactoSelected;
    }

    public Contacto getContactoSelected() {
        return contactoSelected;
    }

    public void setContactoSelected(Contacto contactoSelected) {
        this.contactoSelected = contactoSelected;
    }

    public MedioCnt getMedioCntSelected() {
        return medioCntSelected;
    }

    public void setMedioCntSelected(MedioCnt medioCntSelected) {
        this.medioCntSelected = medioCntSelected;
    }

    public Boolean getEditandoContacto() {
        return editandoContacto;
    }

    public void setEditandoContacto(Boolean editandoContacto) {
        this.editandoContacto = editandoContacto;
    }

    public List<TipoMail> getLstTipoMail() {
        return lstTipoMail;
    }

    public void setLstTipoMail(List<TipoMail> lstTipoMail) {
        this.lstTipoMail = lstTipoMail;
    }

    public List<TipoTelefono> getLstTipoTelefono() {
        return lstTipoTelefono;
    }

    public void setLstTipoTelefono(List<TipoTelefono> lstTipoTelefono) {
        this.lstTipoTelefono = lstTipoTelefono;
    }
}