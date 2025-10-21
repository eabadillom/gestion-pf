package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.stream.Collectors;

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

import mx.com.ferbo.business.clientes.FiscalBL;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.UsoCfdi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.clientes.ServiciosBL;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;

import mx.com.ferbo.business.clientes.ContactoBL;
import mx.com.ferbo.business.clientes.DomiciliosBL;
import mx.com.ferbo.controller.SideBarBean;
import mx.com.ferbo.dao.n.AsentamientoHumanoDAO;
import mx.com.ferbo.dto.ClientesDomiciliosOperacion;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.TipoTelefono;
import mx.com.ferbo.model.TiposDomicilio;
import mx.com.ferbo.util.FacesUtils;

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

    // Objetos para contactos
    @Inject
    ContactoBL contactoBL;

    private ClienteContacto clienteContactoSelected;
    private Contacto contactoSelected;
    private MedioCnt medioCntSelected;
    private Boolean editandoContacto;
    private List<TipoMail> lstTipoMail;
    private List<TipoTelefono> lstTipoTelefono;

    // Objetos para Fiscal
    @Inject
    private FiscalBL fiscalBL;

    private List<UsoCfdi> lstUsoCfdi;
    private List<UsoCfdi> lstUsoCfdiFiltered;
    private List<RegimenFiscal> lstRegimenFiscal;
    private List<RegimenFiscal> lstRegimenFiscalFiltered;
    private List<MetodoPago> lstMetodoPago;
    private List<MedioPago> lstMedioPago;

    // Objetos para Servicios
    @Inject
    private ServiciosBL serviciosBL;

    private PrecioServicio precioServicioSelected;
    private List<PrecioServicio> lstPrecioServicios;
    private Servicio servicioSelected;
    private List<Servicio> lstServicio;
    private UnidadDeManejo unidadDeManejoSelected;
    private List<UnidadDeManejo> lstUnidadManejo;
    
    //Objetos para domicilios
    @Inject
    private DomiciliosBL domicilios;
    
    @Inject
    private AsentamientoHumanoDAO asentamientoHumanoDAO;
    
    private List<ClienteDomicilios> lstClienteDomicilios;
    private List<ClienteDomicilios> lstClienteDomiciliosFiltered;
    private List<ClientesDomiciliosOperacion> lstDomiciliosOperacion;
    private List<TiposDomicilio> lstTiposDomicilio;
    private ClienteDomicilios clienteDomicilioSelected;
    private TiposDomicilio tipoDomicilioSelected;
    private Domicilios domicilioSelected;

    public ClientesBean() {
        this.lstClientes = new ArrayList<>();
        this.clienteSelected = new Cliente();
        this.lstClienteDomicilios = new ArrayList<>();
        this.lstClienteDomiciliosFiltered = new ArrayList<>();
        this.lstDomiciliosOperacion = new ArrayList<>();
        this.lstTiposDomicilio = new ArrayList<>();
        this.lstRegimenFiscalFiltered = new ArrayList<>();
        this.lstUsoCfdiFiltered = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        this.lstClientes = clienteDAO.buscarTodos();
        this.lstMedioPago = fiscalBL.obtenerMediosPago();
        this.lstMetodoPago = fiscalBL.obtenerMetodosPago();
        this.lstTipoMail = contactoBL.obtenerTiposMail();
        this.lstTipoTelefono = contactoBL.obtenerTiposTelefono();
        this.lstServicio = serviciosBL.obtenerServicios();
        this.lstUnidadManejo = serviciosBL.obtenerUnidadesMenjo();
        this.lstPlanta = avisoBL.obtenerPlantas();
        this.lstCategoria = avisoBL.obtenerCategorias();
        this.lstRegimenFiscal = fiscalBL.obtenerRegimenesFiscales();
        this.lstUsoCfdi = fiscalBL.obtenerCfdis();
    }

    public void cargarInfoCliente(Cliente cliente) {
        try {
            this.limpiarListasDomicilios();
            if (cliente == null) {
                throw new InventarioException("El cliente no puede ser vacío");
            }

            if (cliente.getCteCve() == null) {
                throw new InventarioException("El cliente no tiene datos todavida");
            }
            this.clienteSelected = clienteDAO.obtenerPorId(cliente.getCteCve(), true);
            fiscalBL.validarInfoFiscal(this.clienteSelected);
            lstRegimenFiscalFiltered = fiscalBL.filtrarRegimenesFiscales(this.lstRegimenFiscal, this.clienteSelected);
            lstUsoCfdiFiltered = fiscalBL.filtrarCfdis(this.lstUsoCfdi, this.clienteSelected);
            
            this.actualizarListasDomicilios();
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Fiscal", "Información fiscal cargada");
        } catch (InventarioException ex) {
            log.warn(ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Cargar informacion", ex.getMessage());
        } catch (Exception e) {
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Cargar informacion",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void clonarCliente() {
    }

    public void eliminarCliente() {
    }

    public void nuevoCliente() {
        tipoDomicilioSelected = new TiposDomicilio();
        lstTiposDomicilio = domicilios.buscarTiposDomicilios();
        lstRegimenFiscal = fiscalBL.obtenerRegimenesFiscales();
        lstClienteDomicilios.clear();
        lstClienteDomiciliosFiltered.clear();
        lstDomiciliosOperacion.clear();
    }

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
    
    public void nuevoPrecioAviso(){
        this.precioAvisoSelected = new PrecioServicio();
        this.precioAvisoSelected.setAvisoCve(avisoSelected);
        this.precioAvisoSelected.setServicio(new Servicio());
        this.precioAvisoSelected.setUnidad(new UnidadDeManejo());
        this.precioAvisoSelected.setPrecio(BigDecimal.ZERO);
    }
    
    public void copiarPrcioAviso(PrecioServicio precioServicio){
        this.precioAvisoSelected = precioServicio;
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
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Aviso", mensaje);
        } catch (InventarioException ex) {
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Aviso", ex.getMessage());
        } catch (Exception ex) {
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Aviso",
                    "Contacte con el admistrador del sistema.");
        } finally {
             PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void validarCodigoUnico(Cliente cliente) {
        try {
            this.clienteSelected = cliente;
            Cliente cliente2 = clienteDAO.buscarPorCodigoUnico(this.clienteSelected.getCodUnico());
            fiscalBL.validarCodigoUnico(this.clienteSelected, cliente2);
        } catch (InventarioException ex) {
            log.warn(ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Fiscal", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error inesperado. Causado por: ", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Fiscal",
                    "Contacte con el admistrador del sistema.");
        } finally {
             PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void validarRFC(Cliente cliente) {
        try {
            this.clienteSelected = cliente;
            fiscalBL.validarRFC(this.clienteSelected);
        } catch (InventarioException ex) {
            log.warn(ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Fiscal", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error inesperado. Causado por: ", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Fiscal",
                    "Contacte con el admistrador del sistema.");
        } finally {
             PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtrarRegimenesFiscales(){
        try {
            this.lstRegimenFiscalFiltered.clear();
            this.lstUsoCfdiFiltered.clear();
            
            this.lstRegimenFiscalFiltered = fiscalBL.filtrarRegimenesFiscales(this.lstRegimenFiscal, this.clienteSelected);
            this.lstUsoCfdiFiltered = fiscalBL.filtrarCfdis(this.lstUsoCfdi, this.clienteSelected);
        } catch (InventarioException ex) {
            log.warn(ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Fiscal", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error inesperado. Causado por: ", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Fiscal",
                    "Contacte con el admistrador del sistema.");
        } finally {
             PrimeFaces.current().ajax().update("form:messages");
        }
    }

    // Metodos exclusivos de servicios
    public void nuevoPrecioServicio() {
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

    public void operarServicios(String operacion) {
        String mensaje = null;
        try {
            switch (operacion) {
                case "agregarprecioservicio":
                    serviciosBL.agregarOActulizarPrecioServicio(this.clienteSelected, this.precioServicioSelected);
                    mensaje = "Servicio agregado";
                    break;
                case "eliminarprecioservicio":
                    serviciosBL.eliminarPrecioServicio(this.clienteSelected, this.precioServicioSelected);
                    mensaje = "Servicio eliminado";
                    break;
            }
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Servicio", mensaje);
        } catch (InventarioException ex) {
            log.warn(ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Servicio", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Servicio",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
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
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Contacto", mensaje);
        } catch (InventarioException ex) {
            log.warn(ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Contacto", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Contacto", "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void seleccionarMedioContacto() {
        try {
            contactoBL.seleccionarMedioContacto(this.medioCntSelected);
        } catch (InventarioException ex) {
            log.warn(ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Contacto", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Contacto", "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    //Método para domicilios del cliente
    public void cargaInfoDomicilio(){
        if (clienteDomicilioSelected != null && clienteDomicilioSelected.getDomicilios() != null) {
            domicilioSelected = clienteDomicilioSelected.getDomicilios();
        }
    }
    
    public void actualizarListasDomicilios() {
        lstClienteDomicilios = domicilios.buscarDomiciliosPorCliente(clienteSelected);
        
        lstDomiciliosOperacion = lstClienteDomicilios.stream()
            .map(cd -> new ClientesDomiciliosOperacion(cd, ClientesDomiciliosOperacion.EstadoOperacion.EXISTENTE))
            .collect(Collectors.toList());
        
        lstTiposDomicilio = domicilios.buscarTiposDomicilios();
    }
    
    public void filtraListadoDomicilio() {
        lstClienteDomicilios = domicilios.filtrarListado(lstClienteDomicilios, clienteSelected);
        PrimeFaces.current().ajax().update("form:soClienteTipoDom", "form:dt-domiciliosCliente");
    }
    
    public void filtraListadoDomicilioFiltered() {
        if (tipoDomicilioSelected != null) {
            lstClienteDomiciliosFiltered = domicilios.filtrarListadoDomicilio(lstClienteDomicilios, tipoDomicilioSelected, clienteSelected);
        } else {
            tipoDomicilioSelected = null;
            
            if (lstClienteDomiciliosFiltered != null) {
                lstClienteDomiciliosFiltered.clear();
            } else {
                lstClienteDomiciliosFiltered = new ArrayList<>();
            }
        }
        
        PrimeFaces.current().ajax().update("form:dt-domiciliosCliente");
    }
    
    public void limpiarListasDomicilios(){
        lstClienteDomicilios.clear();
        lstClienteDomiciliosFiltered.clear();
        lstDomiciliosOperacion.clear();
        lstTiposDomicilio.clear();
        tipoDomicilioSelected = null;
    }
    
    public void limpiaClienteDomicilio() {
        clienteDomicilioSelected = new ClienteDomicilios();
        domicilioSelected = domicilios.nuevoDomicilio();
    }
    
    public void agregarClienteDomicilio() {
        try{
            log.info("Iniciando el guardando de un domicilio");
            
            if(tipoDomicilioSelected == null)
                throw new InventarioException("Error, no se ha agregado un tipo domicilio.");
                
            if(domicilioSelected.getAsentamiento().getCp() == null)
                throw new InventarioException("Error, no se ha agregado un domicilio.");
            
            List<ClienteDomicilios> domicilioFiscal = domicilios.filtrarListadoDomicilioFiscal(lstClienteDomiciliosFiltered);
            
            for(ClienteDomicilios aux : domicilioFiscal)
                log.info("Domicilio Fiscal: {}", aux.toString());

            if(domicilioFiscal != null && !domicilioFiscal.isEmpty()) {
                clienteDomicilioSelected = domicilios.nuevoClienteDomicilio(clienteSelected, domicilioSelected);
                throw new InventarioException("Ya existe un domicilio fiscal registrado para el cliente.");
            }
            
            domicilioSelected = domicilios.agregarDomicilio(domicilioSelected, tipoDomicilioSelected);
            log.info("Domicilio agregado: {}", domicilioSelected.toString());
            clienteDomicilioSelected = domicilios.nuevoClienteDomicilio(clienteSelected, domicilioSelected);
            
            lstClienteDomicilios.add(clienteDomicilioSelected);
            ClientesDomiciliosOperacion nuevo = new ClientesDomiciliosOperacion(clienteDomicilioSelected, ClientesDomiciliosOperacion.EstadoOperacion.NUEVO);
            lstDomiciliosOperacion.add(nuevo);
            limpiaClienteDomicilio();
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Domicilios", "Se agrego correctamente");
        } catch (InventarioException ex) {
            log.error("Problema para guardar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Domicilios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para guardar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Domicilios", "Hay un problema para guardar la información de domicilios.");
        } finally {
            this.filtraListadoDomicilioFiltered();
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente");
        }
    }
    
    public void actualizaClienteDomicilio(){
        try{
            log.info("Iniciando la actualizacion de un domicilio");
            
            if(clienteDomicilioSelected == null){
                throw new InventarioException("Error al actualizar el domicilio de la lista.");
            }
            
            clienteDomicilioSelected = domicilios.actualizarDomicilios(clienteSelected, clienteDomicilioSelected, domicilioSelected, tipoDomicilioSelected);
            
            this.lstDomiciliosOperacion.stream()
                .filter(domOp -> domOp.getClienteDomicilios().equals(clienteDomicilioSelected)) // Encuentra el wrapper
                .findFirst()
                .ifPresent(wrapper -> {
                    if (wrapper.getEstado() == ClientesDomiciliosOperacion.EstadoOperacion.EXISTENTE) {
                        wrapper.setEstado(ClientesDomiciliosOperacion.EstadoOperacion.ACTUALIZADO);
                        wrapper.setClienteDomicilios(clienteDomicilioSelected);
                    }
                    if (wrapper.getEstado() == ClientesDomiciliosOperacion.EstadoOperacion.NUEVO) {
                        wrapper.setClienteDomicilios(clienteDomicilioSelected);
                    }
                });
            
            lstClienteDomicilios = lstClienteDomicilios.stream()
                .map(aux -> aux.getDomicilios().getDomCve().equals(clienteDomicilioSelected.getDomicilios().getDomCve()) ? clienteDomicilioSelected : aux)
                .collect(Collectors.toList());
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Domicilios", "Se actualizo correctamente");
        } catch (InventarioException ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Domicilios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Domicilios", "Hay un problema para actualizar la información de domicilios.");
        } finally {
            this.filtraListadoDomicilioFiltered();
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente");
        }
    }
    
    public void eliminaClienteDomicilio(){
        try{
            log.info("Iniciando la eliminacion de un domicilio");
            
            if(clienteDomicilioSelected == null){
                throw new InventarioException("Error al eliminar el domicilio de la lista.");
            }
            
            lstDomiciliosOperacion.stream()
                .filter(domOp -> domOp.getClienteDomicilios().getId().equals(clienteDomicilioSelected.getId()))
                .findFirst()
                .ifPresent(wrapper -> {
                    if (wrapper.getEstado() == ClientesDomiciliosOperacion.EstadoOperacion.NUEVO) {
                        log.info("Domicilio nuevo marcado para ser removido.");
                    } else {
                        wrapper.setEstado(ClientesDomiciliosOperacion.EstadoOperacion.ELIMINADO_TEMP);
                        log.info("Domicilio existente marcado para eliminación temporal.");
                    }
                });
            
            lstDomiciliosOperacion = lstDomiciliosOperacion.stream()
                .filter(wrapper -> !(wrapper.getEstado() == ClientesDomiciliosOperacion.EstadoOperacion.NUEVO && 
                                      wrapper.getClienteDomicilios().getId().equals(clienteDomicilioSelected.getId())))
                .collect(Collectors.toList());
            
            lstClienteDomicilios.remove(clienteDomicilioSelected);
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Domicilios", "Se elimino correctamente");
        } catch (InventarioException ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Domicilios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para eliminar 1 domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Domicilios", "Hay un problema para eliminar 1 domicilios.");
        } finally {
            this.filtraListadoDomicilioFiltered();
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente");	
        }
    }
    
    public void guardarCliente(){
        try{
            log.info("Iniciando con los cambios del cliente {}", this.clienteSelected.toString());
            
            domicilios.persistirCambios(lstDomiciliosOperacion);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Cliente", "Actualizando datos del cliente");
        } catch (InventarioException ex) {
            log.error("Problema para actualizar la información del cliente...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Cliente", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para actualizar la información del cliente...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Cliente", "Hay un problema para actualizar la información del cliente.");
        } finally {
            lstDomiciliosOperacion = new ArrayList<>();
            this.actualizarListasDomicilios();
            this.filtraListadoDomicilioFiltered();
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void asignarDomicilio() {
    	try {
            if(domicilioSelected.getAsentamiento() == null){
                throw new InventarioException("No hay objeto Domicilio asignado al cliente.");
            }
            
            log.info("Agregando / Actualizando información al cliente");
            log.debug("Domicilio seleccionado: {}", this.domicilioSelected.getAsentamiento().toString());
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Domicilios", "Domicilio seleccionado.");
        } catch(InventarioException ex) {
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Domicilios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para seleccionar un domicilio...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Domicilios", "Problema para seleccionar un domicilio.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public List<AsentamientoHumano> sugerenciasCodigoPostal(String consulta){
        List<AsentamientoHumano> listaSugerencias = asentamientoHumanoDAO.buscaPorCP(consulta);
        return listaSugerencias;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
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

    // Getter y Setter para Fiscal
    public List<UsoCfdi> getLstUsoCfdi() {
        return lstUsoCfdi;
    }

    public void setLstUsoCfdi(List<UsoCfdi> lstUsoCfdi) {
        this.lstUsoCfdi = lstUsoCfdi;
    }

    public List<UsoCfdi> getLstUsoCfdiFiltered() {
        return lstUsoCfdiFiltered;
    }

    public void setLstUsoCfdiFiltered(List<UsoCfdi> lstUsoCfdiFiltered) {
        this.lstUsoCfdiFiltered = lstUsoCfdiFiltered;
    }

    public List<RegimenFiscal> getLstRegimenFiscal() {
        return lstRegimenFiscal;
    }

    public void setLstRegimenFiscal(List<RegimenFiscal> lstRegimenFiscal) {
        this.lstRegimenFiscal = lstRegimenFiscal;
    }

    public List<RegimenFiscal> getLstRegimenFiscalFiltered() {
        return lstRegimenFiscalFiltered;
    }

    public void setLstRegimenFiscalFiltered(List<RegimenFiscal> lstRegimenFiscalFiltered) {
        this.lstRegimenFiscalFiltered = lstRegimenFiscalFiltered;
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

    // Getters y Setters para Precio servicios
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
    
    //Getters y Setters para domicilios
    public List<ClienteDomicilios> getLstClienteDomicilios() {
        return lstClienteDomicilios;
    }

    public void setLstClienteDomicilios(List<ClienteDomicilios> lstClienteDomicilios) {
        this.lstClienteDomicilios = lstClienteDomicilios;
    }

    public List<ClienteDomicilios> getLstClienteDomiciliosFiltered() {
        return lstClienteDomiciliosFiltered;
    }

    public void setLstClienteDomiciliosFiltered(List<ClienteDomicilios> lstClienteDomiciliosFiltered) {
        this.lstClienteDomiciliosFiltered = lstClienteDomiciliosFiltered;
    }

    public List<ClientesDomiciliosOperacion> getLstDomiciliosOperacion() {
        return lstDomiciliosOperacion;
    }

    public void setLstDomiciliosOperacion(List<ClientesDomiciliosOperacion> lstDomiciliosOperacion) {
        this.lstDomiciliosOperacion = lstDomiciliosOperacion;
    }

    public List<TiposDomicilio> getLstTiposDomicilio() {
        return lstTiposDomicilio;
    }

    public void setLstTiposDomicilio(List<TiposDomicilio> lstTiposDomicilio) {
        this.lstTiposDomicilio = lstTiposDomicilio;
    }

    public ClienteDomicilios getClienteDomicilioSelected() {
        return clienteDomicilioSelected;
    }

    public void setClienteDomicilioSelected(ClienteDomicilios clienteDomicilioSelected) {
        this.clienteDomicilioSelected = clienteDomicilioSelected;
    }

    public TiposDomicilio getTipoDomicilioSelected() {
        return tipoDomicilioSelected;
    }

    public void setTipoDomicilioSelected(TiposDomicilio tipoDomicilioSelected) {
        this.tipoDomicilioSelected = tipoDomicilioSelected;
    }

    public Domicilios getDomicilioSelected() {
        return domicilioSelected;
    }

    public void setDomicilioSelected(Domicilios domicilioSelected) {
        this.domicilioSelected = domicilioSelected;
    }
    //</editor-fold>
    
}
