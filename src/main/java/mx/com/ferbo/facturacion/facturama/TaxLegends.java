package mx.com.ferbo.facturacion.facturama;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**TaxLegends
 * Leyendas Fiscales. El complemento permite incluir en la factura leyendas que son consideradas en las disposiciones fiscales, distintas a las que se mencionan en el estándar técnico del comprobante. Este complemento permite incorporar a una factura los siguientes datos: * Ley, Resolución o Disposición fiscal que regula la leyenda. * Número de Artículo o en su caso Regla que regula la obligación de la leyenda. * La leyenda fiscal.<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=TaxLegends<br>
 * @author esteban
 *
 */
public class TaxLegends {
    
    @SerializedName(value = "Legends")
    private List<Legend> Legends = null;

    public List<Legend> getLegends() {
        return Legends;
    }

    public void setLegends(List<Legend> legends) {
        Legends = legends;
    }

    @Override
    public String toString() {
        return "{\"Legends\":\"" + Legends + "\"}";
    }
}
