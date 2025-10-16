package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.clientes.AvisoBL;
import mx.com.ferbo.dao.n.ClienteDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class ClientesBean implements Serializable {

    // Objetos de clientes
    private Cliente clienteSelected;
    private List<Cliente> lstClientes;

    @Inject
    private ClienteDAO clienteDAO;

    // Objetos para Avisos
    @Inject
    private AvisoBL avisoBL;

    private Aviso avisoSelected;
    private PrecioServicio precioAvisoSelected;
    private String tituloDialogAviso;
    private List<Planta> lstPlanta;
    private List<Categoria> lstCategoria;

    public ClientesBean() {
        this.lstClientes = new ArrayList<>();
        this.clienteSelected = new Cliente();
    }

    @PostConstruct
    public void init() {
        this.lstClientes = clienteDAO.buscarTodos();
    }

    public void cargarInfoCliente(Cliente cliente) {
        try {
            this.clienteSelected = clienteDAO.obtenerPorId(cliente.getCteCve(), true);
            addMessage(FacesMessage.SEVERITY_INFO, "Información", "Información cargada con exito");
        } catch (InventarioException ex) {
            addMessage(FacesMessage.SEVERITY_WARN, "Información", ex.getMessage());
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Información",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form");
        }
    }

    public void clonarCliente(){}

    public void eliminarCliente(){}

    public void nuevoCliente(){}

    // Funciones para Avisos
    public void nuevoAviso() {
        this.tituloDialogAviso = null;
        this.tituloDialogAviso = "Nuevo Aviso";
        this.avisoSelected = avisoBL.nueAviso();
        this.avisoSelected.setCteCve(clienteSelected);
    }

    public void copiarAviso(Aviso aviso) {
        this.tituloDialogAviso = null;
        this.tituloDialogAviso = "Editar Aviso";
        this.avisoSelected = aviso;
    }

    public void operarAvisos(String operacion) {
        String mensaje = null;
        try {
            switch (operacion) {
                case "agregaraviso":
                    avisoBL.agregarAviso(clienteSelected, avisoSelected);
                    mensaje = "Aviso agregado exitosamente";
                    break;

                case "agregarservicio":
                    avisoBL.agregarServicioAviso(avisoSelected, precioAvisoSelected);
                    mensaje = "Servico agregado a aviso exitosamente";
                    break;

                case "eliminaraviso":
                    avisoBL.eliminaAviso(clienteSelected, avisoSelected);
                    mensaje = "Aviso eliminado exitosamente";
                    break;

                case "eliminarservicio":
                    avisoBL.eliminaServicioAviso(avisoSelected, precioAvisoSelected);
                    mensaje = "Servicio eliminado de aviso exitosamente";
                    break;

                default:
                    throw new InventarioException("Operación sobre avisos no válida");
            }
            addMessage(FacesMessage.SEVERITY_INFO, "Aviso", mensaje);
        } catch (InventarioException ex) {
            addMessage(FacesMessage.SEVERITY_WARN, "Aviso", ex.getMessage());
        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Aviso",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form");
        }
    }

    private void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, msg));
    }

    // Getters y Setters para Clientes
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

    // Getters y Setters para Avisos
    public Aviso getAvisoSelected() {
        return avisoSelected;
    }

    public void setAvisoSelected(Aviso avisoSelected) {
        this.avisoSelected = avisoSelected;
    }

    public PrecioServicio getPrecioAvisoSelected() {
        return precioAvisoSelected;
    }

    public void setPrecioAvisoSelected(PrecioServicio precioAvisoSelected) {
        this.precioAvisoSelected = precioAvisoSelected;
    }

    public String getTituloDialogAviso() {
        return tituloDialogAviso;
    }

    public void setTituloDialogAviso(String tituloDialogAviso) {
        this.tituloDialogAviso = tituloDialogAviso;
    }

    public List<Planta> getLstPlanta() {
        return lstPlanta;
    }

    public void setLstPlanta(List<Planta> lstPlanta) {
        this.lstPlanta = lstPlanta;
    }

    public List<Categoria> getLstCategoria() {
        return lstCategoria;
    }

    public void setLstCategoria(List<Categoria> lstCategoria) {
        this.lstCategoria = lstCategoria;
    }
}