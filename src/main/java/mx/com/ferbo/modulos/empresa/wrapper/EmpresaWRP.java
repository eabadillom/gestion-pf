package mx.com.ferbo.modulos.empresa.wrapper;

import java.util.List;

import mx.com.ferbo.modulos.empresa.model.EmisorCFDI;

public class EmpresaWRP {

    private EmisorCFDI emisorCFDISelected;
    private List<EmisorCFDI> lstEmisoresCFDI;

    public EmisorCFDI getEmisorCFDISelected() {
        return emisorCFDISelected;
    }

    public void setEmisorCFDISelected(EmisorCFDI emisorCFDISelected) {
        this.emisorCFDISelected = emisorCFDISelected;
    }

    public List<EmisorCFDI> getLstEmisoresCFDI() {
        return lstEmisoresCFDI;
    }

    public void setLstEmisoresCFDI(List<EmisorCFDI> lstEmisoresCFDI) {
        this.lstEmisoresCFDI = lstEmisoresCFDI;
    }

}
