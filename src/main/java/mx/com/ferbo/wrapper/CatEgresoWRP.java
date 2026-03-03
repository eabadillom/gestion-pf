package mx.com.ferbo.wrapper;

import java.util.List;

import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.categresos.CategoriaEgreso;
import mx.com.ferbo.model.categresos.StatusActivoFijo;
import mx.com.ferbo.model.categresos.StatusCargoEgreso;
import mx.com.ferbo.model.categresos.StatusDevolucionEgreso;
import mx.com.ferbo.model.categresos.StatusEgreso;
import mx.com.ferbo.model.categresos.StatusPagoEgreso;
import mx.com.ferbo.model.categresos.TipoAsignacionEgreso;
import mx.com.ferbo.model.categresos.TipoCargoEgreso;
import mx.com.ferbo.model.categresos.TipoDevolucionEgreso;
import mx.com.ferbo.model.categresos.TipoDocumentoEgreso;
import mx.com.ferbo.model.categresos.TipoEgreso;
import mx.com.ferbo.model.categresos.TipoMovimientoEgreso;
import mx.com.ferbo.model.empresa.NEmisoresCFDIS;

public class CatEgresoWRP {

    private TipoEgreso tipoSelected;
    private List<TipoEgreso> lstTipos;

    private CategoriaEgreso categoriaSelected;
    private List<CategoriaEgreso> lstCategorias;

    private CatConceptoEgreso catConceptoSelected;
    private List<CatConceptoEgreso> lstCatConceptos;

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

    private NEmisoresCFDIS razonSelected;
    private List<NEmisoresCFDIS> lstRazones;

    private MetodoPago metedoPagoSelected;
    private List<MetodoPago> lstMetodosPago;

    private MedioPago medioPagoSelected;
    private List<MedioPago> lstMediosPago;

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

    public CatConceptoEgreso getCatConceptoSelected() {
        return catConceptoSelected;
    }

    public void setCatConceptoSelected(CatConceptoEgreso catConceptoSelected) {
        this.catConceptoSelected = catConceptoSelected;
    }

    public List<CatConceptoEgreso> getLstCatConceptos() {
        return lstCatConceptos;
    }

    public void setLstCatConceptos(List<CatConceptoEgreso> lstCatConceptos) {
        this.lstCatConceptos = lstCatConceptos;
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

    public NEmisoresCFDIS getRazonSelected() {
        return razonSelected;
    }

    public void setRazonSelected(NEmisoresCFDIS razonSelected) {
        this.razonSelected = razonSelected;
    }

    public List<NEmisoresCFDIS> getLstRazones() {
        return lstRazones;
    }

    public void setLstRazones(List<NEmisoresCFDIS> lstRazones) {
        this.lstRazones = lstRazones;
    }

    public MetodoPago getMetedoPagoSelected() {
        return metedoPagoSelected;
    }

    public void setMetedoPagoSelected(MetodoPago metedoPagoSelected) {
        this.metedoPagoSelected = metedoPagoSelected;
    }

    public List<MetodoPago> getLstMetodosPago() {
        return lstMetodosPago;
    }

    public void setLstMetodosPago(List<MetodoPago> lstMetodosPago) {
        this.lstMetodosPago = lstMetodosPago;
    }

    public MedioPago getMedioPagoSelected() {
        return medioPagoSelected;
    }

    public void setMedioPagoSelected(MedioPago medioPagoSelected) {
        this.medioPagoSelected = medioPagoSelected;
    }

    public List<MedioPago> getLstMediosPago() {
        return lstMediosPago;
    }

    public void setLstMediosPago(List<MedioPago> lstMediosPago) {
        this.lstMediosPago = lstMediosPago;
    }

}
