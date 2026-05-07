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
import com.ferbo.tools.exception.ValidationException;

import mx.com.ferbo.egresos.business.EgresoBL;
import mx.com.ferbo.egresos.business.catalogos.CategoriaEgresoBL;
import mx.com.ferbo.egresos.business.catalogos.MaquinaStatusEgreso;
import mx.com.ferbo.egresos.business.catalogos.StatusEgresoBL;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;
import mx.com.ferbo.empresa.business.EmisorCdfiBL;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.pagos.businesslogic.MedioPagoBL;
import mx.com.ferbo.pagos.businesslogic.MetodoPagoBL;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

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
    private MedioPagoBL medioPagoBL;

    @Inject
    private MetodoPagoBL metodoPagoBL;

    @Inject
    private EmisorCdfiBL emisorCfdiBL;

    @Inject
    private MaquinaStatusEgreso maquinaStatusEgreso;

    private List<StatusEgreso> lstStatusEgresos;
    private StatusEgreso statusEgresoSelected;

    private List<CategoriaEgreso> lstCategoriasEgreso;
    private CategoriaEgreso categoriaEgresoSelected;

    private List<MedioPago> lstMediosPago;
    private MedioPago medioPagoSelected;

    private List<MetodoPago> lstMetodosPago;
    private MetodoPago metodoPagoSelected;

    private List<EmisoresCFDIS> lstEmisoresCfdi;
    private EmisoresCFDIS emisorCfdiSelected;

    private Egreso egresoSelected;

    private String titulo;
    private String inicioLeyenda;
    private final Boolean activo = Boolean.TRUE;

    private FacesContext context;
    private HttpServletRequest request;
    private Usuario usuario;

    public AltaEgresoBean() {
        medioPagoSelected = new MedioPago();
        metodoPagoSelected = new MetodoPago();
        categoriaEgresoSelected = new CategoriaEgreso();
        statusEgresoSelected = new StatusEgreso();
        emisorCfdiSelected = new EmisoresCFDIS();
    }

    @PostConstruct
    public void init() {
        titulo = "configuración básica de egresos";
        try {
            request = (HttpServletRequest) context.getExternalContext().getRequest();
            usuario = (Usuario) request.getSession(false).getAttribute("usuario");
            inicioLeyenda = "El usuario " + usuario.getUsuario();
            log.info("{} inicio la carga de {}.", inicioLeyenda, titulo);
            lstMediosPago = medioPagoBL.obtenerMediosPago();
            lstMetodosPago = metodoPagoBL.obtenerMetodosPago();
            lstCategoriasEgreso = categoriaBL.buscarActivos(activo);
            lstStatusEgresos = stutusBL.buscarActivos(activo);
            lstEmisoresCfdi = emisorCfdiBL.obtenerTodos();
            log.info("{} finalizo la carga de {}.", inicioLeyenda, titulo);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "Se ha cargado exitosamente la " + titulo + ".");
        } catch (SystemException ex) {
            log.warn("Error al momento de cargar la {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo.toUpperCase(),
                    ex.getMessage());
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo,
                    "Error al momento de cargar la " + titulo + ". Contacte con el administrador de sistemas.");
        } finally {
            actualizacionComponentesPrimeFaces();
        }
    }

    public void actualizacionComponentesPrimeFaces() {
        PrimeFaces.current().ajax().update("form:messages");
    }

    public void cambiarStatusEgreso() {
        titulo = "cambiar status egreso";
        try {
            log.info("{} inicio el proceso de {}.");
            maquinaStatusEgreso.valiarCambioStatus(lstStatusEgresos, egresoSelected.getStatus(), statusEgresoSelected);
            egresoBL.asignarStatusEgreso(egresoSelected, statusEgresoSelected);
            log.info("{} finalizo el proceso de {}.");
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "Se ha cambiado correctamente el status a: " + statusEgresoSelected.getNombre());
        } catch (ValidationException ex) {
            log.warn("Error al momento de validar los objetos. {}");
        } catch (BusinessException ex) {
            log.warn("Error al momento de cambiar de status en el egreso. {}");
        } finally {
            actualizacionComponentesPrimeFaces();
        }
    }

    public void guardarOActualizarEgreso(Egreso egreso) {
        try {
            if (egreso.getId() == null) {
                titulo = "guardar el egreso";
                log.info("{} inicia el proceso para {}.", inicioLeyenda, titulo);
                egresoBL.crearEgreso(egreso);

            } else {
                titulo = "actualizar el egreso";
                log.info("{} inicia el proceso para {}.", inicioLeyenda, titulo);
                egresoBL.actualizarEgreso(egreso);
            }
            log.info("{} finaliza proceso para {}.", inicioLeyenda, titulo);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "Se ha completado el proceso de " + titulo + ".");
        } catch (ValidationException ex) {
            log.info("Error al momento de validar el egreso. {}", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Validacioón de egreso", ex.getMessage());
        } catch (InventarioException | SystemException ex) {
            log.warn("Error al momento de {}. {}", titulo.toUpperCase(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, ex.getMessage());
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo,
                    "Error al momento de cargar la " + titulo + ". Contacte con el administrador de sistemas.");
        } finally {
            actualizacionComponentesPrimeFaces();
        }
    }

    // Getters y Setters
    public StatusEgresoBL getStutusBL() {
        return stutusBL;
    }

    public void setStutusBL(StatusEgresoBL stutusBL) {
        this.stutusBL = stutusBL;
    }

    public CategoriaEgresoBL getCategoriaBL() {
        return categoriaBL;
    }

    public void setCategoriaBL(CategoriaEgresoBL categoriaBL) {
        this.categoriaBL = categoriaBL;
    }

    public EgresoBL getEgresoBL() {
        return egresoBL;
    }

    public void setEgresoBL(EgresoBL egresoBL) {
        this.egresoBL = egresoBL;
    }

    public MedioPagoBL getMedioPagoBL() {
        return medioPagoBL;
    }

    public void setMedioPagoBL(MedioPagoBL medioPagoBL) {
        this.medioPagoBL = medioPagoBL;
    }

    public MetodoPagoBL getMetodoPagoBL() {
        return metodoPagoBL;
    }

    public void setMetodoPagoBL(MetodoPagoBL metodoPagoBL) {
        this.metodoPagoBL = metodoPagoBL;
    }

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

    public List<MedioPago> getLstMediosPago() {
        return lstMediosPago;
    }

    public void setLstMediosPago(List<MedioPago> lstMediosPago) {
        this.lstMediosPago = lstMediosPago;
    }

    public MedioPago getMedioPagoSelected() {
        return medioPagoSelected;
    }

    public void setMedioPagoSelected(MedioPago medioPagoSelected) {
        this.medioPagoSelected = medioPagoSelected;
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

}
