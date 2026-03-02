package mx.com.ferbo.manager.egresos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;

import mx.com.ferbo.business.egresos.CargoEgresoBL;
import mx.com.ferbo.business.egresos.EgresoBaseBL;
import mx.com.ferbo.model.categresos.StatusCargoEgreso;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.util.InventarioException;

public class CargoEgresoMGR extends EgresoBaseMGR<CargoEgreso, ImporteEgreso, StatusCargoEgreso> {

    @Inject
    private CargoEgresoBL cargoBL;

    @Override
    protected EgresoBaseBL<CargoEgreso, ImporteEgreso, StatusCargoEgreso> getBL() {
        return cargoBL;
    }

    @Override
    protected Integer getId(CargoEgreso entity) {
        return entity.getId();
    }

    public String actualizarDias(CargoEgreso cargo) {
        Integer dias = cargoBL.calcularDias(cargo);

        // Actualizar el campo en el objeto que la vista enlaza
        cargo.setNumeroDias(dias);

        return "Número de días calculado: " + dias;
    }

    public String calcularCargoYMostrar(CargoEgreso cargo, ConceptoEgreso concepto) throws InventarioException {
        BigDecimal resultado = cargoBL.calcularCargo(cargo, concepto);

        if (resultado.compareTo(BigDecimal.ZERO) > 0) {
            return "El cargo calculado es " + resultado.setScale(2, RoundingMode.HALF_UP);
        } else {
            return "No se aplicó cargo para el registro seleccionado";
        }
    }
}
