package mx.com.ferbo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.ferbo.model.FacturacionGeneral;

public class ReporteVentaUtil 
{

    public List<Number> ventaMesPago(HashMap<String, FacturacionGeneral> list, String tipoPago) 
    {
        List<Number> value = new ArrayList<>();

        for (Map.Entry<String, FacturacionGeneral> entry : list.entrySet()) {
            if (entry.getKey().equals(tipoPago)) {
                value.add(entry.getValue().getPagosPorMes());
            }
        }

        return value;
    }

}
