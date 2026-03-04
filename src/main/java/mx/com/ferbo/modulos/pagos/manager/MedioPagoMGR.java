package mx.com.ferbo.modulos.pagos.manager;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.modulos.pagos.bussines.MedioPagoBL;
import mx.com.ferbo.util.InventarioException;

public class MedioPagoMGR extends PagoBaseMGR{

    @Inject
    private MedioPagoBL medioPagoBL;

    public String[] cargarMediosPago(List<MedioPago> lst) throws InventarioException {
        return cargarCatalogoVigente(
                lst,
                () -> medioPagoBL.obtenerVigentes(),
                "Cargar medios de pago",
                "Se han cargado exitosamente los medios de pago."
        );
    }
}
