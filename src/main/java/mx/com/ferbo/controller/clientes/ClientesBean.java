package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
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

import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.clientes.FiscalBL;
import mx.com.ferbo.dao.n.ClienteDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.clientes.ServiciosBL;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UnidadDeManejo;

import mx.com.ferbo.business.clientes.ContactoBL;
import mx.com.ferbo.controller.SideBarBean;
import mx.com.ferbo.model.ClienteContacto;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.model.Mail;
import mx.com.ferbo.model.MedioCnt;
import mx.com.ferbo.model.Telefono;
import mx.com.ferbo.model.TipoMail;
import mx.com.ferbo.model.TipoTelefono;

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
    private List<RegimenFiscal> lstRegimenFiscal;
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

    public ClientesBean() {
        this.lstClientes = new ArrayList<>();
        this.clienteSelected = new Cliente();
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
    }

    public void cargarInfoCliente(Cliente cliente) {
        try {
            if (cliente == null) {
                throw new InventarioException("El cliente no puede ser vacío");
            }

            if (cliente.getCteCve() == null) {
                throw new InventarioException("El cliente no tiene datos todavida");
            }
            this.clienteSelected = clienteDAO.obtenerPorId(cliente.getCteCve(), true);
            fiscalBL.validarInfoFiscal(this.clienteSelected);
            lstRegimenFiscal = fiscalBL.obtenerRegimenesFiscales(this.clienteSelected);
            lstUsoCfdi = fiscalBL.obtenerUsoCfdis(this.clienteSelected);
            addMessage(FacesMessage.SEVERITY_INFO, "Fiscal", "Información fiscal cargada");
        } catch (InventarioException ex) {
            log.warn(ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Cargar informacion", ex.getMessage());
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Cargar informacion",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    // Funciones para clientes
    public void clonarCliente() {
    }

    public void eliminarCliente() {
    }

    public void nuevoCliente() {
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

    public void validarCodigoUnico(Cliente cliente) {
        try {
            this.clienteSelected = cliente;
            Cliente cliente2 = clienteDAO.buscarPorCodigoUnico(this.clienteSelected.getCodUnico());
            fiscalBL.validarCodigoUnico(this.clienteSelected, cliente2);
        } catch (InventarioException ex) {
            log.warn(ex.getMessage(), ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Fiscal", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error inesperado. Causado por: ", ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Fiscal",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form");
        }
    }

    public void validarRFC(Cliente cliente) {
        try {
            this.clienteSelected = cliente;
            fiscalBL.validarRFC(this.clienteSelected);
        } catch (InventarioException ex) {
            log.warn(ex.getMessage(), ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Fiscal", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error inesperado. Causado por: ", ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Fiscal",
                    "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages",
                    "form");
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

    public void OperarServicios(String operacion) {
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
            addMessage(FacesMessage.SEVERITY_INFO, "Servicio", mensaje);
        } catch (InventarioException ex) {
            log.warn(ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Servicio", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Servicio",
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
            addMessage(FacesMessage.SEVERITY_INFO, "Contacto", mensaje);
        } catch (InventarioException ex) {
            log.warn(ex);
            addMessage(FacesMessage.SEVERITY_WARN, "Contacto", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex);
            addMessage(FacesMessage.SEVERITY_ERROR, "Contacto", "Contacte con el admistrador del sistema.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
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
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    private void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity,
                title, msg));
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
}