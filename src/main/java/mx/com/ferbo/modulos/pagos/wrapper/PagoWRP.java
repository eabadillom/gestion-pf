package mx.com.ferbo.modulos.pagos.wrapper;

import java.util.List;

import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.MetodoPago;

public class PagoWRP {

    private MedioPago medioPagoSelected;
    private List<MedioPago> lstMediosPago;

    private MetodoPago metodoPagoSelected;
    private List<MetodoPago> lstMetodosPago;

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

    public MetodoPago getMetodoPagoSelected() {
        return metodoPagoSelected;
    }

    public void setMetodoPagoSelected(MetodoPago metodoPagoSelected) {
        this.metodoPagoSelected = metodoPagoSelected;
    }

    public List<MetodoPago> getLstMetodosPago() {
        return lstMetodosPago;
    }

    public void setLstMetodosPago(List<MetodoPago> lstMetodosPago) {
        this.lstMetodosPago = lstMetodosPago;
    }

}
