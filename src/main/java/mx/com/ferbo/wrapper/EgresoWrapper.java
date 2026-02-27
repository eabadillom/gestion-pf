package mx.com.ferbo.wrapper;

import java.util.List;

import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.categresos.CategoriaEgreso;
import mx.com.ferbo.model.categresos.TipoEgreso;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;

public class EgresoWrapper {
    
    private CatConceptoEgreso catConcepctoSelected;
    private List<CatConceptoEgreso> lstCatConceptos;
    
    private CategoriaEgreso categoriaSelected;
    private List<CategoriaEgreso> lstCategorias;

    private TipoEgreso tipoSelected;
    private List<TipoEgreso> lstTipos;

    private ConceptoEgreso conceptoSelected;

    private ImporteEgreso egresoSelected;
    private List<ImporteEgreso> lstEgresos;

    private PagoEgreso pagoSelected;
    private List<PagoEgreso> lstPagos;

    private DevolucionEgreso devolucionSelected;
    private List<DevolucionEgreso> lstDevoluciones;

    private CargoEgreso cargoSelected;
    private List<CargoEgreso> lstCargos;

    public CatConceptoEgreso getCatConcepctoSelected() {
        return catConcepctoSelected;
    }
    public void setCatConcepctoSelected(CatConceptoEgreso catConcepctoSelected) {
        this.catConcepctoSelected = catConcepctoSelected;
    }
    public List<CatConceptoEgreso> getLstCatConceptos() {
        return lstCatConceptos;
    }
    public void setLstCatConceptos(List<CatConceptoEgreso> lstCatConceptos) {
        this.lstCatConceptos = lstCatConceptos;
    }
    public CategoriaEgreso getCategoriaSelected() {
        return categoriaSelected;
    }
    public void setCategoriaSelected(CategoriaEgreso categoriaSelected) {
        this.categoriaSelected = categoriaSelected;
    }
    public List<CategoriaEgreso> getLstCategorias() {
        return lstCategorias;
    }
    public void setLstCategorias(List<CategoriaEgreso> lstCategorias) {
        this.lstCategorias = lstCategorias;
    }
    public TipoEgreso getTipoSelected() {
        return tipoSelected;
    }
    public void setTipoSelected(TipoEgreso tipoSelected) {
        this.tipoSelected = tipoSelected;
    }
    public List<TipoEgreso> getLstTipos() {
        return lstTipos;
    }
    public void setLstTipos(List<TipoEgreso> lstTipos) {
        this.lstTipos = lstTipos;
    }
    public ConceptoEgreso getConceptoSelected() {
        return conceptoSelected;
    }
    public void setConceptoSelected(ConceptoEgreso conceptoSelected) {
        this.conceptoSelected = conceptoSelected;
    }
    public ImporteEgreso getEgresoSelected() {
        return egresoSelected;
    }
    public void setEgresoSelected(ImporteEgreso egresoSelected) {
        this.egresoSelected = egresoSelected;
    }
    public List<ImporteEgreso> getLstEgresos() {
        return lstEgresos;
    }
    public void setLstEgresos(List<ImporteEgreso> lstEgresos) {
        this.lstEgresos = lstEgresos;
    }
    public PagoEgreso getPagoSelected() {
        return pagoSelected;
    }
    public void setPagoSelected(PagoEgreso pagoSelected) {
        this.pagoSelected = pagoSelected;
    }
    public List<PagoEgreso> getLstPagos() {
        return lstPagos;
    }
    public void setLstPagos(List<PagoEgreso> lstPagos) {
        this.lstPagos = lstPagos;
    }
    public DevolucionEgreso getDevolucionSelected() {
        return devolucionSelected;
    }
    public void setDevolucionSelected(DevolucionEgreso devolucionSelected) {
        this.devolucionSelected = devolucionSelected;
    }
    public List<DevolucionEgreso> getLstDevoluciones() {
        return lstDevoluciones;
    }
    public void setLstDevoluciones(List<DevolucionEgreso> lstDevoluciones) {
        this.lstDevoluciones = lstDevoluciones;
    }
    public CargoEgreso getCargoSelected() {
        return cargoSelected;
    }
    public void setCargoSelected(CargoEgreso cargoSelected) {
        this.cargoSelected = cargoSelected;
    }
    public List<CargoEgreso> getLstCargos() {
        return lstCargos;
    }
    public void setLstCargos(List<CargoEgreso> lstCargos) {
        this.lstCargos = lstCargos;
    }

    
}
