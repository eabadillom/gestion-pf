package mx.com.ferbo.business.n;

import java.math.BigDecimal;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.InventarioDAO;
import mx.com.ferbo.dao.n.SaldoDAO;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Saldo;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class SaldosBL 
{
    private static Logger log = LogManager.getLogger(SaldosBL.class);
    
    @Inject
    private CandadoBL candadoBL;
    
    @Inject
    private InventarioDAO inventarioDAO;
    
    @Inject
    private SaldoDAO saldoDAO;
    
    public void validaSaldo(Cliente cliente, Date fecha) throws InventarioException
    {
        boolean isHabilitarSalida = false;
        Saldo saldo = obtenerSaldoPorCliente(cliente, fecha);
        BigDecimal cantidadInventario = obtenerTotalInventario(cliente, fecha);
        CandadoSalida candadoSalida = candadoBL.obtenerCandadoSalidaPorCliente(cliente);
        BigDecimal saldoVencido = null;
        boolean salVencido = false;
        
        if(saldo == null) {
            log.info("El cliente NO tiene saldos pendientes, puede retirar su mercancia.");
            return;
        }
        
        log.info("Saldo: en plazo = {}, 15 días = {}, 30 días = {}, 60 días = {}, más de 60 días = {}", saldo.getEnPlazo(), saldo.getAtraso15dias(), saldo.getAtraso30dias(), saldo.getAtraso60dias(), saldo.getAtrasoMayor60dias());
        log.info("Cantidad en inventario: {} piezas.", cantidadInventario);
        
        saldoVencido = saldo.getAtraso8dias().add(saldo.getAtraso15dias()).add(saldo.getAtraso30dias()).add(saldo.getAtraso60dias()).add(saldo.getAtrasoMayor60dias());

        if(saldoVencido.compareTo(BigDecimal.ZERO) > 0) {
            salVencido = true;
            log.info("El cliente {} presenta un saldo vencido: {}", cliente.getNombre(), saldo.getSaldo());
        }
        
        isHabilitarSalida = candadoSalida.getHabilitado();
        
        if(salVencido && isHabilitarSalida && candadoSalida.getNumSalidas() > 0) {
            log.info("El cliente tiene un saldo vencido, pero tiene {} salidas permitidas.", candadoSalida.getNumSalidas());
            return;
        }

        if(salVencido && isHabilitarSalida && candadoSalida.getNumSalidas() <= 0) {
            throw new InventarioException("El cliente tiene adeudos vencidos. Favor de contactar con el área de facturacíon");
        }

        if(salVencido && !isHabilitarSalida) {
            throw new InventarioException("El cliente tiene adeudos vencidos. Favor de contactar con el área de facturación.");
        }

        if(!salVencido) {
            //retorna el control sin acciones, debido a que SI tiene permitidas las salidas.
            log.info("El cliente NO tiene saldos vencidos, puede retirar su mercancia.");
            return;
        }

        if(isHabilitarSalida && candadoSalida.getNumSalidas() > 0) {
            log.info("El cliente {} presenta un saldo vencido ({}), pero tiene permitidas las salidas", cliente.getNombre(), saldo.getSaldo());
            return;
        }

        throw new InventarioException("El cliente no tiene  permitida la salida de mercancia porque presenta un adeudo. Favor de contactar al área de cobranza.");
    }
    
    public void validarSalidaMercancia(Cliente cliente, Date fecha, Integer cantidadTotal) throws InventarioException
    {
        CandadoSalida candadoSalida = candadoBL.obtenerCandadoSalidaPorCliente(cliente);
        Saldo saldo = obtenerSaldoPorCliente(cliente, fecha);
        BigDecimal cantidadInventario = obtenerTotalInventario(cliente, fecha);
        
        BigDecimal saldoTotal = (saldo == null ? new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP) : saldo.getSaldo());
        
        if(cantidadInventario.compareTo(new BigDecimal(cantidadTotal).setScale(2, BigDecimal.ROUND_HALF_UP)) <= 0
            && candadoSalida.isSalidaTotal() == false && saldoTotal.compareTo(BigDecimal.ZERO) > 0)
            throw new InventarioException("El cliente no puede sacar toda su mercancía hasta liquidar sus adeudos.");
    }
    
    public Saldo obtenerSaldoPorCliente(Cliente cliente, Date fecha)
    {
        return saldoDAO.getSaldo(cliente, fecha, null);
    }
    
    public BigDecimal obtenerTotalInventario(Cliente cliente, Date fecha)
    {
        return inventarioDAO.getCantidad(cliente.getCteCve(), fecha);
    }
    
}
