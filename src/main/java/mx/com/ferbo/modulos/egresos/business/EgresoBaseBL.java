
package mx.com.ferbo.modulos.egresos.business;

import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.validation.ValidationException;

public abstract class EgresoBaseBL<T, P, C> {

    protected abstract String nombreHijo();

    protected abstract String nombreHijos();

    protected abstract String nombreCatalogo();

    protected String nombrePadre() {
        return "el egreso";
    }

    protected void validarHijoYCatalogo(T son, C catalog) throws InventarioException {

        ValidationException.requireNonNull(son, nombreHijo() + " no puese ser vacío.");
        ValidationException.requireNonNull(catalog, nombreCatalogo() + " no puese ser vacío.");
    }

    public void validarPadreYCatalogo(P father, C catalog) throws InventarioException {
         ValidationException.requireNonNull(father, nombrePadre() + " no puede ser vacío.");
         ValidationException.requireNonNull(catalog, nombreCatalogo() + " no puede ser vacío");
    }

    protected void validarPadreEHijo(P father, T son) throws InventarioException {

        ValidationException.requireNonNull(father, nombrePadre() + " no puede ser vacío.");
        ValidationException.requireNonNull(son, nombreHijo() + " no puede ser vacío.");

    }
}
