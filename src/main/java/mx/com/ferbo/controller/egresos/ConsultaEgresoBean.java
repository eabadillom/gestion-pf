package mx.com.ferbo.controller.egresos;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.catalogos.CatConceptoEgresoBL;

import mx.com.ferbo.business.egresos.ImporteEgresosBL;
import mx.com.ferbo.business.empresa.NEmisoresCFDISBL;
import mx.com.ferbo.model.catalogos.CatConceptoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class ConsultaEgresoBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LogManager.getLogger(ConsultaEgresoBean.class);
    
    private List<ImporteEgreso> lstEgresos;
    private ImporteEgreso egresoSelected;
    
    @Inject
    private ImporteEgresosBL egresoBL;
    
    @Inject    
    private CatConceptoEgresoBL conceptoBL;
    
    @Inject
    private NEmisoresCFDISBL emisorBL;
    
    private YearMonth mesSelected;
    
    private List<CatConceptoEgreso> lstConceptos;
    private CatConceptoEgreso conceptoSelected;
    
    private List<NEmisoresCFDIS> lstEmisores;
    private NEmisoresCFDIS emisorSelected;
    
    public ConsultaEgresoBean() {
    }
    
    @PostConstruct
    public void init() {
        setMesSelected(YearMonth.now());
        cargarEgresos();
        cargarCatalogos();
    }
    
    public List<ImporteEgreso> getLstEgresos() {
        return lstEgresos;
    }
    
    public void setLstEgresos(List<ImporteEgreso> lstEgresos) {
        this.lstEgresos = lstEgresos;
    }
    
    public ImporteEgreso getEgresoSelected() {
        return egresoSelected;
    }
    
    public void setEgresoSelected(ImporteEgreso egresoSelected) {
        this.egresoSelected = egresoSelected;
    }
    
    public YearMonth getMesSelected() {
        return mesSelected;
    }
    
    public void setMesSelected(YearMonth mesSelected) {
        this.mesSelected = mesSelected;
    }
    
    public List<CatConceptoEgreso> getLstConceptos() {
        return lstConceptos;
    }
    
    public void setLstConceptos(List<CatConceptoEgreso> lstConceptos) {
        this.lstConceptos = lstConceptos;
    }
    
    public CatConceptoEgreso getConceptoSelected() {
        return conceptoSelected;
    }
    
    public void setConceptoSelected(CatConceptoEgreso conceptoSelected) {
        this.conceptoSelected = conceptoSelected;
    }
    
    public List<NEmisoresCFDIS> getLstEmisores() {
        return lstEmisores;
    }
    
    public void setLstEmisores(List<NEmisoresCFDIS> lstEmisores) {
        this.lstEmisores = lstEmisores;
    }
    
    public NEmisoresCFDIS getEmisorSelected() {
        return emisorSelected;
    }
    
    public void setEmisorSelected(NEmisoresCFDIS emisorSelected) {
        this.emisorSelected = emisorSelected;
    }
    
    public String irEditar(Integer id) {
        return "/protected/catalogos/egresos/egreso?faces-redirect=true&id=" + id;
    }
    
    private void cargarEgresos() {
        try {
            setLstEgresos(egresoBL.obtenerPorFiltros(conceptoSelected, emisorSelected, mesSelected));
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Egresos", "Los egresos se cargaron exitosamente.");
        } catch (InventarioException ex) {
            log.warn("{}. {}", ex.getMessage(), ex);;
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Egresos", ex.getMessage());
        } catch (Exception ex) {
            log.error("{}. {}", ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Egresos", "Error desconocido al cargar los egresos. Conctecte con el administrador.");
        } finally {
            actualizaciones();
        }
    }
    
    private void cargarCatalogos() {
        try {
            setLstEmisores(emisorBL.obtenerTodos());
            setLstConceptos(conceptoBL.vigentesONoVigentes(Boolean.TRUE));
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Catalogos", "Los catalogos se cargaron exitosamente.");
        } catch (InventarioException ex) {
            log.warn("{}. {}", ex.getMessage(), ex);;
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Catalogos", ex.getMessage());
        } catch (Exception ex) {
            log.error("{}. {}", ex.getMessage(), ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Catalogos", "Error desconocido al cargar los catalogos. Conctecte con el administrador.");
        } finally {
            actualizaciones();
        }
    }
    
    public void actualizaciones() {
        PrimeFaces.current().ajax().update("formConsulta:messages");
    }
}
