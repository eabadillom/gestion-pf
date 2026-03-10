package mx.com.ferbo.modulos.egresos.factory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import mx.com.ferbo.modulos.egresos.business.categreso.StatusPagoEgresoBL;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.StatusEgreso;
import mx.com.ferbo.util.AbstractFactoryMaquinaStatus;
import mx.com.ferbo.util.MaquinaStatusBase;

@RequestScoped
public class ImporteEgresoFactory implements AbstractFactoryMaquinaStatus <StatusEgreso> {

    @Inject
    private StatusPagoEgresoBL statusBL;

    @Override
    public MaquinaStatusBase<StatusEgreso> crearMaquina() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearMaquina'");
    }

}
