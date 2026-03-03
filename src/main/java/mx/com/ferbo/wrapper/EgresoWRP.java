package mx.com.ferbo.wrapper;

import java.util.List;

import mx.com.ferbo.model.egresos.ActivoFijo;
import mx.com.ferbo.model.egresos.AsignacionEgreso;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;

public class EgresoWRP {

    private ConceptoEgreso conceptoSelected;

    private ImporteEgreso egresoSelected;
    private List<ImporteEgreso> lstEgresos;

    private PagoEgreso pagoSelected;
    private List<PagoEgreso> lstPagos;

    private DevolucionEgreso devolucionSelected;
    private List<DevolucionEgreso> lstDevoluciones;

    private CargoEgreso cargoSelected;
    private List<CargoEgreso> lstCargos;

    private AsignacionEgreso asignacionSelected;
    private List<AsignacionEgreso> lstAsignaciones;

    private ActivoFijo activoFijoSelected;

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

    public AsignacionEgreso getAsignacionSelected() {
        return asignacionSelected;
    }

    public void setAsignacionSelected(AsignacionEgreso asignacionSelected) {
        this.asignacionSelected = asignacionSelected;
    }

    public List<AsignacionEgreso> getLstAsignaciones() {
        return lstAsignaciones;
    }

    public void setLstAsignaciones(List<AsignacionEgreso> lstAsignaciones) {
        this.lstAsignaciones = lstAsignaciones;
    }

    public ActivoFijo getActivoFijoSelected() {
        return activoFijoSelected;
    }

    public void setActivoFijoSelected(ActivoFijo activoFijoSelected) {
        this.activoFijoSelected = activoFijoSelected;
    }

}
