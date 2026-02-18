package mx.com.ferbo.controller.egresos;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.egresos.ImporteEgresosBL;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.categresos.CategoriaEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.categresos.TipoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;

@Named
@ApplicationScoped
public class EgresoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(EgresoBean.class);

    private Integer id;

    private ImporteEgreso importeSelected;

    private TipoEgreso tipoEgresoSelected;
    private List<TipoEgreso> lstTiposEgreso;

    private CategoriaEgreso categoriaEgresoSelected;
    private List<CategoriaEgreso> lstCategoriasEgreso;

    private List<CatConceptoEgreso> lstCatConceptosEgreso;

    private ConceptoEgreso conceptoSelected;

    @Inject
    private ImporteEgresosBL importeBL;

    public EgresoBean() {
    }

    @PostConstruct
    public void init() {

        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String idParam = params.get("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                id = Integer.valueOf(idParam);
                importeSelected = importeBL.obtenerPorId(id);
            } catch (NumberFormatException ex) {
                importeSelected = new ImporteEgreso();
            }
        } else {
            importeSelected = new ImporteEgreso();
        }
    }

    public ImporteEgreso getImporte() {
        return importeSelected;
    }

    public void setImporte(ImporteEgreso importe) {
        this.importeSelected = importe;
    }

    public ImporteEgreso getImporteSelected() {
        return importeSelected;
    }

    public void setImporteSelected(ImporteEgreso importeSelected) {
        this.importeSelected = importeSelected;
    }

    public TipoEgreso getTipoEgresoSelected() {
        return tipoEgresoSelected;
    }

    public void setTipoEgresoSelected(TipoEgreso tipoEgresoSelected) {
        this.tipoEgresoSelected = tipoEgresoSelected;
    }

    public List<TipoEgreso> getLstTiposEgreso() {
        return lstTiposEgreso;
    }

    public void setLstTiposEgreso(List<TipoEgreso> lstTiposEgreso) {
        this.lstTiposEgreso = lstTiposEgreso;
    }

    public CategoriaEgreso getCategoriaEgresoSelected() {
        return categoriaEgresoSelected;
    }

    public void setCategoriaEgresoSelected(CategoriaEgreso categoriaEgresoSelected) {
        this.categoriaEgresoSelected = categoriaEgresoSelected;
    }

    public List<CategoriaEgreso> getLstCategoriasEgreso() {
        return lstCategoriasEgreso;
    }

    public void setLstCategoriasEgreso(List<CategoriaEgreso> lstCategoriasEgreso) {
        this.lstCategoriasEgreso = lstCategoriasEgreso;
    }

    public List<CatConceptoEgreso> getLstCatConceptosEgreso() {
        return lstCatConceptosEgreso;
    }

    public void setLstCatConceptosEgreso(List<CatConceptoEgreso> lstCatConceptosEgreso) {
        this.lstCatConceptosEgreso = lstCatConceptosEgreso;
    }

    public ConceptoEgreso getConceptoSelected() {
        return conceptoSelected;
    }

    public void setConceptoSelected(ConceptoEgreso conceptoSelected) {
        this.conceptoSelected = conceptoSelected;
    }

    /*public void clonarConceptoEgreso(CatConceptoEgreso catConcepto) {
        try {
            conceptoSelected = importeBL.obtenerConcepto(catConcepto);
        } catch (InventarioException ex) {
            log.
        } catch (Exception ex) {

        } finally {
            actualizaciones();
        }
    }*/

    /*private <T> void ejecutarOperacion(T objeto, Consumer<T> accion) {
    try {
        accion.accept(objeto);
    } catch (InventarioException ex) {
        // manejar
    } catch (Exception ex) {
        // manejar
    } finally {
        actualizaciones();
    }
}*/

    public void actualizaciones() {
        PrimeFaces.current().ajax().update("formConsulta:messages");
    }

}
