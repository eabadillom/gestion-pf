package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import mx.com.ferbo.dao.AsentamientoHumanoDAO;

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.DomiciliosDAO;
import mx.com.ferbo.dao.TiposDomicilioDAO;
import mx.com.ferbo.model.AsentamientoHumano;

import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.TiposDomicilio;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class DomiciliosBean implements Serializable 
{
    /**
     * @author Juan_Cervantes
     */
    private static final long serialVersionUID = 4150584435387992139L;
    private static Logger log = LogManager.getLogger(DomiciliosBean.class);

    /**
    * Objetos para clientes
    */
    private List<Cliente> lstClientes;
    private Cliente clienteSelected;
    private ClienteDAO clienteDAO;
    
    /**
    * Objetos para domicilio_cliente
    */
    private List<ClienteDomicilios> lstClienteDomicilios;
    private List<ClienteDomicilios> lstClienteDomiciliosFiltered;
    private ClienteDomicilios clienteDomicilioSelected;
    private ClienteDomiciliosDAO clienteDomiciliosDAO;
    
    /**
    * Objetos para tipos_domicilio
    */
    private List<TiposDomicilio> lstTiposDomicilio;
    private TiposDomicilio tipoDomicilioSelected;
    private TiposDomicilioDAO tiposDomicilioDAO;
    
    /**
    * Objetos para domicilio
    */
    private List<Domicilios> lstDomicilios;
    private List<Domicilios> lstDomiciliosFiltered;
    private Domicilios domicilioSelected;
    private DomiciliosDAO domiciliosDAO;
    
    private AsentamientoHumanoDAO asentamientoHumanoDAO;
   
    
    /** 
     * Domicilio nuevo
    */
    private Domicilios domicilioNuevo;
    private String domicilioNvoCalle;
    private String domicilioNvoNumExt;
    private String domicilioNvoNumInt;
    private String domicilioNvoTel1;
    private String domicilioNvoTel2;
    private String domicilioNvoFax;
    
    public DomiciliosBean() {
        clienteDAO = new ClienteDAO();
        clienteDomiciliosDAO = new ClienteDomiciliosDAO();
        tiposDomicilioDAO = new TiposDomicilioDAO();
        domiciliosDAO = new DomiciliosDAO();
        asentamientoHumanoDAO = new AsentamientoHumanoDAO();
        
        lstClienteDomiciliosFiltered = new ArrayList<>();
        lstDomiciliosFiltered = new ArrayList<>();

        lstClientes = clienteDAO.buscarTodos();
        lstTiposDomicilio = tiposDomicilioDAO.buscarTodos();

        lstClienteDomicilios = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        lstClientes = clienteDAO.buscarTodos();
        lstTiposDomicilio = tiposDomicilioDAO.buscarTodos();
        lstDomicilios = domiciliosDAO.buscarTodos();
        lstClienteDomicilios = new ArrayList<>();
    }
    
    /**
    * Método para filtrar del listado original por clave de cliente
    */
    public void filtraListado() {
        lstClienteDomiciliosFiltered.clear();
        lstClienteDomicilios = clienteDomiciliosDAO.buscaPorCliente(clienteSelected);
        lstClienteDomiciliosFiltered = lstClienteDomicilios.stream()
            .filter(ps -> clienteSelected != null ? (ps.getCteCve().getCteCve().intValue() == clienteSelected.getCteCve().intValue()) : false)
            .collect(Collectors.toList());
        PrimeFaces.current().ajax().update("form:soClienteTipoDom");
    }
    
    /**
    * Método para filtrar del listado original por tipo de domicilio
    */
    public void filtraListadoDomicilio() {
        lstClienteDomiciliosFiltered.clear();
        lstClienteDomiciliosFiltered = lstClienteDomicilios.stream()
            .filter(ps -> tipoDomicilioSelected != null ? (ps.getDomicilios().getDomicilioTipoCve().getDomicilioTipoCve() == tipoDomicilioSelected.getDomicilioTipoCve() 
                && ps.getCteCve().getCteCve().intValue() == clienteSelected.getCteCve().intValue()) : false)
            .collect(Collectors.toList());
        PrimeFaces.current().ajax().update("form:buscarClienteDomicilio");
    }
    
    /**
    * Métodos para guardar objeto tipo ClienteDomicilio
    */
    public void nuevoClienteDomicilio() {
        clienteDomicilioSelected = new ClienteDomicilios();
        clienteDomicilioSelected.setCteCve(clienteSelected);
        clienteDomicilioSelected.setDomicilios(new Domicilios());
    }
    
    public void limpiaClienteDomicilio() {
        clienteDomicilioSelected = new ClienteDomicilios();
        domicilioSelected = new Domicilios();
        domicilioNvoCalle = "";
        domicilioNvoFax = "";
        domicilioNvoNumExt = "";
        domicilioNvoNumInt = "";
        domicilioNvoTel1 = "";
        domicilioNvoTel2 = "";
    }

    public void guardaClienteDomicilio() {
        List<ClienteDomicilios> listaDomiciliosCliente = clienteDomiciliosDAO.buscaPorCliente(clienteSelected);
        List<ClienteDomicilios> domicilioFiscal = listaDomiciliosCliente
                        .stream().filter(ps -> ps.getDomicilios().getDomicilioTipoCve().getDomicilioTipoCve() == tipoDomicilioSelected
                        .getDomicilioTipoCve()).collect(Collectors.toList());

        if(domicilioFiscal != null && domicilioFiscal.size() > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe un domicilio fiscal registrado para el cliente."));
                PrimeFaces.current().ajax().update("form:messages");
                nuevoClienteDomicilio();
                return;
        }

        nuevoDomicilio();
        if (clienteDomiciliosDAO.guardar(clienteDomicilioSelected) == null) {
                lstClienteDomiciliosFiltered.add(clienteDomicilioSelected);
                lstClienteDomicilios.add(clienteDomicilioSelected);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Domicilio Agregado"));
        } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error al intentar guardar el Domicilio"));
        }
        clienteDomicilioSelected = new ClienteDomicilios();
        domicilioNuevo = new Domicilios();
        limpiaClienteDomicilio();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-domiciliosCliente", "form:addDireccionCliente");
    }
    
    public void nuevoDomicilio() {
        log.info("Agregando nuevo domicilio!!!");
        if(this.tipoDomicilioSelected == null || this.tipoDomicilioSelected.getDomicilioTipoCve() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un tipo de domicilio."));
            return;
        }
        domicilioNuevo = new Domicilios();
        domicilioNuevo.setAsentamiento(domicilioSelected.getAsentamiento());
        domicilioNuevo.setDomicilioCp(domicilioSelected.getAsentamiento().getCp());
        domicilioNuevo.setDomicilioCalle(domicilioNvoCalle);
        domicilioNuevo.setDomicilioFax(domicilioNvoFax);
        domicilioNuevo.setDomicilioNumExt(domicilioNvoNumExt);
        domicilioNuevo.setDomicilioNumInt(domicilioNvoNumInt);
        domicilioNuevo.setDomicilioTel1(domicilioNvoTel1);
        domicilioNuevo.setDomicilioTel2(domicilioNvoTel2);
        domicilioNuevo.setDomicilioTipoCve(tipoDomicilioSelected);
        domiciliosDAO.guardar(domicilioNuevo);
        nuevoClienteDomicilio();
    }

    /**
    * Métodos para actualizar objeto tipo Domicilios
    */
    public void actualizaDomicilio() {
        log.info("Actualizando 1 domicilio!!!");
        domicilioNuevo = new Domicilios();
        domicilioNuevo.setDomCve(domicilioSelected.getDomCve());
        domicilioNuevo.setAsentamiento(domicilioSelected.getAsentamiento());
        domicilioNuevo.setDomicilioCp(domicilioSelected.getAsentamiento().getCp());
        domicilioNuevo.setDomicilioCalle(domicilioNvoCalle);
        domicilioNuevo.setDomicilioFax(domicilioNvoFax);
        domicilioNuevo.setDomicilioNumExt(domicilioNvoNumExt);
        domicilioNuevo.setDomicilioNumInt(domicilioNvoNumInt);
        domicilioNuevo.setDomicilioTel1(domicilioNvoTel1);
        domicilioNuevo.setDomicilioTel2(domicilioNvoTel1);
        domicilioNuevo.setDomicilioTipoCve(tipoDomicilioSelected);

        if (domiciliosDAO.actualizar(domicilioSelected) == null) {
            clienteDomicilioSelected.setCteCve(clienteSelected);
            clienteDomicilioSelected.setDomicilios(domicilioNuevo);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Domicilio del cliente actualizado"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error al intentar actualizar el domicilio"));
        }
        nuevoClienteDomicilio();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-domiciliosCliente");
    }

    /**
    * Método para eliminar objeto tipo Cliente Domicilio y Domicilio
    */
    public void eliminaClienteDomicilio() {
        clienteDomicilioSelected.setCteCve(clienteSelected);
        if (clienteDomiciliosDAO.eliminar(clienteDomicilioSelected) == null) {
            if (domiciliosDAO.eliminar(clienteDomicilioSelected.getDomicilios()) == null) {
                lstClienteDomiciliosFiltered.remove(clienteDomicilioSelected);
                lstClienteDomicilios.remove(clienteDomicilioSelected);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Domicilio eliminado"));
                PrimeFaces.current().ajax().update("form:messages", "form:dt-domiciliosCliente");
            } else {
                clienteDomiciliosDAO.guardar(clienteDomicilioSelected);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error al intentar eliminar el domicilio"));
            }
        } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error al intentar eliminar el domicilio"));
        }
        PrimeFaces.current().ajax().update("form:messages");
    }
    
    public void pintaActualiza() {
        if (clienteDomicilioSelected != null && clienteDomicilioSelected.getDomicilios() != null) {
            domicilioSelected = clienteDomicilioSelected.getDomicilios();
            domicilioNvoCalle = domicilioSelected.getDomicilioCalle();
            domicilioNvoFax = domicilioSelected.getDomicilioFax();
            domicilioNvoNumExt = domicilioSelected.getDomicilioNumExt();
            domicilioNvoNumInt = domicilioSelected.getDomicilioNumInt();
            domicilioNvoTel1 = domicilioSelected.getDomicilioTel1();
            domicilioNvoTel2 = domicilioSelected.getDomicilioTel2();
        }

        PrimeFaces.current().ajax().update("form:panel-addClienteDireccion");
    }
    
    public void asignarDomicilio() {
    	FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Domicilio";
    	
    	try {
            if(domicilioSelected.getAsentamiento() != null){
                throw new InventarioException("No hay objeto Domicilio asignado al cliente.");
            }
            
            log.info("Agregando / Actualizando información al cliente");
            log.debug("Domicilio seleccionado: {}", this.domicilioSelected.getAsentamiento().toString());
            
            mensaje = "Nuevo domicilio seleccionado.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch(InventarioException ex){
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
        return this.asentamientoHumanoDAO.buscaPorCP(consulta);
    }

    // Getters & Setters
    public List<Cliente> getLstClientes() {
        return lstClientes;
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

    public ClienteDomicilios getClienteDomicilioSelected() {
        return clienteDomicilioSelected;
    }

    public void setClienteDomicilioSelected(ClienteDomicilios clienteDomicilioSelected) {
        this.clienteDomicilioSelected = clienteDomicilioSelected;
    }

    public List<TiposDomicilio> getLstTiposDomicilio() {
        return lstTiposDomicilio;
    }

    public TiposDomicilio getTipoDomicilioSelected() {
        return tipoDomicilioSelected;
    }

    public void setTipoDomicilioSelected(TiposDomicilio tipoDomicilioSelected) {
        this.tipoDomicilioSelected = tipoDomicilioSelected;
    }

    public Domicilios getDomicilioNuevo() {
        return domicilioNuevo;
    }

    public void setDomicilioNuevo(Domicilios domicilioNuevo) {
        this.domicilioNuevo = domicilioNuevo;
    }

    public List<ClienteDomicilios> getLstClienteDomiciliosFiltered() {
        return lstClienteDomiciliosFiltered;
    }

    public void setLstClienteDomiciliosFiltered(List<ClienteDomicilios> lstClienteDomiciliosFiltered) {
        this.lstClienteDomiciliosFiltered = lstClienteDomiciliosFiltered;
    }

    public List<Domicilios> getLstDomicilios() {
        return lstDomicilios;
    }

    public void setLstDomicilios(List<Domicilios> lstDomicilios) {
        this.lstDomicilios = lstDomicilios;
    }

    public List<Domicilios> getLstDomiciliosFiltered() {
        return lstDomiciliosFiltered;
    }

    public void setLstDomiciliosFiltered(List<Domicilios> lstDomiciliosFiltered) {
        this.lstDomiciliosFiltered = lstDomiciliosFiltered;
    }

    public Domicilios getDomicilioSelected() {
        return domicilioSelected;
    }

    public void setDomicilioSelected(Domicilios domicilioSelected) {
        this.domicilioSelected = domicilioSelected;
    }

    public String getDomicilioNvoCalle() {
        return domicilioNvoCalle;
    }

    public void setDomicilioNvoCalle(String domicilioNvoCalle) {
        this.domicilioNvoCalle = domicilioNvoCalle;
    }

    public String getDomicilioNvoNumExt() {
        return domicilioNvoNumExt;
    }

    public void setDomicilioNvoNumExt(String domicilioNvoNumExt) {
        this.domicilioNvoNumExt = domicilioNvoNumExt;
    }

    public String getDomicilioNvoNumInt() {
        return domicilioNvoNumInt;
    }

    public void setDomicilioNvoNumInt(String domicilioNvoNumInt) {
        this.domicilioNvoNumInt = domicilioNvoNumInt;
    }

    public String getDomicilioNvoTel1() {
        return domicilioNvoTel1;
    }

    public void setDomicilioNvoTel1(String domicilioNvoTel1) {
        this.domicilioNvoTel1 = domicilioNvoTel1;
    }

    public String getDomicilioNvoTel2() {
        return domicilioNvoTel2;
    }

    public void setDomicilioNvoTel2(String domicilioNvoTel2) {
        this.domicilioNvoTel2 = domicilioNvoTel2;
    }

    public String getDomicilioNvoFax() {
        return domicilioNvoFax;
    }

    public void setDomicilioNvoFax(String domicilioNvoFax) {
        this.domicilioNvoFax = domicilioNvoFax;
    }

}
