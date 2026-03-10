package mx.com.ferbo.util;

import org.apache.poi.ss.formula.functions.T;

public interface AbstractFactoryMaquinaStatus<T> {
    MaquinaStatusBase<T> crearMaquina() throws InventarioException;
}
