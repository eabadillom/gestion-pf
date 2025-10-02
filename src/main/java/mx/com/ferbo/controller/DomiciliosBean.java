package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.com.ferbo.business.clientes.ClienteBL;
import mx.com.ferbo.business.clientes.DomiciliosBL;

import mx.com.ferbo.dao.n.AsentamientoHumanoDAO;
import mx.com.ferbo.dao.ClienteDAO;

import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.TiposDomicilio;
import mx.com.ferbo.model.Usuario;

import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.SecurityUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.PrimeFaces;

/**
* @author Juan_Cervantes
*/
@Named
@ViewScoped
public class DomiciliosBean implements Serializable 
{
    private static final long serialVersionUID = 4150584435387992139L;
    private static Logger log = LogManager.getLogger(DomiciliosBean.class);
    
    @Inject
    private DomiciliosBL domicilios;
    
    @Inject
    private AsentamientoHumanoDAO asentamientoHumanoDAO;
    
    /**
    * Objetos para clientes
    */
    private List<Cliente> lstClientes;
    private List<Cliente> lstClientesSelected;
    private Cliente clienteSelected;
    private ClienteDAO clienteDAO;
    
    /**
    * Objetos para domicilio_cliente
    */
    private List<ClienteDomicilios> lstClienteDomicilios;
    private List<ClienteDomicilios> lstClienteDomiciliosFiltered;
    private ClienteDomicilios clienteDomicilioSelected;
    
    /**
    * Objetos para tipos_domicilio
    */
    private List<TiposDomicilio> lstTiposDomicilio;
    private TiposDomicilio tipoDomicilioSelected;
    
    /**
    * Objetos para domicilio
    */
    private List<Domicilios> lstDomicilios;
    private Domicilios domicilioSelected;
    
    private Usuario usuario;
    private FacesContext context;
    private HttpServletRequest request;
    
    SecurityUtil util;
    
    public DomiciliosBean() {
        lstClientesSelected = new ArrayList<>();
        
        clienteDAO = new ClienteDAO();
        
        lstClienteDomiciliosFiltered = new ArrayList<>();
        
        nuevoCliente();
        consultaClientes();
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        this.usuario = (Usuario) request.getSession(true).getAttribute("usuario");
        log.info("El usuario {} ingresa al catálogo de clientes.", usuario.getUsuario());
        actualizarListas();
    }
    
    @PreDestroy
    public void destroy() {
        log.info("Saliendo del catálogo de clientes.");
        this.lstClientes = new ArrayList<>();
    }
    
    private void consultaClientes() {
        lstClientes = clienteDAO.buscarTodos(true);
    }
    
    public void nuevoCliente() {
        this.clienteSelected = ClienteBL.nuevoCliente();
        log.info("Nuevo cliente creado.");
    }
    
    public void cargaInfoCliente() {
        log.info("Cargando información del cliente: " + this.clienteSelected);
        
        this.clienteSelected = clienteDAO.buscarPorId(this.clienteSelected.getCteCve(), true);
        log.info("Info cliente: {}", this.clienteSelected.toString());
        
        if (clienteDomicilioSelected != null && clienteDomicilioSelected.getDomicilios() != null) {
            domicilioSelected = clienteDomicilioSelected.getDomicilios();
        }
        
        this.actualizarListas();
        
        PrimeFaces.current().ajax().update("form:tabView:dt-domiciliosCliente");
    }
    
    public boolean clienteSeleccionado() {
        return this.lstClientesSelected != null && !this.lstClientesSelected.isEmpty();
    }
    
    public void actualizarListas() {
        lstClienteDomicilios = domicilios.buscarDomiciliosPorCliente(clienteSelected);
        lstTiposDomicilio = domicilios.buscarTiposDomicilios();
    }
    
    public void filtraListado() {
        lstClienteDomicilios = domicilios.filtrarListado(lstClienteDomicilios, clienteSelected);
        PrimeFaces.current().ajax().update("form:soClienteTipoDom", "form:dt-domiciliosCliente");
    }
    
    public void filtraListadoDomicilio() {
        if (tipoDomicilioSelected != null) {
            lstClienteDomiciliosFiltered = domicilios.filtrarListadoDomicilio(lstClienteDomicilios, tipoDomicilioSelected, clienteSelected);
        } else {
            lstClienteDomiciliosFiltered = new ArrayList<>(lstClienteDomicilios);
        }
        
        this.actualizarListas();
        PrimeFaces.current().ajax().update("form:dt-domiciliosCliente");
    }
    
    public void nuevoClienteDomicilio() {
        clienteDomicilioSelected = new ClienteDomicilios();
        clienteDomicilioSelected.setCteCve(clienteSelected);
        clienteDomicilioSelected.setDomicilios(domicilioSelected);
    }
    
    public void limpiaClienteDomicilio() {
        clienteDomicilioSelected = new ClienteDomicilios();
        domicilioSelected = domicilios.nuevoDomicilio();
    }
    
    public void guardaClienteDomicilio() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Domicilios";
        try{
            log.info("Iniciando el guardando de domicilios");
            
            if(tipoDomicilioSelected == null)
                throw new InventarioException("Error, no se ha agregado un tipo domicilio.");
                
            if(domicilioSelected.getAsentamiento().getCp() == null)
                throw new InventarioException("Error, no se ha agregado un domicilio.");
            
            //List<ClienteDomicilios> listaDomiciliosCliente = DomiciliosBL.buscarDomiciliosPorCliente(clienteSelected);
            List<ClienteDomicilios> domicilioFiscal = domicilios.filtrarListadoDomicilioFiscal(lstClienteDomiciliosFiltered);
            
            for(ClienteDomicilios aux : domicilioFiscal)
                log.info("Domicilio Fiscal: {}", aux.toString());

            if(domicilioFiscal != null && !domicilioFiscal.isEmpty()) {
                nuevoClienteDomicilio();
                throw new InventarioException("Ya existe un domicilio fiscal registrado para el cliente.");
            }
            
            domicilioSelected = domicilios.guardaDomicilio(domicilioSelected, tipoDomicilioSelected);
            nuevoClienteDomicilio();
            
            lstClienteDomicilios = domicilios.guardarClienteDomicilio(lstClienteDomicilios, clienteDomicilioSelected);
            limpiaClienteDomicilio();
            
            mensaje = "Se agrego correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Problema para guardar la información de domicilios...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch(NullPointerException ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            mensaje = "Hay un problema para actualizar la información de domicilios.";
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            log.error("Problema para guardar la información de domicilios...", ex);
            mensaje = "Hay un problema para guardar la información de domicilios.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.actualizarListas();
            this.filtraListadoDomicilio();
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente", "form:tabView:addDireccionCliente");
        }
    }
    
    public void actualizaClienteDomicilio(){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Domicilios";
        try{
            log.info("Iniciando la actualizacion de domicilios");
            
            clienteDomicilioSelected = domicilios.actualizarDomicilios(clienteSelected, clienteDomicilioSelected, domicilioSelected, tipoDomicilioSelected);
            
            mensaje = "Se actualizo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch(NullPointerException ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            mensaje = "Hay un problema para actualizar la información de domicilios.";
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            mensaje = "Hay un problema para actualizar la información de domicilios.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity != null ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.actualizarListas();
            this.filtraListadoDomicilio();
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente");
        }
    }
    
    public void eliminaClienteDomicilio(){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Domicilios";
        try{
            log.info("Iniciando la eliminacion de 1 domicilio");
            
            lstClienteDomicilios = domicilios.eliminarClienteDomicilio(lstClienteDomicilios, clienteDomicilioSelected, clienteSelected);
            
            mensaje = "Se elimino correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Problema para eliminar 1 domicilios...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch(NullPointerException ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            mensaje = "Hay un problema para actualizar la información de domicilios.";
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            log.error("Problema para eliminar 1 domicilios...", ex);
            mensaje = "Hay un problema para eliminar 1 domicilios.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.actualizarListas();
            this.filtraListadoDomicilio();
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente");	
        }
    }
    
    public void asignarDomicilio() {
    	FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Domicilio";
    	try {
            if(domicilioSelected.getAsentamiento() == null){
                throw new InventarioException("No hay objeto Domicilio asignado al cliente.");
            }
            
            log.info("Agregando / Actualizando información al cliente");
            log.debug("Domicilio seleccionado: {}", this.domicilioSelected.getAsentamiento().toString());
            
            mensaje = "Nuevo domicilio seleccionado.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch(InventarioException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para seleccionar un domicilio...", ex);
            mensaje = "Problema para seleccionar un domicilio.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:panel-addClienteDireccion");
        }
    }
    
    public List<AsentamientoHumano> sugerenciasCodigoPostal(String consulta){
        List<AsentamientoHumano> listaSugerencias = asentamientoHumanoDAO.buscaPorCP(consulta);
        return listaSugerencias;
    }

    public List<Cliente> getLstClientes() {
        return lstClientes;
    }

    public void setLstClientes(List<Cliente> lstClientes) {
        this.lstClientes = lstClientes;
    }

    public List<Cliente> getLstClientesSelected() {
        return lstClientesSelected;
    }

    public void setLstClientesSelected(List<Cliente> lstClientesSelected) {
        this.lstClientesSelected = lstClientesSelected;
    }

    public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public void setClienteSelected(Cliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    public List<ClienteDomicilios> getLstClienteDomicilios() {
        return lstClienteDomicilios;
    }

    public List<ClienteDomicilios> getLstClienteDomiciliosFiltered() {
        return lstClienteDomiciliosFiltered;
    }

    public void setLstClienteDomiciliosFiltered(List<ClienteDomicilios> lstClienteDomiciliosFiltered) {
        this.lstClienteDomiciliosFiltered = lstClienteDomiciliosFiltered;
    }

    public void setLstClienteDomicilios(List<ClienteDomicilios> lstClienteDomicilios) {
        this.lstClienteDomicilios = lstClienteDomicilios;
    }

    public ClienteDomicilios getClienteDomicilioSelected() {
        return clienteDomicilioSelected;
    }

    public void setClienteDomicilioSelected(ClienteDomicilios clienteDomicilioSelected) {
        this.clienteDomicilioSelected = clienteDomicilioSelected;
    }

    public List<TiposDomicilio> getLstTiposDomicilio() {
        return lstTiposDomicilio;
    }

    public void setLstTiposDomicilio(List<TiposDomicilio> lstTiposDomicilio) {
        this.lstTiposDomicilio = lstTiposDomicilio;
    }

    public TiposDomicilio getTipoDomicilioSelected() {
        return tipoDomicilioSelected;
    }

    public void setTipoDomicilioSelected(TiposDomicilio tipoDomicilioSelected) {
        this.tipoDomicilioSelected = tipoDomicilioSelected;
    }

    public List<Domicilios> getLstDomicilios() {
        return lstDomicilios;
    }

    public void setLstDomicilios(List<Domicilios> lstDomicilios) {
        this.lstDomicilios = lstDomicilios;
    }

    public Domicilios getDomicilioSelected() {
        return domicilioSelected;
    }

    public void setDomicilioSelected(Domicilios domicilioSelected) {
        this.domicilioSelected = domicilioSelected;
    }
        
}
