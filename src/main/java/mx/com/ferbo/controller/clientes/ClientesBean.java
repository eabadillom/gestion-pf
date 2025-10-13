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

import org.jfree.util.Log;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.clientes.FiscalBL;
import mx.com.ferbo.dao.n.ClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class ClientesBean implements Serializable {

    // Objetos de clientes
    private Cliente clienteSelected;
    private List<Cliente> lstClientes;

    @Inject
    private ClienteDAO clienteDAO;

    // Objetos para Fiscal
    @Inject
    private FiscalBL fiscalBL;

    private List<UsoCfdi> lstUsoCfdi;
    private List<RegimenFiscal> lstRegimenFiscal;
    private List<MetodoPago> lstMetodoPago;
    private List<MedioPago> lstMedioPago;

    public ClientesBean() {
        clienteSelected = new Cliente();
        lstClientes = new ArrayList<>();
        clienteDAO = new ClienteDAO();
        lstUsoCfdi = new ArrayList<>();
        lstRegimenFiscal = new ArrayList<>();
        lstMetodoPago = new ArrayList<>();
        lstMedioPago = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        try {
            lstClientes = clienteDAO.buscarTodos();
            if(lstClientes == null){
                throw new InventarioException("Problema al obtener los datos de clientes");
            }
        } catch (InventarioException ex) {
            Log.info("Error: " + ex.getMessage(), ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Cargar información", ex.getMessage());
        } catch (Exception ex) {
            Log.error("Error inesperado. Causado por: ", ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Cargar información",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form");
        }
    }

    // Metodos exclusivos para contactos
    public void cargarInfoFiscalCliente(Cliente cliente) {
        try {
            if (cliente == null){
                throw new InventarioException("El cliente no puede ser vacío");
            }

            if (cliente.getCteCve() == null) {
                throw new InventarioException("El cliente no tiene datos todavida");
            }
            this.clienteSelected = clienteDAO.obtenerPorId(cliente.getCteCve(), true);
            fiscalBL.validarInfoFiscal(this.clienteSelected);
            lstMedioPago = fiscalBL.obtenerMediosPago();
            lstMetodoPago = fiscalBL.obtenerMetodosPago();
            lstRegimenFiscal = fiscalBL.obtenerRegimenesFiscales(this.clienteSelected);
            lstUsoCfdi = fiscalBL.obtenerUsoCfdis(this.clienteSelected);
            addMessage(FacesMessage.SEVERITY_INFO, "Fiscal", "Información fiscal cargada");
        } catch (InventarioException ex) {
            addMessage(FacesMessage.SEVERITY_WARN, "Fiscal", ex.getMessage());
        } catch (Exception ex) {
            Log.error("Error inesperado. Causado por: ", ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Fiscal",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form");
        }
    }

    public void validarCodigoUnico(Cliente cliente){
        try {
            this.clienteSelected = cliente;
            Cliente cliente2 = clienteDAO.buscarPorCodigoUnico(this.clienteSelected.getCodUnico());
            fiscalBL.validarCodigoUnico(this.clienteSelected, cliente2);
        } catch (InventarioException ex) {
            Log.warn(ex.getMessage(), ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Fiscal", ex.getMessage());
        }catch (Exception ex) {
           Log.error("Error inesperado. Causado por: ", ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Fiscal",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form");
        }
    }

    public void validarRFC(Cliente cliente){
        try {
            this.clienteSelected = cliente;
            fiscalBL.validarRFC(this.clienteSelected);
        } catch (InventarioException ex) {
            Log.warn(ex.getMessage(), ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Fiscal", ex.getMessage());
        }catch (Exception ex) {
           Log.error("Error inesperado. Causado por: ", ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Fiscal",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form");
        }
    }
    
    public void eliminarCliente() {
    }

    public void clonarCliente() {
    }

    public void nuevoCliente() {
    }

    // Getter y Setter para Cliente
    private void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,
                title, msg));
    }

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

    // Getter y Setter para Fiscal
    public List<UsoCfdi> getLstUsoCfdi() {
        return lstUsoCfdi;
    }

    public void setLstUsoCfdi(List<UsoCfdi> lstUsoCfdi) {
        this.lstUsoCfdi = lstUsoCfdi;
    }

    public List<RegimenFiscal> getLstRegimenFiscal() {
        return lstRegimenFiscal;
    }

    public void setLstRegimenFiscal(List<RegimenFiscal> lstRegimenFiscal) {
        this.lstRegimenFiscal = lstRegimenFiscal;
    }

    public List<MetodoPago> getLstMetodoPago() {
        return lstMetodoPago;
    }

    public void setLstMetodoPago(List<MetodoPago> lstMetodoPago) {
        this.lstMetodoPago = lstMetodoPago;
    }

    public List<MedioPago> getLstMedioPago() {
        return lstMedioPago;
    }

    public void setLstMedioPago(List<MedioPago> lstMedioPago) {
        this.lstMedioPago = lstMedioPago;
    }

    // Getter y Setter de sidebar

}