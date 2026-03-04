package mx.com.ferbo.modulos.egresos.wrapper;

import java.util.List;

import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusActivoFijo;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusCargoEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusDevolucionEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusPagoEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoAsignacionEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoCargoEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoDevolucionEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoDocumentoEgreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoMovimientoEgreso;

public class CatSecundariosWRP {

    private Boolean vigente = Boolean.TRUE;

    private TipoAsignacionEgreso tipoAsingancionSelected;
    private List<TipoAsignacionEgreso> lstTiposAsignacion;

    private TipoCargoEgreso tipoCargoSelected;
    private List<TipoCargoEgreso> lstTiposCargo;

    private TipoDevolucionEgreso tipoDevolucionSelected;
    private List<TipoDevolucionEgreso> lstTiposDevolucion;

    private TipoDocumentoEgreso tipoDocumentoSelected;
    private List<TipoDocumentoEgreso> lstTiposDocumento;

    private TipoMovimientoEgreso tipoMovimientoSelected;
    private List<TipoMovimientoEgreso> lstTiposMovimiento;

    private StatusActivoFijo statusActivoFijoSelected;
    private List<StatusActivoFijo> lstStatusActivoFijo;

    private StatusCargoEgreso statusCargoEgresoSelected;
    private List<StatusCargoEgreso> lstStatusCargoEgreso;

    private StatusDevolucionEgreso statusDevolucionEgresoSelected;
    private List<StatusDevolucionEgreso> lstStatusDevolucionEgreso;

    private StatusPagoEgreso statusPagoEgresoSelected;
    private List<StatusPagoEgreso> lstStatusPagoEgreso;

    private StatusEgreso statusEgresoSelected;
    private List<StatusEgreso> lstStatusEgreso;

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public TipoAsignacionEgreso getTipoAsingancionSelected() {
        return tipoAsingancionSelected;
    }

    public void setTipoAsingancionSelected(TipoAsignacionEgreso tipoAsingancionSelected) {
        this.tipoAsingancionSelected = tipoAsingancionSelected;
    }

    public List<TipoAsignacionEgreso> getLstTiposAsignacion() {
        return lstTiposAsignacion;
    }

    public void setLstTiposAsignacion(List<TipoAsignacionEgreso> lstTiposAsignacion) {
        this.lstTiposAsignacion = lstTiposAsignacion;
    }

    public TipoCargoEgreso getTipoCargoSelected() {
        return tipoCargoSelected;
    }

    public void setTipoCargoSelected(TipoCargoEgreso tipoCargoSelected) {
        this.tipoCargoSelected = tipoCargoSelected;
    }

    public List<TipoCargoEgreso> getLstTiposCargo() {
        return lstTiposCargo;
    }

    public void setLstTiposCargo(List<TipoCargoEgreso> lstTiposCargo) {
        this.lstTiposCargo = lstTiposCargo;
    }

    public TipoDevolucionEgreso getTipoDevolucionSelected() {
        return tipoDevolucionSelected;
    }

    public void setTipoDevolucionSelected(TipoDevolucionEgreso tipoDevolucionSelected) {
        this.tipoDevolucionSelected = tipoDevolucionSelected;
    }

    public List<TipoDevolucionEgreso> getLstTiposDevolucion() {
        return lstTiposDevolucion;
    }

    public void setLstTiposDevolucion(List<TipoDevolucionEgreso> lstTiposDevolucion) {
        this.lstTiposDevolucion = lstTiposDevolucion;
    }

    public TipoDocumentoEgreso getTipoDocumentoSelected() {
        return tipoDocumentoSelected;
    }

    public void setTipoDocumentoSelected(TipoDocumentoEgreso tipoDocumentoSelected) {
        this.tipoDocumentoSelected = tipoDocumentoSelected;
    }

    public List<TipoDocumentoEgreso> getLstTiposDocumento() {
        return lstTiposDocumento;
    }

    public void setLstTiposDocumento(List<TipoDocumentoEgreso> lstTiposDocumento) {
        this.lstTiposDocumento = lstTiposDocumento;
    }

    public TipoMovimientoEgreso getTipoMovimientoSelected() {
        return tipoMovimientoSelected;
    }

    public void setTipoMovimientoSelected(TipoMovimientoEgreso tipoMovimientoSelected) {
        this.tipoMovimientoSelected = tipoMovimientoSelected;
    }

    public List<TipoMovimientoEgreso> getLstTiposMovimiento() {
        return lstTiposMovimiento;
    }

    public void setLstTiposMovimiento(List<TipoMovimientoEgreso> lstTiposMovimiento) {
        this.lstTiposMovimiento = lstTiposMovimiento;
    }

    public StatusActivoFijo getStatusActivoFijoSelected() {
        return statusActivoFijoSelected;
    }

    public void setStatusActivoFijoSelected(StatusActivoFijo statusActivoFijoSelected) {
        this.statusActivoFijoSelected = statusActivoFijoSelected;
    }

    public List<StatusActivoFijo> getLstStatusActivoFijo() {
        return lstStatusActivoFijo;
    }

    public void setLstStatusActivoFijo(List<StatusActivoFijo> lstStatusActivoFijo) {
        this.lstStatusActivoFijo = lstStatusActivoFijo;
    }

    public StatusCargoEgreso getStatusCargoEgresoSelected() {
        return statusCargoEgresoSelected;
    }

    public void setStatusCargoEgresoSelected(StatusCargoEgreso statusCargoEgresoSelected) {
        this.statusCargoEgresoSelected = statusCargoEgresoSelected;
    }

    public List<StatusCargoEgreso> getLstStatusCargoEgreso() {
        return lstStatusCargoEgreso;
    }

    public void setLstStatusCargoEgreso(List<StatusCargoEgreso> lstStatusCargoEgreso) {
        this.lstStatusCargoEgreso = lstStatusCargoEgreso;
    }

    public StatusDevolucionEgreso getStatusDevolucionEgresoSelected() {
        return statusDevolucionEgresoSelected;
    }

    public void setStatusDevolucionEgresoSelected(StatusDevolucionEgreso statusDevolucionEgresoSelected) {
        this.statusDevolucionEgresoSelected = statusDevolucionEgresoSelected;
    }

    public List<StatusDevolucionEgreso> getLstStatusDevolucionEgreso() {
        return lstStatusDevolucionEgreso;
    }

    public void setLstStatusDevolucionEgreso(List<StatusDevolucionEgreso> lstStatusDevolucionEgreso) {
        this.lstStatusDevolucionEgreso = lstStatusDevolucionEgreso;
    }

    public StatusPagoEgreso getStatusPagoEgresoSelected() {
        return statusPagoEgresoSelected;
    }

    public void setStatusPagoEgresoSelected(StatusPagoEgreso statusPagoEgresoSelected) {
        this.statusPagoEgresoSelected = statusPagoEgresoSelected;
    }

    public List<StatusPagoEgreso> getLstStatusPagoEgreso() {
        return lstStatusPagoEgreso;
    }

    public void setLstStatusPagoEgreso(List<StatusPagoEgreso> lstStatusPagoEgreso) {
        this.lstStatusPagoEgreso = lstStatusPagoEgreso;
    }

    public StatusEgreso getStatusEgresoSelected() {
        return statusEgresoSelected;
    }

    public void setStatusEgresoSelected(StatusEgreso statusEgresoSelected) {
        this.statusEgresoSelected = statusEgresoSelected;
    }

    public List<StatusEgreso> getLstStatusEgreso() {
        return lstStatusEgreso;
    }

    public void setLstStatusEgreso(List<StatusEgreso> lstStatusEgreso) {
        this.lstStatusEgreso = lstStatusEgreso;
    }

}
