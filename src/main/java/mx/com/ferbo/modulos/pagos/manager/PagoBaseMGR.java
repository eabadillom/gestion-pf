package mx.com.ferbo.modulos.pagos.manager;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.util.InventarioException;

public abstract class PagoBaseMGR {

    private static final Logger log = LogManager.getLogger(PagoBaseMGR.class);

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        List<T> get() throws InventarioException;
    }

    protected <T> String[] cargarCatalogoVigente(List<T> targetList,
            ThrowingSupplier<T> proveedorBL,
            String titulo, String mensaje) throws InventarioException {

        List<T> lista = proveedorBL.get();
        targetList.clear();
        targetList.addAll(lista);

        return new String[] { titulo, mensaje };
    }
}
