package mx.com.ferbo.egresos.util;

import com.ferbo.tools.exception.BusinessException;

public interface AbstractFactoryMaquinaStatus<T> {
    MaquinaStatusBase<T> crearMaquina() throws BusinessException;
}
