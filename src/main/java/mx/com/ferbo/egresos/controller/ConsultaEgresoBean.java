package mx.com.ferbo.egresos.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

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
import com.ferbo.tools.util.date.DateFormatter;

import java.io.IOException;

import mx.com.ferbo.egresos.business.EgresoBL;
import mx.com.ferbo.egresos.business.catalogos.CategoriaEgresoBL;
import mx.com.ferbo.egresos.business.catalogos.StatusEgresoBL;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.egresos.model.calogos.StatusEgreso;
import mx.com.ferbo.empresa.business.EmisorCdfiBL;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.FacesUtils;

@Named
@ViewScoped
public class ConsultaEgresoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static Logger log = LogManager.getLogger(ConsultaEgresoBean.class);

    @Inject
    private StatusEgresoBL stutusBL;

    @Inject
    private CategoriaEgresoBL categoriaBL;

    @Inject
    private EmisorCdfiBL emisorCdfiBL;

    @Inject
    private EgresoBL egresoBL;

    private List<StatusEgreso> lstStatusEgresos;
    private StatusEgreso statusEgresoSelected;

    private List<CategoriaEgreso> lstCategoriasEgreso;
    private CategoriaEgreso categoriaEgresoSelected;

    private List<EmisoresCFDIS> lstEmisoresCFDIs;
    private EmisoresCFDIS emisorSelected;

    private transient List<Egreso> lstEgresos;

    private String conceptoEgreso;

    private LocalDateTime incioMes;
    private LocalDateTime finMes;

    private String titulo;
    private String inicioLeyenda;
    private final Boolean activo = Boolean.TRUE;

    private Usuario usuario;

    private YearMonth mes = YearMonth.now();

    public ConsultaEgresoBean() {
    }

    @PostConstruct
    public void init() {
        titulo = "configuración básica de egresos";
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            usuario = (Usuario) request.getSession(false).getAttribute("usuario");
            inicioLeyenda = "El usuario " + usuario.getUsuario();
            log.info("{} inicio la carga de {}.", inicioLeyenda, titulo);
            lstCategoriasEgreso = categoriaBL.buscarActivos(activo);
            lstStatusEgresos = stutusBL.buscarActivos(activo);
            lstEmisoresCFDIs = emisorCdfiBL.obtenerTodos();
            statusEgresoSelected = null;
            categoriaEgresoSelected = null;
            conceptoEgreso = null;
            lstEgresos = egresoBL.obtenerPorFiltros(mes, null, null, null, null);
            log.info("{} finalizo la carga de {}.", inicioLeyenda, titulo);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "Se ha cargado exitosamente la " + titulo + ".");
        } catch (SystemException | BusinessException ex) {
            log.warn("Error al momento de cargar la {}. {}", titulo, ex);
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
        }
    }

    private void actualizacionComponentesPrimeFaces() {
        PrimeFaces.current().ajax().update("form:messages");
    }

    public void obtenerEgresosPorFiltro() {
        titulo = "cargar egresos";
        try {
            log.info("{} inicio el proceso de {}.", inicioLeyenda, titulo);
            lstEgresos = egresoBL.obtenerPorFiltros(mes, categoriaEgresoSelected, statusEgresoSelected, conceptoEgreso,
                    emisorSelected);
            log.info("{} finalizo el proceso de {}.", inicioLeyenda, titulo);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "Se ha completado exitosamente el proceso de " + titulo + ".");
        } catch (SystemException | BusinessException ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, ex.getMessage());
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo,
                    "Error desconocido. Contacte con el administrador de sistemas.");
        } finally {
            actualizacionComponentesPrimeFaces();
        }
    }

    public String getSoloFecha(LocalDateTime fecha) {
        return DateFormatter.format(
                fecha,
                "dd/MM/yyyy",
                new Locale("es", "MX"));
    }

    public String obtenerSeverity(String status) {

        switch (status) {
            case "PENDIENTE":
                return "warning";

            case "PROCESADO":
                return "success";

            case "CANCELADO":
                return "danger";

            default:
                return "info";
        }
    }

    public void editar(Long id) throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();

        String contextPath = context.getExternalContext().getRequestContextPath();

        context.getExternalContext().redirect(
                contextPath + "/protected/catalogos/egresos/alta/principal.xhtml?id=" + id);
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

    public List<Egreso> getLstEgresos() {
        return lstEgresos;
    }

    public void setLstEgresos(List<Egreso> lstEgresos) {
        this.lstEgresos = lstEgresos;
    }

    public String getConceptoEgreso() {
        return conceptoEgreso;
    }

    public void setConceptoEgreso(String conceptoEgreso) {
        this.conceptoEgreso = conceptoEgreso;
    }

    public LocalDateTime getIncioMes() {
        return incioMes;
    }

    public void setIncioMes(LocalDateTime incioMes) {
        this.incioMes = incioMes;
    }

    public LocalDateTime getFinMes() {
        return finMes;
    }

    public void setFinMes(LocalDateTime finMes) {
        this.finMes = finMes;
    }

    public YearMonth getMes() {
        return mes;
    }

    public void setMes(YearMonth mes) {
        this.mes = mes;
    }

    public List<EmisoresCFDIS> getLstEmisoresCFDIs() {
        return lstEmisoresCFDIs;
    }

    public void setLstEmisoresCFDIs(List<EmisoresCFDIS> lstEmisoresCFDIs) {
        this.lstEmisoresCFDIs = lstEmisoresCFDIs;
    }

    public EmisoresCFDIS getEmisorSelected() {
        return emisorSelected;
    }

    public void setEmisorSelected(EmisoresCFDIS emisorSelected) {
        this.emisorSelected = emisorSelected;
    }

}
