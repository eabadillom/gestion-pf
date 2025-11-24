package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.model.Usuario;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.business.n.AvisoBL;
import mx.com.ferbo.business.n.CategoriaBL;
import mx.com.ferbo.business.n.ClienteBL;
import mx.com.ferbo.business.n.ClienteContactoBL;
import mx.com.ferbo.business.n.DomiciliosBL;
import mx.com.ferbo.business.n.FiscalBL;
import mx.com.ferbo.business.n.MedioContactoBL;
import mx.com.ferbo.business.n.MedioPagoBL;
import mx.com.ferbo.business.n.MetodoPagoBL;
import mx.com.ferbo.business.n.PlantaBL;
import mx.com.ferbo.business.n.PrecioServicioBL;
import mx.com.ferbo.business.n.SeguridadBL;
import mx.com.ferbo.business.n.ServicioBL;
import mx.com.ferbo.business.n.UnidadManejoBL;
import mx.com.ferbo.controller.SideBarBean;
import mx.com.ferbo.dao.n.AsentamientoHumanoDAO;
import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.TipoTelefono;
import mx.com.ferbo.model.TiposDomicilio;
import mx.com.ferbo.util.FacesUtils;

@Named
@ViewScoped
public class ClientesBean implements Serializable {

    private static final long serialVersionUID = 8438449261015571241L;
    private static final Logger log = LogManager.getLogger(ClientesBean.class);

    // Side bar
    @Inject
    private SideBarBean sideBar;

    // Objetos de clientes
    @Inject
    private ClienteBL clienteBL;

    private Cliente clienteSelected;
    private Cliente clonarCliente;
    private List<Cliente> lstClientes;
    private Boolean estatusClientes = Boolean.TRUE;

    // Objetos para Avisos
    @Inject
    private AvisoBL avisoBL;

    private Aviso avisoSelected;
    private PrecioServicio precioAvisoSelected;
    private List<Planta> lstPlanta;
    private List<Categoria> lstCategoria;

    // Objetos para contactos
    @Inject
    ClienteContactoBL contactoBL;

    private ClienteContacto clienteContactoSelected;
    private Contacto contactoSelected;
    private Boolean editandoContacto;
    private MedioCnt medioCntSelected;
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
    private PrecioServicioBL serviciosBL;

    private PrecioServicio precioServicioSelected;
    private List<PrecioServicio> lstPrecioServicios;
    private Servicio servicioSelected;
    private List<Servicio> lstServicio;
    private UnidadDeManejo unidadDeManejoSelected;
    private List<UnidadDeManejo> lstUnidadManejo;

    // Objetos para domicilios
    @Inject
    private DomiciliosBL domicilios;

    @Inject
    private AsentamientoHumanoDAO asentamientoHumanoDAO;

    private List<ClienteDomicilios> lstClienteDomicilios;
    private List<ClienteDomicilios> lstClienteDomiciliosFiltered;
    private List<TiposDomicilio> lstTiposDomicilio;
    private ClienteDomicilios clienteDomicilioSelected;
    private TiposDomicilio tipoDomicilioSelected;
    private Domicilios domicilioSelected;

    // Objetos para seguridad
    @Inject
    private SeguridadBL seguridadBL;

    String nuevaContrasenia;
    String contraseniaConfirmacion;

    private Usuario usuario;
    private FacesContext context;
    private HttpServletRequest request;
    private String agregadoOActualizado;

    // Otros objetos para obtener listas
    @Inject
    private PlantaBL plantaBL;

    @Inject
    private MedioPagoBL medioPagoBL;

    @Inject
    private MetodoPagoBL metodoPagoBL;

    @Inject
    private CategoriaBL categoriaBL;

    @Inject
    private MedioContactoBL medioContactoBL;

    @Inject
    private UnidadManejoBL unidadManejoBL;

    @Inject
    private ServicioBL servicioBL;

    public ClientesBean() {
        this.lstClientes = new ArrayList<>();
        this.clienteSelected = new Cliente();
        this.lstClienteDomicilios = new ArrayList<>();
        this.lstClienteDomiciliosFiltered = new ArrayList<>();
        this.lstTiposDomicilio = new ArrayList<>();
        this.lstRegimenFiscalFiltered = new ArrayList<>();
        this.lstUsoCfdiFiltered = new ArrayList<>();
        this.context = FacesContext.getCurrentInstance();

    }

    @PostConstruct
    public void init() {
        try {
            request = (HttpServletRequest) context.getExternalContext().getRequest();
            this.usuario = (Usuario) request.getSession(true).getAttribute("usuario");
            log.info("El usuario {} ingresa al catálogo de clientes.", usuario.getUsuario());
            this.lstClientes = clienteBL.obtenerTodos();
            this.lstMedioPago = medioPagoBL.obtenerMediosPago();
            this.lstMetodoPago = metodoPagoBL.obtenerMetodosPago();
            this.lstTipoMail = medioContactoBL.obtenerTiposMail();
            this.lstTipoTelefono = medioContactoBL.obtenerTiposTelefono();
            this.lstServicio = servicioBL.obtenerServicios();
            this.lstUnidadManejo = unidadManejoBL.obtenerUnidadesManejo();
            this.lstPlanta = plantaBL.obtenerPlantas(Boolean.FALSE);
            this.lstCategoria = categoriaBL.obtenerCategorias();
            this.lstRegimenFiscal = fiscalBL.obtenerRegimenesFiscales();
            this.lstUsoCfdi = fiscalBL.obtenerCfdis();
        } catch (InventarioException ex) {
            log.warn("Error: " ,ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Cargar Información", ex.getMessage());
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void cargarInfoCliente(Cliente cliente) {
        try {
            this.limpiarListasDomicilios();
            if (cliente == null) {
                throw new InventarioException("El cliente no puede ser vacío");
            }

            this.clienteSelected = clienteBL.obtenerTodoCliente(cliente.getCteCve(), true);
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

    // Funciones para clientes
    public void nuevoCliente() {
        clienteSelected = new Cliente();
        clienteSelected.setMetodoPago(new MetodoPago());
        clienteSelected.setRegimenFiscal(new RegimenFiscal());
        clienteSelected.setUsoCfdi(new UsoCfdi());
        clienteSelected.setClienteContactoList(new ArrayList<>());
        clienteSelected.setAvisoList(new ArrayList<>());
        clienteSelected.setClienteDomiciliosList(new ArrayList<>());
        tipoDomicilioSelected = new TiposDomicilio();
        clienteSelected.setHabilitado(true);
        lstTiposDomicilio = domicilios.buscarTiposDomicilios();
        lstClienteDomicilios.clear();
        lstClienteDomiciliosFiltered.clear();
    }

    public void guardarOActualizarCliente() {
        String mensaje = null;
        try {
            fiscalBL.validarInfoFiscal(clienteSelected);
            mensaje = clienteBL.guardarOActualizar(clienteSelected);
            this.lstClientes = null;
            this.lstClientes = clienteBL.obtenerTodos();
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Cliente", mensaje);
        } catch (InventarioException ex) {
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Cliente", ex.getMessage());
        } catch (Exception ex) {
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Cliente",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public List<Cliente> filtrarPorEstatus() {
        return clienteBL.filtrarPorEstatus(lstClientes, estatusClientes);
    }

    public void verificarAgregadoOActualizado(Integer id) {
        agregadoOActualizado = (id != null) ? "actualizado" : "agregado";
    }

    // Funciones para Avisos
    public void nuevoAviso() {
        this.avisoSelected = avisoBL.nuevoAviso();
        this.avisoSelected.setCteCve(clienteSelected);
    }

    public void nuevoPrecioAviso() {
        this.precioAvisoSelected = avisoBL.nuevoServicioAviso(avisoSelected);
    }

    public void operarAvisos(String operacion) {
        String mensaje = null;
        try {
            switch (operacion) {
                case "agregaraviso":
                    verificarAgregadoOActualizado(avisoSelected.getAvisoCve());
                    avisoBL.agregarAviso(clienteSelected, avisoSelected);
                    mensaje = "Aviso " + agregadoOActualizado + " exitosamente";
                    break;

                case "agregarservicio":
                    verificarAgregadoOActualizado(precioAvisoSelected.getId());
                    avisoBL.agregarServicioAviso(avisoSelected, precioAvisoSelected);
                    mensaje = "Servico " + agregadoOActualizado + " a aviso exitosamente";
                    break;

                case "eliminaraviso":
                    avisoBL.eliminaAviso(clienteSelected, avisoSelected);
                    mensaje = "Aviso eliminado exitosamente";
                    break;

                case "eliminarservicio":
                    avisoBL.eliminaServicioAviso(avisoSelected, precioAvisoSelected);
                    mensaje = "Servicio de aviso eliminado exitosamente";
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
            Cliente cliente2 = clienteBL.obtenerPorCodigoUnico(this.clienteSelected.getCodUnico());
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

    public void validarRFC() {
        try {
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

    public void filtrarRegimenesFiscales() {
        try {
            this.lstRegimenFiscalFiltered.clear();
            this.lstUsoCfdiFiltered.clear();

            this.lstRegimenFiscalFiltered = fiscalBL.filtrarRegimenesFiscales(this.lstRegimenFiscal,
                    this.clienteSelected);
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
        precioServicioSelected = serviciosBL.nuevoPrecioServicio(clienteSelected);
    }

    public void operarServicios(String operacion) {
        String mensaje = null;
        try {
            switch (operacion) {
                case "agregarprecioservicio":
                    verificarAgregadoOActualizado(precioServicioSelected.getId());
                    serviciosBL.agregarOActualizarPrecioServicio(clienteSelected, precioServicioSelected);
                    mensaje = "Servicio " + agregadoOActualizado + " exitosamente";
                    break;
                case "eliminarprecioservicio":
                    serviciosBL.eliminarPrecioServicio(this.clienteSelected, this.precioServicioSelected);
                    mensaje = "Servicio eliminado exitosamente";
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
        this.clienteContactoSelected = contactoBL.nuevoClienteContacto();
    }

    public void nuevoMedioContacto() {
        this.medioCntSelected = medioContactoBL.nuevoMedio();
    }

    public void operarContactos(String operacion) {
        String mensaje = null;
        try {

            switch (operacion) {

                case "agregarcontacto":
                    verificarAgregadoOActualizado(clienteContactoSelected.getId());
                    contactoBL.agregarOActualizarContacto(this.clienteSelected, this.clienteContactoSelected);
                    mensaje = "Contacto " + agregadoOActualizado + " exitosamente";
                    break;

                case "agregarmedio":
                    verificarAgregadoOActualizado(medioCntSelected.getIdMedio());
                    contactoBL.agregarOActualizarMedioContacto(this.clienteContactoSelected, this.medioCntSelected);
                    mensaje = "Medio de contacto " + agregadoOActualizado + " exitosamente";
                    break;

                case "eliminarmedio":
                    contactoBL.eliminarMedioContacto(this.clienteContactoSelected, this.medioCntSelected);
                    mensaje = "Medio de contacto eliminado exitosamente";
                    break;

                case "eliminarcontacto":
                    contactoBL.eliminarContacto(this.clienteSelected, this.clienteContactoSelected);
                    mensaje = "Contacto eliminado exitosamente";
                    break;

                case "cambiarcontrasenia":
                    String contransenia = seguridadBL.cambiarContrasenia(nuevaContrasenia, contraseniaConfirmacion);
                    this.clienteContactoSelected.setNbPassword(contransenia);
                    mensaje = "Contraseña guardada exitosamente";
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
            medioContactoBL.seleccionarMedioContacto(this.medioCntSelected);
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

    // Método para domicilios del cliente
    public void cargaInfoDomicilio() {
        if (clienteDomicilioSelected != null && clienteDomicilioSelected.getDomicilios() != null) {
            domicilioSelected = clienteDomicilioSelected.getDomicilios();
        }
    }

    public void actualizarListasDomicilios() {
        lstClienteDomicilios = new ArrayList<>(clienteSelected.getClienteDomiciliosList());

        lstTiposDomicilio = domicilios.buscarTiposDomicilios();
    }

    public void filtraListadoDomicilioFiltered() {
        if (tipoDomicilioSelected.getDomicilioTipoCve() != null) {
            lstClienteDomiciliosFiltered = domicilios.filtrarListadoDomicilio(lstClienteDomicilios,
                    tipoDomicilioSelected, clienteSelected);
        } else {
            tipoDomicilioSelected = null;

            if (!lstClienteDomiciliosFiltered.isEmpty()) {
                lstClienteDomiciliosFiltered.clear();
            }
        }

        PrimeFaces.current().ajax().update("form:tabView:soClienteTipoDom", "form:tabView:dt-domiciliosCliente");
    }

    public void limpiarListasDomicilios() {
        lstClienteDomicilios.clear();
        lstClienteDomiciliosFiltered.clear();
        lstTiposDomicilio.clear();
        tipoDomicilioSelected = null;
    }

    public void limpiaClienteDomicilio() {
        clienteDomicilioSelected = new ClienteDomicilios();
        domicilioSelected = domicilios.nuevoDomicilio();
    }

    public void agregarClienteDomicilio() {
        try {
            log.info("Iniciando el guardando de un domicilio");

            if (tipoDomicilioSelected == null)
                throw new InventarioException("Error, no se ha agregado un tipo domicilio.");

            if (domicilioSelected.getAsentamiento().getCp() == null)
                throw new InventarioException("Error, no se ha agregado un domicilio.");

            List<ClienteDomicilios> domicilioFiscal = domicilios
                    .filtrarListadoDomicilioFiscal(lstClienteDomiciliosFiltered);

            for (ClienteDomicilios aux : domicilioFiscal)
                log.info("Domicilio Fiscal: {}", aux.toString());

            if (domicilioFiscal != null && !domicilioFiscal.isEmpty()) {
                clienteDomicilioSelected = domicilios.nuevoClienteDomicilio(clienteSelected, domicilioSelected);
                throw new InventarioException("Ya existe un domicilio fiscal registrado para el cliente.");
            }

            domicilioSelected = domicilios.agregarDomicilio(domicilioSelected, tipoDomicilioSelected);
            log.info("Domicilio agregado: {}", domicilioSelected.toString());
            clienteDomicilioSelected = domicilios.nuevoClienteDomicilio(clienteSelected, domicilioSelected);

            lstClienteDomiciliosFiltered.add(clienteDomicilioSelected);
            clienteSelected.setClienteDomiciliosList(lstClienteDomiciliosFiltered);
            limpiaClienteDomicilio();

            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Domicilios", "Se agrego correctamente");
        } catch (InventarioException ex) {
            log.error("Problema para guardar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Domicilios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para guardar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Domicilios",
                    "Hay un problema para guardar la información de domicilios.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente");
        }
    }

    public void actualizaClienteDomicilio() {
        try {
            log.info("Iniciando la actualizacion de un domicilio");

            if (clienteDomicilioSelected == null) {
                throw new InventarioException("Error al actualizar el domicilio de la lista.");
            }

            clienteDomicilioSelected = domicilios.actualizarDomicilios(clienteSelected, clienteDomicilioSelected,
                    domicilioSelected, tipoDomicilioSelected);

            lstClienteDomiciliosFiltered = lstClienteDomiciliosFiltered.stream()
                    .map(aux -> aux.getDomicilios().getDomCve().equals(
                            clienteDomicilioSelected.getDomicilios().getDomCve()) ? clienteDomicilioSelected : aux)
                    .collect(Collectors.toList());

            clienteSelected.setClienteDomiciliosList(lstClienteDomiciliosFiltered);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Domicilios", "Se actualizo correctamente");
        } catch (InventarioException ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Domicilios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Domicilios",
                    "Hay un problema para actualizar la información de domicilios.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente");
        }
    }

    public void eliminaClienteDomicilio() {
        try {
            log.info("Iniciando la eliminacion de un domicilio");

            if (clienteDomicilioSelected == null) {
                throw new InventarioException("Error al eliminar el domicilio de la lista.");
            }

            if (lstClienteDomiciliosFiltered == null || lstClienteDomiciliosFiltered.isEmpty()) {
                throw new InventarioException("El cliente no tiene domicilios registrados.");
            }

            // Buscar el domicilio en la lista y eliminarlo
            boolean eliminado = lstClienteDomiciliosFiltered
                    .removeIf(dom -> Objects.equals(dom.getId(), clienteDomicilioSelected.getId()));

            if (!eliminado) {
                log.info("Elemento no eliminado: {}", eliminado);
                throw new InventarioException("No se encontró el domicilio seleccionado en la lista.");
            }

            clienteSelected.setClienteDomiciliosList(lstClienteDomiciliosFiltered);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Domicilios", "Se elimino correctamente");
        } catch (InventarioException ex) {
            log.error("Problema para actualizar la información de domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Domicilios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para eliminar 1 domicilios...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Domicilios",
                    "Hay un problema para eliminar 1 domicilios.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages", "form:tabView:dt-domiciliosCliente");
        }
    }

    public void asignarDomicilio() {
        try {
            if (domicilioSelected.getAsentamiento() == null) {
                throw new InventarioException("No hay objeto Domicilio asignado al cliente.");
            }

            log.info("Agregando / Actualizando información al cliente");
            log.debug("Domicilio seleccionado: {}", this.domicilioSelected.getAsentamiento().toString());

            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Domicilios", "Domicilio seleccionado.");
        } catch (InventarioException ex) {
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Domicilios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para seleccionar un domicilio...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Domicilios", "Problema para seleccionar un domicilio.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public List<AsentamientoHumano> sugerenciasCodigoPostal(String consulta) {
        List<AsentamientoHumano> listaSugerencias = asentamientoHumanoDAO.buscaPorCP(consulta);
        return listaSugerencias;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters&Setters">
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

    public Cliente getClonarCliente() {
        return clonarCliente;
    }

    public void setClonarCliente(Cliente clonarCliente) {
        this.clonarCliente = clonarCliente;
    }

    public List<Cliente> getLstClientes() {
        return lstClientes;
    }

    public void setLstClientes(List<Cliente> lstClientes) {
        this.lstClientes = lstClientes;
    }

    public Boolean getEstatusClientes() {
        return estatusClientes;
    }

    public void setEstatusClientes(Boolean estatusClientes) {
        this.estatusClientes = estatusClientes;
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

    public Boolean getEditandoContacto() {
        return editandoContacto;
    }

    public void setEditandoContacto(Boolean editandoContacto) {
        this.editandoContacto = editandoContacto;
    }

    public MedioCnt getMedioCntSelected() {
        return medioCntSelected;
    }

    public void setMedioCntSelected(MedioCnt medioCntSelected) {
        this.medioCntSelected = medioCntSelected;
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

    // Getters y Setters para domicilios
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

    // Getters y Setter para seguridad
    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }

    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }

    public String getContraseniaConfirmacion() {
        return contraseniaConfirmacion;
    }

    public void setContraseniaConfirmacion(String contraseniaConfirmacion) {
        this.contraseniaConfirmacion = contraseniaConfirmacion;
    }
    // </editor-fold>

}
