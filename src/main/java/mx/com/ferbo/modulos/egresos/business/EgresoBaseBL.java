
package mx.com.ferbo.modulos.egresos.business;

import mx.com.ferbo.util.ValidationUtils;

import java.util.List;

import mx.com.ferbo.util.InventarioException;

public abstract class EgresoBaseBL<T, P, C> {

    protected abstract void construirMaquinaStatus() throws InventarioException;

    protected abstract String nombreHijo();

    protected abstract String nombreHijos();

    protected abstract String nombreCatalogo();

    protected abstract List<T> obtenerHijos(P father, List<C> catalog) throws InventarioException;

    protected String nombrePadre() {
        return "el egreso";
    }

    protected void validarHijoYCatalogo(T son, C catalog) throws InventarioException {

        ValidationUtils.requireNonNull(son, nombreHijo() + " no puese ser vacío.");
        ValidationUtils.requireNonNull(catalog, nombreCatalogo() + " no puese ser vacío.");
    }

    public void validarPadreYCatalogo(P father, C catalog) throws InventarioException {
         ValidationUtils.requireNonNull(father, nombrePadre() + " no puede ser vacío.");
         ValidationUtils.requireNonNull(catalog, nombreCatalogo() + " no puede ser vacío");
    }

    protected void validarPadreEHijo(P father, T son) throws InventarioException {

        ValidationUtils.requireNonNull(father, nombrePadre() + " no puede ser vacío.");
        ValidationUtils.requireNonNull(son, nombreHijo() + " no puede ser vacío.");

    }
}
