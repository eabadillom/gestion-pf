package mx.com.ferbo.controller.clientes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.business.clientes.ContactoBL;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class ClientesBean {

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

    public ClientesBean(){
        this.clienteSelected = new Cliente();
        this.lstClientes = new ArrayList<>();
        this.clienteDAO = new ClienteDAO();
    }

    @PostConstruct
    private void init(){
        lstClientes = clienteDAO.buscarTodos(false);
    }

    public void cargarInfoCliente(){
        try{
            
            clienteSelected.setClienteContactoList(contactoBL.obtenerListaContactos(this.clienteSelected));


        } catch (InventarioException ex){

        }
        catch (Exception ex){

        }
        finally{

        }
    }

    // Metodos exclusivos para contactos

    public void nuevoClienteContacto(){
        this.clienteContactoSelected = new ClienteContacto();
    }

    public void nuevoContacto(){
        this.contactoSelected = new Contacto();
    }

    public void nuevoMedioContacto(){
        this.medioCntSelected = new MedioCnt();
    }

    public void operarContactos(String operacion){
        try{
            
            switch (operacion){

                case "agregarcontacto":
                    contactoBL.agregarContacto(this.clienteSelected, this.clienteContactoSelected, this.contactoSelected);
                break;

                case "agregarmedio":
                    contactoBL.agregarMedioContacto(this.clienteSelected, this.clienteContactoSelected, this.contactoSelected, this.medioCntSelected);
                break;

                case "eliminarmedio":
                    contactoBL.eliminarMedioContacto(this.medioCntSelected);
                break;

                case "elinarcontacto":
                    contactoBL.eliminarContacto(this.clienteContactoSelected);
                break;

                default :
                    throw new InventarioException("Sin operación valida para contactos");
            }

        } catch (InventarioException ex){

        }
        catch (Exception ex){

        }
        finally{

        }
    }

    // Getter y Setter para Cliente
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

    // Getters y Setters para contactos
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

}
