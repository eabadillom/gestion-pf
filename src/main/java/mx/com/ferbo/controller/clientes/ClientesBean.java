package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

@Named
@ViewScoped
public class ClientesBean implements Serializable {

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

    // Metodos exclusivos de servicios

    // Metodos exclusivos de domicilios

    // Metodos exclusivos para contactos
    public void nuevoClienteContacto() {
        this.editandoContacto = null;
        this.editandoContacto = Boolean.TRUE;
        this.clienteContactoSelected = new ClienteContacto();
    }

    public void actualizarCliente() {
        this.clienteDAO.actualizar(this.clienteSelected);
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
        try {
            this.editandoContacto = null;
            this.editandoContacto = Boolean.FALSE;
            this.clienteContactoSelected = clienteContacto;
        } catch (Exception ex) {

        } finally {

        }
    }

    public void operarContactos(String operacion) {
        try {

            switch (operacion) {

                case "agregarcontacto":
                    contactoBL.agregarContacto(this.clienteSelected, this.clienteContactoSelected);
                    break;

                case "agregarmedio":
                    contactoBL.agregarMedioContacto(this.clienteContactoSelected, this.medioCntSelected);
                    break;

                case "eliminarmedio":
                    contactoBL.eliminarMedioContacto(this.clienteContactoSelected, this.medioCntSelected);
                    break;

                case "eliminarcontacto":
                    contactoBL.eliminarContacto(this.clienteSelected, this.clienteContactoSelected);
                    break;

                default:
                    throw new InventarioException("Sin operación valida para contactos");
            }

        } catch (InventarioException ex) {

        } catch (Exception ex) {

        } finally {

        }
    }

    public void seleccionarMedioContacto() {
        try {
            contactoBL.seleccionarMedioContacto(this.medioCntSelected);
        } catch (InventarioException ex) {

        } catch (Exception ex) {

        } finally {

        }
    }

    // Getter y setter para sidebar
    public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public SideBarBean getSideBar() {
        return sideBar;
    }

    // Getter y Setter para Cliente

    public void setSideBar(SideBarBean sideBar) {
        this.sideBar = sideBar;
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