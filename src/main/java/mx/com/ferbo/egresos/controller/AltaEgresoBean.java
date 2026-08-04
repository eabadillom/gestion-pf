package mx.com.ferbo.egresos.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.ferbo.tools.exception.BusinessException;
import com.ferbo.tools.exception.SystemException;
import java.io.IOException;

import mx.com.ferbo.egresos.business.CancelaEgresoBL;
import mx.com.ferbo.egresos.business.EgresoBL;
import mx.com.ferbo.egresos.business.catalogos.CategoriaEgresoBL;
import mx.com.ferbo.egresos.business.catalogos.StatusEgresoBL;
import mx.com.ferbo.egresos.model.CancelaEgreso;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;
import mx.com.ferbo.empresa.business.EmisorCdfiBL;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.pagos.businesslogic.FormaPagoBL;
import mx.com.ferbo.pagos.businesslogic.MetodoPagoBL;
import mx.com.ferbo.util.FacesUtils;

@Named
@ViewScoped
public class AltaEgresoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(AltaEgresoBean.class);

    @Inject
    private StatusEgresoBL stutusBL;

    @Inject
    private CategoriaEgresoBL categoriaBL;

    @Inject
    private EgresoBL egresoBL;

    @Inject
    private FormaPagoBL medioPagoBL;

    @Inject
    private MetodoPagoBL metodoPagoBL;

    @Inject
    private EmisorCdfiBL emisorCfdiBL;

    @Inject
    private CancelaEgresoBL cancelaEgresoBL;

    private List<StatusEgreso> lstStatusEgresos;
    private StatusEgreso statusEgresoSelected;

    private List<CategoriaEgreso> lstCategoriasEgreso;
    private CategoriaEgreso categoriaEgresoSelected;

    private List<MedioPago> lstFormasPago;
    private MedioPago formaPagoSelected;

    private List<MetodoPago> lstMetodosPago;
    private MetodoPago metodoPagoSelected;

    private List<EmisoresCFDIS> lstEmisoresCfdi;
    private EmisoresCFDIS emisorCfdiSelected;

    private Egreso egresoSelected;

    private CancelaEgreso cancelaEgresoSelected;

    private String titulo;
    private String inicioLeyenda;
    private final Boolean activo = Boolean.TRUE;

    private Usuario usuario;

    public AltaEgresoBean() {
        formaPagoSelected = new MedioPago();
        metodoPagoSelected = new MetodoPago();
        categoriaEgresoSelected = new CategoriaEgreso();
        statusEgresoSelected = new StatusEgreso();
        emisorCfdiSelected = new EmisoresCFDIS();
    }

    @PostConstruct
    public void init() {
        titulo = "configuración básica de egresos";
        try {
            FacesContext context = FacesContext.getCurrentInstance();

            HttpServletRequest request = (HttpServletRequest) context
                    .getExternalContext()
                    .getRequest();
            usuario = (Usuario) request.getSession(false).getAttribute("usuario");

            String idParam = context
                    .getExternalContext()
                    .getRequestParameterMap()
                    .get("id");

            Long id = null;

            if (idParam != null) {
                id = Long.valueOf(idParam);
            }

            egresoSelected = egresoBL.nuevoOExistente(id);

            inicioLeyenda = "El usuario " + usuario.getUsuario();
            log.info("{} inicio la carga de {}.", inicioLeyenda, titulo);
            lstFormasPago = medioPagoBL.obtenerMediosPago();
            lstMetodosPago = metodoPagoBL.obtenerMetodosPago();
            lstCategoriasEgreso = categoriaBL.buscarActivos(activo);
            lstStatusEgresos = stutusBL.buscarActivos(activo);
            lstEmisoresCfdi = emisorCfdiBL.obtenerTodos();
            log.info("{} finalizo la carga de {}.", inicioLeyenda, titulo);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "Se ha cargado exitosamente la " + titulo + ".");
        } catch (SystemException | BusinessException ex) {
            log.warn("Error al momento de cargar la {}. {}", titulo, ex);
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
        }
    }

    private void actualizarMensajes() {
        PrimeFaces.current().ajax().update("form:messages");
    }

    private void actualizarFormulario() throws IOException {
        FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("principal.xhtml?id=" + egresoSelected.getId());
    }

    private void abrirDialogoCancelacion() {
        cancelaEgresoSelected = cancelaEgresoBL.nuevoOExistente(null);
        PrimeFaces.current().ajax().update("form:dlgMotiCance");
        PrimeFaces.current().executeScript("PF('dlgMotiCance').show();");
    }

    public void procesarEgreso() {
        boolean exito = false;
        titulo = "Procesar egreso";
        try {
            egresoBL.validarEgresoYStatus(egresoSelected, statusEgresoSelected);
            egresoBL.validarStatusExistenteONuevo(egresoSelected, statusEgresoSelected);
            if (egresoBL.egresoEstaPersistido(egresoSelected)) {
                egresoBL.validarEgresoNuevo(egresoSelected);
            }
            if (egresoBL.isCancelado(statusEgresoSelected)) {
                abrirDialogoCancelacion();
            } else {
                egresoBL.asigarStatusEgreso(egresoSelected, statusEgresoSelected);
                egresoSelected = egresoBL.crearOActualizarEgreso(egresoSelected, inicioLeyenda);
                FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                        "El egreso se proceso exitosamente");
                exito = true;
                actualizarFormulario();
            }
        } catch (SystemException | BusinessException ex) {
            log.warn("Error al momento de {}. {}", titulo.toUpperCase(), ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo.toUpperCase(), ex.getMessage());
            exito = false;
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo.toUpperCase(),
                    "Error desconocido. Contacte con el administrador de sistemas.");
            exito = false;
        } finally {
            actualizarMensajes();
            PrimeFaces.current().ajax().addCallbackParam("exito", exito);
        }
    }

    public void procesarEgresoConCancelacion() {
        boolean exito = false;
        try {
            titulo = "Procesar egreso con cancelación";
            cancelaEgresoBL.concatenarSiHayMotivoCancelacion(cancelaEgresoSelected, inicioLeyenda);
            cancelaEgresoBL.asignarEgreso(cancelaEgresoSelected, egresoSelected);
            cancelaEgresoBL.guardarOActualizarCancelaEgreso(cancelaEgresoSelected);
            egresoBL.asigarStatusEgreso(egresoSelected, statusEgresoSelected);
            egresoBL.crearOActualizarEgreso(egresoSelected, inicioLeyenda);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "El egreso se proceso exitosamente");
            exito = true;
            actualizarFormulario();
        } catch (SystemException | BusinessException ex) {
            log.warn("Error al momento de {}. {}", titulo.toUpperCase(), ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo.toUpperCase(), ex.getMessage());
            exito = false;
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo.toUpperCase(),
                    "Error desconocido. Contacte con el administrador de sistemas.");
            exito = false;
        } finally {
            actualizarMensajes();
            PrimeFaces.current().ajax().addCallbackParam("exito", exito);
        }
    }

    // Getters y Setters
    public List<StatusEgreso> getLstStatusEgresos() {
        return lstStatusEgresos;
    }

    public void setLstStatusEgresos(List<StatusEgreso> lstStatusEgresos) {
        this.lstStatusEgresos = lstStatusEgresos;
    }

    public StatusEgreso getStatusEgresoSelected() {
        return statusEgresoSelected;
    }

    public void setStatusEgresoSelected(StatusEgreso statusEgresoSelected) {
        this.statusEgresoSelected = statusEgresoSelected;
    }

    public List<CategoriaEgreso> getLstCategoriasEgreso() {
        return lstCategoriasEgreso;
    }

    public void setLstCategoriasEgreso(List<CategoriaEgreso> lstCategoriasEgreso) {
        this.lstCategoriasEgreso = lstCategoriasEgreso;
    }

    public CategoriaEgreso getCategoriaEgresoSelected() {
        return categoriaEgresoSelected;
    }

    public void setCategoriaEgresoSelected(CategoriaEgreso categoriaEgresoSelected) {
        this.categoriaEgresoSelected = categoriaEgresoSelected;
    }

    public List<MedioPago> getLstFormasPago() {
        return lstFormasPago;
    }

    public void setLstFormasPago(List<MedioPago> lstFormasPago) {
        this.lstFormasPago = lstFormasPago;
    }

    public MedioPago getFormaPagoSelected() {
        return formaPagoSelected;
    }

    public void setFormaPagoSelected(MedioPago formaPagoSelected) {
        this.formaPagoSelected = formaPagoSelected;
    }

    public List<MetodoPago> getLstMetodosPago() {
        return lstMetodosPago;
    }

    public void setLstMetodosPago(List<MetodoPago> lstMetodosPago) {
        this.lstMetodosPago = lstMetodosPago;
    }

    public MetodoPago getMetodoPagoSelected() {
        return metodoPagoSelected;
    }

    public void setMetodoPagoSelected(MetodoPago metodoPagoSelected) {
        this.metodoPagoSelected = metodoPagoSelected;
    }

    public Egreso getEgresoSelected() {
        return egresoSelected;
    }

    public void setEgresoSelected(Egreso egresoSelected) {
        this.egresoSelected = egresoSelected;
    }

    public List<EmisoresCFDIS> getLstEmisoresCfdi() {
        return lstEmisoresCfdi;
    }

    public void setLstEmisoresCfdi(List<EmisoresCFDIS> lstEmisoresCfdi) {
        this.lstEmisoresCfdi = lstEmisoresCfdi;
    }

    public EmisoresCFDIS getEmisorCfdiSelected() {
        return emisorCfdiSelected;
    }

    public void setEmisorCfdiSelected(EmisoresCFDIS emisorCfdiSelected) {
        this.emisorCfdiSelected = emisorCfdiSelected;
    }

    public CancelaEgreso getCancelaEgresoSelected() {
        return cancelaEgresoSelected;
    }

    public void setCancelaEgresoSelected(CancelaEgreso cancelaEgresoSelected) {
        this.cancelaEgresoSelected = cancelaEgresoSelected;
    }

}
