package mx.com.ferbo.business.complemento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import mx.com.ferbo.business.ComplementoPagoBL;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.n.ComplementoPagoDAO;
import mx.com.ferbo.dao.n.SerieComplementoPagoDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.SerieComplementoPago;
import mx.com.ferbo.ui.DatosPago;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComplementoBL 
{
    private Logger log = LogManager.getLogger(ComplementoPagoBL.class);
    
    private PagoDAO pagoDAO;
    private ComplementoPagoDAO complementoPagoDAO;
    private SerieComplementoPagoDAO serieComplementoPagoDAO;

    public ComplementoBL() {
        this.pagoDAO = new PagoDAO();
        complementoPagoDAO = new ComplementoPagoDAO();
        this.serieComplementoPagoDAO = new SerieComplementoPagoDAO();
    }
    
    public String obtenerCP(Cliente cliente) {
        String cp = null;

        List<ClienteDomicilios> listDomiciliosCliente = cliente.getClienteDomiciliosList();
        for(ClienteDomicilios aux : listDomiciliosCliente){
            if(aux.getDomicilios().getDomicilioTipoCve().getDomicilioTipoCve() == 1) {
                cp = aux.getDomicilios().getAsentamiento().getCp();
            }
        }

        return cp;
    }

    public DatosPago obtenerSaldos(Pago pago) {
        DatosPago datosPago = new DatosPago();
        Factura factura = pago.getFactura();
        BigDecimal saldoActual = factura.getTotal();
        BigDecimal montoAnterior = BigDecimal.ZERO;
        BigDecimal saldoRestante = BigDecimal.ZERO;
        List<Pago> listaPagosFactura = pagoDAO.buscaPorFactura(factura);
        for(Pago pagoFactura : listaPagosFactura) {
            montoAnterior = saldoActual;
            saldoRestante = montoAnterior.subtract(pagoFactura.getMonto());

            if(pagoFactura.equals(pago)){
                datosPago.setSaldoAnterior(montoAnterior);
                datosPago.setMonto(pagoFactura.getMonto());
                datosPago.setSaldoRestante(saldoRestante);
                break;
            }
            saldoActual = saldoRestante;
        }

        return datosPago;
    }
    
    public ComplementoPago obtenerComplementoPorPago(Integer idComplementoPago) throws InventarioException, DAOException 
    {
        if(idComplementoPago == null)
            throw new InventarioException("Debe seleccionar un emisor para el complemento de pago.");

        return complementoPagoDAO.buscarPorId(idComplementoPago).orElseThrow(() -> new InventarioException("Comlemento de pago no entrontrado."));
    }
    
    public void guardarComplementoPago(SerieComplementoPago serieComplementoPago, String formaPago) throws InventarioException {
        ComplementoPago complementoPago = new ComplementoPago();
        complementoPago.setRegistro(new Date());
        complementoPago.setSerie(serieComplementoPago.getSerie());
        complementoPago.setNumero(serieComplementoPago.getNumero());
        complementoPago.setFormaPago(formaPago);
        complementoPagoDAO.guardar(complementoPago);
    }
    
    public ComplementoPago obtenerPorFolioSerie(String numero, String serie) throws DAOException {
        return complementoPagoDAO.buscarPorFolioSerie(numero, serie);
    }
    
    public SerieComplementoPago obtenerSeriePorEmisor(Integer idEmisor) throws InventarioException, DAOException 
    {
        if(idEmisor == null)
            throw new InventarioException("Debe seleccionar un emisor para el complemento de pago.");

        return serieComplementoPagoDAO.buscarPorEmisor(idEmisor);
    }

    public void actualizarSerieComplemento(SerieComplementoPago serieComplemento) throws InventarioException, CloneNotSupportedException {
        if(serieComplemento == null)
            throw new InventarioException("Debe seleccionar un emisor para el complemento de pago.");
        
        SerieComplementoPago serie = serieComplemento.clone();

        Integer numero = Integer.valueOf(serie.getNumero());
        numero = numero + 1;
        serie.setNumero(String.valueOf(numero));
        serieComplementoPagoDAO.actualizar(serie);
        log.info("Serie complemento de pago actualizado");
    }
    
}
