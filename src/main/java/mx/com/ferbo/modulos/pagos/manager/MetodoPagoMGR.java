package mx.com.ferbo.modulos.pagos.manager;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.modulos.pagos.bussines.MetodoPagoBL;
import mx.com.ferbo.util.InventarioException;

public class MetodoPagoMGR extends PagoBaseMGR {

    @Inject
    private MetodoPagoBL metodoPagoBL;

    public String[] cargarMetodosPago(List<MetodoPago> lst) throws InventarioException{

        return cargarCatalogoVigente(
            lst, 
            () -> metodoPagoBL.obtenerVigentes(), 
            "Cargar métodos de pago", 
            "Se han cargado exitosamente los métodos de pago.");
    }

}
